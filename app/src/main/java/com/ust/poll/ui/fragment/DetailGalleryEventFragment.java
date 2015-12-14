// CSIT 6000B    #  CHAN Shing Chuen     20286820     scchanak@connect.ust.hk
// CSIT 6000B    #  MA Ka Kin            20286533     kkmaab@connect.ust.hk

package com.ust.poll.ui.fragment;

import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.linedone.poll.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.ust.poll.MainActivity;
import com.ust.poll.ui.adaptor.EventPhotoAdapter;
import com.ust.poll.util.MediaUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class DetailGalleryEventFragment extends MainActivity.PlaceholderFragment {
    private ProgressDialog progressDialog;
    String objectId;
    ArrayList<String> strImages;
    ListView galleryList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_active, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        strImages = new ArrayList<String>();
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = this.getArguments();
        objectId = bundle.getString("objectId");

        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("EventPhoto");
        parseQuery.whereEqualTo("EventId", objectId);
        parseQuery.orderByAscending("File");
        progressDialog = ProgressDialog.show(getActivity(), "", "Loading records...", true);
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> parseObjects, ParseException e) {
                progressDialog.dismiss();
                if (e == null) {
                    retrieveEventSuccess(parseObjects, e);
                }
            }
        });
    }

    public void retrieveEventSuccess(List<ParseObject> parseObjects, ParseException e) {
        if (e==null) {
            int counter = 0;
            for (ParseObject parseObject : parseObjects) {
                ParseFile fileObject = parseObjects.get(counter).getParseFile("File");
                if(fileObject!=null){
                    try {
                        strImages.add(Base64.encodeToString(fileObject.getData(), Base64.DEFAULT));
                    }
                    catch (ParseException ePhotoMsg) {
                        Log.e("File", "Error: " + ePhotoMsg.getMessage());
                    }
                }
                else {  //NO Image uploaded
                    strImages.add(MediaUtil.getStringFromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.empty, null)));
                }
                counter++;
            }
            Log.d("Database", "Retrieved " + parseObjects.size() + " Event");
            if (parseObjects.size()==0) {
                Toast.makeText(getActivity().getApplicationContext(), "No Image for this Event!", Toast.LENGTH_LONG).show();
            }
            else if (strImages!=null) {
                galleryList = (ListView) getActivity().findViewById(R.id.activeEventListView);
                EventPhotoAdapter mAdapter = new EventPhotoAdapter(getActivity(), strImages);
                galleryList.setAdapter(mAdapter);
            }
        }
        else {
            Toast.makeText(getActivity().getApplicationContext(), "Querying failure...", Toast.LENGTH_LONG).show();
            Log.e("Database", "Error: " + e.getMessage());
        }
    }
}