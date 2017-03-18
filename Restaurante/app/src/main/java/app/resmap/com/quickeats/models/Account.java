package app.resmap.com.quickeats.models;

/**
 * Created by arwin on 3/18/17.
 */

public class Account {
    private String title;
    private String value;


    public Account() {
    }

    public Account(String title, String value) {
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }



}

