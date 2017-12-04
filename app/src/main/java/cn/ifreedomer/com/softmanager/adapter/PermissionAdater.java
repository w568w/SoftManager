package cn.ifreedomer.com.softmanager.adapter;

import android.content.Context;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.model.AppInfo;
import cn.ifreedomer.com.softmanager.util.LogUtil;

/**
 * @author:eavawu
 * @since: 03/11/2017.
 * TODO:
 */

public class PermissionAdater extends CommonAdapter<AppInfo> {
    private static final String TAG = PermissionAdater.class.getSimpleName();
    public PermissionAdater(Context context, int layoutId, List<AppInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, AppInfo appInfo, int position) {
        holder.setText(R.id.tv_name, appInfo.getAppName());
        holder.setImageDrawable(R.id.iv_icon, appInfo.getAppIcon());
        if (appInfo.getPermissionDetailList() != null) {
            holder.setText(R.id.tv_category, appInfo.getPermissionDetailList().size() + mContext.getString(R.string.permission_count));
            LogUtil.e(TAG,"appName="+appInfo.getAppName()+"  permission="+appInfo.getPermissionDetailList());
        } else {
            holder.setText(R.id.tv_category, 0 + mContext.getString(R.string.permission_count));
        }
    }
}
