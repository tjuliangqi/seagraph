package cn.tju.seagraph.daomain;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String email;
    private String username;
    private String passwd;
    private String keywords;
    private int root;

    public User() {
    }

    public User(String email, String username, String passwd, String keywords, int root) {
        this.email = email;
        this.username = username;
        this.passwd = passwd;
        this.keywords = keywords;
        this.root = root;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public int getRoot() {
        return root;
    }

    public void setRoot(int root) {
        this.root = root;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", passwd='" + passwd + '\'' +
                ", keywords='" + keywords + '\'' +
                ", root=" + root +
                '}';
    }
}
