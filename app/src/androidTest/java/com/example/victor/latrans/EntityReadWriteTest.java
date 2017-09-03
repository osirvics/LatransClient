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
import com.example.victor.latrans.util.Util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

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
    public void insertAndRead() throws InterruptedException {
        User user = new User("kedvic@gmail.com",1, "Victor","http", 5, "Edu","victor");
        db.userDao().insertUser(user);
        User loaded = Util.getValue(db.userDao().loadUserById(1));
        assertThat(loaded, notNullValue());
        assertThat(loaded.username, is("victor"));
        assertThat(loaded.email, is("kedvic@gmail.com"));
        assertThat(loaded.name, notNullValue());
        assertThat(loaded.surname, is("Edu"));
    }

    @Test
    public void insertConversationAndRead() throws InterruptedException {
        Conversation conversation = new Conversation(2, 3, 2);
        db.conversationDao().insertConversation(conversation);
        Conversation loaded = Util.getValue(db.conversationDao().getAConversationByID(2));
        assertThat(loaded, notNullValue());
        assertEquals(2, loaded.user_two_id);

    }

    @Test
    public void InsertMessageAndRead() throws InterruptedException{
        Message message = new Message(1, 3, 2, "victor", "Yeah, way to excel", 233454546, 1 );
        db.messageDao().insertMesaage(message);
        Message loaded = Util.getValue(db.messageDao().getAMessageById(1));
        assertThat(loaded, notNullValue());
        assertThat(loaded.message, is("Yeah, way to excel"));

    }

    @Test
    public void insertTripAndValidateCRUD() throws  InterruptedException{
        Trip trip = new Trip(1, "07034464116", 16454545, 34431331, "24 july", "Minna", "Niger"
        , "Calabar", "Cross River", 2);
        db.tripDao().insertTrip(trip);
        Trip loaded = Util.getValue(db.tripDao().findAtripById(1));
        assertThat(loaded, notNullValue());
        assertThat(loaded.phone_no, is("07034464116"));


    }




}
