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

public class ServiceAdapter extends CommonAdapter<AppInfo> {
    private String mSuffix = "";

    public ServiceAdapter(Context context, int layoutId, List<AppInfo> datas) {
        super(context, layoutId, datas);
        mSuffix = context.getResources().getString(R.string.service_suffix);
    }

    @Override
    protected void convert(ViewHolder holder, AppInfo appInfo, int position) {
        holder.setText(R.id.tv_name, appInfo.getAppName());
        holder.setImageDrawable(R.id.iv_icon, appInfo.getAppIcon());
        if (appInfo.getServiceList() != null) {
            holder.setText(R.id.tv_count, appInfo.getServiceList().size() + mSuffix);
        } else {
            holder.setText(R.id.tv_count, 0 + mSuffix);
        }
    }
}
