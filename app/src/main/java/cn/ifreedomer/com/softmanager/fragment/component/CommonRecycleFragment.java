package cn.ifreedomer.com.softmanager.fragment.component;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhy.adapter.recyclerview.CommonAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.R;

/**
 * @author:eavawu
 * @since: 02/12/2017.
 * TODO:
 */

public class CommonRecycleFragment extends Fragment {


    @InjectView(R.id.rv)
    RecyclerView rv;
    @InjectView(R.id.tv_no_content)
    TextView tvNoContent;
    private CommonAdapter mCommonAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receycle, container, false);
        ButterKnife.inject(this, view);
        rv.setLayoutManager(mLayoutManager);
        rv.setAdapter(mCommonAdapter);
        if (mCommonAdapter.getDatas() == null || mCommonAdapter.getDatas().size() == 0) {
            setNoContentVisible(View.VISIBLE);
        }
        return view;
    }


    public void setAdapter(CommonAdapter commonAdapter) {
        this.mCommonAdapter = commonAdapter;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public void setNoContentVisible(int visible) {
        tvNoContent.setVisibility(visible);
    }

}
