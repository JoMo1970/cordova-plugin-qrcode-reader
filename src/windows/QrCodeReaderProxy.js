//init required modules
var cordova = require('cordova');

//add the gateway client to cordova as a proxy
cordova.commandProxy.add("MaximoGatewayClient", {
    hascertificate: function (successCallback, errorCallback, strInput) {
        //init the client object
        var client = MaximoGatewayClient.MaximoGatewayClientRT;
        console.log("Windows proxy (hascertificate) is invoking");
        var res = client.hascertificate();
        console.log("Certificate Response: " + res);
        successCallback(res);
    },
    authenticate: function (successCallback, errorCallback, strInput) {
        //init the client object
        var client = MaximoGatewayClient.MaximoGatewayClientRT;
        console.log("Windows proxy (performmaximoconnection) is invoking");
        var res = client.performmaximoconnection();
        console.log("Authentication Response: " + res);
        successCallback(res);
    },
});
