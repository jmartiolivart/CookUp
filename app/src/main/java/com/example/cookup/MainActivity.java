package com.example.cookup;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.cookup.preferences.PreferencesActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    public static Uri photo;
    public static final String WIFI = "Wi-Fi";
    public static final String ANY = "Any";
    private static SharedPreferences sharedPrefs;

    private static boolean wifiConnected = false;
    private static boolean mobileConnected = false;

    public static String sPref = null;
    private MainActivity.NetworkReceiver receiver = new NetworkReceiver();
    private MyFirebaseMessagingService fms = new MyFirebaseMessagingService();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        mAuth = FirebaseAuth.getInstance();
        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .setAvailableProviders(providers)
                .setLogo(R.drawable.logocookup)
                .build(), 5);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkReceiver();
        this.registerReceiver(receiver, filter);

        if (Build.VERSION.SDK_INT >= 23)
            if (! checkPermissions())
                requestPermissions();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 5){
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode == RESULT_OK){
                FirebaseUser user = mAuth.getCurrentUser();
                setSharedPrefs();
                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if(!task.isSuccessful()){
                            return;
                        }
                        String token = task.getResult();
                        fms.onNewToken(token);

                    }
                });
                Fragment fragment = activeInternet();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                System.out.println("Success");
            }else{
                Toast.makeText(this,R.string.loginfail,Toast.LENGTH_LONG).show();
                System.out.println("Fail");
                finish();
            }
        }

    }

    public void setSharedPrefs(){
        FirebaseUser user = mAuth.getCurrentUser();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.name), user.getDisplayName());
        editor.putString(getString(R.string.email), user.getEmail());
        photo = user.getPhotoUrl();
        editor.apply();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {

                case R.id.HomeButton:
                    selectedFragment = activeInternet();
                    break;
                case R.id.SearchButton:
                    selectedFragment = new AdvancedSearchFragment();
                    break;
                case R.id.AddRecipeButton:
                    selectedFragment = new RecipeFragment();
                    break;
                case R.id.ProfileButton:
                    selectedFragment = new ProfileFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };

    public Fragment activeInternet(){

        Fragment selectedFragment = null;

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        sPref = sharedPrefs.getString("listPref", "Wi-Fi");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            updateConnectedFlags();
        }

        if (((sPref.equals(ANY)) && (wifiConnected || mobileConnected)) || ((sPref.equals(WIFI)) && (wifiConnected))) {
            selectedFragment = new HomeFragment();
        } else {
            selectedFragment = new ErrorInternetFragment();
        }

        return selectedFragment;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.config:
                startActivity(new Intent(this, PreferencesActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onStart() {
        super.onStart();
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        sPref = sharedPrefs.getString("listPref", "Wi-Fi");
        updateConnectedFlags();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            this.unregisterReceiver(receiver);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void updateConnectedFlags() {
        ConnectivityManager cMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        Network nw = cMgr.getActiveNetwork();
        if (nw == null) {
            wifiConnected = false;
            mobileConnected = false;
        } else {
            NetworkCapabilities actNw = cMgr.getNetworkCapabilities(nw);
            if (actNw == null) {
                wifiConnected = false;
                mobileConnected = false;
            }
            else if (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
                wifiConnected = true;
            else if (actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
                mobileConnected = true;
        }
    }

    public static class NetworkReceiver extends BroadcastReceiver {

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceive(Context context, Intent intent) {

            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            sPref = sharedPrefs.getString("listPref", "Wi-Fi");
            Network nw = connMgr.getActiveNetwork();
            if (nw == null) {
                Toast.makeText(context, R.string.lost_connection, Toast.LENGTH_SHORT).show();
            } else {
                NetworkCapabilities actNw = connMgr.getNetworkCapabilities(nw);
                if (actNw == null) {
                    Toast.makeText(context, R.string.lost_connection, Toast.LENGTH_SHORT).show();
                } else if (WIFI.equals(sPref) && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))) {
                    Toast.makeText(context, R.string.wifi_connected, Toast.LENGTH_SHORT).show();
                } else if (ANY.equals(sPref))
                    Toast.makeText(context, R.string.connection_enabled, Toast.LENGTH_SHORT).show();
            }
        }
    }
    private boolean checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.INTERNET) ==
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_NETWORK_STATE) ==
                    PackageManager.PERMISSION_GRANTED)
                return true;
            else
                return false;
        } else
            return true;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE,},
                0);
    }
}