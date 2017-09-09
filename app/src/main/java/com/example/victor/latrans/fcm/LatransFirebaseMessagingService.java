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
import com.example.victor.latrans.repocitory.local.db.entity.Message;
import com.example.victor.latrans.view.ui.App;
import com.example.victor.latrans.view.ui.message.MessageActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import timber.log.Timber;


public class LatransFirebaseMessagingService extends FirebaseMessagingService {
    @Inject
    AppExecutors mAppExecutors;
    @Inject
    AppDatabase mAppDatabase;
    @Inject
    Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
      ((App) getApplication()).getAppComponent().inject(this);

    }

    private static final int NOTIFICATION_MAX_CHARACTERS = 30;

    private static final String JSON_KEY_MESSAGE_ID = "id";
    private static final String JSON_KEY_SENDER_ID = "sender_id";
    private static final String JSON_KEY_RECIPIENT_ID = "recipient_id";
    private static final String JSON_KEY_MESSAGE = "message";
    private static final String JSON_KEY_TIME = "time_sent";
    private static final String JSON_KEY_SENDER_USERNAME = "sender_username";
    private static final String JSON_KEY_SENDER_PICTURE = "sender_picture";
    private static final String JSON_KEY_CONVERSATION_ID = "conversation_id";




    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Timber.e( "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.

        Map<String, String> data = remoteMessage.getData();

        if (data.size() > 0) {
            Timber.d( "Message data payload: " + data);

            persistMessagePayload(data);
            sendNotification(data);

        }
    }

    public void persistMessagePayload(final Map<String, String> data){

        long conversation_id = Integer.parseInt((String) data.get(JSON_KEY_CONVERSATION_ID));
        long userone_id =  Integer.parseInt((String) data.get(JSON_KEY_SENDER_ID));
        long usertwo_id =  Integer.parseInt((String) data.get(JSON_KEY_RECIPIENT_ID));

        Conversation conversation = new Conversation();
        conversation.setId(conversation_id);
        conversation.setUserOneId(userone_id);
        conversation.setUserTwoId(usertwo_id);

        long message_id = Integer.parseInt((String) data.get(JSON_KEY_MESSAGE_ID));
        long time = Long.parseLong((String) data.get(JSON_KEY_TIME));

        Message message = new Message();
        message.id = message_id;
        message.sender_id = userone_id;
        message.recipient_id = usertwo_id;
        message.message = data.get(JSON_KEY_MESSAGE);
        message.time_sent = time;
        message.sender_username = data.get(JSON_KEY_SENDER_USERNAME);
        message.sender_picture = data.get(JSON_KEY_SENDER_PICTURE);
        message.conversation_id = conversation_id;


        mAppExecutors.diskIO().execute(() -> {
            mAppDatabase.conversationDao().insertConversation(conversation);
            mAppDatabase.messageDao().insertMesaage(message);

                List<Conversation> con = mAppDatabase.conversationDao().getAllConversationNL();
                Timber.e("Conversation size: " + con.size());
                List<Message> mes = mAppDatabase.messageDao().getAllMessageNL();
                Timber.e("Message size: " + mes.size());
        });

    }

    private void sendNotification(Map<String, String> data) {
        Intent intent = new Intent(this, MessageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Create the pending intent to launch the activity
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String sender_name = data.get(JSON_KEY_SENDER_USERNAME);
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
