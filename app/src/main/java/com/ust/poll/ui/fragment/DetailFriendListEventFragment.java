package com.ust.poll.ui.fragment;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.linedone.poll.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;
import com.ust.poll.MainActivity;
import com.ust.poll.util.MediaUtil;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.ButterKnife;

public class DetailFriendListEventFragment extends MainActivity.PlaceholderFragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    private ProgressDialog progressDialog;
    private static int RESULT_CAMERA = 1;
    String objectId;
    String userId;
    String fileName;
    String strMember;
    ListView firendList;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_detail_friendlist, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = this.getArguments();
        objectId = bundle.getString("objectId");
        userId = bundle.getString("userId");
        Log.i("Event Object ID", objectId);
        progressDialog = ProgressDialog.show(getActivity(), "", "Loading records...", true);
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Event");
        parseQuery.getInBackground(objectId, new GetCallback<ParseObject>() {
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    retrieveEventSuccess(parseObject, e);
                }
            }
        });
    }

    public void retrieveEventSuccess(ParseObject parseObject, ParseException e) {
        if (e==null) {
            ImageButton cameraButton = (ImageButton) getActivity().findViewById(R.id.cameraButton);
            ImageButton galleryButton = (ImageButton) getActivity().findViewById(R.id.galleryButton);

            TextView txtCameraButton = (TextView) getActivity().findViewById(R.id.txtCameraButton);
            TextView txtGalleryButton = (TextView) getActivity().findViewById(R.id.txtGalleyButton);

            TextView titleGroupMember = (TextView) getActivity().findViewById(R.id.txt_efl_title_group_member);
            TextView title = (TextView) getActivity().findViewById(R.id.txt_eflTitle);
            TextView date = (TextView) getActivity().findViewById(R.id.txt_eflDate);
            TextView time = (TextView) getActivity().findViewById(R.id.txt_eflTime);
            TextView venue = (TextView) getActivity().findViewById(R.id.txt_eflVenue);
            TextView remark = (TextView) getActivity().findViewById(R.id.txt_eflRemarkURL);

            progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar);

            // Image Button
            Bitmap iconCamera = BitmapFactory.decodeResource(getResources(), R.drawable.camera);
            Bitmap iconGallery = BitmapFactory.decodeResource(getResources(), R.drawable.gallery);
            cameraButton.setImageBitmap(iconCamera);
            cameraButton.setOnClickListener(this);
            galleryButton.setImageBitmap(iconGallery);
            galleryButton.setOnClickListener(this);

            txtCameraButton.setText("Camera");
            txtGalleryButton.setText("Gallery");

            titleGroupMember.setText("Group Members");
            title.setText("Title: " + parseObject.get("EventTitle").toString());
            date.setText("Date: " + parseObject.get("EventDate").toString());
            time.setText("Time: " + parseObject.get("EventTime").toString());
            venue.setText("Venue: " + parseObject.get("EventVenue").toString());
            remark.setText("Remark: " + parseObject.get("EventRemarkURL").toString());

            strMember = parseObject.get("EventMembers").toString();

            // Spilt strMember to an array and find the name by phone number
            String[] arrayMemberPhones = strMember.split(",");
            String[] arrayMemberNames = new String[arrayMemberPhones.length];
            for (int i=0; i<arrayMemberPhones.length; i++){
                arrayMemberNames[i] = getContactName(arrayMemberPhones[i]) + " [" + arrayMemberPhones[i] + "]";
            }

            if (strMember!=null) {  // Construct a ListView
                firendList = (ListView) getActivity().findViewById(R.id.eventfriendListView);
                ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayMemberNames);
                firendList.setAdapter(mAdapter);
                firendList.setOnItemClickListener(this);
                progressDialog.dismiss();
            }
            else {
                Log.e("ERROR", "Group member list is equal to null!!!");
            }
        }
        else {
            Toast.makeText(getActivity().getApplicationContext(), "Querying failure...", Toast.LENGTH_LONG).show();
            Log.e("Database", "Error: " + e.getMessage());
        }
    }

    public String getContactName(final String number) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String contactName = "";

        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor contactLookup = contentResolver.query(uri, new String[]{
                BaseColumns._ID,
                ContactsContract.PhoneLookup.DISPLAY_NAME
        }, null, null, null);

        try {
            if (contactLookup != null && contactLookup.getCount() > 0) {
                contactLookup.moveToNext();
                contactName = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
            }
        }
        finally {
            if (contactLookup != null) {
                contactLookup.close();
            }
        }

        return contactName;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String contact = (String)this.firendList.getItemAtPosition(position);
        Log.i("Contact", contact);
        String number = contact.substring(contact.indexOf("[") + 1, contact.indexOf("]"));

        try {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number)));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cameraButton:
                SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.UK);
                fileName = objectId+"_"+objectId+"_"+sdFormat.format(new Date()).toString()+".jpg";
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(fileName));
                if (cameraIntent.resolveActivity(getActivity().getPackageManager())!=null) {
                    startActivityForResult(cameraIntent, RESULT_CAMERA);
                }
                break;

            case R.id.galleryButton:
                DetailGalleryEventFragment fragment = new DetailGalleryEventFragment();
                Bundle bundle = new Bundle();
                bundle.putString("objectId", objectId);
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
                break;

            default:
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_CAMERA && resultCode == getActivity().RESULT_OK) {
            Uri takenPhotoUri = getPhotoFileUri(fileName);
            byte[] imgFile = MediaUtil.getBytesFromImagePath(takenPhotoUri.getPath());
            if (imgFile!=null) {
                ParseObject eventObject = new ParseObject("EventPhoto");
                eventObject.put("EventId", objectId);
                eventObject.put("UserId", userId);


                ParseFile fileObject = new ParseFile(fileName, imgFile);
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
                        progressBar.setProgress(percentDone);
                    }
                });
                eventObject.put("File", fileObject);
                eventObject.saveInBackground();
                eventObject.fetchInBackground(new GetCallback<ParseObject>() {
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            // Success!
                            String objectId = object.getObjectId();
                            Toast.makeText(getActivity().getApplicationContext(), "Photo upload success!", Toast.LENGTH_LONG).show();
                        } else {
                            // Failure!
                            Toast.makeText(getActivity().getApplicationContext(), "Server connection failure...", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
    }

    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        boolean status;
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            status = true;
        }
        else {
            status = false;
        }

        return status;
    }

    public Uri getPhotoFileUri(String fileName) {
        Uri resource;
        if (isExternalStorageAvailable()) {  // Only continue if the SD Card is mounted
            File storage = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "LETSMEET");
            // Create the storage directory if it does not exist
            if (!storage.exists() && !storage.mkdirs()){
                Log.d("Camera", "Create directory failed!");
            }

            // Return the file target for the photo based on filename
            resource = Uri.fromFile(new File(storage.getPath() + File.separator + fileName));
        }
        else {
            resource = null;
        }

        return resource;
    }
}