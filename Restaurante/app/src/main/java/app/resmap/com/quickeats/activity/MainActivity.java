package app.resmap.com.quickeats.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import app.resmap.com.quickeats.R;
import app.resmap.com.quickeats.app.AppController;
import app.resmap.com.quickeats.helper.SQLiteHandler;
import app.resmap.com.quickeats.helper.SessionManager;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

	private GoogleApiClient mGoogleApiClient;
	private TextView txtName, txtLast, txtPhone, txtReferral, txtAddress;
	private TextView txtEmail;
	private Button btnLogout;

	private SQLiteHandler db;
	private SessionManager session;
	private BroadcastReceiver broadCast;
	private Location mLastLocation;
	double uLatitude, uLongitude;
	// boolean flag to toggle periodic location updates
	private boolean mRequestingLocationUpdates = false;
	private LocationRequest mLocationRequest;
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

	// Location updates intervals in sec
	private static int UPDATE_INTERVAL = 10000; // 10 sec
	private static int FATEST_INTERVAL = 5000; // 5 sec
	private static int DISPLACEMENT = 10; // 10 meters



	Button btn1, btn2;

	RequestQueue requestQueue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		txtName = (TextView) findViewById(R.id.name);
		txtLast = (TextView) findViewById(R.id.lname);
		txtPhone = (TextView) findViewById(R.id.phone);
		txtEmail = (TextView) findViewById(R.id.email);
		txtReferral = (TextView) findViewById(R.id.referral);
		txtAddress = (TextView) findViewById(R.id.address);
		btnLogout = (Button) findViewById(R.id.btnLogout);

		//initCollapsingToolbar();

		db = new SQLiteHandler(getApplicationContext());

		session = new SessionManager(getApplicationContext());

		if (!session.isLoggedIn()) {
			logoutUser();
		}

		HashMap<String, String> user = db.getUserDetails();

		String name = user.get("first_name");
		String email = user.get("email");
		String last = user.get("last_name");
		String phone = user.get("phone");
		String referral_by = user.get("referral");

		txtName.setText("First Name: " + name);
		txtLast.setText("Last Name: " + last);
		txtReferral.setText("Referred by: " + referral_by);
		txtPhone.setText("Mobile No: " + phone);
		txtEmail.setText("Email: " + email);

		btnLogout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				logoutUser();
			}
		});

		// First we need to check availability of play services
		if (checkPlayServices()) {

			// Building the GoogleApi client
			buildGoogleApiClient();
		}

		createLocationRequest();

	}

	@Override
	protected void onStart() {
		super.onStart();
		if (mGoogleApiClient != null) {
			mGoogleApiClient.connect();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		checkPlayServices();
		// Resuming the periodic location updates
		try{
			if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
				startLocationUpdates();
			}
		}catch(NullPointerException e){
			//if(doLog) Log.e(TAG, CTAG + "> NullPointerException : " + e.toString());
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(broadCast != null){
			unregisterReceiver(broadCast);
		}
	}


	private void logoutUser() {
		session.setLogin(false);

		db.deleteUsers();

		Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

	protected void createLocationRequest() {
		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(UPDATE_INTERVAL);
		mLocationRequest.setFastestInterval(FATEST_INTERVAL);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setSmallestDisplacement(DISPLACEMENT); // 10 meters
	}


	@Override
	public void onConnected(Bundle bundle) {
		//Log.i(TAG, CTAG + "Play Connection Succeeded");

		mLastLocation = LocationServices.FusedLocationApi .getLastLocation(mGoogleApiClient);

		if (mLastLocation != null) {
			double latitude = mLastLocation.getLatitude();
			double longitude = mLastLocation.getLongitude();
			uLatitude = latitude;
			uLongitude = longitude;
			formattedAddress(latitude, longitude);
		}

		if (mRequestingLocationUpdates) {
			startLocationUpdates();
		}

	}

	@Override
	public void onConnectionSuspended(int i) {
		mGoogleApiClient.connect();
		//Log.i(TAG, CTAG + "Play Connection Suspended : " + i);
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		//Log.i(TAG, CTAG + "Play Connection failed : " + connectionResult.getErrorCode());
	}
	/**
	 * Creating google api client object
	 * */
	protected synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API).build();
	}

	/**
	 * Starting the location updates
	 * */
	protected void startLocationUpdates() {
		LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
	}

	/**
	 * Stopping location updates
	 */
	protected void stopLocationUpdates() {
		LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
	}

	@Override
	public void onLocationChanged(Location location) {

	}

	/**
	 * Method to verify google play services on the device
	 * */
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Toast.makeText(getApplicationContext(),
						"This device is not supported.", Toast.LENGTH_LONG)
						.show();
				finish();
			}
			return false;
		}
		return true;
	}

	private void formattedAddress(double latitude, double longitude){

		requestQueue = Volley.newRequestQueue(getApplicationContext());

		String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng="+latitude+","+longitude+"&key=AIzaSyBYffmc03xcmBX_hVl3wbh1FO8zcOHsMyE";

		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
				url, null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {


				try {
					// Parsing json object response
					// response will be a json object
					String address = response.getJSONArray("results").getJSONObject(0).getString("formatted_address");

					txtAddress.setText("Current location: " + address);


				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(),
							"Error: " + e.getMessage(),
							Toast.LENGTH_LONG).show();
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d("Error", "Error: " + error.getMessage());
				Toast.makeText(getApplicationContext(),
						error.getMessage(), Toast.LENGTH_SHORT).show();

			}
		});

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq);

	}

	/**
	 * Initializing collapsing toolbar
	 * Will show and hide the toolbar title on scroll
	 */
//	private void initCollapsingToolbar() {
//		final CollapsingToolbarLayout collapsingToolbar =
//				(CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//		collapsingToolbar.setTitle(" ");
//		AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
//		appBarLayout.setExpanded(true);
//
//		// hiding & showing the title when toolbar expanded & collapsed
//		appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//			boolean isShow = false;
//			int scrollRange = -1;
//
//			@Override
//			public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//				if (scrollRange == -1) {
//					scrollRange = appBarLayout.getTotalScrollRange();
//				}
//				if (scrollRange + verticalOffset == 0) {
//					collapsingToolbar.setTitle(getString(R.string.app_name));
//					isShow = true;
//				} else if (isShow) {
//					collapsingToolbar.setTitle(" ");
//					isShow = false;
//				}
//			}
//		});
//	}
}
