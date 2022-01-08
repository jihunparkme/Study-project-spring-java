package com.youthcon21.handsonlab.springevent.user.event;

public class UserAdminEvent {
    private final String username;

    public UserAdminEvent(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
