package cn.ifreedomer.com.softmanager.util;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import cn.ifreedomer.com.softmanager.R;

/**
 * @author:eavawu
 * @since: 29/10/2017.
 * TODO:
 */

public class ToolbarUtil {

    public static void setTitleBarWhiteBack(AppCompatActivity context, Toolbar toolbar) {
        context.setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(context.getResources().getColor(R.color.whiteColor));
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.finish();
            }
        });
    }
}
