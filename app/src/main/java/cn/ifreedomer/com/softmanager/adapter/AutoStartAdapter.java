package cn.ifreedomer.com.softmanager.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.zhy.base.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import cn.ifreedomer.com.softmanager.GlobalDataManager;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.util.AutoStartInfo;
import cn.ifreedomer.com.softmanager.util.ShellUtils;


/**
 * @author wuyihua
 * @Date 2017/2/23
 */

public class AutoStartAdapter extends com.zhy.base.adapter.recyclerview.CommonAdapter<AutoStartInfo> {

    public AutoStartAdapter(Context context, int layoutId, List<AutoStartInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, final AutoStartInfo autoStartInfo) {
        final int position = holder.getPosition();
        Log.e("position", position + "");
        holder.setText(R.id.tv_appname, autoStartInfo.getLabel());
        holder.setImageDrawable(R.id.iv_icon, autoStartInfo.getIcon());
        final Switch switchWidget = holder.getView(R.id.swith_auto);

        holder.setOnClickListener(R.id.swith_auto, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!GlobalDataManager.getInstance().checkOrRequestedRootPermission()) {
                    switchWidget.setChecked(!switchWidget.isChecked());
                    Toast.makeText(mContext, R.string.no_root, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mDatas.get(position).isEnable()) {
                    diasableApp(autoStartInfo);
                } else {
                    enableApp(autoStartInfo);
                }
                mDatas.get(position).setEnable(switchWidget.isChecked());

            }
        });
        if (mDatas.get(position).isEnable()) {
            switchWidget.setChecked(true);
        } else {
            switchWidget.setChecked(false);
        }
        Log.e(autoStartInfo.getLabel(), autoStartInfo.isEnable() + "");

    }

    private void diasableApp(AutoStartInfo item) {
        String packageReceiverList[] = item.getPackageReceiver().toString().split(";");

        final List<String> mSring = new ArrayList<>();
        for (int j = 0; j < packageReceiverList.length; j++) {
            String cmd = "pm disable " + packageReceiverList[j];
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
