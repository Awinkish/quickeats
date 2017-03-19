package app.resmap.com.quickeats.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.resmap.com.quickeats.R;
import app.resmap.com.quickeats.SharedPreference;
import app.resmap.com.quickeats.adapters.AddressAdapter;
import app.resmap.com.quickeats.adapters.SettingsAdapter;
import app.resmap.com.quickeats.app.AppConfig;
import app.resmap.com.quickeats.app.AppController;
import app.resmap.com.quickeats.models.Account;
import app.resmap.com.quickeats.models.Address;
import app.resmap.com.quickeats.models.Validate;

public class AddressBook extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Locale";
    private RecyclerView recyclerView;
    private AddressAdapter adapter;
    private List<Address> addressList;
    private SharedPreference shared;
    private ProgressDialog pDialog;

    public static final String PREFS_NICKNAME_NAME = "nickname_name";
    public static final String PREFS_NICKNAME = "nickname";
    public static final String PREFS_ADDRESS_NAME = "address_name";
    public static final String PREFS_ADDRESS = "address";
    public static final String PREFS_DESC_NAME = "desc_name";
    public static final String PREFS_DESC = "desc";
    String address, uid_check;

    boolean update = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_book);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        SharedPreferences sharedpreferences = getSharedPreferences(LoginActivity.ACCOUNT_PREFERENCES, Context.MODE_PRIVATE);
        uid_check = sharedpreferences.getString("uid", null);

        initialize();

        loadAddress();

        //loadAddress();

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem mAccounts = menu.findItem(R.id.action_accounts);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_address, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_address:

                showAlert("Arwin", "mbugua");
                break;
            default:
                break;
        }

        return true;
    }

    public void loadAddress(){

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();
        autocompleteFragment.setFilter(typeFilter);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());

                address = place.getName().toString();

                Toast.makeText(getApplicationContext(), "Click on add", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    private void initialize() {

        addressList = new ArrayList<>();
        adapter = new AddressAdapter(this, addressList);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new AccountSettings.GridSpacingItemDecoration(2, dpToPx(1), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.setOnClickListener(this);
    }

    private void loadData(String nickname, String address, String description) {

        Address a = new Address(description, address, nickname);
        addressList.add(a);

        adapter.notifyDataSetChanged();
    }

    /**
     * Converting dp to pixel
     */
    public int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
    private void addressModule(final String uid, final String nickname, final String address, final String description) {

        // Tag used to cancel the request
        String tag_string_req = "req_login";

        String url;

        if(update){
            url = AppConfig.URL_ADDRESS;
            pDialog.setMessage("Updating information ...");
        } else {
            url = AppConfig.URL_ADDRESS;
            pDialog.setMessage("Loading information ...");
        }

        showDialog();

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

                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String nickname = user.getString("nickname");
                        String address = user.getString("address");
                        //String description = user.getString("description");
                        String success = user.getString("success");

                        loadData(nickname, address, description);

                        shared = new SharedPreference();

                        shared.save(getApplicationContext(), PREFS_NICKNAME_NAME,
                                PREFS_NICKNAME, nickname);

                        shared.save(getApplicationContext(), PREFS_ADDRESS_NAME,
                                PREFS_ADDRESS, address);

                        shared.save(getApplicationContext(), PREFS_DESC_NAME,
                                PREFS_DESC, description);

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

                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("nickname", nickname);
                params.put("address", address);
                params.put("description", description);
                params.put("uid", uid_check);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    @Override
    public void onClick(View view) {

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void showAlert(final String prefName, final String prefValue){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.address_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edtNick = (EditText) dialogView.findViewById(R.id.nickname);
        final EditText edtAdd = (EditText) dialogView.findViewById(R.id.address);

        edtAdd.setText(address);

        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                addressModule(uid_check ,prefName, prefValue, "none");

                loadData(edtNick.getText().toString(), address, "none");
                //do something with edt.getText().toString();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();

    }

}
