package cn.ifreedomer.com.softmanager.fragment.clean;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.bean.CleanCardInfo;
import cn.ifreedomer.com.softmanager.widget.ItemCardView;

/**
 * @author:eavawu
 * @since: 26/10/2017.
 * TODO:
 */

public class CleanFragment extends Fragment {
    @InjectView(R.id.iv_clean)
    ImageView ivClean;
    @InjectView(R.id.qq_card)
    ItemCardView qqCardView;
    @InjectView(R.id.big_file_card)
    ItemCardView bigFileCardView;
    @InjectView(R.id.garbage_card)
    ItemCardView garbageCardView;
    @InjectView(R.id.deep_card)
    ItemCardView deepCard;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clean, container, false);
        ButterKnife.inject(this, view);
        setData();
        return view;
    }

    private void setData() {
        garbageCardView.setData(new CleanCardInfo(getString(R.string.garbage_clean), R.mipmap.clean));
        deepCard.setData(new CleanCardInfo(getString(R.string.deep_clean), R.mipmap.deep_clean));
        qqCardView.setData(new CleanCardInfo(getString(R.string.qq_clean), R.mipmap.qq));
        bigFileCardView.setData(new CleanCardInfo(getString(R.string.big_file), R.mipmap.file));


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
