package app.resmap.com.quickeats.models;

/**
 * Created by arwin on 3/14/17.
 */

public class Validate {

    public Validate(){

    }

    public boolean password(String password, String confirm){

        boolean validate;

        if(password.equals(confirm))
            validate = true;

        else validate = false;

        return validate;

    }

    public boolean email(String email){

        boolean validate;

        if(email.contains("@"))
            validate = true;
        else validate = false;

        return validate;

    }

//    public String getMessage(){
//
//    }

}
