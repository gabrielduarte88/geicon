function getKeyCode(a) {
    var b;
    if (document.all) {
        b = a.keyCode;
    } else {
        if (document.layers && navigator.appName.indexOf("Mozilla") == -1) {
            b = a.which;
        } else {
            b = a.which;
        }
    }
    return b;
}

function isEnterDown(a) {
    return(getKeyCode(a) == 13);
}

function getScreenWidth() {
    if (window.innerWidth != null) {
        return window.innerWidth * 1;
    }
    if (document.documentElement != null) {
        return document.documentElement.clientWidth * 1;
    }
    if (document.body != null) {
        return document.body.clientWidth * 1;
    }
    return window.screen.width * 1;
}

function getScreenHeight() {
    if (window.innerHeight != null) {
        return window.innerHeight * 1;
    }
    if (document.documentElement != null) {
        return document.documentElement.clientHeight * 1;
    }
    if (document.body != null) {
        return document.body.clientHeight * 1;
    }
    return window.screen.height * 1;
}

function shuffleArray(array) {
    for (var i = array.length - 1; i > 0; i--) {
        var j = Math.floor(Math.random() * (i + 1));
        var temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    return array;
}

function clone(obj) {
    return $.extend(true, {}, obj);
}