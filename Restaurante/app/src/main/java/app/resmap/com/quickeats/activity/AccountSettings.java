package app.resmap.com.quickeats.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.resmap.com.quickeats.R;
import app.resmap.com.quickeats.SharedPreference;
import app.resmap.com.quickeats.adapters.SettingsAdapter;
import app.resmap.com.quickeats.app.AppConfig;
import app.resmap.com.quickeats.app.AppController;
import app.resmap.com.quickeats.models.Account;

public class AccountSettings extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private SettingsAdapter adapter;
    private List<Account> albumList;
    private static final String TAG = AccountSettings.class.getSimpleName();
    private ProgressDialog pDialog;
    private Button btnToolBar;
    SharedPreference shared;
    boolean update = false;

    String fname, sname, email, mobile, password,email_check;

    public static final String PREFS_FIRST_NAME = "fname";
    public static final String PREFS_FIRST = "firstname";
    public static final String PREFS_LAST_NAME = "lname";
    public static final String PREFS_LAST = "lastname";
    public static final String PREFS_EMAIL_NAME = "email_name";
    public static final String PREFS_EMAIL = "email";
    public static final String PREFS_MOBILE_NAME = "mobile_name";
    public static final String PREFS_MOBILE = "mobile";
    public static final String PREFS_PASSWORD = "mobile";
    public static final String PREFS_PASSWORD_NAME = "mobile";

    public AccountSettings() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        SharedPreferences sharedpreferences = getSharedPreferences(LoginActivity.ACCOUNT_PREFERENCES, Context.MODE_PRIVATE);
        email_check = sharedpreferences.getString("email", null);

        checkUser(email_check, "", "", "", "");

        initialize();

    }

    private void initialize() {

        albumList = new ArrayList<>();
        adapter = new SettingsAdapter(this, albumList);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(1), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem mAccounts = menu.findItem(R.id.action_accounts);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_accounts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent = new Intent(getApplicationContext(), AccountSettings.class);
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_accounts:

                break;
            default:
                break;
        }

        return true;
    }

    public void update(MenuItem item) {

        update = true;

        shared = new SharedPreference();

        fname = shared.getValue(getApplicationContext(), PREFS_FIRST_NAME, PREFS_FIRST);
        sname = shared.getValue(getApplicationContext(), PREFS_LAST_NAME, PREFS_LAST);
        email = shared.getValue(getApplicationContext(), PREFS_EMAIL_NAME, PREFS_EMAIL);
        mobile = shared.getValue(getApplicationContext(), PREFS_MOBILE_NAME, PREFS_MOBILE);
        password = shared.getValue(getApplicationContext(), PREFS_PASSWORD_NAME, PREFS_PASSWORD);

        checkUser(email, fname, sname, mobile, password);


    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    static class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    public int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void loadData(String firstname, String lastname, String mobile, String email,
                          String password, String new_pass) {

        Account a = new Account("First Name", firstname);
        albumList.add(a);

        a = new Account("Last Name", lastname);
        albumList.add(a);

        a = new Account("Email", email);
        albumList.add(a);

        a = new Account("Mobile", mobile);
        albumList.add(a);

        a = new Account("Current Password", password);
        albumList.add(a);

        a = new Account("New Password", new_pass);
        albumList.add(a);

        adapter.notifyDataSetChanged();
    }

    private void checkUser(final String emailVerify, final String firstname, final String lastname,
                           final String phone, String pass) {

        if(pass.equals("") || pass == null){
            pass = "empty";
        }
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        String url;

        if(update){
            url = AppConfig.URL_UPDATE;
            pDialog.setMessage("Updating information ...");
        }
        else {
            url = AppConfig.URL_ACCOUNT;
            pDialog.setMessage("Loading information ...");
        }
        showDialog();


        final String finalPass = pass;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");


                    if (!error) {

                        // store in SQLite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String fname = user.getString("fname");
                        String sname = user.getString("sname");
                        String mobile = user.getString("phone");
                        String email = user.getString("email");
                        String success = user.getString("success");
                        String created_at = user
                                .getString("created_at");

                        // Store user information temporarily

                        initialize();

                        shared = new SharedPreference();

                        shared.save(getApplicationContext(), AccountSettings.PREFS_FIRST_NAME,
                                AccountSettings.PREFS_FIRST, fname);

                        shared.save(getApplicationContext(), AccountSettings.PREFS_LAST_NAME,
                                AccountSettings.PREFS_LAST, sname);

                        shared.save(getApplicationContext(), AccountSettings.PREFS_MOBILE_NAME,
                                AccountSettings.PREFS_MOBILE, mobile);

                        shared.save(getApplicationContext(), AccountSettings.PREFS_EMAIL_NAME,
                                AccountSettings.PREFS_EMAIL, email);

                        loadData(fname, sname, mobile, email, "*********", "Enter new password");

                        Toast.makeText(getApplicationContext(),
                                success, Toast.LENGTH_LONG).show();

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

                SharedPreferences sharedpreferences = getSharedPreferences(LoginActivity.ACCOUNT_PREFERENCES, Context.MODE_PRIVATE);
                String email_old = sharedpreferences.getString("email", null);
                String mobile_old = sharedpreferences.getString("mobile", null);

                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("fname", firstname);
                params.put("sname", lastname);
                params.put("email", emailVerify);
                params.put("email_old", email_old);
                params.put("mobile_old", mobile_old);
                params.put("mobile", phone);
                params.put("password", finalPass);

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

    @Override
    protected void onResume() {
        super.onResume();
        recyclerView.invalidate();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
