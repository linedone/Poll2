// CSIT 6000B    #  CHAN Shing Chuen     20286820     scchanak@connect.ust.hk
// CSIT 6000B    #  MA Ka Kin            20286533     kkmaab@connect.ust.hk

package com.ust.poll;

import android.app.Application;

import com.linedone.poll.R;
import com.parse.Parse;

/**
 * Created by Ken on 10/7/2015.
 */
public class PollApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        String app_key = getString(R.string.parse_app_id);
        String client_key = getString(R.string.parse_client_key);
        Parse.initialize(this, app_key, client_key);

        // use for push notification
        //ParseInstallation.getCurrentInstallation().saveInBackground();
        //ParsePush.subscribeInBackground(Util.PARSE_CHANNEL);
    }
}
