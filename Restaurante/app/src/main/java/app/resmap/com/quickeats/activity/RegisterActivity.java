package app.resmap.com.quickeats.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import app.resmap.com.quickeats.app.AppConfig;
import app.resmap.com.quickeats.app.AppController;
import app.resmap.com.quickeats.helper.SQLiteHandler;
import app.resmap.com.quickeats.helper.SessionManager;

public class RegisterActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText firstName, secondName, inputEmail, inputPassword, inputMobile, inputPasswordConfirm
            , firstNameAgent, secondNameAgent, inputEmailAgent, inputMobileAgent, inputPasswordAgent,
            inputPasswordConfirmAgent, referralCode;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    boolean userLogin = true;
    LinearLayout userLayout, agentLayout;

    public RegisterActivity() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // User fields
        referralCode = (EditText) findViewById(R.id.referralCode);
        firstName = (EditText) findViewById(R.id.firstName);
        secondName = (EditText) findViewById(R.id.secondName);
        inputEmail = (EditText) findViewById(R.id.email);
        inputMobile = (EditText) findViewById(R.id.mobile);
        inputPassword = (EditText) findViewById(R.id.password);
        inputPasswordConfirm = (EditText) findViewById(R.id.passwordConfirm);

        // Agents fields
        firstNameAgent = (EditText) findViewById(R.id.firstNameAgent);
        secondNameAgent = (EditText) findViewById(R.id.secondNameAgent);
        inputEmailAgent = (EditText) findViewById(R.id.emailAgent);
        inputMobileAgent = (EditText) findViewById(R.id.mobileAgent);
        inputPasswordAgent = (EditText) findViewById(R.id.passwordAgent);
        inputPasswordConfirmAgent = (EditText) findViewById(R.id.passwordConfirmAgent);

        userLayout = (LinearLayout) findViewById(R.id.user);
        agentLayout = (LinearLayout) findViewById(R.id.agent);

        btnRegister = (Button) findViewById(R.id.btnRegister);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());

        db = new SQLiteHandler(getApplicationContext());


        if (session.isLoggedIn()) {

            Intent intent = new Intent(RegisterActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }


    }

    /**
     * Function to store user in MySQL database
     * */
    private void registerUser(final String fName, final String sName, final String email, final String mobile,
                              final String password, final  String referral) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_REGISTER_USER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        //Store in sqlite

                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String fname = user.getString("fname");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");

                        // Inserting row in users table
                        db.addUser(fname, email, uid, created_at);

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                RegisterActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("referral", referral);
                params.put("fname", fName);
                params.put("sname", sName);
                params.put("moile", mobile);
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void registerAgent(final String fName, final String sName, final String email, final String mobile,
                               final String password) {

        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_REGISTER_AGENT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        // store in sqlite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String fname = user.getString("fname");
                        String sname = user.getString("sname");
                        String mobile = user.getString("mobile");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");

                        db.addUser(fname, email, uid, created_at);

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(
                                RegisterActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("fname", fName);
                params.put("sname", sName);
                params.put("moile", mobile);
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void agent(View view) {

        userLogin = false;

        selectScreen();

        agentLayout.setVisibility(View.VISIBLE);
        userLayout.setVisibility(View.GONE);

    }

    public void user(View view) {

        userLogin = true;

        selectScreen();

        userLayout.setVisibility(View.VISIBLE);
        agentLayout.setVisibility(View.GONE);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void selectScreen(){
        // Choice between user and agent handled here
        if(userLogin){

            btnRegister.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    String referral = referralCode.getText().toString().trim();
                    String fName = firstName.getText().toString().trim();
                    String sName = secondName.getText().toString().trim();
                    String mobile = inputMobile.getText().toString().trim();
                    String email = inputEmail.getText().toString().trim();
                    String password = inputPassword.getText().toString().trim();

                    if (!fName.isEmpty() && !sName.isEmpty() && !mobile.isEmpty() &&
                            !email.isEmpty() && !password.isEmpty() && !referral.isEmpty()) {
                        registerUser(fName, sName, email, mobile, password, referral);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Please enter your details!", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            });

        } else {

            btnRegister.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    String fName = firstNameAgent.getText().toString().trim();
                    String sName = secondNameAgent.getText().toString().trim();
                    String mobile = inputMobileAgent.getText().toString().trim();
                    String email = inputEmailAgent.getText().toString().trim();
                    String password = inputPasswordAgent.getText().toString().trim();

                    if (!fName.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                        registerAgent(fName, sName, email, mobile, password);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Please enter your details Man!", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            });

        }
    }
}
