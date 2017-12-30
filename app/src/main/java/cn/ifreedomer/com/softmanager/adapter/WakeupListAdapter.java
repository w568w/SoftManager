package cn.ifreedomer.com.softmanager.adapter;

import android.content.Context;
import android.widget.Switch;
import android.widget.Toast;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.activity.BaseActivity;
import cn.ifreedomer.com.softmanager.bean.ComponentEntity;
import cn.ifreedomer.com.softmanager.manager.GlobalDataManager;
import cn.ifreedomer.com.softmanager.manager.PackageInfoManager;
import cn.ifreedomer.com.softmanager.manager.PermissionManager;
import cn.ifreedomer.com.softmanager.model.AppInfo;

/**
 * @author:eavawu
 * @since: 02/12/2017.
 * TODO:
 */

public class WakeupListAdapter extends CommonAdapter<AppInfo> {
    private List<ComponentEntity> mComponentEntitylist = null;

    public WakeupListAdapter(Context context, int layoutId, List<AppInfo> datas, List<ComponentEntity> componentEntitylist) {
        super(context, layoutId, datas);
        this.mComponentEntitylist = componentEntitylist;
    }

    @Override
    protected void convert(ViewHolder holder, AppInfo appInfo, int position) {
        ComponentEntity componentEntity = mComponentEntitylist.get(position);
        holder.setText(R.id.tv_name, appInfo.getAppName());
        holder.setText(R.id.tv_category, componentEntity.getName());
        holder.setImageDrawable(R.id.iv_icon, appInfo.getAppIcon());
        Switch aSwitch = holder.getView(R.id.cb);
        aSwitch.setChecked(mComponentEntitylist.get(position).isChecked());
        holder.getView(R.id.cb).setOnClickListener(v -> {
            if (GlobalDataManager.getInstance().isOpenRecharge()) {
                aSwitch.setChecked(!aSwitch.isChecked());
                ((BaseActivity) mContext).showPayDialog();
                return;
            }
            if (!PermissionManager.getInstance().checkOrRequestedRootPermission()) {
                Toast.makeText(mContext, R.string.no_root, Toast.LENGTH_SHORT).show();
                aSwitch.setChecked(componentEntity.isChecked());
                return;
            }
            componentEntity.setChecked(!componentEntity.isChecked());
            aSwitch.setChecked(componentEntity.isChecked());
            if (componentEntity.isChecked()) {
                PackageInfoManager.getInstance().enableAndRemoveComponent(componentEntity);
            } else {
                PackageInfoManager.getInstance().disableAndSaveComponent(componentEntity);
            }
        });
    }


}
