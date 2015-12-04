package com.ust.poll.activity;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.linedone.poll.R;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.ust.poll.MainActivity;
import com.ust.poll.ui.dialog.DialogHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;


public class LoginActivity extends Activity {


    private static final String TAG = "LoginActivity";
    public static final String PARSE_DEFAULT_PSS = "1234";
    public static String validationCode = "<I am Ken Validation, This is to validation of the phone no.>";

    //private static final String url = "http://www.afreesms.com/worldwide/hong-kong";
    private static final String CAP_IMG = "Capcha.jpg";
    private Map cookies;

    @Bind(R.id.txt_phone_no)
    BootstrapEditText txt_phone_no;
    @Bind(R.id.txt_captcha_code)
    BootstrapEditText txt_captcha_code;
    @Bind(R.id.img_captcha)
    ImageView imgView;

    ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_reg);
        ButterKnife.bind(this);

        fnReset(null);
        fnInitParse();

        fnCheckPhoneID(savedInstanceState);

    }

    private void fnCheckPhoneID(Bundle savedInstanceState) {
        String phone = "";
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                phone = extras.getString("PHONE_NO");
            }
        } else {
            phone = (String) savedInstanceState.getSerializable("PHONE_NO");
        }

        if (phone != null && !phone.equalsIgnoreCase("")) {
            fnSignup(phone);
        }
    }

    public void fnLogin(final String no) {
        fnPreSave();
        Log.d(TAG, ">>>>>>>>>>>> fnLogin >>>>>>>>>>>> " + no);
        final LoginActivity ctx = this;
        // Send data to Parse.com for verification
        ParseUser.logInInBackground(no, PARSE_DEFAULT_PSS,
                new LogInCallback() {
                    public void done(ParseUser parseUser, ParseException e) {
                        fnPostSave();
                        if (e != null) {
                            Log.d(TAG, ">>>>>>>>>>>> Error Login >>>>>>>>>>>> " + e.getMessage());
                            DialogHelper.getOkAlertDialog(ctx,
                                    "Error in connecting server..",
                                    e.getMessage()).show();
                        } else {
                            if (parseUser != null) {
                                // If user exist and authenticated
                                user = parseUser;
                                Log.d(TAG, ">>>>>>>>>>>> User Login >>>>>>>>>>>> " + parseUser.getUsername());
                                fnPostLogin();
                            } else {
                                // If user not - exist create new
                                fnSignup(no);
                            }
                        }
                    }
                });
    }

    public void fnSignup(final String no) {
        //installation for parse
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("username", no);
        installation.saveInBackground();

        Log.d(TAG, ">>>>>>>>>>>> Signup >>>>>>>>>>>> " + no);
        final LoginActivity ctx = this;
        fnPreSave();
        // Save new user data into Parse.com Data Storage
        final ParseUser puser = new ParseUser();
        puser.setUsername(no);
        puser.setPassword(PARSE_DEFAULT_PSS);
        puser.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                fnPostSave();
                if (e == null) {
                    // Show a simple Toast message upon successful
                    // registration
                    Toast.makeText(getApplicationContext(),
                            "Successfully Signed up, please log in.",
                            Toast.LENGTH_LONG).show();
                    Log.d(TAG, ">>>>>>>>>>>> New User created >>>>>>>>>>>> " + puser.getUsername() + " " + puser.getObjectId());
                    user = puser;
                    fnPostLogin();
                } else {
                    Log.d(TAG, ">>>>>>>>>>>> Error Signup >>>>>>>>>>"+e.getCode());
                    Log.d(TAG, ">>>>>>>>>>>> Error Signup >>>>>>>>>>" + e.getMessage());
                    if(e.getCode()==202){
                        // username already taken , try login with token
                        fnLogin(no);
                    }else{
                        DialogHelper.getOkAlertDialog(ctx,
                                "Error in connecting server..", e.getMessage())
                                .show();
                    }

                }
            }
        });
    }

    /*
    * Core functions to connect to Parse backend
    * */
    private void fnInitParse() {
        // Use for analytic
        ParseAnalytics.trackAppOpenedInBackground(this.getIntent());
        user = ParseUser.getCurrentUser();
        // IF already login, move to next page
        fnCheckUser();
    }


    public void fnReset(View view) {
        TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        this.validationCode = tMgr.getDeviceId();

        Log.d(TAG, ">>>>>>>>>>>> DEFAULT PHONE NO. >>>>>>>>>>>> " + mPhoneNumber);
        if (mPhoneNumber != null && !mPhoneNumber.equalsIgnoreCase("")) {
            txt_phone_no.setText(mPhoneNumber);
            txt_phone_no.setEnabled(false);
        } else {
            txt_phone_no.setEnabled(true);
        }
        txt_phone_no.setText("");
        txt_captcha_code.setText("");
        txt_captcha_code.setVisibility(View.INVISIBLE);
    }

    public void fnSubmit(View view) {
        String number = txt_phone_no.getText().toString();
        if (number.equalsIgnoreCase("")) {
            DialogHelper.getOkAlertDialog(this,
                    "Please input current phone no.", "Phone cannot be empty.")
                    .show();
            return;
        }
        sendSMS(number, validationCode);
    }


    private void fnCheckUser() {
        if (user != null && user.getObjectId() != null) {
            Log.d(TAG, ">>>>>>>>>>>> fnCheckUser >>>>>>>>>>>> user has already login");
            fnPostLogin();
            return;
        }
    }

    private void fnPostLogin() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "Successfully Logged in",
                Toast.LENGTH_LONG).show();
        finish();
    }

    private void fnPreSave() {
        DialogHelper.fnShowDialog(this);
    }

    private void fnPostSave() {
        DialogHelper.fnCloseDialog();
    }

    private void fnSaveImage(byte[] bytes) {
        Log.d(TAG, ">>>>>>>>>>>>>>>>>>> CREATING IAMGE FILE !!!!!!!!!!!!!!!!!!!!!!!!!!");
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), CAP_IMG);
        if (file.exists()) file.delete();

        FileOutputStream out = null;
        try {
            out = (new FileOutputStream(file));
            out.write(bytes);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fnLoadImg() {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        try {
            File f = new File(path, CAP_IMG);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            imgView.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void sendSMS(String phoneNumber, String message) {
        PendingIntent pi = PendingIntent.getActivity(this, 0,
                new Intent(this, LoginActivity.class), 0);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, pi, null);
    }
}
