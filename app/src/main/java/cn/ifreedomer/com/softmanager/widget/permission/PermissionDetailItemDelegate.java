package cn.ifreedomer.com.softmanager.widget.permission;

import android.content.Context;
import android.widget.Switch;
import android.widget.Toast;

import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import cn.ifreedomer.com.softmanager.GlobalDataManager;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.bean.PermissionDetail;
import cn.ifreedomer.com.softmanager.bean.PermissionWrap;
import cn.ifreedomer.com.softmanager.manager.PermissionManager;
import cn.ifreedomer.com.softmanager.model.AppInfo;

/**
 * @author:eavawu
 * @since: 05/11/2017.
 * TODO:
 */

public class PermissionDetailItemDelegate implements ItemViewDelegate<PermissionWrap<PermissionDetail>> {
    private Context mContext;
    private AppInfo mAppInfo;

    public PermissionDetailItemDelegate(Context context, AppInfo appInfo) {
        this.mContext = context;
        this.mAppInfo = appInfo;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_permission_detail;
    }

    @Override
    public boolean isForViewType(PermissionWrap<PermissionDetail> item, int position) {
        return item.getType() == PermissionWrap.PERMISSION_TYPE;
    }

    @Override
    public void convert(ViewHolder holder, PermissionWrap<PermissionDetail> permissionDetailPermissionWrap, int position) {
        PermissionDetail permissionDetail = permissionDetailPermissionWrap.getData();
        holder.setText(R.id.tv_name, permissionDetail.getTitle());
//        holder.setImageDrawable(R.id.iv_icon, permissionDetail.getIcon());
        if (permissionDetail.getPermissionDes() != null) {
            holder.setText(R.id.tv_des, permissionDetail.getPermissionDes());
        }
        Switch cb = holder.getView(R.id.cb);
        cb.setChecked(permissionDetail.isGranted());
        cb.setOnClickListener(v -> {
            if (PermissionManager.getInstance().checkOrRequestedRootPermission()) {
                GlobalDataManager.getInstance().getThreadPool().execute(() -> PermissionManager.getInstance().setPermission(mContext, mAppInfo.getPackname(), permissionDetail.getPermission(), permissionDetail.isGranted()));
                permissionDetail.setGranted(!permissionDetail.isGranted());
            } else {
                cb.setChecked(!cb.isChecked());
                Toast.makeText(mContext, mContext.getString(R.string.no_root), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
