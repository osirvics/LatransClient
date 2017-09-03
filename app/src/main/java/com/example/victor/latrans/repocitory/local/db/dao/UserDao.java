package com.example.victor.latrans.repocitory.local.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.victor.latrans.repocitory.local.db.entity.User;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserDao {
    @Query("select * from user")
    List<User> loadAllUsers();

    @Query("select * from user")
    LiveData<User> loadUser();

    @Query("select * from user where id = :id")
    LiveData<User> loadUserById(int id);

    @Query("select * from user where username = :username")
    User loadUserByUsename(String username);

    @Query("select * from user where name = :name and surname = :surname")
    List<User> findByNameAndLastName(String name, String surname);

    @Insert(onConflict = REPLACE)
    void insertUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("DELETE FROM User")
    void deleteAll();

    @Insert(onConflict = REPLACE)
    void insertOrReplaceUsers(User... users);

//    @Query("delete from user where name like :badName OR lastName like :badName")
//    int deleteUsersByName(String badName);
//
//    @Insert(onConflict = IGNORE)
//    void insertOrReplaceUsers(User... users);
//
//    @Delete
//    void deleteUsers(User user1, User user2);
//
//    @Query("SELECT * FROM User WHERE age > :age") // TODO: Fix this!
//    List<User> findYoungerThan(int age);
//
//    @Query("SELECT * FROM User WHERE age < :age")
//    List<User> findYoungerThanSolution(int age);


}

