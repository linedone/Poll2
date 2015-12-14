// CSIT 6000B    #  CHAN Shing Chuen     20286820     scchanak@connect.ust.hk
// CSIT 6000B    #  MA Ka Kin            20286533     kkmaab@connect.ust.hk

package com.ust.poll.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.ust.poll.MainActivity;
import com.linedone.poll.R;
import com.ust.poll.model.NewsItem;
import com.ust.poll.model.Poll;
import com.ust.poll.model.Polled;
import com.ust.poll.model.Result;
import com.ust.poll.ui.adaptor.CustomListAdapter;
import com.ust.poll.util.TelephonyUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PollResultFragment extends MainActivity.PlaceholderFragment implements AdapterView.OnItemClickListener {

    private ProgressDialog progressDialog;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a Z");  // 2015-12-13T11:31:00.000Z
    @Nullable
    @Bind(R.id.btn_result_chart_back) BootstrapButton btn_result_chart_back;
    @Bind(R.id.result_custom_list) ListView lv1;
    private PieChart mChart;  // we're going to display pie chart for smartphones martket shares

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_poll_result, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final ArrayList<String> list = new ArrayList<String>();
        final Context ctx = this.getActivity();

        // Expired Date
        ParseQuery expiredQuery = new ParseQuery(Poll.TABLE_NAME);
        expiredQuery.whereLessThan(Poll.END_AT, new Date());  // Deadline_Date < Today(), "END_AT"<TODAY

        // Voted Result, but non-Expired
        ParseQuery votedQuery = new ParseQuery(Poll.TABLE_NAME);
        votedQuery.whereContainedIn(Poll.FRIEND_PHONE, Arrays.asList("null"));  // All members voted, "FRIEND_PHONE"==null

        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(expiredQuery);
        queries.add(votedQuery);
        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);

        progressDialog = ProgressDialog.show(getActivity(), "", "Loading records...", true);
        mainQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    retrievePollResultSuccess(parseObjects, e);
                }
                progressDialog.dismiss();
            }
        });

        mChart = (PieChart) getView().findViewById(R.id.chart);
        lv1.setOnItemClickListener(this);
    }

    private void addData(Float[] yData, String[] xData, String titleOpt) {

        mChart.setDescription(""+titleOpt);

        // configure pie chart
        mChart.setUsePercentValues(true);

        // enable hole and configure
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColorTransparent(true);
        mChart.setHoleRadius(7);
        mChart.setTransparentCircleRadius(10);

        // enable rotation of the chart by touch
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);

        // set a chart value selected listener
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < yData.length; i++)
            yVals1.add(new Entry(yData[i], i));

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < xData.length; i++)
            xVals.add(xData[i]);

        // create pie data set
        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        // add many colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        // instantiate pie data object now
        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.GRAY);

        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.animateXY(2000, 2000);
        // update pie chart
        mChart.invalidate();
    }

    public void retrievePollResultSuccess(List<ParseObject> parseObjects, ParseException e) {
        if (e==null) {
            int counter = 0;

            ArrayList<String> idList = new ArrayList<String>();
            ArrayList<NewsItem> results = new ArrayList<NewsItem>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm");
            String id;
            String t;
            String dt;
            String op;
            String cph;
            for (ParseObject parseObject : parseObjects) {
                id = parseObject.getObjectId();
                idList.add(id);
                t = parseObject.get(Poll.TITLE).toString();
                dt = sdf.format((Date) parseObject.get(Poll.END_AT));
                op = parseObject.get(Poll.OPTIONS).toString();
                cph = parseObject.get(Poll.CREATORPHONE).toString();

                NewsItem newsData = new NewsItem();
                newsData.setHeadline("" + t);
                newsData.setReporterName("" + TelephonyUtil.getContactName(getActivity(), cph));
                newsData.setDate("" + dt);
                newsData.setpollID(id);

                String allOption = "";
                ArrayList sortedResult = new ArrayList();

                ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(Polled.TABLE_NAME);
                parseQuery.whereEqualTo(Polled.POLLID, id);

                try {
                    List<ParseObject> pObject = parseQuery.find();

                    for (ParseObject p : pObject) {
                        allOption += p.get(Polled.OPTION) + ",";
                    }
                    String[] optArray = allOption.split(",");

                    sortedResult = displayDuplicate(optArray);

                    allOption = "";
                    for(int i = 0; i < sortedResult.size(); i++) {

                        String[] tmpArray = sortedResult.get(i).toString().split("!#####!");
                        if(!tmpArray[0].equals("")){
                            allOption += "\n Option: " + tmpArray[0]+", voted"+tmpArray[1];
                            Log.d("result+++++++", "" + sortedResult.get(i));
                        }
                        if(allOption.equals("")){

                            allOption += "\n No one voted : 0";
                        }
                    }
                    //Log.d("result------", "" + allOpt);
                }
                catch (ParseException e1) {
                    e1.printStackTrace();
                }
                newsData.setResult(sortedResult);
                //for(int i = 0; i < sortedResult.size(); i++) {

                //    Log.d("result------", "" + sortedResult.get(i));
                //}

                newsData.setallOpt("" + allOption);
                //Log.d("result------", "" + sortedResult.size());
                results.add(newsData);
            }

            lv1.setAdapter(new CustomListAdapter(getActivity().getBaseContext(), results));
            Log.d("Database", "Retrieved " + parseObjects.size() + " Active");
        }
        else {
            Toast.makeText(getActivity().getApplicationContext(), "Querying failure...", Toast.LENGTH_LONG).show();
            Log.e("Database", "Error: " + e.getMessage());
        }
    }

    public ArrayList displayDuplicate(String[] ar) {
        //String resultStr = "";

        ArrayList<Result> resultList = new ArrayList<Result>();

        boolean[] done = new boolean[ar.length];
        for(int i=0; i<ar.length; i++) {
            if(done[i])
                continue;
            int nb = 0;
            for(int j = i; j < ar.length; j++) {
                if(done[j])
                    continue;
                if(ar[j].equals(ar[i])) {
                    done[j] = true;
                    nb++;
                }
            }
            resultList.add(new Result(nb, ar[i]));
        }

        Collections.sort(resultList);
        for(int i=0; i<resultList.size(); i++) {
            Log.d("result", "" + resultList.get(i).toString());
        }
        return resultList;
    }

    public static String[] cleanString(final String[] v) {
        int r, w;
        final int n = r = w = v.length;
        while (r > 0) {
            final String s = v[--r];
            if (!s.equals("null")) {
                v[--w] = s;
            }
        }
        return Arrays.copyOfRange(v, w, n);
    }

    @OnClick(R.id.btn_result_chart_back)
    public void fnBackChart(View view) {

        TextView resultTitle = (TextView) getView().findViewById(R.id.Title);
        mChart.setVisibility(View.INVISIBLE);
        btn_result_chart_back.setVisibility(View.INVISIBLE);
        lv1.setVisibility(View.VISIBLE);
        resultTitle.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
        int itemPosition = position;

        String optTitle = ((TextView) view.findViewById(R.id.title)).getText().toString();
        ArrayList<String> titlelist = new ArrayList<String>();
        ArrayList<Float> countlist = new ArrayList<Float>();

        if (!((TextView) view.findViewById(R.id.option1Result)).getText().toString().isEmpty()) {
            String[] tmpOpt1Arry = ((TextView) view.findViewById(R.id.option1Result)).getText().toString().split("!#####!");
            titlelist.add(tmpOpt1Arry[0]);
            countlist.add(Float.valueOf(tmpOpt1Arry[1]));
        }
        if (!((TextView) view.findViewById(R.id.option2Result)).getText().toString().isEmpty()) {
            String[] tmpOpt2Arry = ((TextView) view.findViewById(R.id.option2Result)).getText().toString().split("!#####!");
            titlelist.add(tmpOpt2Arry[0]);
            countlist.add(Float.valueOf(tmpOpt2Arry[1]));
        }
        if (!((TextView) view.findViewById(R.id.option3Result)).getText().toString().isEmpty()) {
            String[] tmpOpt3Arry = ((TextView) view.findViewById(R.id.option3Result)).getText().toString().split("!#####!");
            titlelist.add(tmpOpt3Arry[0]);
            countlist.add(Float.valueOf(tmpOpt3Arry[1]));
        }
        if (!((TextView) view.findViewById(R.id.option4Result)).getText().toString().isEmpty()) {
            String[] tmpOpt4Arry = ((TextView) view.findViewById(R.id.option4Result)).getText().toString().split("!#####!");
            titlelist.add(tmpOpt4Arry[0]);
            countlist.add(Float.valueOf(tmpOpt4Arry[1]));
        }

        Float[] countArray = countlist.toArray(new Float[countlist.size()]);
        String[] titleArray = titlelist.toArray(new String[titlelist.size()]);

//                Log.d("test", "a" + ((TextView) view.findViewById(R.id.option1Result)).getText().toString());
//                Log.d("test", "b" + ((TextView) view.findViewById(R.id.option2Result)).getText().toString());
//                Log.d("test", "c" + ((TextView) view.findViewById(R.id.option3Result)).getText().toString());
//                Log.d("test", "d" + ((TextView) view.findViewById(R.id.option4Result)).getText().toString());

        TextView resultTitle = (TextView) getView().findViewById(R.id.Title);
        btn_result_chart_back.setVisibility(View.VISIBLE);
        mChart.setVisibility(View.VISIBLE);
        lv1.setVisibility(View.INVISIBLE);
        resultTitle.setVisibility(View.INVISIBLE);
        // add data
        addData(countArray, titleArray, optTitle);

        // customize legends
        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);
    }
}
