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


/**
 * @author:eavawu
 * @since: 29/10/2017.
 * TODO:
 */

public class MemoryAdapter extends CommonAdapter<ProcessItem> {
    private static final String TAG = MemoryAdapter.class.getSimpleName();
    private HashMap<String, Integer> mimeTypeMap = new HashMap<>();

    public MemoryAdapter(Context context, int layoutId, List<ProcessItem> datas) {
        super(context, layoutId, datas);
        mimeTypeMap.put("application/octet-stream", R.mipmap.unknow_file);
        mimeTypeMap.put("application/vnd.android.package-archive", R.mipmap.apk_file);
        mimeTypeMap.put("audio/mpeg", R.mipmap.music_file);
        mimeTypeMap.put("video/mp4", R.mipmap.video_file);
        mimeTypeMap.put("application/zip", R.mipmap.zip);
        mimeTypeMap.put("application/rar", R.mipmap.zip);

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
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processItem.setChecked(!processItem.isChecked());
            }
        });

    }

    public void removeCheckedItems() {
//        for (int i = mDatas.size() - 1; i >= 0; i--) {
//            boolean checked = mDatas.get(i).isChecked();
//            final FileInfo fileInfo = mDatas.get(i);
//            if (checked) {
//                GlobalDataManager.getInstance().getThreadPool().execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        File file = new File(fileInfo.getPath());
//                        file.delete();
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
