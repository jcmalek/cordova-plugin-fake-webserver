package org.apache.cordova.plugin;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaResourceApi;
import org.apache.cordova.CordovaResourceApi.OpenForReadResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.content.Context;
import android.content.ContentResolver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;

/**
* This class echoes a string called from JavaScript.
*/
public class fake_httpd extends CordovaPlugin {

    public static final String HTTP_PROTOCOL = "http";
    public static final String HTTPS_PROTOCOL = "https";

    @Override
    public Uri remapUri(Uri uri) {
        if (! (HTTP_PROTOCOL.equals(uri.getScheme()) || HTTPS_PROTOCOL.equals(uri.getScheme()) ) ) {
            return null;
        }
        // for handleOpenForRead to get called the Uri needs to be formatted as cdvplugin://pluginId/
        return toPluginUri(uri);
    }

    @Override
    public CordovaResourceApi.OpenForReadResult handleOpenForRead(Uri uri) throws IOException {
        //Uri original_uri = fromPluginUri(uri);
        Uri file_uri = uri.buildUpon().scheme("file").authority("").path("/android_asset/www/" + uri.getPath()).build();

        FileInputStream input_stream = new FileInputStream(file_uri.getPath());

        Context context = this.cordova.getActivity().getApplicationContext();
        ContentResolver cR = context.getContentResolver();

        return new OpenForReadResult(uri, input_stream, cR.getType(file_uri), input_stream.getChannel().size(), null);
    }

}