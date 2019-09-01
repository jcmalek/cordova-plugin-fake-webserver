cordova-plugin-fake-webserver
=======

WARNING: Please keep in mind this can be a very bad practice in terms of security. Use at your own risk.

A Cordova plugin to simulate a fake webserver. Does very little except translate http://localhost and https://localhost addresses to file:///android_asset/www/ URIs. It is useful for fooling some javascript libraries into thinking you are running over http:// instead of file:// to get them to run in a WebView. Much more lightweight than trying to run a web server inside your app.

You may also need or wish to fake your "user agent" (browser) to get some javascript libraries to run. From the Cordova documentation you can add the following or similar to your config.xml:

&lt;preference name="OverrideUserAgent" value="Mozilla/5.0 (Linux; Android 8.0.0;) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Mobile Safari/537.36" />

