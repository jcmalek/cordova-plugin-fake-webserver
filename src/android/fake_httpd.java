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
        Log.wtf(TAG, "remapUri(" + uri + ")");
        if (! (HTTP_PROTOCOL.equals(uri.getScheme()) || HTTPS_PROTOCOL.equals(uri.getScheme()) ) ) {
            return null;
        }
        if (! LOCALHOST_AUTHORITY.equals(uri.getAuthority())) {
            return null;
        }
        Log.wtf(TAG, "toPluginUri:  " + toPluginUri(uri));
        // for handleOpenForRead to get called the Uri needs to be formatted as cdvplugin://pluginId/
        return toPluginUri(uri);
    }

    @Override
    public CordovaResourceApi.OpenForReadResult handleOpenForRead(Uri uri) throws IOException {
        Log.wtf(TAG, "handleOpenForRead called!");
        Uri original_uri = fromPluginUri(uri);
        Uri file_uri = original_uri.buildUpon().scheme("file").authority("").path("www" + original_uri.getPath()).build();
        Log.wtf(TAG, "file_uri:  " + file_uri);
        Log.wtf(TAG, "getPath:  " + file_uri.getPath());

        Context context = this.cordova.getActivity().getApplicationContext();
        AssetManager assets = context.getAssets();
        InputStream input_stream = assets.open(file_uri.getPath());
        //String[] filesList = assets.list("");
        //for(String f : filesList){
        //    Log.wtf(TAG, "files:  " + f);
        //}        
        //FileInputStream input_stream = new FileInputStream(file_uri.getPath());

        //Context context = this.cordova.getActivity().getApplicationContext();
        ContentResolver cR = context.getContentResolver();
        //InputStream input_stream = cR.OpenInputStream(file_uri);

        Log.wtf(TAG, "getType:  " + cR.getType(file_uri));
        //Log.wtf(TAG, "size:  " + input_stream.getChannel().size());

        //input_stream.getChannel().size()
        return new OpenForReadResult(uri, input_stream, cR.getType(file_uri), null, null);
    }

}