/**
 * Author: Archie, Disono (webmonsph@gmail.com)
 * Website: http://www.webmons.com
 *
 * Created at: 12/08/2018
 */
package disono.com.webmons.ruler;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

public class CordovaCameraRuler extends CordovaPlugin {
    private static final String TAG = "CordovaCameraRuler";

    private Activity activity;
    private CallbackContext callbackContext;

    BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String length = intent.getStringExtra("length");
                String unit = intent.getStringExtra("unit");
                if (length != null && unit != null) {
                    _cordovaSendResult("length", length, unit);
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        activity.unregisterReceiver(br);
    }

    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        Log.d(TAG, "Initializing CordovaCameraRuler");
    }

    public boolean execute(String action, JSONArray args, final CallbackContext cb) throws JSONException {
        activity = cordova.getActivity();

        if (callbackContext == null) {
            callbackContext = cb;
        }

        if (action.equals("takePhoto")) {
            _broadcastRCV();
            Intent intent = new Intent(cordova.getContext(), CameraActivity.class);
            cordova.getActivity().startActivity(intent);

            return true;
        }

        PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
        pluginResult.setKeepCallback(true);
        callbackContext.sendPluginResult(pluginResult);

        return false;
    }

    private void _broadcastRCV() {
        IntentFilter filter = new IntentFilter("CordovaCameraRuler.Filter");
        activity.registerReceiver(br, filter);
    }

    private void _cordovaSendResult(String event, String data, String unit) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("event", event);
            obj.put("data", data);
            obj.put("unit", unit);
        } catch (JSONException e) {
            Toast.makeText(activity.getApplicationContext(), "Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();

            PluginResult pluginResult = new PluginResult(PluginResult.Status.ERROR, e.getMessage());
            pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
            return;
        }

        PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, obj);
        pluginResult.setKeepCallback(true);
        callbackContext.sendPluginResult(pluginResult);
    }

}
