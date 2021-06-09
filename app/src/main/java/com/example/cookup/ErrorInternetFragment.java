package com.example.cookup;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ErrorInternetFragment extends Fragment {

    TextView errorMessage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.fragment_errorinternet, container, false);

         errorMessage = view.findViewById(R.id.errorTextInternet);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        ConnectivityManager cMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        Network nw = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            nw = cMgr.getActiveNetwork();
        }

        NetworkCapabilities actNw = cMgr.getNetworkCapabilities(nw);

        if(nw == null)
        if(!actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
            errorMessage.append(". " + this.getResources().getString(R.string.noWifiErrorMessage));
        } else if(!actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            errorMessage.setText(". " + this.getResources().getString(R.string.noWifinoInternet));
        }
    }
}
