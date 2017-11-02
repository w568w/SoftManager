package cn.ifreedomer.com.softmanager.activity.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.activity.BaseActivity;
import cn.ifreedomer.com.softmanager.util.ToolbarUtil;
import cn.ifreedomer.com.softmanager.widget.SettingItemView;


public class SettingActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.ll_layout)
    LinearLayout llLayout;
    @InjectView(R.id.rootview)
    RelativeLayout rootview;
    private ArrayList<SettingItemView> itemViews = new ArrayList<>();
    private static final int CHECK_UPDATE_INDEX = 0;
    private static final int REMOVE_AD_INDEX = 1;
    private static final int COMMON_POINT_INDEX = 3;
    private static final int PUSH_INDEX = 2;
    private static final int HELP_INDEX = 4;
    private static final int FEEDBACK_INDEX = 5;
    private static final int ABOUT_INDEX = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.inject(this);
        initTitleBar();
        initItems();

    }

    private void initTitleBar() {
        ToolbarUtil.setTitleBarWhiteBack(this, mToolbar);
        getSupportActionBar().setTitle(getString(R.string.setting));
        mToolbar.setNavigationOnClickListener(v -> {
            SettingActivity.this.finish();
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initItems() {
        for (int i = 0; i < 5; i++) {
            SettingItemView itemView = new SettingItemView(this);
            itemView.setShowSwitch(false);
            itemView.setShowSub(false);
            itemView.setId(i);
            itemView.setOnClickListener(this);
//            if (i == CHECK_UPDATE_INDEX) {
//                itemView.setTitle(getString(R.string.check_update));
//            }
//            if (i == REMOVE_AD_INDEX) {
//                itemView.setShowSwitch(true);
//                itemView.setTitle(getString(R.string.remove_ad));
//                itemView.setSwitchListner(this);
//            }


            if (i == 0) {
                itemView.setShowSwitch(false);
                itemView.setTitle(getString(R.string.common_point));
            }

//            if (i == 1) {
//                itemView.setShowSub(true);
//                itemView.setTitle(getString(R.string.close_send));
//                itemView.setShowSwitch(true);
//                itemView.setSubTitle(getString(R.string.send_introduce));
//            }


            if (i == 1) {
                itemView.setTitle(getString(R.string.help));
                itemView.setMarginTop(R.dimen.dp15);
            }
            if (i == 2) {
                itemView.setTitle(getString(R.string.feedback));
                itemView.setShowSub(true);
                itemView.setSubTitle(getString(R.string.comment_introduce));
            }
            if (i == 3) {
                itemView.setTitle(getString(R.string.about));
                itemView.setShowSub(true);
                itemView.setSubTitle(getString(R.string.app_introduce));
            }
//            if (i == 7) {
//                itemView.setMarginTop(R.dimen.dp20);
//                itemView.setTitle(getString(R.string.quit));
////                if (user != null) {
////                    itemView.setShowSub(true);
////                    itemView.setSubTitle(user.getName());
////                } else {
////                    itemView.setVisibility(View.GONE);
////                }
//            }

            llLayout.addView(itemView);
        }
    }

    @Override
    public void onClick(View v) {
        SettingItemView itemView = (SettingItemView) v;
        switch (v.getId()) {
            case 0:
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
//                UpdateService.checkUpdate();
//                LoadingDialog loadingDialog = (LoadingDialog) showLoadingDialog();
//                loadingDialog.setOnLoadCancelLisener(this);
                break;
            case 1:
                startActivity(new Intent(this, HelpActivity.class));
                break;
            case 2:
                startActivity(new Intent(this, FeedBackActivity.class));
                break;
            case 3:
                startActivity(new Intent(this, AboutActivity.class));
//                boolean isPush = !SaveUtil.get(SaveConstants.ISPUSH);
//                SaveUtil.save(SaveConstants.ISPUSH, isPush);
//                itemView.setCheck(isPush);
//                if (isPush) {
//                    MiPushClient.registerPush(GuideApplication.getInstance(), Constants.XIAOMI_PUSNID, Constants.XIAOMI_PUSHKEY);
//                } else {
//                    MiPushClient.unregisterPush(GuideApplication.getInstance());
//                }
                break;
            case 4:
//                IntentUtils.startFeedbackActivity(this);
                break;
            case 5:
//                if (AppManager.getInstance().getUser() != null && AppManager.getInstance().getUser().isRecharge()) {
//                    itemView.setCheck(!itemView.isCheck());
//                } else {
//                    TipsDialog.showDialog(this, getString(R.string.remove_ad_tip), getString(R.string.ad_content), new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            IntentUtils.startPayActivity(SettingActivity.this);
//                        }
//                    });
//                }
                break;
            case ABOUT_INDEX:
//                IntentUtils.startAboutActivity(this);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void showDialog(final UpdateEvent updateEvent) {
//        dismissDialog();
//        TipsDialog.showDialog(this, getString(R.string.update_title), getString(R.string.update_content), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                DownLoadItem downLoadItem = new DownLoadItem();
//                downLoadItem.setDownLoaderListener(SettingActivity.this);
//                int lastIndexOf = updateEvent.getUpdateInfo().getUrl().lastIndexOf("/");
//                String url = updateEvent.getUpdateInfo().getUrl();
//                downLoadItem.setPath(CacheUtil.getSdCacheDir() + url.substring(lastIndexOf + 1, url.length()));
//                downLoadItem.setUrl(updateEvent.getUpdateInfo().getUrl());
//                DownLoadManager.getInstance().beginDownLoad(downLoadItem);
//            }
//        });
//    }


//    @Override
//    public void onCancel(View view) {
//        dismissDialog();
//    }
//
//
//    @Override
//    public void downLoadBegin() {
//        showLoadingDialog();
//    }
//
//    @Override
//    public void downLoadFinish(DownLoadItem downLoadItem) {
//        dismissDialog();
//        IntentUtils.startInstallActivity(this, downLoadItem.getPath());
//    }
//
//    @Override
//    public void downLoadFailed() {
//        dismissDialog();
//    }
//
//    @Override
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        switch (buttonView.getId()) {
//
////            break;
////            case
//        }
//    }
}
