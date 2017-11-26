package com.example.victor.latrans;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.victor.latrans.repocitory.local.db.AppDatabase;
import com.example.victor.latrans.repocitory.local.db.dao.ConversationDao;
import com.example.victor.latrans.repocitory.local.db.dao.UserDao;
import com.example.victor.latrans.repocitory.local.db.entity.Conversation;
import com.example.victor.latrans.repocitory.local.db.entity.Message;
import com.example.victor.latrans.repocitory.local.db.entity.Trip;
import com.example.victor.latrans.repocitory.local.db.entity.User;
import com.example.victor.latrans.repocitory.local.db.entity.ConversationAndMessage;
import com.example.victor.latrans.util.Util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by Victor on 27/08/2017.
 */
@RunWith(AndroidJUnit4.class)
public class EntityReadWriteTest {

    private UserDao userDao;
    private ConversationDao mConversationDao;
    private AppDatabase db;


    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        userDao = db.userDao();
        mConversationDao = db.conversationDao();

        Conversation conversation = new Conversation(1, 3, 2);
        db.conversationDao().insertConversation(conversation);
//
//        Conversation conversation = new Conversation(2, 4,3);
//        db.conversationDao().insertConversation(conversation);
    }

    @After
    public void tearDown() throws Exception {
        db.close();
    }

    @Test
    public void shouldCreateDatabase() {
        assertNotNull(db);
    }

    @Test
    public void shouldCreateDao() {
        assertNotNull(userDao);
    }


    @Test
    public void VallidateConversationDuplicate() throws InterruptedException {
        Conversation conversation = new Conversation(1, 3, 2);
        db.conversationDao().insertConversation(conversation);
        List<Conversation> conversationList = Util.getValue(db.conversationDao().getAllConversation());
        assertEquals(1, conversationList.size());

        Message message = new Message(2, 3, 2, "victor", "Yeah, way to excel", 233454546, 1 ,"http:///");
        db.messageDao().insertMesaage(message);

        Message message2 = new Message(3, 3, 2, "edu", "Second message", 233454546, 1 ,"http:////");
        db.messageDao().insertMesaage(message2);

        List<Message> messages = Util.getValue(db.messageDao().getAllMessage());

        assertEquals(2, messages.size());


        List<ConversationAndMessage> loaded = Util.getValue(db.messageDao().findConversationAndMessage());
        assertThat(loaded, notNullValue());

        assertEquals(2, loaded.size());


        ConversationAndMessage current = loaded.get(0);
        assertEquals(current.getId(), 1);
//        ConversationAndMessage cuurrent = loaded.get(1);
    }

    @Test
    public void insertAndRead() throws InterruptedException {
        User user = new User("kedvic@gmail.com",1, "Victor","http", 5, "Edu","victor");
        db.userDao().insertUser(user);
        User loaded = Util.getValue(db.userDao().loadUserById(1));
        assertThat(loaded, notNullValue());
        assertThat(loaded.username, is("victor"));
        assertThat(loaded.getEmail(), is("kedvic@gmail.com"));
        assertThat(loaded.name, notNullValue());
        assertThat(loaded.surname, is("Edu"));
    }

    @Test
    public void insertConversationsAndRead() throws InterruptedException {
        Conversation conversation = new Conversation(2, 3, 2);
        db.conversationDao().insertConversation(conversation);
        Conversation loaded = Util.getValue(db.conversationDao().getAConversationByID(2));
        assertThat(loaded, notNullValue());
        assertEquals(2, loaded.getUserTwoId());
        assertEquals(2, loaded.getId());
    }



//    @Test
//    public void insertConversationAndRead() throws InterruptedException {
//        Conversation conversation = new Conversation(1, 3, 2);
//        db.conversationDao().insertConversation(conversation);
//
//
//        Message message = new Message(1, 3, 2, "edu", "way to excel", 23345546, 1 ,"http://");
//        db.messageDao().insertMesaage(message);
//
//        Conversation conversation2 = new Conversation(2, 2, 4);
//        db.conversationDao().insertConversation(conversation2);
//
//
//        Conversation conversation3 = new Conversation(2, 4, 2);
//        db.conversationDao().insertConversation(conversation3);
//
//        List<Conversation> conversationList = Util.getValue(db.conversationDao().getAllConversation());
//
//
//        assertEquals(2, conversationList.size());
//
//
//        Message message2 = new Message(2, 4, 2, "victor", "Yeah, way to excel", 233454546, 2 ,"http:///");
//        db.messageDao().insertMesaage(message2);
//
//        List<ConversationAndMessage> loaded = Util.getValue(db.messageDao().findConversationAndMessage());
//        assertThat(loaded, notNullValue());
//
//        ConversationAndMessage first = loaded.get(0);
//        ConversationAndMessage cuurrent = loaded.get(1);
//
//
//        assertEquals(2, loaded.size());
//
//        assertThat(first.sender_username, is("edu"));
//        assertThat(first.sender_picture, is("http://"));
//        assertEquals(first.id, 1);
//
//        assertThat(cuurrent.sender_username, is("victor"));
//       assertThat(cuurrent.sender_picture, is("http:///"));
//        assertEquals(2, cuurrent.id);
//
//    }

    @Test
    public void InsertMessageAndRead() throws InterruptedException{
        Message message = new Message(1, 3, 2, "victor", "Yeah, way to excel", 233454546, 1 ,"http://");
        db.messageDao().insertMesaage(message);
        Message loaded = Util.getValue(db.messageDao().getAMessageById(1));
        assertThat(loaded, notNullValue());
        assertEquals(1, loaded.getId());
        assertThat(loaded.getMessage(), is("Yeah, way to excel"));

    }

    @Test
    public void insertTripAndValidateCRUD() throws  InterruptedException{
        Trip trip = new Trip(1, "07034464116", 16454545, 34431331,"http://www.ga.com", "24 july", "Minna", "Niger"
        , "Calabar", "Cross River", 2);
        db.tripDao().insertTrip(trip);
        Trip loaded = Util.getValue(db.tripDao().findAtripById(1));

        assertThat(loaded, notNullValue());
        assertThat(loaded.getPhone_no(), is("07034464116"));


    }

    @Test
    public void insertListAndValidateCRUD() throws InterruptedException{
        Trip trip = new Trip(1, "07034464116", 16454545, 34431331,"http://www.ga.com", "24 july", "Minna", "Niger"
                , "Calabar", "Cross River", 2);

        Trip trip2 = new Trip(2, "07034464116", 16454545, 34431331,"http://www.ga.com", "24 july", "Minna", "Niger"
                , "Calabar", "Cross River", 2);
        Trip trip3 = new Trip(3, "07034464116", 16454545, 34431331,"http://www.ga.com", "24 july", "Minna", "Niger"
                , "Calabar", "Cross River", 2);
        List<Trip> trips = new ArrayList<>();
        trips.add(trip);
        trips.add(trip2);
        trips.add(trip3);
        db.tripDao().insertTrips(trips);
        List<Trip> loaded = Util.getValue(db.tripDao().getAllTrips());
        assertThat(loaded, notNullValue());
        assertThat(loaded.size(), is(3));
    }




}
