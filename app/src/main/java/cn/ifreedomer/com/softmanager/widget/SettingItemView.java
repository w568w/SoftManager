package cn.ifreedomer.com.softmanager.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.R;

/**
 * @author:eavawu
 * @date: 6/26/16.
 * @todo:
 */
public class SettingItemView extends RelativeLayout {


    @InjectView(R.id.title_tv)
    TextView titleTv;
    @InjectView(R.id.subtitle_tv)
    TextView subtitleTv;
    @InjectView(R.id.swtch)
    Switch swtch;
    private boolean isShowSwitch = true;
    private boolean isShowSub = false;
    private Context context;


    public SettingItemView(Context context) {
        super(context);
        initView(context);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        View inflate = LayoutInflater.from(context).inflate(R.layout.setting_item_view, this);
        ButterKnife.inject(this);

    }

    public boolean isShowSwitch() {
        return isShowSwitch;
    }

    public void setShowSwitch(boolean showCb) {
        isShowSwitch = showCb;
        int visible = showCb ? VISIBLE : GONE;
        swtch.setVisibility(visible);

    }

    public boolean isShowSub() {
        return isShowSub;
    }

    public void setShowSub(boolean showSub) {
        isShowSub = showSub;
        int visible = showSub ? VISIBLE : GONE;
        subtitleTv.setVisibility(visible);
    }

    public void setCannotCheck() {
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    public void setMarginTop(int dimenId) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT
                , LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, (int) getResources().getDimension(dimenId), 0, 0);
        this.setLayoutParams(params);
        ;
    }

    public void setSwitchListner(CompoundButton.OnCheckedChangeListener listener) {
        swtch.setOnCheckedChangeListener(listener);
    }

    public void setTitle(String title) {
        titleTv.setText(title);
    }

    public void setSubTitle(String subTitle) {
        subtitleTv.setText(subTitle);
    }

    public void setCheck(boolean b) {
        swtch.setChecked(b);
    }

    public boolean isCheck() {
        return swtch.isChecked();
    }

}
