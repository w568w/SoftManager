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
import cn.ifreedomer.com.softmanager.util.LogUtil;

/**
 * @author:eavawu
 * @since: 02/12/2017.
 * TODO:
 */

public class DisableComponentAdapter extends CommonAdapter<ComponentEntity> {
    private static final String TAG = DisableComponentAdapter.class.getSimpleName();

    public DisableComponentAdapter(Context context, int layoutId, List<ComponentEntity> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ComponentEntity componentEntity, int position) {
        String componentEntityName = componentEntity.getName();
        int suffixBegin = componentEntityName.lastIndexOf(".") + 1;
        holder.setText(R.id.tv_name, componentEntityName.substring(suffixBegin, componentEntityName.length()));
        holder.setText(R.id.tv_category, componentEntityName);
//        holder.setImageDrawable(R.id.iv_icon, mAppInfo.getAppIcon());
        Switch aSwitch = holder.getView(R.id.cb);
        aSwitch.setChecked(componentEntity.isEnable());
        holder.getView(R.id.cb).setOnClickListener(v -> {
            if (GlobalDataManager.getInstance().isOpenRecharge()) {
                ((BaseActivity) mContext).showPayDialog();
                aSwitch.setChecked(!aSwitch.isChecked());
                return;
            }
            if (!PermissionManager.getInstance().checkOrRequestedRootPermission()) {
                Toast.makeText(mContext, R.string.no_root, Toast.LENGTH_SHORT).show();
                aSwitch.setChecked(componentEntity.isEnable());
                return;
            }
            componentEntity.setEnable(!componentEntity.isEnable());
            aSwitch.setChecked(componentEntity.isEnable());
            if (componentEntity.isEnable()) {
                LogUtil.d(TAG, String.format("pkgName = %s component name = %s", componentEntity.getBelongPkg(), componentEntity.getName()));
                ComponentEntity appComponent = PackageInfoManager.getInstance().getAppComponent(componentEntity.getBelongPkg(), componentEntity.getName());
                //组件数据已经加载
                if (appComponent != null) {
                    appComponent.setEnable(true);
                }
                LogUtil.d(TAG, "appComponent = " + appComponent);

                PackageInfoManager.getInstance().enableAndRemoveComponent(componentEntity);

            } else {
                LogUtil.d(TAG, String.format("pkgName = %s component name = %s", componentEntity.getBelongPkg(), componentEntity.getName()));

                ComponentEntity appComponent = PackageInfoManager.getInstance().getAppComponent(componentEntity.getBelongPkg(), componentEntity.getName());
                //组件数据已经加载
                if (appComponent != null) {
                    appComponent.setEnable(false);
                }
                LogUtil.d(TAG, "appComponent = " + appComponent);

                PackageInfoManager.getInstance().disableAndSaveComponent(componentEntity);

            }
        });
    }


}
