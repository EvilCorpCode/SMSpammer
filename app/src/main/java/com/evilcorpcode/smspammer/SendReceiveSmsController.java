package com.evilcorpcode.smspammer;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import com.evilcorpcode.smspammer.models.ContactWithMessagesWrapper;

import java.util.ArrayList;

public class SendReceiveSmsController {
    private static final int MAX_SMS_MESSAGE_LENGTH = 160;
    public static final String SMS_SENT = "SMS_SENT";
    public static final String SMS_DELIVERED = "SMS_DELIVERED";
    private static final int SMS_PORT = 0;

    private ContactWithMessagesWrapper mContactWithMessagesWrapper;
    private int mNumberOfMsgsToSend;

    private SendReceiveSmsControllerListener mSendReceiveSmsControllerListener = new SendReceiveSmsControllerListener() {
        @Override
        public void onControllerBroadcastReceive(BroadcastReceiver broadcastReceiver) {
            //null pattern
        }
    };

    public SendReceiveSmsController(SendReceiveSmsControllerListener mSendReceiveSmsControllerListener) {
        this.mSendReceiveSmsControllerListener = mSendReceiveSmsControllerListener;
    }

    public void setContactWithMessagesWrapper(ContactWithMessagesWrapper contactWithMessagesWrapper, int numberOfMsgsToSend) {
        mContactWithMessagesWrapper = contactWithMessagesWrapper;
        mNumberOfMsgsToSend = numberOfMsgsToSend;
    }


    public void initiateSmsSending(Context context) {
        if (mNumberOfMsgsToSend > 0) {
            mNumberOfMsgsToSend -= 1;
            sendSms(context,
                    mContactWithMessagesWrapper.getContact().getPhoneNumber(),
                    mContactWithMessagesWrapper.getRandomMsg(),
                    false
            );
        }
    }

    private void sendSms(Context context, String phoneNumber, String message, boolean isBinary) {
        SmsManager manager = SmsManager.getDefault();

        PendingIntent piSend = PendingIntent.getBroadcast(context, 0, new Intent(SMS_SENT), 0);
        PendingIntent piDelivered = PendingIntent.getBroadcast(context, 0, new Intent(SMS_DELIVERED), 0);

        if (isBinary) {
            byte[] data = new byte[message.length()];

            for (int index = 0; index < message.length() && index < MAX_SMS_MESSAGE_LENGTH; ++index) {
                data[index] = (byte) message.charAt(index);
            }

            manager.sendDataMessage(phoneNumber, null, (short) SMS_PORT, data, piSend, piDelivered);
        } else {
            int length = message.length();

            if (length > MAX_SMS_MESSAGE_LENGTH) {
                ArrayList<String> messagelist = manager.divideMessage(message);
                manager.sendMultipartTextMessage(phoneNumber, null, messagelist, null, null);
            } else {
                manager.sendTextMessage(phoneNumber, null, message, piSend, piDelivered);
            }
        }
    }

    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mSendReceiveSmsControllerListener.onControllerBroadcastReceive(this);
        }
    };

    public interface SendReceiveSmsControllerListener {
        void onControllerBroadcastReceive(BroadcastReceiver broadcastReceiver);
    }
}
