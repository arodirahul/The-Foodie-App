package com.airwatch.thefoodieapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by rarodi on 8/19/2015.
 */

public class NetworkChange extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Network", "Network state changed");

    }
}
