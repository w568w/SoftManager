package cn.ifreedomer.com.softmanager.adapter;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;
import android.widget.CheckBox;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.bean.clean.ProcessItem;
import cn.ifreedomer.com.softmanager.util.DataTypeUtil;
import cn.ifreedomer.com.softmanager.util.LogUtil;

import static android.content.Context.ACTIVITY_SERVICE;


/**
 * @author:eavawu
 * @since: 29/10/2017.
 * TODO:
 */

public class MemoryAdapter extends CommonAdapter<ProcessItem> {
    private static final String TAG = MemoryAdapter.class.getSimpleName();

    public MemoryAdapter(Context context, int layoutId, List<ProcessItem> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final ProcessItem processItem, int position) {
        holder.setText(R.id.tv_name, processItem.getLabel().toString());
        holder.setText(R.id.tv_size, DataTypeUtil.getTextBySize(processItem.getMemorySize()));
        holder.setVisible(R.id.tv_category, false);
        LogUtil.e(TAG, processItem.toString());

        holder.setImageDrawable(R.id.iv_icon, processItem.getIcon());

        CheckBox checkBox = holder.getView(R.id.cb);
        checkBox.setChecked(processItem.isChecked());
        checkBox.setOnClickListener(v -> processItem.setChecked(!processItem.isChecked()));

    }

    public void removeCheckedItems() {
        LogUtil.e(TAG, "removeCheckedItems0");
        ActivityManager activityManger = (ActivityManager) mContext.getSystemService(ACTIVITY_SERVICE);
        for (int i = mDatas.size() - 1; i >= 0; i--) {
            ProcessItem processItem = mDatas.get(i);
            Log.e(TAG, "removeCheckedItems: package name=" + processItem.getPkgName());

            if (processItem.isChecked()) {
                mDatas.remove(i);
                activityManger.killBackgroundProcesses(processItem.getPkgName());
            }
        }
    }


}
