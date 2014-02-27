var argscheck = require('cordova/argscheck'),
  channel = require('cordova/channel'),
  utils = require('cordova/utils'),
  exec = require('cordova/exec'),
  cordova = require('cordova');

channel.createSticky('onCordovaInfoReady');
// Tell cordova channel to wait on the CordovaInfoReady event
channel.waitForInitialization('onCordovaInfoReady');

function AndroidSensor(){
  this.sensor_type = {
    TYPE_ACCELEROMETER: 1,
    TYPE_AMBIENT_TEMPERATURE: 13,
    TYPE_LIGHT: 5,
    TYPE_GRAVITY: 9,
    TYPE_GYROSCOPE: 4,
    TYPE_LINEAR_ACCELERATION: 10,
    TYPE_MAGNETIC_FIELD: 2,
    TYPE_PRESSURE: 6,
    TYPE_PROXIMITY: 8,
    TYPE_RELATIVE_HUMIDITY: 12,
    TYPE_ROTATION_VECTOR: 11
  };
  this.available = false;
  this.sensors = [];
  var self = this;

  channel.onCordovaReady.subscribe(function(){
    self.getAll(function(results) {
      self.available = true;
      for(result in results) {
        self.sensors[result.type] = result;
      }
      console.log(self.sensors.toString());
      channel.onCordovaInfoReady.fire();
    },function(e) {
        self.available = false;
        console.log("[ERROR] Error initiailzing Cordova: " + e);
    });
  });
}

AndroidSensor.prototype.getSpecific = function(type, successCallback, errorCallback) {
  argscheck.checkArgs('fF', 'AndroidSensor.getSpecific', arguments);
  exec(successCallback, errorCallback, "AndroidSensor", "getSpecific", [type]);
}


AndroidSensor.prototype.getAll = function(successCallback, errorCallback) {
  argscheck.checkArgs('fF', 'AndroidSensor.getInfo', arguments);
  exec(successCallback, errorCallback, "AndroidSensor", "poolAllSensors", []);
}

module.exports = new AndroidSensor();