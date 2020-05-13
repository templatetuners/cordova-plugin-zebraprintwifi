cordova.define("cordova-plugin-zebraprintwifi.zebraprintwifi", function(require, exports, module) {
// based on https://www.outsystems.com/blog/posts/how-to-create-a-cordova-plugin-from-scratch/

// Empty constructor
function zebraprintwifi() {}

// Print
// The function that passes work along to native shells
// Message is a string
zebraprintwifi.prototype.print = function(message, address, successCallback, errorCallback) {
  	var options = {};
  	options.message = message;
  	options.address = address;
  	cordova.exec(successCallback, errorCallback, 'zebraprintwifi', 'print', [options]);
}

zebraprintwifi.prototype.printpdf = function(message, address, successCallback, errorCallback) {
    var options = {};
    options.message = message;
    options.address = address;
    cordova.exec(successCallback, errorCallback, 'zebraprintwifi', 'printpdf', [options]);
}

// test print
zebraprintwifi.prototype.testprint = function(message, address, successCallback, errorCallback) {
    var options = {};
    options.message = message;
    options.address = address;
    cordova.exec(successCallback, errorCallback, 'zebraprintwifi', 'testprint', [options]);
}

// Connect
zebraprintwifi.prototype.connect = function(message, address, successCallback, errorCallback) {
  	var options = {};
  	options.message = message;
    options.address = address;
  	cordova.exec(successCallback, errorCallback, 'zebraprintwifi', 'connect', [options]);
}

// Disonnect
zebraprintwifi.prototype.disconnect = function(message, address, successCallback, errorCallback) {
  	var options = {};
    options.message = message;
    options.address = address;
  	cordova.exec(successCallback, errorCallback, 'zebraprintwifi', 'disconnect', [options]);
}

// Status
zebraprintwifi.prototype.status = function(message, address, successCallback, errorCallback) {
  	var options = {};
  	options.message = message;
  	options.address = address;
  	cordova.exec(successCallback, errorCallback, 'zebraprintwifi', 'status', [options]);
}

// Installation constructor that binds zebraprintwifi to window
zebraprintwifi.install = function() {
  	if (!window.plugins) {
    	window.plugins = {};
  	}
  	window.plugins.zebraprintwifi = new zebraprintwifi();
  	return window.plugins.zebraprintwifi;
};
cordova.addConstructor(zebraprintwifi.install);

});
