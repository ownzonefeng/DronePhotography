package com.example.zhaodong.dronephotography;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class PhotoUploadService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.example.zhaodong.dronephotography.action.FOO";
    private static final String ACTION_BAZ = "com.example.zhaodong.dronephotography.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.example.zhaodong.dronephotography.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.example.zhaodong.dronephotography.extra.PARAM2";

    public PhotoUploadService() {
        super("PhotoUploadService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, PhotoUploadService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, PhotoUploadService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //Check whether connected to Wifi:
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connManager.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;

        if(isConnected && isWiFi){
            if(hasInternetAccess()){


            }else{

            }

        }

    }


    public static boolean hasInternetAccess() {

            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://clients3.google.com/generate_204").openConnection());
                urlc.setRequestProperty("User-Agent", "Android");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                //Connection is successful:
                if (urlc.getResponseCode() == 204 && urlc.getContentLength() == 0){
                    Log.d(TAG, "Network connection is sucessful!");
                    return true;
                }
            } catch (IOException e) {
                Log.e(TAG, "Error checking internet connection", e);
            }

                return false;
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
