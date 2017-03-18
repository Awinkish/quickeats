package app.resmap.com.quickeats.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;

import app.resmap.com.quickeats.R;
import app.resmap.com.quickeats.helper.SessionManager;

public class MainActivity extends AppCompatActivity{

	private GoogleApiClient mGoogleApiClient;
	private TextView txtName, txtLast, txtPhone, txtReferral, txtAddress;
	private TextView txtEmail;
	private Button btnLogout, btnToolBar;

	private SessionManager session;

	public MainActivity() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		txtName = (TextView) findViewById(R.id.name);
		txtLast = (TextView) findViewById(R.id.lname);
		txtPhone = (TextView) findViewById(R.id.phone);
		txtEmail = (TextView) findViewById(R.id.email);
		txtReferral = (TextView) findViewById(R.id.referral);
		txtAddress = (TextView) findViewById(R.id.address);
		btnLogout = (Button) findViewById(R.id.btnLogout);

		//initCollapsingToolbar();

		session = new SessionManager(getApplicationContext());

		if (!session.isLoggedIn()) {
			logoutUser();
		}

		btnLogout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				logoutUser();
			}
		});


	}
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem mAccounts = menu.findItem(R.id.action_accounts);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Intent intent = new Intent(getApplicationContext(), AccountSettings.class);
		switch (item.getItemId()) {
			// action with ID action_refresh was selected
			case R.id.action_accounts:

				startActivity(intent);
				break;
			case R.id.action_book:
				startActivity(intent);
				break;
			case R.id.action_logout:
				logoutUser();

				break;
			default:
				break;
		}

		return true;
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
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}


	private void logoutUser() {
		session.setLogin(false);

		Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
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
