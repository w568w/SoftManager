package cn.ifreedomer.com.softmanager.fragment.network;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.ifreedomer.com.softmanager.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NetworkSpeedFragment extends Fragment {


    public NetworkSpeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_network_speed, container, false);
    }

}
