package cn.ifreedomer.com.softmanager.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.HashMap;
import java.util.List;

import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.bean.clean.ProcessItem;
import cn.ifreedomer.com.softmanager.util.DataTypeUtil;
import cn.ifreedomer.com.softmanager.util.LogUtil;
import cn.ifreedomer.com.softmanager.util.ProcessManagerUtils;


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
        for (int i = mDatas.size() - 1; i == 0; i++) {
            ProcessItem processItem = mDatas.get(i);
            if (processItem.isChecked()) {
                ProcessManagerUtils.killProcess(mContext, processItem);
            }
            mDatas.remove(i);
        }
        notifyDataSetChanged();
//        for (int i = mDatas.size() - 1; i >= 0; i--) {
//            boolean checked = mDatas.get(i).isChecked();
//            final FileInfo fileInfo = mDatas.get(i);
//            if (checked) {
//                GlobalDataManager.getInstance().getThreadPool().execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        File file = new File(fileInfo.getPath());
// s                       file.delete();
//                    }
//                });
//
//                mDatas.remove(i);
//            }
//
//
//        }

    }


}
