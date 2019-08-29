package org.apache.cordova.plugin;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
* This class echoes a string called from JavaScript.
*/
public class fake_httpd extends CordovaPlugin {

    @Override
    public Uri remapUri(Uri uri) {
    }

    @Override
    public CordovaResourceApi.OpenForReadResult handleOpenForRead(Uri uri) throws IOException {
        throw new FileNotFoundException("Plugin can't handle uri: " + uri);
    }

}