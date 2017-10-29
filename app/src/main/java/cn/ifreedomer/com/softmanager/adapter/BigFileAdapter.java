package cn.ifreedomer.com.softmanager.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import cn.ifreedomer.com.softmanager.GlobalDataManager;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.bean.FileInfo;
import cn.ifreedomer.com.softmanager.util.LogUtil;


/**
 * @author:eavawu
 * @since: 29/10/2017.
 * TODO:
 */

public class BigFileAdapter extends CommonAdapter<FileInfo> {
    private static final String TAG = BigFileAdapter.class.getSimpleName();
    private HashMap<String, Integer> mimeTypeMap = new HashMap<>();

    public BigFileAdapter(Context context, int layoutId, List<FileInfo> datas) {
        super(context, layoutId, datas);
        mimeTypeMap.put("application/octet-stream", R.mipmap.unknow_file);
        mimeTypeMap.put("application/vnd.android.package-archive", R.mipmap.apk_file);

    }

    @Override
    protected void convert(ViewHolder holder, final FileInfo fileInfo, int position) {
        holder.setText(R.id.tv_name, fileInfo.getName());
        holder.setText(R.id.tv_path, fileInfo.getSize() + " MB");
        LogUtil.e(TAG, fileInfo.toString());
        int icon = R.mipmap.unknow_file;
        if (!TextUtils.isEmpty(fileInfo.getType())) {
            icon = mimeTypeMap.get(fileInfo.getType());
        }
        holder.setImageResource(R.id.iv_icon, icon);

        LogUtil.e(TAG, "position=:" + position + "     checked=" + fileInfo.isChecked());
        CheckBox checkBox = holder.getView(R.id.cb);
        checkBox.setChecked(fileInfo.isChecked());
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileInfo.setChecked(!fileInfo.isChecked());
            }
        });

    }

    public void removeCheckedItems() {
        for (int i = mDatas.size() - 1; i >= 0; i--) {
            boolean checked = mDatas.get(i).isChecked();
            final FileInfo fileInfo = mDatas.get(i);
            if (checked) {
                GlobalDataManager.getInstance().getThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        File file = new File(fileInfo.getPath());
                        file.delete();
                    }
                });

                mDatas.remove(i);
            }


        }

    }


}
