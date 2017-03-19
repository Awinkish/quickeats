package app.resmap.com.quickeats.models;

/**
 * Created by arwin on 3/19/17.
 */

public class Address {

    private String description;
    private String address;
    private String nickname;


    public Address() {
    }

    public Address(String description, String address, String nickname) {
        this.description = description;
        this.address = address;
        this.nickname = nickname;
    }

    public String getTitle() {
        return description;
    }

    public void setTitle(String title) {
        this.description = title;
    }

    public String getValue() {
        return address;
    }

    public void setValue(String address) {
        this.address = address;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }



}


