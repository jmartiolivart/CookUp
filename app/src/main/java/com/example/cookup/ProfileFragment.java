package com.example.cookup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment implements View.OnClickListener{

    ImageLoader imageLoader;
    DisplayImageOptions options;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Button maps = (Button) view.findViewById(R.id.LocationButton);
        maps.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        buildTitle();
    }

    private void buildTitle() {

        TextView name = (TextView) getActivity().findViewById(R.id.namedefault);
        TextView email = (TextView) getActivity().findViewById(R.id.emailInput);
        ImageView imageView = (ImageView) getActivity().findViewById(R.id.profile_image);
        TextView coordinates = (TextView) getActivity().findViewById(R.id.coordinatesInput);

        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String alias = mySharedPreferences.getString(getString(R.string.name), "No name");
        String mail = mySharedPreferences.getString(getString(R.string.email), "No email");
        String lat = mySharedPreferences.getString(getString(R.string.latitud), "-");
        String lon = mySharedPreferences.getString(getString(R.string.longitud), "-");

        name.setText(alias);
        email.setText(mail);
        coordinates.setText(String.format("Lat: %s \nLon: %s", lat, lon));

        Uri photo = MainActivity.photo;
        if(photo!=null){
            Picasso.get().load(photo).into(imageView);
        }



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.LocationButton:
                Intent locationIntent = new Intent(getActivity(), MapsActivity.class);
                startActivityForResult(locationIntent, 2);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2){
            if(resultCode == getActivity().RESULT_OK){
                String lat = data.getStringExtra("Lat");
                String lon = data.getStringExtra("Lon");
                //coord.setText(lat + "\n" + lon);
                Toast.makeText(getActivity().getApplicationContext(),"Lat: " + lat + "\n" + "Lon: " + lon, Toast.LENGTH_LONG).show();

            }
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.config:
                startActivity(new Intent(getActivity(), com.example.cookup.preferences.PreferencesActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
