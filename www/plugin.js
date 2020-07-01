var exec = require('cordova/exec');

var PLUGIN_NAME = 'CordovaCameraRuler';

var CordovaCameraRuler = {
    takePhoto: function(cb) {
        exec(cb, null, PLUGIN_NAME, 'takePhoto', []);
    }
};

module.exports = CordovaCameraRuler;
