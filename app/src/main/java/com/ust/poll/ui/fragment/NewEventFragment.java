package com.ust.poll.ui.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.linedone.poll.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;
import com.ust.poll.MainActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewEventFragment extends MainActivity.PlaceholderFragment {
    private static int FRAGMENT_CODE = 0;
    private static int RESULT_LOAD_IMG = 1;
    private ProgressDialog progressDialog;
    String imgDecodableString;
    byte[] imgFile;
    String eventMembers;

    @Bind(R.id.txt_etitle) BootstrapEditText txt_etitle;
    @Bind(R.id.txt_eDate) BootstrapEditText txt_eDate;
    @Bind(R.id.txt_eTime) BootstrapEditText txt_eTime;
    @Bind(R.id.txt_eVenue) BootstrapEditText txt_eVenue;
    @Bind(R.id.txt_eRemarkURL) BootstrapEditText txt_eRemarkURL;

    @Bind(R.id.btn_event_pick_photo) BootstrapButton btn_event_pick_photo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_new, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }


    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //TODO: Doesn't work!!!
        if (savedInstanceState != null) {
            Log.i("Restore Bundle", savedInstanceState.toString());
            txt_etitle.setText(savedInstanceState.getString("txt_etitle"));
            txt_eDate.setText(savedInstanceState.getString("txt_eDate"));
            txt_eTime.setText(savedInstanceState.getString("txt_eTime"));
            txt_eVenue.setText(savedInstanceState.getString("txt_eVenue"));
            txt_eRemarkURL.setText(savedInstanceState.getString("txt_eRemarkURL"));
        }
    }

    @OnClick(R.id.txt_eDate)
    public void fnPickDate(View view) {
        final Calendar eventDate = Calendar.getInstance();
        final Calendar todayDate = Calendar.getInstance();
        int mYear = eventDate.get(Calendar.YEAR);
        int mMonth = eventDate.get(Calendar.MONTH);
        int mDay = eventDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int year, int monthOfYear, int dayOfMonth) {
                SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
                eventDate.set(Calendar.YEAR, year);
                eventDate.set(Calendar.MONTH, monthOfYear);
                eventDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                todayDate.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
                todayDate.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
                todayDate.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

                if (eventDate.getTime().compareTo(todayDate.getTime()) >= 0) {
                    txt_eDate.setText(sdFormat.format(eventDate.getTime()));
                }
                else {
                    txt_eDate.setText("");
                    Toast.makeText(getActivity().getApplicationContext(), "Please select a suitable event date.", Toast.LENGTH_LONG).show();
                }
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
                if (hourOfDay<10 && minute<10) {
                    txt_eTime.setText("0" + hourOfDay + ":0" + minute);
                }
                else if (hourOfDay>=10 && minute<10){
                    txt_eTime.setText(hourOfDay + ":0" + minute);
                }
                else if (hourOfDay<10 && minute>=10){
                    txt_eTime.setText("0" + hourOfDay + ":" + minute);
                }
                else {
                    txt_eTime.setText(hourOfDay + ":" + minute);
                }
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState); // the UI component values are saved here.
        outState.putString("txt_etitle", txt_etitle.getText().toString());
        outState.putString("txt_eDate", txt_eDate.getText().toString());
        outState.putString("txt_eTime", txt_eTime.getText().toString());
        outState.putString("txt_eVenue", txt_eVenue.getText().toString());
        outState.putString("txt_eRemarkURL", txt_eRemarkURL.getText().toString());
        Log.i("State Saved", outState.toString());
    }

    @Override
    public void onResume() {
        super.onResume();

        ImageView imgView = (ImageView) getActivity().findViewById(R.id.image_event_photo);
        imgView.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==FRAGMENT_CODE && resultCode==getActivity().RESULT_OK) {
            if(data != null) {
                if(data.getStringExtra("eventMembers") != null) {
                    eventMembers = data.getStringExtra("eventMembers");
                    Log.i("PickFriend", "Data passed from PickFriend Fragment = " + eventMembers);
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

                    btn_event_pick_photo.setText("Change Photo");
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

        if (eventMembers==null) {
            Toast.makeText(getActivity().getApplicationContext(), "Please select group members!!!", Toast.LENGTH_LONG).show();
        }
        else if (txt_etitle.getText().toString().length()==0) {
            txt_etitle.setError("Event Title is required!");
        }
        else if (txt_etitle.getText().toString().length()>54) {
            txt_etitle.setError("Event Title maximum 54 character letters!");
        }
        else if (txt_eDate.getText().toString().length()==0) {
            txt_eDate.setError("Event Date is required!");
        }
        else if (txt_eTime.getText().toString().length()==0) {
            txt_eTime.setError("Event Time is required!");
        }
        else if (txt_eVenue.getText().toString().length()==0) {
            txt_eVenue.setError("Event Venue is required!");
        }
        else {
            isValidInput = true;
        }

        return isValidInput;
    }

    @OnClick(R.id.btn_event_pick_friend_list)
    public void fnPickFriends(View view) {
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
            progressDialog = ProgressDialog.show(getActivity(), "", "Saving record...", true);
            ParseObject eventObject = new ParseObject("Event");
            eventObject.put("EventTitle", txt_etitle.getText().toString());
            eventObject.put("EventDate", txt_eDate.getText().toString());
            eventObject.put("EventTime", txt_eTime.getText().toString());
            eventObject.put("EventVenue", txt_eVenue.getText().toString());
            if (txt_eRemarkURL.getText().length()==0) {
                eventObject.put("EventRemarkURL", "N/A");
            }
            else {
                eventObject.put("EventRemarkURL", txt_eRemarkURL.getText().toString());
            }
            if (imgDecodableString!=null) {
                File f = new File(imgDecodableString);
                ParseFile fileObject = new ParseFile(f.getName().toString(), imgFile);
                fileObject.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.i("PhotoUpload", "Photo upload successful.");
                        } else {
                            Log.e("PhotoUpload", "Photo upload failed. " + e);
                        }
                    }
                }, new ProgressCallback() {
                    public void done(Integer percentDone) {
                    }
                });
                eventObject.put("EventPhoto", fileObject);
            }
            eventObject.put("EventMembers", eventMembers);
            eventObject.saveInBackground();

            eventObject.fetchInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    if (e == null) {
                        // Success!
                        String objectId = object.getObjectId();
                        fnSendPushNotification(objectId, eventMembers);
                        Toast.makeText(getActivity().getApplicationContext(), "Event created successfully!", Toast.LENGTH_LONG).show();
                        Log.d("ObjectID", objectId);
                        progressDialog.dismiss();
                    }
                    else {
                        // Failure!
                        Toast.makeText(getActivity().getApplicationContext(), "Server connection failure...", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void fnSendPushNotification(String objectId, String phoneList) {
        String[] userArray = phoneList.split(",");

        for (int i=0; i<userArray.length; i++) {
            ParsePush push = new ParsePush();
            ParseQuery query = ParseInstallation.getQuery();
            query.whereEqualTo("username", userArray[i]);
            push.setQuery(query);
            push.setMessage("New Event");
            push.sendInBackground();
        }

        ActiveEventFragment fragment = new ActiveEventFragment();
        Bundle bundle = new Bundle();
        bundle.putString("objectId", objectId);
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
    }
}