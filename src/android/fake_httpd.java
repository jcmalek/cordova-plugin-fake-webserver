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
import android.content.res.AssetManager;
import android.content.res.AssetFileDescriptor;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
* This class echoes a string called from JavaScript.
*/
public class fake_httpd extends CordovaPlugin {

    public static final String TAG = "fake_httpd";
    public static final String HTTP_PROTOCOL = "http";
    public static final String HTTPS_PROTOCOL = "https";
    public static final String LOCALHOST_AUTHORITY = "localhost";

    @Override
    public Uri remapUri(Uri uri) {
        if (! (HTTP_PROTOCOL.equals(uri.getScheme()) || HTTPS_PROTOCOL.equals(uri.getScheme()) ) ) {
            return null;
        }
        if (! LOCALHOST_AUTHORITY.equals(uri.getAuthority())) {
            return null;
        }
        // for handleOpenForRead to get called the Uri needs to be formatted as cdvplugin://pluginId/
        return toPluginUri(uri);
    }

    @Override
    public CordovaResourceApi.OpenForReadResult handleOpenForRead(Uri uri) throws IOException {
        Uri original_uri = fromPluginUri(uri);
        Uri file_uri = original_uri.buildUpon().scheme("file").authority("").path(original_uri.getPath()).build();

        Context context = this.cordova.getActivity().getApplicationContext();
        AssetManager asset_manager = context.getAssets();

        //String[] filesList = asset_manager.list("www");
        //for(String f : filesList){
        //    Log.wtf(TAG, "files:  " + f);
        //}    

        // can't use openFd because it doesn't work on compressed files (many extensions are automatically compressed during build)
        InputStream input_stream = asset_manager.open("www" + file_uri.getPath());

        //CordovaWebView cordovaWebView;
        CordovaResourceApi resourceApi = this.webView.getResourceApi();
        String mime_type = resourceApi.getMimeType(file_uri);

        // count the number of bytes in the file (required by OpenForReadResult call below)
        byte[] bytes = new byte[1024];
        int length = 0;
        int count = 0;
        while ((count = input_stream.read(bytes, 0, 1024)) > 0) {
            length += count;
        }
        // close and re-open the input stream to position it back at the beginning of the file
        input_stream.close();
        input_stream = asset_manager.open("www" + file_uri.getPath());

        return new OpenForReadResult(uri, input_stream, mime_type, length, null);
    }

}