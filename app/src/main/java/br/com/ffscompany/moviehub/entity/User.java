package br.com.ffscompany.moviehub.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.util.List;

@Entity
public class User {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    private String name;

    private String email;

    private String password;

    private String key;

    public User() {
    }

    public User(String name, String email, String password, String key) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.key = key;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
