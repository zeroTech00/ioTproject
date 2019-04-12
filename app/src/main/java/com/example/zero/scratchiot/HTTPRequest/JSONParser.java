package com.example.zero.scratchiot.HTTPRequest;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.util.List;

public class JSONParser {

     InputStream is = null;
     JSONObject jOBj = null;
     String json = "";

    int timeout = 1000;

    public JSONParser()
    {

    }

    public JSONObject makeHttpRequest(String url, String method, List<NameValuePair> params)
    {
        try{
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeout);
            HttpConnectionParams.setSoTimeout(httpParameters, timeout);

            if (method=="POST")
            {
                DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }

            else if(method=="GET")
            {
                DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
                String paramString = URLEncodedUtils.format(params,"utf-8");
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }
        }

        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        catch (ClientProtocolException e)
        {
            e.printStackTrace();
        }

        catch (SocketException ste)
        {
            Log.e("Timeout Exception: ",ste.toString());
        }

        catch (ConnectTimeoutException e)
        {
            Log.e("Timeout Exception: ",e.toString());
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }

        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine())!=null)
            {
                sb.append(line+"\n");
            }

            is.close();
            json = sb.toString();
        }

        catch (Exception e)
        {
            Log.e("Buffer Error","Error converting result "+e.toString());
        }

        try {
            jOBj = new JSONObject(json);
        }

        catch (JSONException e)
        {
            Log.e("JSON Parser","Error parsing data "+e.toString());
        }

        return jOBj;
    }
}
