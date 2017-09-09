package com.example.victor.latrans.repocitory.local.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.victor.latrans.repocitory.local.db.dao.ConversationDao;
import com.example.victor.latrans.repocitory.local.db.dao.MessAndConvDoa;
import com.example.victor.latrans.repocitory.local.db.dao.MessageDao;
import com.example.victor.latrans.repocitory.local.db.dao.TripDao;
import com.example.victor.latrans.repocitory.local.db.dao.UserDao;
import com.example.victor.latrans.repocitory.local.db.entity.Conversation;
import com.example.victor.latrans.repocitory.local.db.entity.Message;
import com.example.victor.latrans.repocitory.local.db.entity.Trip;
import com.example.victor.latrans.repocitory.local.db.entity.User;
import com.example.victor.latrans.repocitory.local.model.ConversationAndMessage;

import javax.inject.Singleton;

@Singleton
@Database(version = 1, entities = {User.class, Conversation.class, Message.class, Trip.class, ConversationAndMessage.class})
   public abstract class AppDatabase extends RoomDatabase{

        public static final String DATABASE_NAME = "latrans-db";
        abstract public UserDao userDao();
        abstract public MessageDao messageDao();
        abstract public ConversationDao conversationDao();
        abstract public TripDao tripDao();
        abstract public MessAndConvDoa dialogueDao();


        }