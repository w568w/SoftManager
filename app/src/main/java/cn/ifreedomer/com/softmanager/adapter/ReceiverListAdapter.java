package cn.ifreedomer.com.softmanager.adapter;

import android.content.Context;
import android.widget.Switch;
import android.widget.Toast;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.bean.ComponentEntity;
import cn.ifreedomer.com.softmanager.manager.PackageInfoManager;
import cn.ifreedomer.com.softmanager.manager.PermissionManager;
import cn.ifreedomer.com.softmanager.model.AppInfo;

/**
 * @author:eavawu
 * @since: 02/12/2017.
 * TODO:
 */

public class ReceiverListAdapter extends CommonAdapter<ComponentEntity> {
    private AppInfo mAppInfo = null;

    public ReceiverListAdapter(Context context, int layoutId, List<ComponentEntity> datas, AppInfo appInfo) {
        super(context, layoutId, datas);
        this.mAppInfo = appInfo;
    }

    @Override
    protected void convert(ViewHolder holder, ComponentEntity componentEntity, int position) {
        String componentEntityName = componentEntity.getName();
        int suffixBegin = componentEntityName.lastIndexOf(".") + 1;
        holder.setText(R.id.tv_name, componentEntityName.substring(suffixBegin, componentEntityName.length()));
        holder.setText(R.id.tv_category, componentEntityName);
        holder.setImageDrawable(R.id.iv_icon, mAppInfo.getAppIcon());
        Switch aSwitch = holder.getView(R.id.cb);
        aSwitch.setChecked(componentEntity.isChecked());
        if (componentEntity.getActionList() != null && componentEntity.getActionList().size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(mContext.getString(R.string.action));
            for (int i = 0; i < componentEntity.getActionList().size(); i++) {
                stringBuilder.append(componentEntity.getActionList().get(i) + "\n");
            }
            holder.setText(R.id.tv_action, stringBuilder.toString());
        }
        holder.getView(R.id.cb).setOnClickListener(v -> {
            if (!PermissionManager.getInstance().checkOrRequestedRootPermission()) {
                Toast.makeText(mContext, R.string.no_root, Toast.LENGTH_SHORT).show();
                aSwitch.setChecked(componentEntity.isChecked());
                return;
            }
            componentEntity.setChecked(!componentEntity.isChecked());
            aSwitch.setChecked(componentEntity.isChecked());
            if (componentEntity.isChecked()) {
                PackageInfoManager.getInstance().enableComponent(componentEntity.getName());
            } else {
                PackageInfoManager.getInstance().disableComponent(componentEntity.getName());
            }
        });
    }


}
