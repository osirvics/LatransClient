package com.example.victor.latrans.repocitory.local.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;

import com.example.victor.latrans.repocitory.local.db.dao.ConversationDao;
import com.example.victor.latrans.repocitory.local.db.dao.MessAndConvDoa;
import com.example.victor.latrans.repocitory.local.db.dao.MessageDao;
import com.example.victor.latrans.repocitory.local.db.dao.OrderDao;
import com.example.victor.latrans.repocitory.local.db.dao.TripDao;
import com.example.victor.latrans.repocitory.local.db.dao.UserDao;
import com.example.victor.latrans.repocitory.local.db.entity.Conversation;
import com.example.victor.latrans.repocitory.local.db.entity.ConversationAndMessage;
import com.example.victor.latrans.repocitory.local.db.entity.Message;
import com.example.victor.latrans.repocitory.local.db.entity.Request;
import com.example.victor.latrans.repocitory.local.db.entity.Trip;
import com.example.victor.latrans.repocitory.local.db.entity.User;

import javax.inject.Singleton;

@Singleton
@Database(version = 2, entities = {User.class, Conversation.class, Message.class,
        Trip.class, ConversationAndMessage.class, Request.class})
   public abstract class AppDatabase extends RoomDatabase{
         //.fallbackToDestructiveMigration()
        public static final String DATABASE_NAME = "latrans-db";

        abstract public UserDao userDao();
        abstract public MessageDao messageDao();
        abstract public ConversationDao conversationDao();
        abstract public TripDao tripDao();
        abstract public MessAndConvDoa dialogueDao();
        abstract public OrderDao orderDao();

     public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
          @Override
          public void migrate(SupportSQLiteDatabase database) {
               database.execSQL("ALTER TABLE message "
                       + " ADD COLUMN sent_status TEXT");
          }
     };


        }