package cn.ifreedomer.com.softmanager;

import android.content.Context;

import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.recyclerview.CommonAdapter;

import java.util.List;

/**
 * @author:eavawu
 * @date: 19/02/2017.
 * @todo:
 */
public class AppAdapter extends CommonAdapter<AppInfo> {

    public AppAdapter(Context context, int layoutId, List<AppInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, AppInfo appInfo) {
        holder.setText(R.id.tv_appname,appInfo.getAppName());
        holder.setImageDrawable(R.id.iv_icon,appInfo.getAppIcon());
        holder.setText(R.id.tv_appcache,appInfo.getPkgSize()+"");
          //  holder.setText(R.id.info_text,"helloworld");
    }
}
