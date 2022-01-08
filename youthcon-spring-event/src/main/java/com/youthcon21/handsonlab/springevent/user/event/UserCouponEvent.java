package com.youthcon21.handsonlab.springevent.user.event;

public class UserCouponEvent {
    private final String email;

    public UserCouponEvent(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
