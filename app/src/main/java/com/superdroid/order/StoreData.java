package com.superdroid.order;

import android.graphics.Bitmap;

public class StoreData {
    private String member_id;
    private String member_type;
    private String member_name;

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    private String member_address;
    private Bitmap member_image;

    public String getMember_type() {
        return member_type;
    }

    public void setMember_type(String member_type) {
        this.member_type = member_type;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getMember_address() {
        return member_address;
    }

    public void setMember_address(String member_address) {
        this.member_address = member_address;
    }

    public Bitmap getMember_image() {
        return member_image;
    }

    public void setMember_image(Bitmap member_image) {
        this.member_image = member_image;
    }
}
