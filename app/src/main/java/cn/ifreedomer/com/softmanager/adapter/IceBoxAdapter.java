package cn.ifreedomer.com.softmanager.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.manager.GlobalDataManager;
import cn.ifreedomer.com.softmanager.manager.PermissionManager;
import cn.ifreedomer.com.softmanager.model.AppInfo;
import cn.ifreedomer.com.softmanager.util.Terminal;

/**
 * @author:eavawu
 * @since: 19/11/2017.
 * TODO:
 */

public class IceBoxAdapter extends CommonAdapter<AppInfo> {

    public IceBoxAdapter(Context context, int layoutId, List<AppInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, AppInfo appInfo, int position) {
        holder.setText(R.id.tv_name, appInfo.getAppName());
        holder.setImageDrawable(R.id.iv_icon, appInfo.getAppIcon());
        Switch switchView = holder.getView(R.id.switch_enable);
        switchView.setChecked(appInfo.isEnable());
        holder.setOnClickListener(R.id.switch_enable, (View v) -> {
            if (!PermissionManager.getInstance().checkOrRequestedRootPermission()) {
                switchView.setChecked(!switchView.isChecked());
                Toast.makeText(mContext, R.string.no_root, Toast.LENGTH_SHORT).show();
                return;
            }
            if (mDatas.get(position).isEnable()) {
                GlobalDataManager.getInstance().getThreadPool().execute(() -> Terminal.disableApp(appInfo.getPackname()));
            } else {

                GlobalDataManager.getInstance().getThreadPool().execute(() -> Terminal.enableApp(appInfo.getPackname()));
            }
            mDatas.get(position).setEnable(switchView.isChecked());
            if (mDatas.get(position).isEnable()) {
                switchView.setChecked(true);
            } else {
                switchView.setChecked(false);
            }
        });
    }


}
