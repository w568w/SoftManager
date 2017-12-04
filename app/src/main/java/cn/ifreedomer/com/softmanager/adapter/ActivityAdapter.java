package cn.ifreedomer.com.softmanager.adapter;

import android.content.Context;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.model.AppInfo;

/**
 * @author:eavawu
 * @since: 02/12/2017.
 * TODO:
 */

public class ActivityAdapter extends CommonAdapter<AppInfo> {
    private String mSuffix = "";

    public ActivityAdapter(Context context, int layoutId, List<AppInfo> datas) {
        super(context, layoutId, datas);
        mSuffix = context.getString(R.string.activity_suffix);

    }

    @Override
    protected void convert(ViewHolder holder, AppInfo appInfo, int position) {
        holder.setText(R.id.tv_name, appInfo.getAppName());
        holder.setImageDrawable(R.id.iv_icon, appInfo.getAppIcon());
        if (appInfo.getActivityList() != null) {
            holder.setText(R.id.tv_category, appInfo.getActivityList().size() + mSuffix);
        } else {
            holder.setText(R.id.tv_category, 0 + mSuffix);
        }
    }
}
