#!/usr/bin/env node

var fs = require('fs');
var path = require('path');

var IOS_DIR = './platforms/ios';
var ANDROID_DIR = './platforms/android';

var PLATFORM = {
    ANDROID: {
        dest: ANDROID_DIR + '/openCVLibrary343',
        src: './plugins/cordova-plugin-camera-ruler/src/android/openCVLibrary343'
    }
};

var mkdir = function(dir) {
	try {
		if (!fs.existsSync(dir)) {
			fs.mkdirSync(dir);
		}
	} catch(e) {
		if(e.code != "EEXIST") {
			throw e;
		}
	}
};

var copyDir = function(src, dest) {
	mkdir(dest);
	var files = fs.readdirSync(src);
	for(var i = 0; i < files.length; i++) {
		var current = fs.lstatSync(path.join(src, files[i]));
		if(current.isDirectory()) {
			copyDir(path.join(src, files[i]), path.join(dest, files[i]));
		} else if(current.isSymbolicLink()) {
			var symlink = fs.readlinkSync(path.join(src, files[i]));
			fs.symlinkSync(symlink, path.join(dest, files[i]));
		} else {
			copy(path.join(src, files[i]), path.join(dest, files[i]));
		}
	}
};

var copy = function(src, dest) {
	var oldFile = fs.createReadStream(src);
	var newFile = fs.createWriteStream(dest);
	oldFile.pipe(newFile);
};

copyDir(PLATFORM.ANDROID.src, PLATFORM.ANDROID.dest);

var fd = fs.openSync(ANDROID_DIR + '/project.properties', 'a');
fs.writeSync(fd, '\nandroid.library.reference.3=openCVLibrary343');
fs.closeSync(fd);