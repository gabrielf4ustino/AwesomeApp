package br.com.ffscompany.moviehub.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import br.com.ffscompany.moviehub.entity.User;

@Dao
public interface UserDao {

    @Transaction
    @Query("SELECT * FROM User WHERE email = :email")
    User getUserByEmail(String email);

    @Update
    void update(User user);

    @Insert
    void insert(User user);

    @Delete
    void delete(User user);
}
