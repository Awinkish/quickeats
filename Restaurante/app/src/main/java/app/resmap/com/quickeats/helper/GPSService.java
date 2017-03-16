package app.resmap.com.quickeats.helper;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;


/**
 * Created by arwin on 3/16/17.
 */

public class GPSService extends Service {
    private LocationListener listener;
    private LocationManager locationManager;
    Context mContext;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onCreate() {

        String context = Context.LOCATION_SERVICE;

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Intent intent = new Intent("location_update");
                intent.putExtra("coordinates", location.getLongitude() +" " + location.getLatitude());
                sendBroadcast(intent);

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        };


        locationManager = (LocationManager) getApplicationContext().getSystemService(context);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 0, listener);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(locationManager != null){

            locationManager.removeUpdates(listener);

        }
    }
}
