package com.cu.cleverboy.secretmessageis.Model;

public class Contact {
    private String name,phone, public_key, reference_key;

    public Contact(String name, String phone, String public_key, String reference_key) {
        this.name = name;
        this.phone = phone;
        this.public_key = public_key;
        this.reference_key = reference_key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPublic_key() {
        return public_key;
    }

    public void setPublic_key(String private_key) {
        this.public_key = private_key;
    }

    public String getReference_key() {
        return reference_key;
    }

    public void setReference_key(String reference_key) {
        this.reference_key = reference_key;
    }
}
