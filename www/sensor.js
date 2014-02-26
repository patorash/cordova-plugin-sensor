var Sensor = new(function () {

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

  this.getSpecific = function (type, succ, fail) {

    /* Success Callback  */
    function success(msg) {
      succ(msg);
    }

    /* Error/Failure Callback */
    function failback(err) {
      fail(err);
    }

    /* Execute the native code */
    cordova.exec(function (msg) {
        succ(msg);
      },

      function (err) {
        fail(err);
      }, "Sensor", "getSpecificSensor", [type]);
  };

  /* getAll() - get every sensor on the device */
  this.getAll = function (succ, fail) {
    /* Success Callback  */
    function success(msg) {
      succ(msg);
    }

    /* Error/Failure Callback */
    function failback(err) {
      fail(err);
    }

    /* Execute the native code */
    cordova.exec(function (msg) {
        succ(msg);
      },

      function (err) {
        fail(err);
      }, "Sensor", "poolAllSensors", []);
  };
})();