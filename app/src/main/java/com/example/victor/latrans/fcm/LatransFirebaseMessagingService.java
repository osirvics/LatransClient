package com.example.victor.latrans.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.example.victor.latrans.R;
import com.example.victor.latrans.google.AppExecutors;
import com.example.victor.latrans.repocitory.local.db.AppDatabase;
import com.example.victor.latrans.repocitory.local.db.entity.Conversation;
import com.example.victor.latrans.repocitory.local.db.entity.ConversationAndMessage;
import com.example.victor.latrans.repocitory.local.db.entity.Message;
import com.example.victor.latrans.repocitory.local.db.entity.Request;
import com.example.victor.latrans.repocitory.local.db.entity.Trip;
import com.example.victor.latrans.util.SharedPrefsHelper;
import com.example.victor.latrans.view.ui.App;
import com.example.victor.latrans.view.ui.message.MessageActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import javax.inject.Inject;

import timber.log.Timber;


public class LatransFirebaseMessagingService extends FirebaseMessagingService {
    @Inject
    AppExecutors mAppExecutors;
    @Inject
    AppDatabase mAppDatabase;
    @Inject
    SharedPrefsHelper mSharedPrefsHelper;
    long userId;
    private static final int NOTIFICATION_MAX_CHARACTERS = 30;
    private static final String TRIP_FCM_TOPIC= "/topics/trips_fcm";
    private static final String ORDER_FCM_TOPIC = "/topics/request_fcm";

    //message key-value constants
    private static final String JSON_KEY_MESSAGE_ID = "id";
    private static final String JSON_KEY_SENDER_ID = "sender_id";
    private static final String JSON_KEY_RECIPIENT_ID = "recipient_id";
    private static final String JSON_KEY_MESSAGE = "message";
    private static final String JSON_KEY_TIME = "time_sent";
    private static final String JSON_KEY_SENDER_FIRST_NAME = "sender_first_name";
    private static final String JSON_KEY_SENDER_LAST_NAME = "sender_last_name";
    private static final String JSON_KEY_RECIPIENT_FIRST_NAME = "recipient_first_name";
    private static final String JSON_KEY_RECIPIENT_LAST_NAME = "recipient_last_name";
    private static final String JSON_KEY_SENDER_PICTURE = "sender_picture";
    private static final String JSON_KEY_CONVERSATION_ID = "conversation_id";
    private static final String JSON_KEY_SENT_STATUS = "sent_status";

    //Trip key-value constants
    private static final String JSON_KEY_TRIP_ID = "id";
    private static final String JSON_KEY_TRIP_PHONE_NO = "phone_no";
    private static final String JSON_KEY_TRIP_FROM_STATE = "traveling_from_state";
    private static final String JSON_KEY_TRIP_FROM_CITY = "traveling_from_city";
    private static final String JSON_KEY_TRIP_TO_STATE = "traveling_to_state";
    private static final String JSON_KEY_TRIP_TO_CITY = "traveling_to_city";
    private static final String JSON_KEY_TRIP_TRAVELLING_DATE = "traveling_date";
    private static final String JSON_KEY_TRIP_POSTED_ON = "posted_on";
    private static final String JSON_KEY_TRIP_USER_ID = "user_id";
    private static final String JSON_KEY_TRIP_TIME_UPDATED = "time_updated";
    private static final String JSON_KEY_TRIP_PROFILE_IMAGE = "profile_image";
    private static final String JSON_KEY_TRIP_USER_FIRST_NAME = "user_first_name";
    private static final String JSON_KEY_TRIP_USER_LAST_NAME = "user_last_name";

    //Order key-value constants
    private static final String JSON_KEY_ORDER_ID = "id";
    private static final String JSON_KEY_ORDER_DELIVERY_STATE = "delivery_state";
    private static final String JSON_KEY_ORDER_DELIVERY_CITY = "delivery_city";
    private static final String JSON_KEY_ORDER_ITEM_LOCATION_STATE = "item_location_state";
    private static final String JSON_KEY_ORDER_ITEM_LOCATION_CITY = "item_location_city";
    private static final String JSON_KEY_ORDER_PICTURE = "picture";
    private static final String JSON_KEY_ORDER_OFFER_AMOUNT = "offer_amount";
    private static final String JSON_KEY_ORDER_DELIVER_BEFORE = "deliver_before";
    private static final String JSON_KEY_ORDER_POSTED_ON = "posted_on";
    private static final String JSON_KEY_ORDER_TIME_UPDATED = "time_updated";
    private static final String JSON_KEY_ORDER_ITEM_NAME = "item_name";
    private static final String JSON_KEY_ORDER_PROFILE_IMAGE = "profile_image";
    private static final String JSON_KEY_ORDER_USER_FIRST_NAME = "user_first_name";
    private static final String JSON_KEY_ORDER_USER_LAST_NAME = "user_last_name";
    private static final String JSON_KEY_ORDER_USER_ID = "user_id";
    private static final String JSON_KEY_ORDER_PHONE_NO = "phone_no";




    @Override
    public void onCreate() {
        super.onCreate();
      ((App) getApplication()).getAppComponent().inject(this);
      userId = mSharedPrefsHelper.getUserId();
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Timber.e( "From: " + remoteMessage.getFrom());
        String topic = remoteMessage.getFrom();
        // Check if message contains a data payload.
        Map<String, String> data = remoteMessage.getData();
        if (data.size() > 0) {
            Timber.d( "Message data payload: " + data);
            //Checks if message is from trips or a user sent message
            switch (topic){
                case TRIP_FCM_TOPIC:
                    persistTripPayload(data);
                    break;
                case ORDER_FCM_TOPIC:
                    persistOrderPayload(data);
                    break;
                default:
                    persistMessagePayload(data);
                    break;
            }

        }
    }

    private void persistTripPayload(final Map<String, String> data){
        long trip_id = Long.parseLong(data.get(JSON_KEY_TRIP_ID));
        long user_id = Long.parseLong(data.get(JSON_KEY_TRIP_USER_ID));
        long posted_on = Long.parseLong(data.get(JSON_KEY_TRIP_POSTED_ON));
        long time_updated = Long.parseLong(data.get(JSON_KEY_TRIP_TIME_UPDATED));

        Trip trip = new Trip();
        trip.setId(trip_id);
        trip.setUser_id(user_id);
        trip.setPosted_on(posted_on);
        trip.setTime_updated(time_updated);
        trip.setTraveling_from_state(data.get(JSON_KEY_TRIP_FROM_STATE));
        trip.setTraveling_from_city(data.get(JSON_KEY_TRIP_FROM_CITY));
        trip.setTraveling_to_state(data.get(JSON_KEY_TRIP_TO_STATE));
        trip.setTraveling_to_city(data.get(JSON_KEY_TRIP_TO_CITY));
        trip.setPhone_no(data.get(JSON_KEY_TRIP_PHONE_NO));
        trip.setTraveling_date(data.get(JSON_KEY_TRIP_TRAVELLING_DATE));
        trip.setProfile_image(data.get(JSON_KEY_TRIP_PROFILE_IMAGE));
        trip.setUser_first_name(data.get(JSON_KEY_TRIP_USER_FIRST_NAME));
        trip.setUser_last_name(data.get(JSON_KEY_TRIP_USER_LAST_NAME));
        mAppExecutors.diskIO().execute(() -> mAppDatabase.tripDao().insertTrip(trip));

    }


    private void persistOrderPayload(final Map<String, String> data){
        long order_id = Long.parseLong(data.get(JSON_KEY_ORDER_ID));
        long user_id = Long.parseLong(data.get(JSON_KEY_ORDER_USER_ID));
        long posted_on = Long.parseLong(data.get(JSON_KEY_ORDER_POSTED_ON));
        long time_updated = Long.parseLong(data.get(JSON_KEY_ORDER_TIME_UPDATED));

        Request request = new Request();
        request.setId(order_id);
        request.setUser_id(user_id);
        request.setPosted_on(posted_on);
        request.setTime_updated(time_updated);
        request.setDelivery_state(data.get(JSON_KEY_ORDER_DELIVERY_STATE));
        request.setDelivery_city(data.get(JSON_KEY_ORDER_DELIVERY_CITY));
        request.setItem_location_state(data.get(JSON_KEY_ORDER_ITEM_LOCATION_STATE));
        request.setItem_location_city(data.get(JSON_KEY_ORDER_ITEM_LOCATION_CITY));
        request.setOffer_amount(data.get(JSON_KEY_ORDER_OFFER_AMOUNT));
        request.setDeliver_before(data.get(JSON_KEY_ORDER_DELIVER_BEFORE));
        request.setPicture(data.get(JSON_KEY_ORDER_PICTURE));
        request.setItem_name(data.get(JSON_KEY_ORDER_ITEM_NAME));
        request.setProfile_image(data.get(JSON_KEY_ORDER_PROFILE_IMAGE));
        request.setUser_first_name(data.get(JSON_KEY_ORDER_USER_FIRST_NAME));
        request.setUser_last_name(data.get(JSON_KEY_ORDER_USER_LAST_NAME));
        request.setPhone_no(data.get(JSON_KEY_ORDER_PHONE_NO));
        mAppExecutors.diskIO().execute(() -> mAppDatabase.orderDao().insertRequest(request));

    }

    /*  Cache FCM data messages.
        Response contains messages, but we're creating the conversation object from here also
     */
    public void persistMessagePayload(final Map<String, String> data){

        long conversation_id =  Long.parseLong(data.get(JSON_KEY_CONVERSATION_ID));
        long userone_id =  Long.parseLong(data.get(JSON_KEY_SENDER_ID));
        long usertwo_id =  Long.parseLong( data.get(JSON_KEY_RECIPIENT_ID));

        Conversation conversation = new Conversation();
        conversation.setId(conversation_id);
        conversation.setUser_one_id(userone_id);
        conversation.setUser_two_id(usertwo_id);

        long message_id = Integer.parseInt(data.get(JSON_KEY_MESSAGE_ID));
        long time = Long.parseLong(data.get(JSON_KEY_TIME));

        Message message = new Message();
        message.setId(message_id);
        message.setSender_id(userone_id);
        message.setRecipient_id(usertwo_id);
        message.setMessage(data.get(JSON_KEY_MESSAGE));
        message.setTime_sent(time);
        message.setSender_first_name(data.get(JSON_KEY_SENDER_FIRST_NAME));
        message.setSender_last_name(data.get(JSON_KEY_SENDER_LAST_NAME));
        message.setSender_picture(data.get(JSON_KEY_SENDER_PICTURE));
        message.setRecipient_first_name(data.get(JSON_KEY_RECIPIENT_FIRST_NAME));
        message.setRecipient_last_name(data.get(JSON_KEY_RECIPIENT_LAST_NAME));
        message.setConversation_id(conversation_id);
        message.setSent_status(data.get(JSON_KEY_SENT_STATUS));

        ConversationAndMessage conversationAndMessage = new ConversationAndMessage();
        conversationAndMessage.setId(conversation_id);
        conversationAndMessage.setSender_id(userone_id);
        conversationAndMessage.setRecipient_id(usertwo_id);
        conversationAndMessage.setMessage(data.get(JSON_KEY_MESSAGE));
        conversationAndMessage.setSender_first_name(data.get(JSON_KEY_SENDER_FIRST_NAME));
        conversationAndMessage.setSender_last_name(data.get(JSON_KEY_SENDER_LAST_NAME));
        conversationAndMessage.setRecipient_first_name(data.get(JSON_KEY_RECIPIENT_FIRST_NAME));
        conversationAndMessage.setRecipient_last_name(data.get(JSON_KEY_RECIPIENT_LAST_NAME));
        conversationAndMessage.setSender_picture(data.get(JSON_KEY_SENDER_PICTURE));
        conversationAndMessage.setTime_sent(time);


        mAppExecutors.diskIO().execute(() -> {
            mAppDatabase.conversationDao().insertConversation(conversation);
            mAppDatabase.messageDao().insertMesaage(message);
            mAppDatabase.dialogueDao().insertDialogue(conversationAndMessage);

            // Sending notification on the main thread
            mAppExecutors.mainThread().execute(() -> sendNotification(data, conversation_id, userone_id));
        });

    }

    private void sendNotification(Map<String, String> data, long conversationId, long senderId) {
        Intent intent = MessageActivity.newIntent(this,conversationId, senderId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Create the pending intent to launch the activity
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String first_name = data.get(JSON_KEY_SENDER_FIRST_NAME);
        String last_name = data.get(JSON_KEY_SENDER_LAST_NAME);
        String sender_name = first_name + " " + last_name;
        String message = data.get(JSON_KEY_MESSAGE);

        // If the message is longer than the max number of characters we want in our
        // notification, truncate it and add the unicode character for ellipsis
        if (message.length() > NOTIFICATION_MAX_CHARACTERS) {
            message = message.substring(0, NOTIFICATION_MAX_CHARACTERS) + "\u2026";
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_person_black_48dp)
                .setContentTitle(String.format(getString(R.string.notification_message), sender_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

}
