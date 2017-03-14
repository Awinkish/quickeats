package app.resmap.com.quickeats.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.resmap.com.quickeats.R;
import app.resmap.com.quickeats.app.*;
import app.resmap.com.quickeats.app.AppController;
import app.resmap.com.quickeats.helper.SQLiteHandler;
import app.resmap.com.quickeats.helper.SessionManager;
import info.hoang8f.android.segmented.SegmentedGroup;


public class LoginActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private Button btnLoginAgent;
    private EditText inputEmail;
    private EditText inputPassword, inputEmailAgent, inputPasswordAgent;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    boolean userLogin;
    String tab;

    LinearLayout agentLayout, userLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = (EditText) findViewById(R.id.email);
        inputEmailAgent = (EditText) findViewById(R.id.email_agent);
        inputPassword = (EditText) findViewById(R.id.password);
        inputPasswordAgent = (EditText) findViewById(R.id.password_agent);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLoginAgent = (Button) findViewById(R.id.btnLogin_agent);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

        userLayout = (LinearLayout) findViewById(R.id.user);
        agentLayout = (LinearLayout) findViewById(R.id.agent);

        SegmentedGroup segmented3 = (SegmentedGroup) findViewById(R.id.segmented2);
        segmented3.setTintColor(Color.parseColor("#CCCCCC"), Color.parseColor("#FFFFFF"));

        Bundle extras = getIntent().getExtras();

        if(extras != null){
            tab = extras.getString("tab");
            if (tab.equals("user") )
                segmented3.check(R.id.button21);
                else segmented3.check(R.id.button22);
        }else {
            segmented3.check(R.id.button21);
        }

        //logoRounded();


        segmented3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (view.getId() == R.id.button21) {


                    //some code
                } else if (view.getId() == R.id.button22) {
                    //some code
                }
            }
        });


        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();


                if (!email.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(email, password);
                } else {

                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });


    }

    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        String url;

        if (userLogin)
            url = AppConfig.URL_LOGIN_USER;
         else  url = AppConfig.URL_LOGIN_AGENT;

        StringRequest strReq = new StringRequest(Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");


                    if (!error) {

                        session.setLogin(true);

                        // store   in SQLite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("fname");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");

                        db.addUser(name, email, uid, created_at);

                        Intent intent = new Intent(LoginActivity.this,
                                MainActivity.class);

                        startActivity(intent);
                        finish();
                    } else {

                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void agent(View view) {

        userLogin = false;

        screenSelect();

        agentLayout.setVisibility(View.VISIBLE);
        userLayout.setVisibility(View.GONE);

    }

    public void user(View view) {

        userLogin = true;

        screenSelect();

        userLayout.setVisibility(View.VISIBLE);
        agentLayout.setVisibility(View.GONE);
    }

    public void screenSelect(){

        if(userLogin){

            // Login button Click Event
            btnLogin.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    String email = inputEmail.getText().toString().trim();
                    String password = inputPassword.getText().toString().trim();

                    // Check for empty data in the form
                    if (!email.isEmpty() && !password.isEmpty()) {
                        // login user
                        checkLogin(email, password);
                    } else {
                        // Prompt user to enter credentials
                        Toast.makeText(getApplicationContext(),
                                "Please enter the credentials User!", Toast.LENGTH_LONG)
                                .show();
                    }
                }

            });
        }else{

            btnLoginAgent.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    String email = inputEmailAgent.getText().toString().trim();
                    String password = inputPasswordAgent.getText().toString().trim();


                    if (!email.isEmpty() && !password.isEmpty()) {

                        checkLogin(email, password);
                    } else {

                        Toast.makeText(getApplicationContext(),
                                "Please enter the credentials Agent!", Toast.LENGTH_LONG)
                                .show();
                    }
                }

            });
        }

    }
}
