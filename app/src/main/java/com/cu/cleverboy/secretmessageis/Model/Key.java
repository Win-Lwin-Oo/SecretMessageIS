package com.cu.cleverboy.secretmessageis.Model;

public class Key {
    int id;
    String privateKey , publicKey;
    String refPrivate, refPublic;

    public String getRefPrivate() {
        return refPrivate;
    }

    public void setRefPrivate(String refPrivate) {
        this.refPrivate = refPrivate;
    }

    public String getRefPublic() {
        return refPublic;
    }

    public void setRefPublic(String refPublic) {
        this.refPublic = refPublic;
    }

    public Key(int id, String refPrivate, String refPublic, String privateKey, String publicKey ) {
        this.id = id;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.refPrivate = refPrivate;
        this.refPublic = refPublic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
