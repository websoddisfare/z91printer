var exec = require('cordova/exec');

module.exports.print = function (arg0, success, error) {
    exec(success, error, 'z91Printer', 'print', [arg0]);
};
