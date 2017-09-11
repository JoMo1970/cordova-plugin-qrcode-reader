package com.qrcode.reader.plugins;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.att.qrcode.barcode.BarcodeCaptureActivity;
import android.content.Intent;
import android.graphics.Point;
import com.att.qrcode.camera.CameraSource;
import com.att.qrcode.camera.CameraSourcePreview;


public class QrCodeReader extends CordovaPlugin { // implements BarcodeTracker.BarcodeGraphicTrackerCallback {

  //init private class variables
  private static final String ACTION_RENDER_CAMERA = "launchqrreader";
  private static final String ACTION_INIT_CAMERA_SOURCE = "initcamerasource";
  private static final String TAG = "QrCodeReader";
  private JSONObject responseJSON;
  private Context context;
  private Dialog dialog;
  private CallbackContext callback;
  private static final int BARCODE_READER_REQUEST_CODE = 1;
  private CameraSource mCameraSource;
  private CameraSourcePreview mPreview;
  public static final String BarcodeObject = "Barcode";
  private static final int RC_HANDLE_GMS = 9001;

  @Override
  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
      super.initialize(cordova, webView);
  }

  //plugin main interface function
  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    //init response json object and context
    responseJSON = new JSONObject();
    context = this.cordova.getActivity();
    callback = callbackContext;

    try {

      //check incoming actions
      if(ACTION_RENDER_CAMERA.equals(action)) {

          //init a runnable thread
          final Object __this = this;
          cordova.getActivity().runOnUiThread(new Runnable() {
              @Override
              public void run() {
                  //set activity result callback
                  cordova.setActivityResultCallback((CordovaPlugin) __this);

                  //init intent
                  Intent intent = new Intent(cordova.getActivity().getApplicationContext(), BarcodeCaptureActivity.class);
                  cordova.getActivity().startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
              }
          });
      }
      else {
        //default action
        responseJSON.put("success", false);
        callback.error(responseJSON);
      }
    } catch(Exception e) {
        System.err.println("Exception: " + e.getMessage());

        //init response json object
        responseJSON.put("Error", e.getMessage());

        //send back the response with json object
        callback.error(responseJSON);
    }
    return true;
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
      Log.i(TAG, "Activity Result Returned - " + data.toString());
      if (requestCode == BARCODE_READER_REQUEST_CODE) {
          if (resultCode == CommonStatusCodes.SUCCESS) {
              try {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Point[] p = barcode.cornerPoints;
                    responseJSON.put("success", true);
                    responseJSON.put("secret", barcode.displayValue);
                }
                else {
                  responseJSON.put("success", false);
                }
                //send back result
                Log.i(TAG, "Sending back result - " + responseJSON.toString());
                callback.success(responseJSON);
              }
              catch(JSONException je) {
                Log.e(TAG, "JSON Exception: " + je.toString());
              }
          }
          else {
            Log.e(TAG, "Invalid Format - " +  CommonStatusCodes.getStatusCodeString(resultCode));
          }
      } else {
        super.onActivityResult(requestCode, resultCode, data);
      }
  }

  //this function will provide resource data
  private int getAppResource(String name, String type) {
    return this.cordova.getActivity().getResources().getIdentifier(name, type, this.cordova.getActivity().getPackageName());
  }

}
