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
    public static final String LOCALHOST_AUTHORITY = "localhost";

    @Override
    public Uri remapUri(Uri uri) {
        LOG.d(TAG, "remapUri(" + uri + ")");
        if (! (HTTP_PROTOCOL.equals(uri.getScheme()) || HTTPS_PROTOCOL.equals(uri.getScheme()) ) ) {
            return null;
        }
        if (! LOCALHOST_AUTHORITY.equals(uri.getAuthority())) {
            return null;
        }
        LOG.d(TAG, "toPluginUri:  " + toPluginUri(uri));
        // for handleOpenForRead to get called the Uri needs to be formatted as cdvplugin://pluginId/
        return toPluginUri(uri);
    }

    @Override
    public CordovaResourceApi.OpenForReadResult handleOpenForRead(Uri uri) throws IOException {
        //Uri original_uri = fromPluginUri(uri);
        Uri file_uri = uri.buildUpon().scheme("file").authority("").path("/android_asset/www/" + uri.getPath()).build();
        LOG.d(TAG, "file_uri:  " + file_uri);
        LOG.d(TAG, "getPath:  " + file_uri.getPath());

        FileInputStream input_stream = new FileInputStream(file_uri.getPath());

        Context context = this.cordova.getActivity().getApplicationContext();
        ContentResolver cR = context.getContentResolver();

        LOG.d(TAG, "getType:  " + cR.getType(file_uri));
        LOG.d(TAG, "size:  " + input_stream.getChannel().size());

        return new OpenForReadResult(uri, input_stream, cR.getType(file_uri), input_stream.getChannel().size(), null);
    }

}