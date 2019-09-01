package org.apache.cordova.plugin;

import org.apache.cordova.CordovaPlugin;

import android.net.Uri;
import android.util.Log;

/**
* This class remaps http://localhost and https://localhost URIs to file:///android_asset/www/
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
        return uri.buildUpon().scheme("file").authority("").path("/android_asset/www" + uri.getPath()).build();
    }
}