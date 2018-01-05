var exec = require('cordova/exec');

//init exports
module.exports = {
  launchqrreader : function(arg0, success, error) {
    exec(success, error, 'QrCodeReader', 'launchqrreader', [arg0]);
  },
  checkpermissions : function(arg0, success, error) {
    exec(success, error, 'QrCodeReader', 'checkpermissions', [arg0]);
  }
};
