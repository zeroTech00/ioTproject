package com.example.zero.scratchiot.menu;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zero.scratchiot.Database.DatabaseHelper;
import com.example.zero.scratchiot.HTTPRequest.JSONParser;
import com.example.zero.scratchiot.R;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StreamingFragment extends Fragment {

    String url_create = "http://104.248.159.38/ambildata";

    DatabaseHelper mDB;
    boolean suksesDatabase;

    String saveIDServer, saveSensor1, saveSensor2, saveSensor3, saveSensor4, saveTanggal;
    String test,dataPrimeryKey;

    TextView tv_test, tv_key;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_streaming,container,false);


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDB = new DatabaseHelper(getActivity());

        tv_test = (TextView) view.findViewById(R.id.txt_test);
        tv_key = (TextView) view.findViewById(R.id.txt_key);

        TimerGetServer timerGetServer = new TimerGetServer();
        Timer timerIntervalServer = new Timer();

        timerIntervalServer.schedule(timerGetServer, 1000, 5000);

    }

    class TimerGetServer extends TimerTask {

        final class asyncServer extends AsyncTask<String, String, JSONArray> {

            @Override
            protected JSONArray doInBackground(String... strings) {
                JSONParser jsonParser = new JSONParser();
                JSONObject json;
                JSONArray jsonArray;
                List<NameValuePair> params = new ArrayList<>();

                json = jsonParser.makeHttpRequest(url_create, "GET", params);

                try {
                    jsonArray = json.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject objID = jsonArray.getJSONObject(i);

                        saveIDServer    = objID.getString("id");
                        saveSensor1     = objID.getString("sensor1");
                        saveSensor2     = objID.getString("sensor2");
                        saveSensor3     = objID.getString("sensor3");
                        saveSensor4     = objID.getString("sensor4");
                        saveTanggal     = objID.getString("tanggal");

                        suksesDatabase = mDB.insertData(saveIDServer, saveSensor1, saveSensor2, saveSensor3, saveSensor4, saveTanggal);


                    }

                    return jsonArray;

                } catch (Exception e) {
                    e.printStackTrace();
                }


                return null;


            }

            @Override
            protected void onPostExecute(JSONArray s) {
                super.onPostExecute(s);

                try {

                    dataPrimeryKey = mDB.latest("ID");


//                    test = s.toString();
                    if(suksesDatabase) {
                        test = "sukses";
                    } else {test = "gagal";}
                    tv_test.setText(test);
                    tv_key.setText(dataPrimeryKey);

                } catch (Exception e) {

                    e.printStackTrace();
                }

            }
        }

        @Override
        public void run() {
            asyncServer asyncServerRun = new asyncServer();
            asyncServerRun.execute();
        }

    }

}
