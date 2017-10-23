package cn.ifreedomer.com.softmanager.fragment.soft;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.ifreedomer.com.softmanager.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SoftFragment extends Fragment {


    public SoftFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_soft, container, false);
    }

}
