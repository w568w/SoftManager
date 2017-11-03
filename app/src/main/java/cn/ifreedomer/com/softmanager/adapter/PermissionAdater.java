package cn.ifreedomer.com.softmanager.adapter;

import android.content.Context;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.model.AppInfo;

/**
 * @author:eavawu
 * @since: 03/11/2017.
 * TODO:
 */

public class PermissionAdater extends CommonAdapter<AppInfo> {

    public PermissionAdater(Context context, int layoutId, List<AppInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, AppInfo appInfo, int position) {
        holder.setText(R.id.tv_appname, appInfo.getAppName());
        holder.setImageDrawable(R.id.iv_icon, appInfo.getAppIcon());
        holder.setText(R.id.tv_appcache, appInfo.getPkgSize() + "M");
    }
}