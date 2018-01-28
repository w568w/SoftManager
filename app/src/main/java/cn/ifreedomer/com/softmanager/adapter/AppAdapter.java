package cn.ifreedomer.com.softmanager.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.listener.GenericListener;
import cn.ifreedomer.com.softmanager.listener.OnUnInstallListener;
import cn.ifreedomer.com.softmanager.manager.GlobalDataManager;
import cn.ifreedomer.com.softmanager.manager.PackageInfoManager;
import cn.ifreedomer.com.softmanager.manager.PermissionManager;
import cn.ifreedomer.com.softmanager.model.AppInfo;
import cn.ifreedomer.com.softmanager.util.DataTypeUtil;

/**
 * @author:eavawu
 * @date: 19/02/2017.
 * @todo:
 */
public class AppAdapter extends CommonAdapter<AppInfo> {
    private OnUnInstallListener unInstallListener;
    private AppInfo curAppInfo = null;
    public AppAdapter(Context context, int layoutId, List<AppInfo> datas) {
        super(context, layoutId, datas);
    }


    public OnUnInstallListener getUnInstallListener() {
        return unInstallListener;
    }

    public void setUnInstallListener(OnUnInstallListener unInstallListener) {
        this.unInstallListener = unInstallListener;
    }

    public List<AppInfo> getData() {
        return mDatas;
    }


    @Override
    protected void convert(ViewHolder holder, final AppInfo appInfo, int position) {
        holder.setText(R.id.tv_name, appInfo.getAppName());
        holder.setImageDrawable(R.id.iv_icon, appInfo.getAppIcon());
        holder.setText(R.id.tv_appcache, DataTypeUtil.getTextBySize(appInfo.getPkgSize()));
        holder.setVisible(R.id.iv_move, appInfo.isUserApp());
        if (appInfo.isUserApp()) {

            holder.setOnClickListener(R.id.iv_move, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!PermissionManager.getInstance().checkOrRequestedRootPermission()) {
                        Toast.makeText(mContext, R.string.no_root, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    curAppInfo = appInfo;
                    Toast.makeText(mContext, R.string.moving, Toast.LENGTH_SHORT).show();
                    GlobalDataManager.getInstance().getThreadPool().execute(() -> PackageInfoManager.getInstance().moveToSystem((Activity) mContext, appInfo.getPackname(), genericListener));
                }
            });
        }
        holder.setOnClickListener(R.id.iv_uninstall, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (unInstallListener != null) {
                    unInstallListener.onUninstall(appInfo);
                }
            }
        });
        //  holder.setText(R.id.info_text,"helloworld");
    }

    public GenericListener genericListener = new GenericListener() {

        @Override
        public void onSuccess() {
            if (curAppInfo != null) {
                mDatas.remove(curAppInfo);
                new Handler(Looper.getMainLooper()).post(() -> {
                    notifyDataSetChanged();
                    Toast.makeText(mContext, R.string.move_success, Toast.LENGTH_LONG).show();
                });

            }
        }

        @Override
        public void onFailed(int errorCode, String errorMsg) {
            new Handler(Looper.getMainLooper()).post(() -> {
                Toast.makeText(mContext, mContext.getString(R.string.remove_failed) + errorCode + "  errorMsg = " + errorMsg, Toast.LENGTH_SHORT).show();

            });
        }
    };
}
