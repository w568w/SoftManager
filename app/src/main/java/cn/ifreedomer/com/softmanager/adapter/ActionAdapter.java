package cn.ifreedomer.com.softmanager.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.manager.GlobalDataManager;
import cn.ifreedomer.com.softmanager.model.WakeupPathInfo;

/**
 * @author:eavawu
 * @since: 02/12/2017.
 * TODO:
 */

public class ActionAdapter extends CommonAdapter<WakeupPathInfo> {
    private String mSuffix = "";

    public ActionAdapter(Context context, int layoutId, List<WakeupPathInfo> datas) {
        super(context, layoutId, datas);
        mSuffix = context.getString(R.string.action_listener);

    }

    @Override
    protected void convert(ViewHolder holder, WakeupPathInfo wakeupPathInfo, int position) {
        String actionDesc = GlobalDataManager.getInstance().getActionMap().get(wakeupPathInfo.getWakeUpName());
        if (TextUtils.isEmpty(actionDesc)) {
            holder.setText(R.id.tv_name, wakeupPathInfo.getWakeUpName());
        } else {
            holder.setText(R.id.tv_name, actionDesc);

        }
        if (wakeupPathInfo.getWakeupPath() != null) {
            holder.setText(R.id.tv_count, wakeupPathInfo.getWakeupPath().size() + mSuffix);
        } else {
            holder.setText(R.id.tv_count, 0 + mSuffix);
        }
    }
}
