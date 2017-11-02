package cn.ifreedomer.com.softmanager.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.R;

/**
 * @author:eavawu
 * @date: 7/3/16.
 * @todo:
 */
public class AboutItemView extends RelativeLayout {

    @InjectView(R.id.title_tv)
    TextView titleTv;
    @InjectView(R.id.content_tv)
    TextView contentTv;
    private Context context;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public AboutItemView(Context context) {
        super(context);
        initView(context);
    }

    public AboutItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        View inflate = LayoutInflater.from(context).inflate(R.layout.about_item, this);

    }

    public void setTitle(String title) {
        titleTv.setText(title);

    }

    public void setContent(String content) {
        contentTv.setText(content);
    }

    public void setInputType(int inputType) {
        contentTv.setInputType(inputType);
    }
}
