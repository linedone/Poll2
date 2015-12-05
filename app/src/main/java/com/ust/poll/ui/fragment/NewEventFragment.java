package com.ust.poll.ui.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.linedone.poll.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;
import com.ust.poll.MainActivity;
import com.ust.poll.model.Poll;
import com.ust.poll.ui.dialog.DialogHelper;
import com.ust.poll.util.Util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewEventFragment extends MainActivity.PlaceholderFragment {
    private static int FRAGMENT_CODE = 0;
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    byte[] imgFile;
    String eventMembers;

    @Bind(R.id.txt_etitle) BootstrapEditText txt_etitle;
    @Bind(R.id.txt_eDate) BootstrapEditText txt_eDate;
    @Bind(R.id.txt_eTime) BootstrapEditText txt_eTime;
    @Bind(R.id.txt_eVenue) BootstrapEditText txt_eVenue;
    @Bind(R.id.txt_eRemarkURL) BootstrapEditText txt_eRemarkURL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_new, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }


    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //TODO: restore from savedInstanceState
        if (savedInstanceState != null ) {
            Toast.makeText(getActivity().getApplicationContext(), savedInstanceState .getString("message"), Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.txt_eDate)
    public void fnPickDate(View view) {
        final Calendar eventDate = Calendar.getInstance();
        int mYear = eventDate.get(Calendar.YEAR);
        int mMonth = eventDate.get(Calendar.MONTH);
        int mDay = eventDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int year, int monthOfYear, int dayOfMonth) {
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdFormat = new SimpleDateFormat(myFormat, Locale.UK);
                eventDate.set(Calendar.YEAR, year);
                eventDate.set(Calendar.MONTH, monthOfYear);
                eventDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                txt_eDate.setText(sdFormat.format(eventDate.getTime()));
            }
        },mYear, mMonth, mDay);
        mDatePicker.setTitle("Select Date");
        mDatePicker.show();
    }


    @OnClick(R.id.txt_eTime)
    public void fnPickTime(View view) {
        final Calendar eventTime = Calendar.getInstance();
        int hour = eventTime.get(Calendar.HOUR_OF_DAY);
        int minute = eventTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                //TODO: minute 0 -> 00
                txt_eTime.setText(hourOfDay + ":" + minute);
            }
        }, hour, minute, true);  // true for 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }


    @OnClick(R.id.btn_event_pick_photo)
    public void fnPickPhoto(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);  // Start the Intent
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==FRAGMENT_CODE && resultCode==getActivity().RESULT_OK) {
            if(data != null) {
                String value = data.getStringExtra("eventMembers");
                if(value != null) {
                    Log.i("PickFriend", "Data passed from PickFriend fragment = " + value);
                }
            }
        }
        else if (requestCode==RESULT_LOAD_IMG && resultCode==getActivity().RESULT_OK) {
            // Select Photo from Gallery
            try {
                // When an Image is picked
                if (data!=null) {
                    // Get the Image from data
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    // Get the cursor
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();  // Move to first row
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();

                    ImageView imgView = (ImageView) getActivity().findViewById(R.id.image_event_photo);
                    // Set the Image in ImageView after decoding the String
                    imgView.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
                    imgFile = getBytesFromBitmap(BitmapFactory.decodeFile(imgDecodableString));
                }
                else {
                    Toast.makeText(getActivity().getApplicationContext(), "You haven't picked Image.", Toast.LENGTH_LONG).show();
                    Log.i("NewEvent_Gallery: ", "You haven't picked Image");
                }
            }
            catch (Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                Log.e("NewEvent_Gallery: ", "Something went wrong.");
            }
            // End of Select Photo from Gallery
        }
    }


    public boolean fnEventInputValidation(View view) {
        boolean isValidInput = false;

        // Get Friend List Bundle
        Bundle bundle = this.getArguments();
        eventMembers = bundle.getString("eventMembers");
        Log.e("Bundle Event Members: ", eventMembers);
        // End of Get Friend List Bundle

        if (txt_etitle.getText().toString().length() == 0) {
            txt_etitle.setError("Event Title is required!");
        }
        else if (txt_eDate.getText().toString().length() == 0) {
            txt_eDate.setError("Event Date is required!");
        }
        else if (txt_eTime.getText().toString().length() == 0) {
            txt_eTime.setError("Event Time is required!");
        }
        else if (txt_eVenue.getText().toString().length() == 0) {
            txt_eVenue.setError("Event Venue is required!");
        }
        else if (eventMembers.isEmpty()) {
            Toast.makeText(getActivity().getApplicationContext(), "Please select group members!!!", Toast.LENGTH_LONG).show();
        }
        else {
            isValidInput = true;
        }

        return isValidInput;
    }


    @OnClick(R.id.btn_event_pick_friend_list)
    public void fnPickFriends(View view) {

//
//        FragmentChild fragmentChild = new FragmentChild();
//        fragmentChild.setTargetFragment(this, FRAGMENT_CODE);
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.replace(R.id.frl_view_container, fragmentChild);
//        transaction.addToBackStack(null);
//        transaction.commit();


        PickFriendFragment fragment = new PickFriendFragment();
        fragment.setTargetFragment(this, FRAGMENT_CODE);
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        return stream.toByteArray();
    }

    @OnClick(R.id.btn_event_create)
    public void fnEventCreate(View view) {
        boolean isValidInput = fnEventInputValidation(view);

        if(isValidInput) {
            File f = new File(imgDecodableString);
            ParseFile file = new ParseFile(f.getName().toString(), imgFile);
            file.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        //TODO: Success
                    } else {
                        //TODO: Failed
                    }
                }
            }, new ProgressCallback() {
                public void done(Integer percentDone) {
                    // Update your progress spinner here.
                    // percentDone will be between 0 and 100.
                }
            });

            ParseObject eventObject = new ParseObject("Event");
            eventObject.put("EventTitle", txt_etitle.getText().toString());
            eventObject.put("EventDate", txt_eDate.getText().toString());
            eventObject.put("EventTime", txt_eTime.getText().toString());
            eventObject.put("EventVenue", txt_eVenue.getText().toString());
            eventObject.put("EventRemarkURL", txt_eRemarkURL.getText().toString());
            eventObject.put("EventPhoto", file);
            //TODO: String Array
            //eventObject.addAllUnique("EventMembers", ArrayList);
            eventObject.saveInBackground();

            //TODO: check parseId get or not
            String parseId = eventObject.getObjectId();

            //TODO: why saveInBackground not work on ParseObject
            eventObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    DialogHelper.fnCloseDialog();
                    if (e == null) {
                        Toast.makeText(getActivity().getApplicationContext(), "Poll Successfully created.", Toast.LENGTH_LONG).show();

                    }
                    else {
                        Toast.makeText(getActivity().getApplicationContext(), "Error in connecting server...", Toast.LENGTH_LONG).show();
                    }
                }
            });
            //eventObject.addAllUnique("EventMembers", Arrays.asList(eventMembers));
//            eventObject.saveInBackground(new SaveCallback() {
//                @Override
//                public void done(ParseException e) {
//                    DialogHelper.fnCloseDialog();
//                    if (e == null) {
//                        Toast.makeText(getActivity().getApplicationContext(), "Event created successfully.", Toast.LENGTH_LONG).show();
//                    }
//                    else {
//                        DialogHelper.getOkAlertDialog(getActivity().getApplicationContext(), "Error in connecting server...", e.getMessage()).show();
//                    }
//                }
//            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        //TODO: Save input values
        // Store UI state to the savedInstanceState.
        // This bundle will be passed to onCreate on next call.  EditText txtName = (EditText)findViewById(R.id.txtName);
        String strEventTitle = txt_etitle.getText().toString();
//        String strEventDate = txt_eDate.getText().toString();
//        String strEventTime = txt_eTime.getText().toString();
//        String strEventVenue = txt_eVenue.getText().toString();
//        String strEventRemarkURL = txt_eRemarkURL.getText().toString();
//        String strEventPhoto = imgDecodableString;

        savedInstanceState.putString("EventTitle", strEventTitle);
//        savedInstanceState.putString("EventDate", strEventDate);
//        savedInstanceState.putString("EventTime", strEventTime);
//        savedInstanceState.putString("EventVenue", strEventVenue);
//        savedInstanceState.putString("EventRemarkURL", strEventRemarkURL);
//        savedInstanceState.putString("EventPhoto", strEventPhoto);
    }

//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        // Restore UI state from the savedInstanceState.
//        // This bundle has also been passed to onCreate.
//        txt_etitle.setText(savedInstanceState.getString("EventTitle"));
//        txt_eDate.setText(savedInstanceState.getString("EventDate"));
//        txt_eTime.setText(savedInstanceState.getString("EventTime"));
//        txt_eVenue.setText(savedInstanceState.getString("EventVenue"));
//        txt_eRemarkURL.setText(savedInstanceState.getString("EventRemarkURL"));
//        imgDecodableString = savedInstanceState.getString("EventPhoto");
//        eventMembers = savedInstanceState.getStringArray("EventMembers");
//
//    }

    private void fnSendPushNotification() {
        ParsePush push = new ParsePush();
        push.setChannel(Util.PARSE_CHANNEL);
        push.setMessage(txt_etitle.getText().toString());
        //push.setExpirationTime(1424841505);
        push.sendInBackground();
    }
}