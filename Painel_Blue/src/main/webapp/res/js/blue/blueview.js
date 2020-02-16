var BlueView = function (options) {
    var blueview = this;

    this.core = clone(blueViewCore.core);
    this.settings = $.extend(blueViewCore.settings, options);
    this.data = clone(blueViewCore.data);
    this.components = clone(blueViewCore.components);
    this.util = clone(blueViewCore.util);
    this.graphics = clone(blueViewCore.graphics);

    this.events = {
        'onDataLoaded': function (event) {
            var core = blueview.core;
            var sets = blueview.settings;
            var data = blueview.data;
            var comps = blueview.components;
            var util = blueview.util;
            var graphs = blueview.graphics;
            var evts = blueview.events;
            var acts = blueview.actions;

            var item = event.item;

//            if (item != null && item.type == createjs.AbstractLoader.JSON) {
//                var jsonData = JSON.parse(event.rawResult);
            if (item != null) {
                var jsonData = JSON.parse(event.result);

                if (jsonData && jsonData.proposition && jsonData.axes && jsonData.axes.X && jsonData.axes.Y &&
                        jsonData.linkTypes && jsonData.clusters && jsonData.nodes && jsonData.links) {
                    data.loaded = true;
                    data.proposition = jsonData.proposition;
                    data.axes.X = jsonData.axes.X;
                    data.axes.Y = jsonData.axes.Y;
                    data.linkTypes = jsonData.linkTypes;
                    data.clusters = jsonData.clusters;
                    data.nodes = jsonData.nodes;
                    data.links = jsonData.links;

                    if (sets.onDataLoaded) {
                        sets.onDataLoaded();
                    }

                    acts.drawBackground();
                    acts.drawTitle();
                    acts.drawAxes();
                    acts.drawGroups();
                    acts.drawNodes();
                    acts.drawLinks();

                    core.stage.update();

                    createjs.Ticker.addEventListener("tick", evts.onFrame);
                } else {
                    if (sets.events.onDataLoadError) {
                        sets.events.onDataLoadError();
                    }
                }
            }
        },
        'onLoadError': function (event) {
            var core = blueview.core;
            var sets = blueview.settings;
            var data = blueview.data;
            var comps = blueview.components;
            var util = blueview.util;
            var graphs = blueview.graphics;
            var evts = blueview.events;
            var acts = blueview.actions;

            data.loaded = false;

            if (blueview.settings.events.onDataLoadError) {
                blueview.settings.events.onDataLoadError();
            }
        },
        'onFrame': function () {
            if (!createjs.Ticker.getPaused()) {
                var core = blueview.core;
                var sets = blueview.settings;
                var data = blueview.data;
                var comps = blueview.components;
                var util = blueview.util;
                var graphs = blueview.graphics;
                var evts = blueview.events;
                var acts = blueview.actions;

                var gravity = sets.force.gravity;

                var container = comps.nodes.container;
                var width = container.width;
                var height = container.height;
                
                var area = width * height;
                var k = Math.sqrt(area / comps.nodes.items.length);
                var coolingDist = (Math.sqrt(area) / 100) * .001;

                var dGridX = [];
                for (var i = 0; i <= comps.axes.X.count; i++) {
                    dGridX[i] = 0;
                }
                var dGridY = [];
                for (var i = 0; i <= comps.axes.Y.count; i++) {
                    dGridY[i] = 0;
                }

                for (var i in comps.nodes.items) {
                    //limpeza
                    var node = comps.nodes.items[i];

                    var cluster = util.getClusterById(comps.clusters.items, node.data.group);

                    node.dx = 0;
                    node.dy = 0;

                    //repulsão dos eixos
                    var minX = cluster.container.x;
                    var maxX = cluster.container.x + cluster.container.width - (sets.node.circle.radius * 2);
                    var minY = cluster.container.y;
                    var maxY = cluster.container.y + cluster.container.height - (sets.node.circle.radius * 2);

                    //gravidade
                    if (core.galaxyOfNodes) {
                        var xDist = node.container.x - (width / 2);
                        var yDist = node.container.y - (height / 2);
                        var dist = util.distance(node.container.x, node.container.y, xDist, yDist);

                        var attractiveF = util.fa(dist, k) * gravity;

                        if (dist > 0) {
                            node.dx -= xDist / dist * attractiveF;
                            node.dy -= yDist / dist * attractiveF;
                        }
                    }

                    //repulsão
                    for (var j in comps.nodes.items) {
                        var node2 = comps.nodes.items[j];

                        if (i != j && (node.data.group == node2.data.group || core.galaxyOfNodes === true)) {
                            var xDist = node.container.x - node2.container.x;
                            var yDist = node.container.y - node2.container.y;
                            var dist = util.distance(node.container.x, node.container.y, node2.container.x, node2.container.y);

                            if (dist > 0) {
                                var repulsiveF = util.fr(dist, k);

                                node.dx += xDist / dist * repulsiveF;
                                node.dy += yDist / dist * repulsiveF;
                            }
                        }
                    }

                    //atração de mesmo grupo
                    if (core.galaxyOfNodes) {
                        for (var j in comps.nodes.items) {
                            var node2 = comps.nodes.items[j];

                            var cluster2 = util.getClusterById(comps.clusters.items, node2.data.group);

                            if (i != j && cluster.data.color == cluster2.data.color) {
                                var xDist = node.container.x - node2.container.x;
                                var yDist = node.container.y - node2.container.y;
                                var dist = util.distance(node.container.x, node.container.y, node2.container.x, node2.container.y);

                                var attractiveF = util.fa(dist, k);

                                if (dist > 0) {
                                    node.dx -= xDist / dist * attractiveF;
                                    node.dy -= yDist / dist * attractiveF;
                                    node2.dx += xDist / dist * attractiveF;
                                    node2.dy += yDist / dist * attractiveF;
                                }
                            }
                        }
                    }

                    if (!core.galaxyOfNodes) {
                        //Superior
                        {
                            var yDist = node.container.y - minY;
                            var dist = Math.abs(yDist);

                            if (dist > 0) {
                                var repulsiveF = util.fr(dist, k);

                                if (cluster.data.Y > 0) {
                                    node.dy += yDist / dist * repulsiveF;
//                                    dGridY[cluster.data.Y] -= yDist / dist * repulsiveF;
                                } else {
                                    node.dy += (yDist / dist * repulsiveF);
                                }
                            }
                        }
                        //Esquerdo
                        {
                            var xDist = node.container.x - minX;
                            var dist = Math.abs(xDist);

                            if (dist > 0) {
                                var repulsiveF = util.fr(dist, k);

                                if (cluster.data.X > 0) {
                                    node.dx += xDist / dist * repulsiveF;
//                                    dGridX[cluster.data.X] -= xDist / dist * repulsiveF;
                                } else {
                                    node.dx += (xDist / dist * repulsiveF);
                                }
                            }
                        }

                        //Inferior
                        {
                            var yDist = node.container.y - maxY;
                            var dist = Math.abs(yDist);

                            if (dist > 0) {
                                var repulsiveF = util.fr(dist, k);

                                if (cluster.data.Y < comps.axes.Y.count - 1) {
                                    node.dy += yDist / dist * repulsiveF;
//                                    dGridY[cluster.data.Y + 1] -= yDist / dist * repulsiveF;
                                } else {
                                    node.dy += (yDist / dist * repulsiveF);
                                }
                            }
                        }

                        //Direito
                        {
                            var xDist = node.container.x - maxX;
                            var dist = Math.abs(xDist);

                            if (dist > 0) {
                                var repulsiveF = util.fr(dist, k);

                                if (cluster.data.X < comps.axes.X.count - 1) {
                                    node.dx += xDist / dist * repulsiveF;
//                                    dGridX[cluster.data.X + 1] -= xDist / dist * repulsiveF;
                                } else {
                                    node.dx += (xDist / dist * repulsiveF);
                                }
                            }
                        }
                    }
                }

                //atração
                for (var i in comps.links.items) {
                    var link = comps.links.items[i];

                    var source = util.getNodeById(comps.nodes.items, link.data.source);
                    var target = util.getNodeById(comps.nodes.items, link.data.target);

                    var xDist = source.container.x - target.container.x;
                    var yDist = source.container.y - target.container.y;
                    var dist = util.distance(source.container.x, source.container.y, target.container.x, target.container.y);

                    var attractiveF = util.fa(dist, k);

                    if (dist > 0) {
                        source.dx -= xDist / dist * attractiveF;
                        source.dy -= yDist / dist * attractiveF;
                        target.dx += xDist / dist * attractiveF;
                        target.dy += yDist / dist * attractiveF;
                    }
                }

                if (core.cellAdjust) {
//                    for (var i = 0; i < comps.axes.X.count; i++) {
//                        //DX
//                        {
//                            var xDist = dGridX[i * 1];
//                            var dist = Math.abs(xDist);
//
//                            if (dist > 0) {
//                                var nx = data.grid.X[i] + xDist / dist;
//
//                                data.grid.X[i] = nx;
//                            }
//                        }
//                    }
//                    for (var i = 0; i < comps.axes.Y.count; i++) {
//                        //DY
//                        {
//                            var yDist = dGridY[i * 1];
//                            var dist = Math.abs(yDist);
//
//                            if (dist > 0) {
//                                var ny = data.grid.Y[i] + yDist / dist;
//
//                                data.grid.Y[i] = ny;
//                            }
//                        }
//                    }
                }

                var warm = false;

                for (var i in comps.nodes.items) {
                    //limpeza
                    var node = comps.nodes.items[i];

                    //aplicação da velocidade
                    if (!node.data.fixed) {
                        var cluster = util.getClusterById(comps.clusters.items, node.data.group);

                        var xDist = node.dx;
                        var yDist = node.dy;
                        var dist = Math.sqrt(Math.pow(node.dx, 2) + Math.pow(node.dy, 2));

                        if (dist > 0) {
                            var minX = cluster.container.x;
                            var maxX = cluster.container.x + cluster.container.width - (sets.node.circle.radius * 2);
                            var minY = cluster.container.y;
                            var maxY = cluster.container.y + cluster.container.height - (sets.node.circle.radius * 2);

                            var nx = node.container.x + xDist / dist;
                            var ny = node.container.y + yDist / dist;

                            if (nx < minX)
                                nx = minX;
                            if (nx > maxX)
                                nx = maxX;
                            if (ny < minY)
                                ny = minY;
                            if (ny > maxY)
                                ny = maxY;

                            node.container.x = nx;
                            node.container.y = ny;

                            node.container.label.x = nx - (node.container.label.width / 2) + (sets.node.circle.radius);
                            node.container.label.y = ny - node.container.label.height - sets.node.label.distance;
                        }

                        warm |= (Math.abs(node.container.ix - node.container.x) > (coolingDist * width) || Math.abs(node.container.iy - node.container.y) > (coolingDist * height));
                    }
                }

                if (core.cycletime < sets.force.cycletime) {
                    core.cycletime++;
                } else {
                    core.cycletime = 0;

                    for (var i in comps.nodes.items) {
                        var node = comps.nodes.items[i];

                        node.container.ix = node.container.x;
                        node.container.iy = node.container.y;
                    }

                    if (!warm) {
                        createjs.Ticker.setPaused(true);
                        blueview.events.onStop();
                    }
                }

                blueview.actions.drawLinks();
                blueview.actions.redrawAxes();
                blueview.actions.drawGroups();

                core.stage.update();
            }
        },
        'onStart': function () {
            var core = blueview.core;
            var sets = blueview.settings;
            var data = blueview.data;
            var comps = blueview.components;
            var util = blueview.util;
            var graphs = blueview.graphics;
            var evts = blueview.events;
            var acts = blueview.actions;

            if (sets.events.onStart) {
                sets.events.onStart();
            }
        },
        'onStop': function () {
            var core = blueview.core;
            var sets = blueview.settings;
            var data = blueview.data;
            var comps = blueview.components;
            var util = blueview.util;
            var graphs = blueview.graphics;
            var evts = blueview.events;
            var acts = blueview.actions;

            if (sets.events.onStop) {
                sets.events.onStop();
            }
        }
    };

    //actions
    this.actions = {
        //stage init
        'setupStage': function () {
            var core = blueview.core;
            var sets = blueview.settings;
            var data = blueview.data;
            var comps = blueview.components;
            var util = blueview.util;
            var graphs = blueview.graphics;
            var evts = blueview.events;
            var acts = blueview.actions;

            //stage
            var stage = new createjs.Stage("bluecanvas");
            core.stage = stage;

            //mouse event
            stage.enableMouseOver(20);
            stage.mouseMoveOutside = true;

            //scale
            stage.scaleX = stage.scaleY = 300 / 96;

            return stage;
        },
        //canvas init
        'setupCanvas': function (width, height) {
            var core = blueview.core;
            var sets = blueview.settings;
            var data = blueview.data;
            var comps = blueview.components;
            var util = blueview.util;
            var graphs = blueview.graphics;
            var evts = blueview.events;
            var acts = blueview.actions;

            //stage
            var stage = core.stage;

            //width / height
            var margin = sets.margin;

            var canvas = stage.canvas;
            comps.canvas = canvas;

            canvas.width = width;
            canvas.height = height;

            canvas.style.width = canvas.width + 'px';
            canvas.style.height = canvas.height + 'px';

            var scaleFactor = 300 / 96;
            canvas.width = Math.ceil(canvas.width * scaleFactor);
            canvas.height = Math.ceil(canvas.height * scaleFactor);
            var ctx = canvas.getContext('2d');
            ctx.scale(scaleFactor, scaleFactor);

            core.width = (canvas.width - margin * 2) / scaleFactor;
            core.height = (canvas.height - margin * 2) / scaleFactor;

            data.grid.X = [];
            data.grid.Y = [];

            return canvas;
        },
        //containers init
        'setupContainer': function () {
            var core = blueview.core;
            var sets = blueview.settings;
            var data = blueview.data;
            var comps = blueview.components;
            var util = blueview.util;
            var graphs = blueview.graphics;
            var evts = blueview.events;
            var acts = blueview.actions;

            //stage
            var stage = core.stage;

            //canvas
            var canvas = comps.canvas;

            //stage container
            var container = comps.container;
            var newcontainer = false;

            if (!container) {
                container = new createjs.Container();
                comps.container = container;

                newcontainer = true;
            }

            container.x = -0.5;
            container.y = -0.5;
            container.width = canvas.width;
            container.height = canvas.height;

            //stage container / background / main container
            if (newcontainer) {
                stage.addChild(container);
                comps.container = container;

                //containers
                var mainContainer = util.createBasedContainer(container);
                comps.main.container = mainContainer;

                comps.background.container = util.createBasedContainer(mainContainer);
                comps.title.container = util.createBasedContainer(mainContainer);
                comps.axes.container = util.createBasedContainer(mainContainer);
                comps.clusters.container = util.createBasedContainer(mainContainer);
                comps.links.container = util.createBasedContainer(mainContainer);
                comps.nodes.container = util.createBasedContainer(mainContainer);
                comps.linkLabels.container = util.createBasedContainer(mainContainer);
                comps.nodeLabels.container = util.createBasedContainer(mainContainer);

                //subcontainers
                comps.axes.X.container = util.createBasedContainer(comps.axes.container);
                comps.axes.Y.container = util.createBasedContainer(comps.axes.container);
            }

            var mainContainer = comps.main.container;

            mainContainer.x = sets.margin;
            mainContainer.y = sets.margin;
            mainContainer.width = core.width;
            mainContainer.height = core.height;

            comps.background.container.width = mainContainer.width;
            comps.background.container.height = mainContainer.height;

            comps.title.container.width = mainContainer.width;
            comps.title.container.height = sets.title.height;

            comps.axes.container.y = sets.title.height;
            comps.axes.container.width = mainContainer.width;
            comps.axes.container.height = mainContainer.height - sets.title.height;

            comps.clusters.container.y = sets.title.height + (!core.galaxyOfNodes ? sets.axis.size : 0);
            comps.clusters.container.width = mainContainer.width - (!core.galaxyOfNodes ? sets.axis.size : 0);
            comps.clusters.container.height = mainContainer.height - sets.title.height - (!core.galaxyOfNodes ? sets.axis.size : 0);

            comps.links.container.y = comps.clusters.container.y;
            comps.links.container.width = comps.clusters.container.width;
            comps.links.container.height = comps.clusters.container.height;

            comps.nodes.container.y = comps.clusters.container.y;
            comps.nodes.container.width = comps.clusters.container.width;
            comps.nodes.container.height = comps.clusters.container.height;

            comps.linkLabels.container.y = comps.clusters.container.y;
            comps.linkLabels.container.width = comps.clusters.container.width;
            comps.linkLabels.container.height = comps.clusters.container.height;

            comps.nodeLabels.container.y = comps.clusters.container.y;
            comps.nodeLabels.container.width = comps.clusters.container.width;
            comps.nodeLabels.container.height = comps.clusters.container.height;

            comps.axes.X.container.width = comps.clusters.container.width;
            comps.axes.X.container.height = sets.axis.size;

            comps.axes.Y.container.x = core.width - sets.axis.size;
            comps.axes.Y.container.y = sets.axis.size;
            comps.axes.Y.container.width = sets.axis.size;
            comps.axes.Y.container.height = comps.clusters.container.height;

            core.cycletime = 0;
        },
        //background draw
        'drawBackground': function () {
            var core = blueview.core;
            var sets = blueview.settings;
            var data = blueview.data;
            var comps = blueview.components;
            var util = blueview.util;
            var graphs = blueview.graphics;
            var evts = blueview.events;
            var acts = blueview.actions;

            //container
            var container = comps.background.container;
            container.removeAllChildren();

            //rect
            var rect = graphs.drawRect(sets.background.color, sets.background.border, 0, 0, container.width, container.height);
            comps.background.rect = rect;

            container.addChild(rect);
        },
        'drawTitle': function () {
            var core = blueview.core;
            var sets = blueview.settings;
            var data = blueview.data;
            var comps = blueview.components;
            var util = blueview.util;
            var graphs = blueview.graphics;
            var evts = blueview.events;
            var acts = blueview.actions;

            //container
            var container = comps.title.container;
            container.removeAllChildren();

            //rect
            var rect = graphs.drawRect(null, sets.title.border, 0, 0, container.width, container.height);
            comps.title.rect = rect;

            container.addChild(rect);

            //text
            var text = graphs.drawText("P: " + data.proposition, sets.title.style, sets.title.color);
            comps.title.text = text;

            text.lineWidth = container.width - sets.title.padding;
            text.mask = new createjs.Shape();
            text.mask.graphics.drawRect(sets.title.padding, sets.title.padding, container.width - (sets.title.padding * 2), container.height - (sets.title.padding * 2));

            util.setTooltip(comps.canvas, text, text.text);

            container.addChild(text);

            if (text.getMetrics().width < container.width) {
                text.x = container.width / 2 - text.getMetrics().width / 2;
            }
            if (text.getMetrics().height < container.height) {
                text.textBaseline = 'top';
                text.y = container.height / 2 - text.getMetrics().height / 2;
            }
        },
        'drawAxes': function () {
            var core = blueview.core;
            var sets = blueview.settings;
            var data = blueview.data;
            var comps = blueview.components;
            var util = blueview.util;
            var graphs = blueview.graphics;
            var evts = blueview.events;
            var acts = blueview.actions;

            //containers
            var containerX = comps.axes.X.container;
            containerX.removeAllChildren();
            comps.axes.X.count = 0;

//            //border
//            var rect = graphs.drawRect(null, sets.title.border, 0, 0, containerX.width, containerX.height);
//            containerX.addChild(rect);

            var containerY = comps.axes.Y.container;
            containerY.removeAllChildren();
            comps.axes.Y.count = 0;

            //border
//            var rect = graphs.drawRect(null, sets.title.border, 0, 0, containerY.width, containerY.height);
//            containerY.addChild(rect);

            if (!core.galaxyOfNodes) {
                //limits
                var axisWidth = containerX.width / data.axes.X.length;
                var axisHeight = containerY.height / data.axes.Y.length;
                
                //Tamanho
                for (var i in data.axes.X) {
                    var axis = data.axes.X[i];
                    
                    var qtdNodes = 1;

                    for (var j in blueview.data.nodes) {
                        var node = blueview.data.nodes[j];

                        for (var k in data.clusters) {
                            var cluster = blueview.data.clusters[k];
                            
                            if (cluster.id == node.group && cluster.X == i) {
                                qtdNodes++;
                            }
                        }
                    }
                    
                    data.axes.X[i].nodes = qtdNodes;
                }
                
                //X
                var x = 0;
                for (var i in data.axes.X) {
                    var axis = data.axes.X[i];
                    
                    //item
                    var item = {'data': axis, 'container': null, 'icon': {'container': null, 'circle': null, 'text': null}, 'text': {'container': null, 'text': null}};
                    comps.axes.X.items[comps.axes.X.count++] = item;

                    //container
                    var container = new createjs.Container();
                    
                    container.x = x;
                    container.y = 0;
                    container.width = core.cellAdjust ? containerX.width * axis.nodes / (blueview.data.nodes.length + data.axes.X.length) : axisWidth;
                    container.height = sets.axis.size;
                    
                    x = container.x + container.width;

                    data.grid.X[i] = container.x;

                    item.container = container;
                    containerX.addChild(container);

                    //                //border
                    //                var rect = graphs.drawRect(null, sets.title.border, 0, 0, container.width, container.height);
                    //                container.addChild(rect);

                    //icon
                    var iconContainer = new createjs.Container();

                    iconContainer.x = sets.axis.icon.margin;
                    iconContainer.y = sets.axis.size / 2 - sets.axis.icon.radius;
                    iconContainer.width = sets.axis.icon.radius * 2;
                    iconContainer.height = sets.axis.icon.radius * 2;

                    item.icon.container = iconContainer;
                    container.addChild(iconContainer);

                    //icon circle
                    var circle = graphs.drawCircle(axis.color, sets.axis.icon.border, sets.axis.icon.radius);
                    item.icon.circle = circle;
                    iconContainer.addChild(circle);

                    //icon text
                    var text = graphs.drawText(axis.name, sets.axis.icon.style, sets.axis.icon.color);
                    text.textBaseline = 'middle';
                    text.textAlign = 'center';
                    text.x = iconContainer.width / 2;
                    text.y = iconContainer.height / 2;

                    item.icon.text = text;
                    iconContainer.addChild(text);

                    util.setTooltip(comps.canvas, iconContainer, axis.fullname);

                    //text container
                    var textContainer = new createjs.Container();

                    textContainer.x = iconContainer.x + (sets.axis.icon.radius * 2) + sets.axis.icon.margin;
                    textContainer.y = iconContainer.y + sets.axis.icon.radius;
                    textContainer.width = container.width - iconContainer.width - (sets.axis.icon.margin * 3);
                    textContainer.height = iconContainer.height;

                    item.text.container = textContainer;
                    container.addChild(textContainer);

                    //text
                    var text = graphs.drawText(axis.fullname, sets.axis.style, sets.axis.color);
                    text.textBaseline = 'middle';
                    text.textAlign = 'left';

                    text.mask = new createjs.Shape();
                    text.mask.graphics.drawRect(0, -sets.axis.icon.radius, textContainer.width + (sets.axis.icon.radius / 2) - 5, sets.axis.icon.radius * 2);

                    item.text.text = text;
                    textContainer.addChild(text);

                    var labelsize = iconContainer.width + text.getMetrics().width + sets.axis.icon.margin;
                    if (labelsize < container.width) {
                        iconContainer.x = container.width / 2 - labelsize / 2;
                        textContainer.x = iconContainer.x + (sets.axis.icon.radius * 2) + sets.axis.icon.margin;
                        textContainer.width = container.width - (textContainer.x);

                        var text = graphs.drawText(axis.fullname, sets.axis.style, sets.axis.color);
                        text.textBaseline = 'middle';
                        text.textAlign = 'left';

                        text.mask = new createjs.Shape();
                        text.mask.graphics.drawRect(0, -sets.axis.icon.radius, textContainer.width + (sets.axis.icon.radius / 2) - 5, sets.axis.icon.radius * 2);

                        item.text.text = text;
                        textContainer.addChild(text);
                    }
                }

                data.grid.X[i * 1 + 1] = containerX.width;

                //Tamanho
                for (var i in data.axes.Y) {
                    var axis = data.axes.Y[i];
                    
                    var qtdNodes = 1;

                    for (var j in blueview.data.nodes) {
                        var node = blueview.data.nodes[j];

                        for (var k in data.clusters) {
                            var cluster = blueview.data.clusters[k];
                            
                            if (cluster.id == node.group && cluster.Y == i) {
                                qtdNodes++;
                            }
                        }
                    }
                    
                    data.axes.Y[i].nodes = qtdNodes;
                }

                //Y
                var y = 0;
                for (var i in data.axes.Y) {
                    var axis = data.axes.Y[i];

                    //item
                    var item = {'data': axis, 'container': null, 'icon': {'container': null, 'circle': null, 'text': null}, 'text': {'container': null, 'text': null}};
                    comps.axes.Y.items[comps.axes.Y.count++] = item;

                    //container
                    var container = new createjs.Container();

                    container.data = axis;
                    container.x = 0;
                    container.y = y;
                    container.width = sets.axis.size;
                    container.height = core.cellAdjust ? containerY.height * axis.nodes / (blueview.data.nodes.length + data.axes.Y.length) : axisHeight;
                    
                    y = container.y + container.height;

                    data.grid.Y[i] = container.y;

                    item.container = container;
                    containerY.addChild(container);

                    //                //border
                    //                var rect = graphs.drawRect(null, sets.title.border, 0, 0, container.width, container.height);
                    //                container.addChild(rect);

                    //icon
                    var iconContainer = new createjs.Container();

                    iconContainer.x = sets.axis.size / 2 - sets.axis.icon.radius;
                    iconContainer.y = sets.axis.icon.margin;
                    iconContainer.width = sets.axis.icon.radius * 2;
                    iconContainer.height = sets.axis.icon.radius * 2;

                    item.icon.container = iconContainer;
                    container.addChild(iconContainer);

                    //icon circle
                    var circle = graphs.drawCircle(axis.color, sets.axis.icon.border, sets.axis.icon.radius);
                    item.icon.circle = circle;
                    iconContainer.addChild(circle);

                    //icon text
                    var text = graphs.drawText(axis.name, sets.axis.icon.style, sets.axis.icon.color);
                    text.textBaseline = 'middle';
                    text.textAlign = 'center';
                    text.x = iconContainer.width / 2;
                    text.y = iconContainer.height / 2;

                    item.icon.text = text;
                    iconContainer.addChild(text);

                    util.setTooltip(comps.canvas, iconContainer, axis.fullname);

                    //text container
                    var textContainer = new createjs.Container();

                    textContainer.x = iconContainer.x + sets.axis.icon.radius;
                    textContainer.y = iconContainer.y + (sets.axis.icon.radius * 2) + sets.axis.icon.margin;
                    textContainer.width = container.height - iconContainer.width - (sets.axis.icon.margin * 3);
                    textContainer.height = iconContainer.height;

                    item.text.container = textContainer;
                    container.addChild(textContainer);

                    //texts
                    var text = graphs.drawText(axis.fullname, sets.axis.style, sets.axis.color);
                    text.textBaseline = 'middle';
                    text.textAlign = 'left';

                    text.mask = new createjs.Shape();
                    text.mask.graphics.drawRect(0, -sets.axis.icon.radius, textContainer.width + (sets.axis.icon.radius / 2) - 5, sets.axis.icon.radius * 2);

                    item.text.text = text;
                    textContainer.addChild(text);

                    textContainer.rotation = 90;

                    var labelsize = iconContainer.height + text.getMetrics().width + sets.axis.icon.margin;
                    if (labelsize < container.height) {
                        iconContainer.y = container.height / 2 - labelsize / 2;
                        textContainer.y = iconContainer.y + (sets.axis.icon.radius * 2) + sets.axis.icon.margin;
                        textContainer.width = container.height - textContainer.y;

                        textContainer.removeAllChildren();

                        var text = graphs.drawText(axis.fullname, sets.axis.style, sets.axis.color);
                        text.textBaseline = 'middle';
                        text.textAlign = 'left';

                        text.mask = new createjs.Shape();
                        text.mask.graphics.drawRect(0, -sets.axis.icon.radius, textContainer.width + (sets.axis.icon.radius / 2) - 5, sets.axis.icon.radius * 2);

                        item.text.text = text;
                        textContainer.addChild(text);

                        textContainer.rotation = 90;
                    }
                }

                data.grid.Y[i * 1 + 1] = containerY.height;
            }
        },
        'redrawAxes': function () {
            var core = blueview.core;
            var sets = blueview.settings;
            var data = blueview.data;
            var comps = blueview.components;
            var util = blueview.util;
            var graphs = blueview.graphics;
            var evts = blueview.events;
            var acts = blueview.actions;

            //containers
            var containerX = comps.axes.X.container;
            var containerY = comps.axes.Y.container;

            for (var i in comps.axes.X.items) {
                var axis = data.axes.X[i];

                //item
                var item = comps.axes.X.items[i];

                //container
                var container = item.container;
                container.removeAllChildren();

                container.x = data.grid.X[i];
                container.width = data.grid.X[i * 1 + 1] - container.x;

                //icon
                var iconContainer = new createjs.Container();

                iconContainer.x = sets.axis.icon.margin;
                iconContainer.y = sets.axis.size / 2 - sets.axis.icon.radius;
                iconContainer.width = sets.axis.icon.radius * 2;
                iconContainer.height = sets.axis.icon.radius * 2;

                item.icon.container = iconContainer;
                container.addChild(iconContainer);

                //icon circle
                var circle = graphs.drawCircle(axis.color, sets.axis.icon.border, sets.axis.icon.radius);
                item.icon.circle = circle;
                iconContainer.addChild(circle);

                //icon text
                var text = graphs.drawText(axis.name, sets.axis.icon.style, sets.axis.icon.color);
                text.textBaseline = 'middle';
                text.textAlign = 'center';
                text.x = iconContainer.width / 2;
                text.y = iconContainer.height / 2;

                item.icon.text = text;
                iconContainer.addChild(text);

                util.setTooltip(comps.canvas, iconContainer, axis.fullname);

                //text container
                var textContainer = new createjs.Container();

                textContainer.x = iconContainer.x + (sets.axis.icon.radius * 2) + sets.axis.icon.margin;
                textContainer.y = iconContainer.y + sets.axis.icon.radius;
                textContainer.width = container.width - iconContainer.width - (sets.axis.icon.margin * 3);
                textContainer.height = iconContainer.height;

                item.text.container = textContainer;
                container.addChild(textContainer);

                //text
                var text = graphs.drawText(axis.fullname, sets.axis.style, sets.axis.color);
                text.textBaseline = 'middle';
                text.textAlign = 'left';

                text.mask = new createjs.Shape();
                text.mask.graphics.drawRect(0, -sets.axis.icon.radius, textContainer.width + (sets.axis.icon.radius / 2) - 5, sets.axis.icon.radius * 2);

                item.text.text = text;
                textContainer.addChild(text);

                var labelsize = iconContainer.width + text.getMetrics().width + sets.axis.icon.margin;
                if (labelsize < container.width) {
                    iconContainer.x = container.width / 2 - labelsize / 2;
                    textContainer.x = iconContainer.x + (sets.axis.icon.radius * 2) + sets.axis.icon.margin;
                    textContainer.width = container.width - (textContainer.x);

                    var text = graphs.drawText(axis.fullname, sets.axis.style, sets.axis.color);
                    text.textBaseline = 'middle';
                    text.textAlign = 'left';

                    text.mask = new createjs.Shape();
                    text.mask.graphics.drawRect(0, -sets.axis.icon.radius, textContainer.width + (sets.axis.icon.radius / 2) - 5, sets.axis.icon.radius * 2);

                    item.text.text = text;
                    textContainer.addChild(text);
                }
            }

            for (var i in comps.axes.Y.items) {
                var axis = data.axes.Y[i];

                //item
                var item = comps.axes.Y.items[i];

                //container
                var container = item.container;
                container.removeAllChildren();

                container.y = data.grid.Y[i];
                container.height = data.grid.Y[i * 1 + 1] - container.y;

                //icon
                var iconContainer = new createjs.Container();

                iconContainer.x = sets.axis.size / 2 - sets.axis.icon.radius;
                iconContainer.y = sets.axis.icon.margin;
                iconContainer.width = sets.axis.icon.radius * 2;
                iconContainer.height = sets.axis.icon.radius * 2;

                item.icon.container = iconContainer;
                container.addChild(iconContainer);

                //icon circle
                var circle = graphs.drawCircle(axis.color, sets.axis.icon.border, sets.axis.icon.radius);
                item.icon.circle = circle;
                iconContainer.addChild(circle);

                //icon text
                var text = graphs.drawText(axis.name, sets.axis.icon.style, sets.axis.icon.color);
                text.textBaseline = 'middle';
                text.textAlign = 'center';
                text.x = iconContainer.width / 2;
                text.y = iconContainer.height / 2;

                item.icon.text = text;
                iconContainer.addChild(text);

                util.setTooltip(comps.canvas, iconContainer, axis.fullname);

                //text container
                var textContainer = new createjs.Container();

                textContainer.x = iconContainer.x + sets.axis.icon.radius;
                textContainer.y = iconContainer.y + (sets.axis.icon.radius * 2) + sets.axis.icon.margin;
                textContainer.width = container.height - iconContainer.width - (sets.axis.icon.margin * 3);
                textContainer.height = iconContainer.height;

                item.text.container = textContainer;
                container.addChild(textContainer);

                //texts
                var text = graphs.drawText(axis.fullname, sets.axis.style, sets.axis.color);
                text.textBaseline = 'middle';
                text.textAlign = 'left';

                text.mask = new createjs.Shape();
                text.mask.graphics.drawRect(0, -sets.axis.icon.radius, textContainer.width + (sets.axis.icon.radius / 2) - 5, sets.axis.icon.radius * 2);

                item.text.text = text;
                textContainer.addChild(text);

                textContainer.rotation = 90;

                var labelsize = iconContainer.height + text.getMetrics().width + sets.axis.icon.margin;
                if (labelsize < container.height) {
                    iconContainer.y = container.height / 2 - labelsize / 2;
                    textContainer.y = iconContainer.y + (sets.axis.icon.radius * 2) + sets.axis.icon.margin;
                    textContainer.width = container.height - textContainer.y;

                    textContainer.removeAllChildren();

                    var text = graphs.drawText(axis.fullname, sets.axis.style, sets.axis.color);
                    text.textBaseline = 'middle';
                    text.textAlign = 'left';

                    text.mask = new createjs.Shape();
                    text.mask.graphics.drawRect(0, -sets.axis.icon.radius, textContainer.width + (sets.axis.icon.radius / 2) - 5, sets.axis.icon.radius * 2);

                    item.text.text = text;
                    textContainer.addChild(text);

                    textContainer.rotation = 90;
                }
            }
        },
        'drawGroups': function () {
            var core = blueview.core;
            var sets = blueview.settings;
            var data = blueview.data;
            var comps = blueview.components;
            var util = blueview.util;
            var graphs = blueview.graphics;
            var evts = blueview.events;
            var acts = blueview.actions;

            //container
            var container = comps.clusters.container;
            container.removeAllChildren();
            comps.clusters.count = 0;

            //border
//            var rect = graphs.drawRect(null, sets.title.border, 0, 0, container.width, container.height);
//            container.addChild(rect);

            for (var i in data.clusters) {
                var cluster = data.clusters[i];

                var axisX = comps.axes.X.items[cluster.X].container.x;
                var axisY = comps.axes.Y.items[cluster.Y].container.y;
                var axisW = comps.axes.X.items[cluster.X].container.width;
                var axisH = comps.axes.Y.items[cluster.Y].container.height;

                var item = {'data': cluster, 'container': null, 'rect': null};
                comps.clusters.items[comps.clusters.count++] = item;

                //cluster container
                var clusterContainer = new createjs.Container();

                container.data = cluster;

                if (!core.galaxyOfNodes) {
                    clusterContainer.x = axisX;
                    clusterContainer.y = axisY;
                    clusterContainer.width = axisW;
                    clusterContainer.height = axisH;
                } else {
                    clusterContainer.x = 0;
                    clusterContainer.y = 0;
                    clusterContainer.width = container.width;
                    clusterContainer.height = container.height;
                }

//                //border
//                var rect = graphs.drawRect(null, sets.title.border, 0, 0, clusterContainer.width, clusterContainer.height);
//                clusterContainer.addChild(rect);

                item.container = clusterContainer;
                container.addChild(clusterContainer);

                //rect
                var rect = graphs.drawRect(!core.galaxyOfNodes ? cluster.color : '#000', sets.group.border, 0, 0, clusterContainer.width, clusterContainer.height);

                if (!core.galaxyOfNodes) {
                    rect.alpha = sets.group.alpha;
                }

                item.rect = rect;
                clusterContainer.addChild(rect);
            }
        },
        'drawNodes': function () {
            var core = blueview.core;
            var sets = blueview.settings;
            var data = blueview.data;
            var comps = blueview.components;
            var util = blueview.util;
            var graphs = blueview.graphics;
            var evts = blueview.events;
            var acts = blueview.actions;

            //container
            var container = comps.nodes.container;
            container.removeAllChildren();
            comps.nodes.count = 0;

//            //border
//            var rect = graphs.drawRect(null, sets.title.border, 0, 0, container.width, container.height);
//            container.addChild(rect);

            //labels container
            var labelsContainer = comps.nodeLabels.container;
            labelsContainer.removeAllChildren();

//            //border
//            var rect = graphs.drawRect(null, sets.title.border, 0, 0, labelsContainer.width, labelsContainer.height);
//            labelsContainer.addChild(rect);

            for (var i in blueview.data.nodes) {
                var node = blueview.data.nodes[i];

                var item = {'data': node, 'container': null, 'circle': {'container': null, 'circle': null, 'text': null}, 'label': {'container': null, 'rect': null, 'text': null}};
                comps.nodes.items[comps.nodes.count++] = item;

                //cluster
                var cluster = util.getClusterById(comps.clusters.items, node.group);

                //node container
                var nodeContainer = new createjs.Container();

                nodeContainer.data = node;
                nodeContainer.x = (cluster.container.x - sets.node.circle.radius) + (cluster.container.width / 2);
                nodeContainer.y = (cluster.container.y - sets.node.circle.radius) + (cluster.container.height / 2);

                nodeContainer.x += -(cluster.container.width * .1) + (Math.random() * cluster.container.width * .2);
                nodeContainer.y += -(cluster.container.height * .1) + (Math.random() * cluster.container.height * .2);

                nodeContainer.ix = nodeContainer.x;
                nodeContainer.iy = nodeContainer.y;

                nodeContainer.width = sets.node.circle.radius * 2;
                nodeContainer.height = sets.node.circle.radius * 2;

                item.container = nodeContainer;
                container.addChild(nodeContainer);

                //border
//                var rect = graphs.drawRect(null, sets.title.border, 0, 0, nodeContainer.width, nodeContainer.height);
//                nodeContainer.addChild(rect);

                if (core.galaxyOfNodes) {
                    var x = sets.node.circle.radius - sets.node.galaxy.width / 2;
                    var y = sets.node.circle.radius - sets.node.galaxy.height / 2;
                    var w = sets.node.galaxy.width;
                    var h = sets.node.galaxy.height;

                    var rect = graphs.drawRect(cluster.data.color, null, x, y, w, h);

                    rect.alpha = sets.node.galaxy.alpha;

                    nodeContainer.addChild(rect);
                }

                //circle container
                var circleContainer = new createjs.Container();

                circleContainer.width = sets.node.circle.radius * 2;
                circleContainer.height = sets.node.circle.radius * 2;

                item.circle.container = circleContainer;
                nodeContainer.addChild(circleContainer);

                //circle
                var circle = graphs.drawCircle(cluster.data.color, sets.node.border, sets.node.circle.radius);

                circle.cursor = "pointer";

                item.circle.circle = circle;
                circleContainer.addChild(circle);

                //circle text
                var circleText = graphs.drawText((i * 1 + 1), sets.node.circle.label.style, sets.node.circle.label.color);
                circleText.textAlign = 'center';
                circleText.textBaselign = 'top';
                circleText.x = sets.node.circle.radius;
                circleText.y = 1;

                item.circle.text = circle;
                circleContainer.addChild(circleText);

                //label container
                var labelContainer = new createjs.Container();

                labelContainer.width = sets.node.label.width;
                labelContainer.height = 50;
                labelContainer.x = nodeContainer.x;
                labelContainer.y = nodeContainer.y;

                labelContainer.x -= (labelContainer.width / 2) - (sets.node.circle.radius);

                item.label.container = labelContainer;
                nodeContainer.label = labelContainer;
                labelsContainer.addChild(labelContainer);

//                //border
//                var rect = graphs.drawRect(null, sets.title.border, 0, 0, labelContainer.width, labelContainer.height);
//                labelContainer.addChild(rect);

                //label
                var text = graphs.drawText((i * 1 + 1) + '. ' + node.name, sets.node.label.style, sets.node.label.color);
                text.textAlign = 'center';
                text.textBaselign = 'middle';

                text.x = labelContainer.width / 2;
                text.y = sets.node.label.padding / 2;
                text.lineWidth = labelContainer.width - sets.node.label.padding;
                text.mask = new createjs.Shape();
                text.mask.graphics.drawRect(sets.node.label.padding, sets.node.label.padding, labelContainer.width - (sets.node.label.padding * 2), labelContainer.height - (sets.node.label.padding * 2));

                item.label.text = text;

                labelContainer.height = text.getMetrics().height + (sets.node.label.padding * 2);
                labelContainer.y -= labelContainer.height + sets.node.label.distance;

                //label rect
                var rect = graphs.drawRect(sets.node.label.backgroundColor, sets.node.label.border, 0, 0, labelContainer.width, labelContainer.height);

                item.label.rect = rect;
                labelContainer.addChild(rect);

                labelContainer.addChild(text);

                util.setTooltip(comps.canvas, labelsContainer, text.text);

                //drag
                circleContainer.on("pressmove", function (evt) {
                    createjs.Ticker.setPaused(true);
                    blueview.events.onStop();

                    var cluster = util.getClusterById(comps.clusters.items, evt.currentTarget.parent.data.group);

                    var minX = cluster.container.x;
                    var maxX = cluster.container.x + cluster.container.width - (sets.node.circle.radius * 2);
                    var minY = cluster.container.y;
                    var maxY = cluster.container.y + cluster.container.height - (sets.node.circle.radius * 2);

                    var dx = evt.currentTarget.parent.x + evt.localX;
                    var dy = evt.currentTarget.parent.y + evt.localY;

                    if (minX <= dx && dx <= maxX) {
                        evt.currentTarget.parent.x = dx;
                        evt.currentTarget.parent.ix = dx;
                        evt.currentTarget.parent.label.x = dx - (evt.currentTarget.parent.label.width / 2) + (sets.node.circle.radius);
                    }
                    if (minY <= dy && dy <= maxY) {
                        evt.currentTarget.parent.y = dy;
                        evt.currentTarget.parent.iy = dy;
                        evt.currentTarget.parent.label.y = dy - evt.currentTarget.parent.label.height - sets.node.label.distance;
                    }

                    blueview.actions.drawLinks();
                    core.stage.update();
                });
                //mouseover
                circleContainer.on("mouseover", function (evt) {
                    core.hovernode = evt.currentTarget;
                    blueview.actions.drawLinks();
                    core.stage.update();
                });
                //mouseout
                circleContainer.on("mouseout", function (evt) {
                    core.hovernode = null;
                    blueview.actions.drawLinks();
                    core.stage.update();
                });
            }
        },
        'drawLinks': function () {
            var core = blueview.core;
            var sets = blueview.settings;
            var data = blueview.data;
            var comps = blueview.components;
            var util = blueview.util;
            var graphs = blueview.graphics;
            var evts = blueview.events;
            var acts = blueview.actions;

            //container
            var container = comps.links.container;
            container.removeAllChildren();
            comps.links.items = [];
            comps.links.count = 0;

//            //border
//            var rect = graphs.drawRect(null, sets.title.border, 0, 0, container.width, container.height);
//            container.addChild(rect);

            //labels container
            var labelsContainer = comps.linkLabels.container;
            labelsContainer.removeAllChildren();

//            //border
//            var rect = graphs.drawRect(null, sets.title.border, 0, 0, labelsContainer.width, labelsContainer.height);
//            labelsContainer.addChild(rect);

            for (var i in blueview.data.links) {
                var link = blueview.data.links[i];

                var source = util.getNodeById(comps.nodes.items, link.source);
                var target = util.getNodeById(comps.nodes.items, link.target);

                var sourceX = source.container.x + sets.node.circle.radius;
                var sourceY = source.container.y + sets.node.circle.radius;
                var targetX = target.container.x + sets.node.circle.radius;
                var targetY = target.container.y + sets.node.circle.radius;

                var item = {'data': link, 'container': null, 'line': null, 'arrow': null, 'label': {'container': null, 'rect': null, 'text': null}};
                comps.links.items[comps.links.count++] = item;

                //link container
                var linkContainer = new createjs.Container();

                linkContainer.data = link;
                item.container = linkContainer;
                container.addChild(linkContainer);

                if (core.hovernode) {
                    if (source.data.id != core.hovernode.parent.data.id && target.data.id != core.hovernode.parent.data.id) {
                        linkContainer.alpha = .2;
                    }
                }

                //type
                var type = util.getLinkTypeById(data.linkTypes, link.type);

                //label container
                var labelContainer = new createjs.Container();
                labelContainer.width = sets.link.label.width;
                labelContainer.height = 50;

                item.label.container = labelContainer;

                //label
                var text = graphs.drawText(link.name, sets.link.label.style, sets.link.label.color);
                text.textAlign = 'center';
                text.textBaselign = 'middle';

                text.x = labelContainer.width / 2;
                text.y = sets.link.label.padding / 2;
                text.lineWidth = labelContainer.width - sets.link.label.padding;
                text.mask = new createjs.Shape();
                text.mask.graphics.drawRect(sets.link.label.padding, sets.link.label.padding, labelContainer.width - (sets.link.label.padding * 2), labelContainer.height - (sets.link.label.padding * 2));

                item.label.text = text;

                labelContainer.height = text.getMetrics().height + (sets.link.label.padding * 2);

                //label rect
                var rect = graphs.drawRect(sets.link.label.backgroundColor, sets.link.label.border, 0, 0, labelContainer.width, labelContainer.height);

                //line
                var mx = (sourceX + targetX) / 2;
                var my = (sourceY + targetY) / 2;
                var angle = util.angle(sourceX, sourceY, targetX, targetY);
                var dist = util.distance(sourceX, sourceY, targetX, targetY) / sets.link.curve;

                var cx = (Math.sin(angle) * (dist * 2) + mx) + (util.hash(link.name) / 3);
                var cy = (-Math.cos(angle) * (dist * 2) + my) + (util.hash(link.name) / 3);
                var cfx = (Math.sin(angle) * dist + mx + (util.hash(link.name) / 5));
                var cfy = (-Math.cos(angle) * dist + my + (util.hash(link.name) / 5));

                if (util.distance(container.width / 2, container.height / 2, cx, cy) < util.distance(container.width / 2, container.height / 2, mx, my)) {
                    dist = -dist;

                    cx = (Math.sin(angle) * (dist * 2) + mx);
                    cy = (-Math.cos(angle) * (dist * 2) + my);
                    cfx = (Math.sin(angle) * dist + mx);
                    cfy = (-Math.cos(angle) * dist + my);
                }

                if (cfx < (labelContainer.width / 2)) {
                    cfx = (labelContainer.width / 2);
                    cx = (cfx * 2 - mx);
                }
                if (cfx > ((container.width * .95) - (labelContainer.width / 2))) {
                    cfx = (container.width * .95) - (labelContainer.width / 2);
                    cx = (cfx * 2 - mx);
                }
                if (cfy < (labelContainer.height / 2)) {
                    cfy = (labelContainer.height / 2);
                    cy = (cfy * 2 - my);
                }
                if (cfy > ((container.height * .95) - (labelContainer.height / 2))) {
                    cfy = (container.height * .95) - (labelContainer.height / 2);
                    cy = (cfy * 2 - my);
                }

                var line = graphs.drawCurve(!core.galaxyOfNodes ? type.color : '#FFF', sets.link.size, type.style, sourceX, sourceY, cx, cy, targetX, targetY);
//                var line = graphs.drawLine(type.color, sets.link.size, type.style, sourceX, sourceY, targetX, targetY);

                item.line = line;
                linkContainer.addChild(line);

                //arrow
                var arrowpos = util.attraction(targetX, targetY, cx, cy, sets.node.circle.radius);

                var arrow = graphs.drawArrow(!core.galaxyOfNodes ? sets.link.arrow.color : '#FFF', sets.link.arrow.border, arrowpos.x, arrowpos.y,
                        sets.link.arrow.width, sets.link.arrow.height, util.angle(cx, cy, targetX, targetY));

                item.arrow = arrow;
                linkContainer.addChild(arrow);

                //label position
                labelContainer.x = cfx - (labelContainer.width / 2);
                labelContainer.y = cfy - (labelContainer.height / 2);

                item.label.rect = rect;
                labelContainer.addChild(rect);

                labelContainer.addChild(text);

                labelsContainer.addChild(labelContainer);

                util.setTooltip(comps.canvas, linkContainer, link.name);
            }
        }
    };

    //inicialização do canvas
    this.init = function (width, height) {
        var core = blueview.core;
        var sets = blueview.settings;
        var data = blueview.data;
        var comps = blueview.components;
        var util = blueview.util;
        var graphs = blueview.graphics;
        var evts = blueview.events;
        var acts = blueview.actions;

        //stage
        acts.setupStage();

        //canvas
        acts.setupCanvas(width, height);

        //preload
        core.preload = new createjs.LoadQueue(true);
        core.preload.on("fileload", evts.onDataLoaded);
        core.preload.on("error", evts.onLoadError);

        //stage container
        acts.setupContainer();

        //ticker
        createjs.Ticker.setFPS(sets.fps);
    };

    //carregar dados
    this.loadData = function (src) {
        var core = blueview.core;
        var sets = blueview.settings;
        var data = blueview.data;
        var comps = blueview.components;
        var util = blueview.util;
        var graphs = blueview.graphics;
        var evts = blueview.events;
        var acts = blueview.actions;

        core.preload.loadFile({'src': src}, true);
    };

    //recriar o grafo
    this.redraw = function (width, height) {
        var core = blueview.core;
        var sets = blueview.settings;
        var data = blueview.data;
        var comps = blueview.components;
        var util = blueview.util;
        var graphs = blueview.graphics;
        var evts = blueview.events;
        var acts = blueview.actions;

        createjs.Ticker.setPaused(true);

        acts.setupCanvas(width, height);
        acts.setupContainer();

        if (data.loaded) {
            acts.drawBackground();
            acts.drawTitle();
            acts.drawAxes();
            acts.drawGroups();
            acts.drawNodes();
            acts.drawLinks();
        }

        core.stage.update();

        createjs.Ticker.setPaused(false);
        blueview.events.onStart();
    };

    //exibir/ocultar rótulos dos nós
    this.toggleNodeLabels = function () {
        var core = blueview.core;
        var sets = blueview.settings;
        var data = blueview.data;
        var comps = blueview.components;
        var util = blueview.util;
        var graphs = blueview.graphics;
        var evts = blueview.events;
        var acts = blueview.actions;

        if (comps.nodeLabels.container.alpha == 1) {
            comps.nodeLabels.container.alpha = 0;
        } else {
            comps.nodeLabels.container.alpha = 1;
        }

        core.stage.update();
    };

    //exibir/ocultar rótulos das relações
    this.toggleLinkLabels = function () {
        var core = blueview.core;
        var sets = blueview.settings;
        var data = blueview.data;
        var comps = blueview.components;
        var util = blueview.util;
        var graphs = blueview.graphics;
        var evts = blueview.events;
        var acts = blueview.actions;

        if (comps.linkLabels.container.alpha == 1) {
            comps.linkLabels.container.alpha = 0;
        } else {
            comps.linkLabels.container.alpha = 1;
        }

        core.stage.update();
    };

    //ativar ajuste das células
    this.enableCellAdjust = function () {
        var core = blueview.core;
        var sets = blueview.settings;
        var data = blueview.data;
        var comps = blueview.components;
        var util = blueview.util;
        var graphs = blueview.graphics;
        var evts = blueview.events;
        var acts = blueview.actions;

        core.galaxyOfNodes = false;
        comps.linkLabels.container.alpha = 1;
        comps.nodeLabels.container.alpha = 1;

        core.cellAdjust = true;

        createjs.Ticker.setPaused(true);

        acts.setupContainer();

        if (data.loaded) {
            acts.drawBackground();
            acts.drawTitle();
            acts.drawAxes();
            acts.drawGroups();
            acts.drawNodes();
            acts.drawLinks();
        }

        core.stage.update();

        createjs.Ticker.setPaused(false);
        blueview.events.onStart();
    };

    //desativar ajuste das células
    this.disableCellAdjust = function () {
        var core = blueview.core;
        var sets = blueview.settings;
        var data = blueview.data;
        var comps = blueview.components;
        var util = blueview.util;
        var graphs = blueview.graphics;
        var evts = blueview.events;
        var acts = blueview.actions;

        core.galaxyOfNodes = false;
        comps.linkLabels.container.alpha = 1;
        comps.nodeLabels.container.alpha = 1;

        core.cellAdjust = false;

        createjs.Ticker.setPaused(true);

        acts.setupContainer();

        if (data.loaded) {
            acts.drawBackground();
            acts.drawTitle();
            acts.drawAxes();
            acts.drawGroups();
            acts.drawNodes();
            acts.drawLinks();
        }

        core.stage.update();

        createjs.Ticker.setPaused(false);
        blueview.events.onStart();
    };

    //ativar/desativar galaxy of nodes
    this.enableGalaxyOfNodes = function () {
        var core = blueview.core;
        var sets = blueview.settings;
        var data = blueview.data;
        var comps = blueview.components;
        var util = blueview.util;
        var graphs = blueview.graphics;
        var evts = blueview.events;
        var acts = blueview.actions;

        core.cellAdjust = false;

        core.galaxyOfNodes = true;

        comps.linkLabels.container.alpha = 0;
        comps.nodeLabels.container.alpha = 0;

        createjs.Ticker.setPaused(true);

        acts.setupContainer();

        if (data.loaded) {
            acts.drawBackground();
            acts.drawTitle();
            acts.drawAxes();
            acts.drawGroups();
            acts.drawNodes();
            acts.drawLinks();
        }

        core.stage.update();

        createjs.Ticker.setPaused(false);
        blueview.events.onStart();
    };

    //organizar
    this.toggleRun = function () {
        var core = blueview.core;
        var sets = blueview.settings;
        var data = blueview.data;
        var comps = blueview.components;
        var util = blueview.util;
        var graphs = blueview.graphics;
        var evts = blueview.events;
        var acts = blueview.actions;

        if (createjs.Ticker.getPaused()) {
            createjs.Ticker.setPaused(false);
        } else {
            createjs.Ticker.setPaused(true);
        }
    };
};