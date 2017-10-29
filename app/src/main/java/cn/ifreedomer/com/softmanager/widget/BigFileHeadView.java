package cn.ifreedomer.com.softmanager.widget;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import cn.ifreedomer.com.softmanager.R;

/**
 * @author:eavawu
 * @since: 29/10/2017.
 * TODO:
 */

public class BigFileHeadView extends RelativeLayout {
    public BigFileHeadView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.big_file_headview, this);
    }
}
