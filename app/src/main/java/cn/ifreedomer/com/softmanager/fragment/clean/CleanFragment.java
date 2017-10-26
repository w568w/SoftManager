package cn.ifreedomer.com.softmanager.fragment.clean;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.ifreedomer.com.softmanager.R;

/**
 * @author:eavawu
 * @since: 26/10/2017.
 * TODO:
 */

public class CleanFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clean, container, false);
        return view;
    }
}
