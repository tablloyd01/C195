package com.stone.rosetta.repository.model;

public class User extends Entity{

    private String username;
    private String password;

    private Boolean active;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(Long id, String username, String password, Boolean active) {
        super(id);
        this.username = username;
        this.password = password;
        this.active = active;
    }
    
    public User(Long id, String username) {
        super(id);
        this.username = username;
    }

    public User(Long id, String username, Boolean active) {
        super(id);
        this.username = username;
        this.active = active;
    }




    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
