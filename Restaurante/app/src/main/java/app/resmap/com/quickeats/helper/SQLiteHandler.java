package app.resmap.com.quickeats.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

	private static final String TAG = SQLiteHandler.class.getSimpleName();

	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "quickeat_db";

	// Login table name
	private static final String TABLE_USER = "user";

	// Login Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_FNAME = "first_name";
	private static final String KEY_LNAME = "last_name";
	private static final String KEY_PHONE= "phone";
	private static final String KEY_DISCOUNTS= "discounts";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_REFERRAL = "referral";
	private static final String KEY_UID = "uid";
	private static final String KEY_CREATED_AT = "created_at";

	public SQLiteHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_FNAME + " TEXT,"
				+ KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT," + KEY_LNAME + " TEXT,"
				+ " TEXT," + KEY_PHONE + " TEXT,"  + KEY_DISCOUNTS
				+ " TEXT," + KEY_REFERRAL + " TEXT,"
				+ KEY_CREATED_AT + " TEXT" + ")";
		db.execSQL(CREATE_LOGIN_TABLE);

		Log.d(TAG, "Database tables created");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);


		onCreate(db);
	}

	public void addUser(String fname, String lname, String phone, String discounts, String referral,
						String email, String uid, String created_at) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_FNAME, fname);
		values.put(KEY_EMAIL, email);
		values.put(KEY_UID, uid);
		values.put(KEY_LNAME, lname);
		values.put(KEY_PHONE, phone);
		values.put(KEY_DISCOUNTS, discounts);
		values.put(KEY_REFERRAL, referral);
		values.put(KEY_CREATED_AT, created_at);

		long id = db.insert(TABLE_USER, null, values);
		db.close();

		Log.d(TAG, "New user inserted into sqlite: " + id);
	}


	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		String selectQuery = "SELECT  * FROM " + TABLE_USER;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			user.put("first_name", cursor.getString(1));
			user.put("email", cursor.getString(2));
			user.put("uid", cursor.getString(3));
			user.put("last_name", cursor.getString(4));
			user.put("phone", cursor.getString(5));
			user.put("discounts", cursor.getString(6));
			user.put("referral", cursor.getString(7));
			user.put("created_at", cursor.getString(8));
		}
		cursor.close();
		db.close();

		Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

		return user;
	}

	public void deleteUsers() {
		SQLiteDatabase db = this.getWritableDatabase();

		db.delete(TABLE_USER, null, null);
		db.close();

		Log.d(TAG, "Deleted all user info from sqlite");
	}

}
