package cn.ifreedomer.com.softmanager.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.zhy.base.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

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
        Log.e("position",position+"");
       ;
        holder.setText(R.id.tv_appname, autoStartInfo.getLabel());
        holder.setImageDrawable(R.id.iv_icon, autoStartInfo.getIcon());
//        holder.setText(R.id.tv_appcache, autoStartInfo.getPackageName() + "");
        final Switch switchWidget = holder.getView(R.id.swith_auto);

        holder.setOnClickListener(R.id.swith_auto, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ShellUtils.checkRootPermission()){
                    switchWidget.setChecked(!switchWidget.isChecked());
                    Toast.makeText(mContext, R.string.no_root,Toast.LENGTH_SHORT).show();
                    return;
                }

                int result = -1 ;
                if (mDatas.get(position).isEnable()){
                    result = diasableApp(autoStartInfo);
                }else{
                    result =  enableApp(autoStartInfo);
                }
                if (result==0){
                    mDatas.get(position).setEnable(switchWidget.isChecked());
                }else{
                    switchWidget.setChecked(!switchWidget.isChecked());
                }

            }
        });
        if (mDatas.get(position).isEnable()){
            switchWidget.setChecked(true);
        }else{
            switchWidget.setChecked(false);
        }
        Log.e(autoStartInfo.getLabel(),autoStartInfo.isEnable()+"");

    }

    private int diasableApp(AutoStartInfo item) {
        String packageReceiverList[] = item.getPackageReceiver().toString().split(";");

        List<String> mSring = new ArrayList<>();
        for (int j = 0; j < packageReceiverList.length; j++) {
            String cmd = "pm disable " + packageReceiverList[j];
            //部分receiver包含$符号，需要做进一步处理，用"$"替换掉$
            cmd = cmd.replace("$", "\"" + "$" + "\"");
            //执行命令
            mSring.add(cmd);

        }
        ShellUtils.CommandResult mCommandResult = ShellUtils.execCommand(mSring, true, true);

        return mCommandResult.result;

        // T.showLong(mContext, mCommandResult.result + "" + mCommandResult.errorMsg + mCommandResult.successMsg);
    }

    private int enableApp(AutoStartInfo item) {
        String packageReceiverList[] = item.getPackageReceiver().toString().split(";");

        List<String> mSring = new ArrayList<>();
        for (int j = 0; j < packageReceiverList.length; j++) {
            String cmd = "pm enable " + packageReceiverList[j];
            //部分receiver包含$符号，需要做进一步处理，用"$"替换掉$
            cmd = cmd.replace("$", "\"" + "$" + "\"");
            //执行命令
            mSring.add(cmd);

        }
        ShellUtils.CommandResult mCommandResult = ShellUtils.execCommand(mSring, true, true);
        return mCommandResult.result;

    }


}
