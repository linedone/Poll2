package com.ust.poll.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

import com.ust.poll.MainActivity;
import com.ust.poll.activity.LoginActivity;

/**
 * Created by CSH529 on 10/29/2015.
 */
public class SMSMonitor extends BroadcastReceiver {
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null &&
                intent.getAction() != null &&
                ACTION.compareToIgnoreCase(intent.getAction()) == 0) {

            Log.d("SMSMonitor", ">>>>>>>>>>>>>>>>>>>>>> SMS Message Received <<<<<<<<<<<<<<<<<<<<<<<<<<");

            Object[] pduArray = (Object[]) intent.getExtras().get("pdus");
            SmsMessage[] messages = new SmsMessage[pduArray.length];

            for (int i = 0; i < pduArray.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pduArray[i]);
                String phoneNo = messages[i].getOriginatingAddress();
                String body = messages[i].getMessageBody().toString();
                Log.d("SMSMonitor", ">>>>>>>>>>>>>>>>>>>>>> sender : "+phoneNo);
                Log.d("SMSMonitor", ">>>>>>>>>>>>>>>>>>>>>> messages : "+body);

                Log.d("SMSMonitor", ">>>>>>>>>>>>>>>>>>>>>> validationCode : "+LoginActivity.validationCode);
                if(body.equals(LoginActivity.validationCode)){
                    Intent inten = new Intent(context, LoginActivity.class);
                    inten.putExtra("PHONE_NO", phoneNo);
                    inten.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(inten);
                }
            }
        }
    }
}