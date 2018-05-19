package cn.ifreedomer.com.softmanager.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.fragment.soft.BackupFragment;
import cn.ifreedomer.com.softmanager.manager.GlobalDataManager;
import cn.ifreedomer.com.softmanager.manager.PackageInfoManager;
import cn.ifreedomer.com.softmanager.model.AppInfo;
import cn.ifreedomer.com.softmanager.util.AutoStartInfo;
import cn.ifreedomer.com.softmanager.util.LogUtil;
import cn.ifreedomer.com.softmanager.util.ShellUtils;
import cn.ifreedomer.com.softmanager.util.Terminal;


/**
 * @author wuyihua
 * @Date 2017/2/23
 */

public class BackupAdapter extends CommonAdapter<AppInfo> {
    private static final String TAG = BackupAdapter.class.getSimpleName();
    private BackupFragment backupFragment;

    public BackupAdapter(Context context, int layoutId, List<AppInfo> datas) {
        super(context, layoutId, datas);
    }


    public void setBackupFragment(BackupFragment homeActivity) {
        this.backupFragment = homeActivity;
    }


    @Override
    public void convert(ViewHolder holder, final AppInfo appInfo, final int position) {
        holder.setText(R.id.tv_name, appInfo.getAppName());
        holder.setImageDrawable(R.id.iv_icon, appInfo.getAppIcon());

        holder.setOnClickListener(R.id.btn_restore, v -> GlobalDataManager.getInstance().getThreadPool().execute(() -> {
            ((Activity) mContext).runOnUiThread(() -> {
                backupFragment.loadTv.setText(R.string.restoreing);
                backupFragment.linLoading.setVisibility(View.VISIBLE);
            });
            boolean isRestoreSuccess = Terminal.restoreApp(appInfo);
            LogUtil.d(TAG, "isRestoreSuccess = " + isRestoreSuccess);
            if (!isRestoreSuccess) {
                ((Activity) mContext).runOnUiThread(() -> {
                    backupFragment.linLoading.setVisibility(View.GONE);
                    Toast.makeText(mContext, mContext.getString(R.string.restore_failed), Toast.LENGTH_SHORT).show();
                });
                return;
            }
            //刷新数据
            mDatas.remove(appInfo);
            LogUtil.d(TAG, "remove = ");

            PackageInfoManager.getInstance().getBackupList().remove(appInfo);
            PackageInfoManager.getInstance().getSystemApps().add(appInfo);
            ((Activity) mContext).runOnUiThread(() -> {
                LogUtil.d(TAG, "show success ui = ");
                notifyDataSetChanged();
                backupFragment.linLoading.setVisibility(View.GONE);
                Toast.makeText(mContext, mContext.getString(R.string.restore_sucess), Toast.LENGTH_SHORT).show();
            });
        }));

    }

    private void diasableApp(AppInfo item) {
//        String packageReceiverList[] = item.getPackageReceiver().toString().split(";");
//
//        final List<String> mSring = new ArrayList<>();
//        for (int j = 0; j < packageReceiverList.length; j++) {
//            String cmd = "pm disable " + packageReceiverList[j];
//            //部分receiver包含$符号，需要做进一步处理，用"$"替换掉$
//            cmd = cmd.replace("$", "\"" + "$" + "\"");
//            //执行命令
//            mSring.add(cmd);
//
//        }
//        GlobalDataManager.getInstance().getThreadPool().execute(new Runnable() {
//            @Override
//            public void run() {
//                ShellUtils.CommandResult mCommandResult = ShellUtils.execCommand(mSring, true, true);
//            }
//        });


        // T.showLong(mContext, mCommandResult.result + "" + mCommandResult.errorMsg + mCommandResult.successMsg);
    }

    private void enableApp(AutoStartInfo item) {
        String packageReceiverList[] = item.getPackageReceiver().toString().split(";");

        final List<String> mSring = new ArrayList<>();
        for (int j = 0; j < packageReceiverList.length; j++) {
            String cmd = "pm enable " + packageReceiverList[j];
            //部分receiver包含$符号，需要做进一步处理，用"$"替换掉$
            cmd = cmd.replace("$", "\"" + "$" + "\"");
            //执行命令
            mSring.add(cmd);

        }
        GlobalDataManager.getInstance().getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                ShellUtils.CommandResult mCommandResult = ShellUtils.execCommand(mSring, true, true);
            }
        });

    }


}
