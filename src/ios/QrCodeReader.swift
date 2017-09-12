import Foundation

@objc(QrCodeReader) class QrCodeReader : CDVPlugin, URLSessionDelegate {


 //this function will validate if a certificate is installed within the app file structure or in the keychain of the device
 @objc(launchqrreader:)
 func launchqrreader(command: CDVInvokedUrlCommand) {
    //init plugin result
    var response: Dictionary = ["success" : true ] as [String : Any]
    var pluginResult = CDVPluginResult(
        status: CDVCommandStatus_OK,
        messageAs: response
    )

    //send the callback object back
     print("Sending back cordova response")
     self.commandDelegate!.send(
       pluginResult,
       callbackId: command.callbackId
     )
 }

}
