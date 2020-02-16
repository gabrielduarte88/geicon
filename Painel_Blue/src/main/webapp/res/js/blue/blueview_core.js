var blueViewCore = {
    'core': {
        'width': 0,
        'height': 0,
        'preload': null,
        'cellAdjust': false,
        'galaxyOfNodes': false,
        'stage': null,
        'hovernode': null,
        'cycletime': 1 //tempo do ciclo atual
    },
    //SETTINGS
    'settings': {
        'margin': 0,
        'fps': 60,
        'force': {
            'gravity': 3, //gravidade
            'cycletime': 100 //tempo de um ciclo
        },
        'background': {
            'color': '#FFF',
            'border': null
        },
        'title': {
            'height': 60,
            'padding': 5,
            'style': "18px Arial",
            'color': "#000",
            'border': {
                'size': 1,
                'color': '#000'
            }
        },
        'axis': {
            'size': 50,
            'style': "13px Arial",
            'color': "#333",
            'icon': {
                'radius': 15,
                'margin': 5,
                'style': "18px Arial",
                'color': "#FFF",
                'border': null
            }
        },
        'group': {
            'alpha': .2,
            'border': {
                'size': .1,
                'color': '#333'
            }
        },
        'node': {
            'border': {
                'size': 1,
                'color': 'rgba(0, 0, 0, .1)'
            },
            'label': {
                'width': 100,
                'padding': 2,
                'distance': 5,
                'style': '11px Arial',
                'color': '#000',
                'backgroundColor': "rgba(255, 255, 255, 1)",
                'border': {
                    'size': .1,
                    'color': '#333'
                }
            },
            'circle': {
                'radius': 7,
                'label': {
                    'style': '10px Arial',
                    'color': '#333'
                }
            },
            'galaxy': {
                'width': 100,
                'height': 80,
                'alpha': .1
            }
        },
        'link': {
            'size': .5,
            'color': '#000',
            'curve': 8,
            'label': {
                'width': 100,
                'padding': 2,
                'distance': 5,
                'style': '11px Arial',
                'color': '#333',
                'backgroundColor': "rgba(240, 240, 240, .8)",
                'border': {
                    'size': .1,
                    'color': '#333'
                }
            },
            'arrow': {
                'width': 4,
                'height': 8,
                'color': '#000',
                'border': {
                    'size': 0,
                    'color': '#000'
                }
            }
        },
        'events': {
            'onDataLoadad': function () {},
            'onDataLoadError': function () {},
            'onStar': function () {},
            'onStop': function () {}
        }
    },
    //DATA
    'data': {
        'loaded': false,
        'proposition': '',
        'grid': {
            'X': [],
            'Y': []
        },
        'axes': {
            'X': [],
            'Y': []
        },
        'linkTypes': [],
        'clusters': [],
        'nodes': [],
        'links': []
    },
    //COMPONENTS
    'components': {
        'canvas': null,
        'container': null,
        'background': {
            'container': null,
            'rect': null
        },
        'main': {
            'container': null
        },
        'title': {
            'container': null,
            'rect': null,
            'text': null
        },
        'axes': {
            'container': null,
            'X': {
                'container': null,
                'items': [], //{'data': null, 'container': null, 'icon': {'container': null, 'circle': null, 'text': null}, 'text': {'container': null, 'text': null}}
                'count': 0
            },
            'Y': {
                'container': null,
                'items': [], //{'data': null, 'container': null, 'icon': {'container': null, 'circle': null, 'text': null}, 'text': {'container': null, 'text': null}}
                'count': 0
            }
        },
        'clusters': {
            'container': null,
            'items': [], //{'data': null, 'container': null, 'rect': null}
            'count': 0
        },
        'nodes': {
            'container': null,
            'items': [], //{'data': null, 'container': null, 'circle': {'container': null, 'circle': null, 'text': null}, 'label': {'container': null, 'rect': null, 'text': null}}
            'count': 0
        },
        'links': {
            'container': null,
            'items': [], //{'data': null, 'container': null, 'line': null, 'arrow': null, 'label': {'container': null, 'rect': null, 'text': null}}
            'count': 0
        },
        'nodeLabels': {
            'container': null,
            'items': [], //{'container': null, 'rect': null, 'text': null}
            'count': 0
        },
        'linkLabels': {
            'container': null,
            'items': [], //{'container': null, 'rect': null, 'text': null}
            'count': 0
        }
    },
    //UTIL
    'util': {
        //ativar tooltip no elemento
        'setTooltip': function (canvas, element, text) {
            //mouseover
            element.on("mouseover", function (evt) {
                return function (canvas, text) {
                    canvas.title = text;
                }(canvas, text);
            });
            //mouseout
            element.on("mouseout", function (evt) {
                return function (canvas) {
                    canvas.title = '';
                }(canvas);
            });
        },
        //obter cluster por ID
        'getClusterById': function (clusters, id) {
            for (var i in clusters) {
                var cluster = clusters[i];

                if (cluster.data.id == id)
                    return cluster;
            }

            return null;
        },
        //obter nó por ID
        'getNodeById': function (nodes, id) {
            for (var i in nodes) {
                var node = nodes[i];

                if (node.data.id == id)
                    return node;
            }

            return null;
        },
        //obter tipo de link por ID
        'getLinkTypeById': function (linkTypes, id) {
            for (var i in linkTypes) {
                var lt = linkTypes[i];

                if (lt.id == id)
                    return lt;
            }

            return null;
        },
        //Hash
        'hash': function (str) {
            var hash = 0, i, chr, len;
            if (str.length === 0)
                return hash;
            
            for (i = 0, len = str.length; i < len; i++) {
                chr = str.charCodeAt(i);
                if (hash < chr) {
                    hash = chr;
                }   
            }
            
            return hash;
        },
        //Calcula da força de atração
        'fa': function (x, k) {
            return Math.pow(x, 2) / k;
        },
        'fr': function (x, k) {
            return Math.pow(k, 2) / x;
        },
        //Distância entre pontos
        'distance': function (x1, y1, x2, y2) {
            return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        },
        //Angulo
        'angle': function (x1, y1, x2, y2) {
            return Math.atan2(y2 - y1, x2 - x1);
        },
        //Atração
        'attraction': function (x1, y1, x2, y2, factor) {
            var xDist = x1 - x2;
            var yDist = y1 - y2;
            var dist = Math.sqrt(xDist * xDist + yDist * yDist);

            if (dist > 0) {
                x1 -= xDist / dist * factor;
                y1 -= yDist / dist * factor;
            }

            return {x: x1, y: y1};
        },
        'createBasedContainer': function (parent) {
            var container = new createjs.Container();

            container.x = 0;
            container.y = 0;
            container.width = parent.width;
            container.height = parent.height;

            parent.addChild(container);

            return container;
        }
    },
    //GRAPHICS
    'graphics': {
        'drawRect': function (fill, border, x, y, width, height) {
            var rect = new createjs.Shape();

            rect.graphics.beginFill(fill);
            rect.graphics.beginStroke(border ? border.color : null);
            rect.graphics.setStrokeStyle(border ? border.size : .1, 'round', 'round', 10, false);

            rect.graphics.drawRect(x, y, width, height);

            return rect;
        },
        'drawText': function (text, style, color) {
            return new createjs.Text(text, style, color);
        },
        'drawCircle': function (fill, border, radius) {
            var circle = new createjs.Shape();

            circle.graphics.beginFill(fill);
            circle.graphics.beginStroke(border ? border.color : null);
            circle.graphics.setStrokeStyle(border ? border.size : .1, 'round', 'round', 10, false);

            circle.graphics.drawCircle(radius, radius, radius);

            return circle;
        },
        'drawLine': function (color, size, style, x1, y1, x2, y2) {
            var line = new createjs.Shape();

            line.graphics.beginStroke(color);
            line.graphics.setStrokeStyle(size, 'round', 'round', 10, false);
            line.graphics.setStrokeDash(style, 0);
            line.graphics.moveTo(x1, y1);
            line.graphics.lineTo(x2, y2);

            return line;
        },
        'drawCurve': function (color, size, style, x1, y1, cx, cy, x2, y2) {
            var line = new createjs.Shape();

            line.graphics.beginStroke(color);
            line.graphics.setStrokeStyle(size, 'round', 'round', 10, false);
            line.graphics.setStrokeDash(style, 0);
            line.graphics.moveTo(x1, y1);
            line.graphics.curveTo(cx, cy, x2, y2);

            return line;
        },
        'drawArrow': function (color, border, x, y, width, height, angle) {
            var arrow = new createjs.Shape();

            arrow.graphics.beginFill(color);
            arrow.graphics.beginStroke(border.color);
            arrow.graphics.setStrokeStyle(border.size, 'round', 'round', 10, false);
            arrow.graphics.moveTo(-height, -width).lineTo(0, 0).lineTo(-height, width).cp();

            var degree = angle / Math.PI * 180;
            arrow.x = x;
            arrow.y = y;
            arrow.rotation = degree;

            return arrow;
        }
    }
};