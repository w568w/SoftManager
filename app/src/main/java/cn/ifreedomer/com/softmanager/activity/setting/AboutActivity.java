package cn.ifreedomer.com.softmanager.activity.setting;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.manager.PackageInfoManager;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.activity.BaseActivity;
import cn.ifreedomer.com.softmanager.util.ToolbarUtil;
import cn.ifreedomer.com.softmanager.widget.AboutItemView;

public class AboutActivity extends BaseActivity {


    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.icon_iv)
    ImageView iconIv;
    @InjectView(R.id.install_ll)
    LinearLayout installLl;
    @InjectView(R.id.visit_ll)
    LinearLayout visitLl;
    private ArrayList<AboutItemView> installList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.inject(this);
        ToolbarUtil.setTitleBarWhiteBack(this,toolbar);
        initItems();
    }

    private void initItems() {
        AboutItemView versionItemView = new AboutItemView(this);
        versionItemView.setTitle(getString(R.string.version_title));
        versionItemView.setContent(PackageInfoManager.getInstance().getVersionCode(this) + "");
        installLl.addView(versionItemView);

        AboutItemView copyRightItemView = new AboutItemView(this);
        versionItemView.setTitle(getString(R.string.authority));
        versionItemView.setContent(getString(R.string.baihua));
        installLl.addView(copyRightItemView);

        AboutItemView emailItemView = new AboutItemView(this);
        emailItemView.setTitle(getString(R.string.gmail_title));
        emailItemView.setContent(getString(R.string.email));
        emailItemView.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        installLl.addView(emailItemView);



        AboutItemView qqGroup = new AboutItemView(this);
        qqGroup.setTitle(getString(R.string.qqgroup));
        qqGroup.setContent(getString(R.string.group_name));
        installLl.addView(qqGroup);

        AboutItemView githubItemView = new AboutItemView(this);
        githubItemView.setTitle(getString(R.string.thanks));
        githubItemView.setContent(getString(R.string.thanks_names));
        githubItemView.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
        installLl.addView(githubItemView);


//        installList.add(0,"0.0.8");
//
//        for (int i = 0; i < 3; i++) {
//            installLl.addView(new AboutItemView(this));
//        }
//        for (int i = 0; i < 3; i++) {
//            visitLl.addView(new AboutItemView(this));
//        }
    }


}
