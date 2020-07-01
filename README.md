### cordova-plugin-camera-ruler
Simple ruler using camera

### Installation
Latest stable version from npm:
```
$ cordova plugin install cordova-plugin-camera-ruler
```
Bleeding edge version from Github:
```
$ cordova plugin add 
```

### Usage
```
window.CordovaCameraRuler.takePhoto(function (res) {
    console.log(res.unit);
    console.log(res.length);
});
```

### License
Cordova Plugin Camera Ruler is licensed under the Apache License (ASL) license. For more information, see the LICENSE file in this repository.