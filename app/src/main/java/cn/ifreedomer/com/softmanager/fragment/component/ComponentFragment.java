package cn.ifreedomer.com.softmanager.fragment.component;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.manager.PackageInfoManager;
import cn.ifreedomer.com.softmanager.util.SPUtil;
import cn.ifreedomer.com.softmanager.widget.ContentDialog;

/**
 * @author:eavawu
 * @since: 02/12/2017.
 * TODO:
 */

public class ComponentFragment extends Fragment implements MenuItem.OnMenuItemClickListener {

    @InjectView(R.id.pb)
    ProgressBar pb;
    @InjectView(R.id.lin_loading)
    LinearLayout linLoading;
    @InjectView(R.id.frame_content)
    FrameLayout frameContent;
    private Toolbar mToolBar;
    private List<Fragment> fragmentList = new ArrayList<>();
    private Fragment mLastFragment = null;
    private boolean isLoaded = false;
    private static final int LOAD_UI = 1;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOAD_UI:
                    initFragments();
                    linLoading.setVisibility(View.GONE);
                    isLoaded = true;
                    break;
                default:
                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_component, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    private void initFragments() {
        fragmentList.add(new ActivityFragment());
        fragmentList.add(new ReceiverFragment());
        fragmentList.add(new ServiceFragment());
        fragmentList.add(new ContentProviderFragment());
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        for (int i = 0; i < fragmentList.size(); i++) {
            Fragment fragment = fragmentList.get(i);
            fragmentTransaction.add(R.id.frame_content, fragment).hide(fragment);
        }
        fragmentTransaction.show(fragmentList.get(0));
        mLastFragment = fragmentList.get(0);
        fragmentTransaction.commitAllowingStateLoss();

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mToolBar = (Toolbar) getActivity().findViewById(R.id.toolbar);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (!PackageInfoManager.getInstance().isComponentLoaded()) {
                linLoading.setVisibility(View.VISIBLE);
                    PackageInfoManager.getInstance().loadAllComponent(() -> mHandler.sendEmptyMessage(LOAD_UI));
            }
            if (PackageInfoManager.getInstance().isComponentLoaded()) {
                if (!isLoaded) {
                    mHandler.sendEmptyMessage(LOAD_UI);
                }
            }
        }
        if (hidden) {
            resetTitleBar();
        } else {
            setTitleBar();
            isShowDialog();
        }
    }

    private void setTitleBar() {
        if (mToolBar != null) {
            mToolBar.inflateMenu(R.menu.component_menu);
            mToolBar.setOverflowIcon(getResources().getDrawable(R.mipmap.ic_switch));
            mToolBar.getMenu().findItem(R.id.activity).setOnMenuItemClickListener(this);
            mToolBar.getMenu().findItem(R.id.receiver).setOnMenuItemClickListener(this);
            mToolBar.getMenu().findItem(R.id.content_provider).setOnMenuItemClickListener(this);
            mToolBar.getMenu().findItem(R.id.service).setOnMenuItemClickListener(this);
        }

    }


    private void resetTitleBar() {
        if (mToolBar != null) {
            mToolBar.getMenu().clear();
            mToolBar.setOverflowIcon(null);
        }

    }


    private void isShowDialog() {
        boolean isShowIce = (boolean) SPUtil.get(getActivity(), "isShowComponent", true);
        if (isShowIce) {
            ContentDialog contentDialog = new ContentDialog(getActivity());
            contentDialog.show();
            contentDialog.setData(getString(R.string.component_manager), getString(R.string.component_manager_des));
            SPUtil.put(getActivity(), "isShowComponent", false);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (!PackageInfoManager.getInstance().isComponentLoaded()) {
            Toast.makeText(getActivity(), getString(R.string.loading_data), Toast.LENGTH_SHORT).show();
            return false;
        }
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.hide(mLastFragment);
        switch (item.getItemId()) {
            case R.id.activity:
                mLastFragment = fragmentList.get(0);
                break;
            case R.id.receiver:
                mLastFragment = fragmentList.get(1);
                break;
            case R.id.service:
                mLastFragment = fragmentList.get(2);
                break;
            case R.id.content_provider:
                mLastFragment = fragmentList.get(3);
                break;
        }
        fragmentTransaction.show(mLastFragment).commit();
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
