package cn.ifreedomer.com.softmanager.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import cn.ifreedomer.com.softmanager.GlobalDataManager;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.bean.FileInfo;
import cn.ifreedomer.com.softmanager.util.DataTypeUtil;
import cn.ifreedomer.com.softmanager.util.FileUtil;

/**
 * @author:eavawu
 * @since: 30/10/2017.
 * TODO:
 */

public class QQCleanAdapter extends BaseExpandableListAdapter {
    private String[] mTitles;
    private List<List<FileInfo>> mFileInfoGroupList;
    private Context mContext;
    private HashMap<String, Integer> mimeTypeMap = new HashMap<>();

    public QQCleanAdapter(Context context, String[] titles, List<List<FileInfo>> fileInfoGroupList) {
        this.mTitles = titles;
        this.mFileInfoGroupList = fileInfoGroupList;
        this.mContext = context;
        mimeTypeMap.put("application/octet-stream", R.mipmap.unknow_file);
        mimeTypeMap.put("application/vnd.android.package-archive", R.mipmap.apk_file);
        mimeTypeMap.put("audio/mpeg", R.mipmap.music_file);
        mimeTypeMap.put("video/mp4", R.mipmap.video_file);

    }

    @Override
    public int getGroupCount() {
        return mFileInfoGroupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mFileInfoGroupList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mFileInfoGroupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mFileInfoGroupList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View groupView = View.inflate(mContext, R.layout.item_qq_group, null);
        ImageView iconIv = (ImageView) groupView.findViewById(R.id.iv_icon);
        TextView titleTv = (TextView) groupView.findViewById(R.id.tv_title);
        titleTv.setText(mTitles[groupPosition]);

        if (isExpanded) {
            iconIv.setBackgroundResource(R.mipmap.bottom_arrow);
        } else {
            iconIv.setBackgroundResource(R.mipmap.top_arrow);
        }


        return groupView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final FileInfo fileInfo = mFileInfoGroupList.get(groupPosition).get(childPosition);
        View childView = View.inflate(mContext, R.layout.item_qq_child, null);
        TextView nameTv = (TextView) childView.findViewById(R.id.tv_name);
        nameTv.setText(fileInfo.getName());
        TextView sizeTv = (TextView) childView.findViewById(R.id.tv_size);
        String sizeStr = DataTypeUtil.getTwoFloat(fileInfo.getSize() / FileUtil.MB) + " MB";
        sizeTv.setText(sizeStr);

        int iconRes = R.mipmap.unknow_file;
        if (!TextUtils.isEmpty(fileInfo.getType())) {
            iconRes = mimeTypeMap.get(fileInfo.getType()) == null ? R.mipmap.unknow_file : mimeTypeMap.get(fileInfo.getType());
        }
        ImageView fileIv = (ImageView) childView.findViewById(R.id.iv_icon);
        fileIv.setImageResource(iconRes);

        final CheckBox cb = (CheckBox) childView.findViewById(R.id.cb);
        cb.setChecked(fileInfo.isChecked());
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileInfo.setChecked(!fileInfo.isChecked());
                notifyDataSetChanged();
            }
        });


        return childView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void removeCheckedItems() {
        for (List<FileInfo> fileInfoList : mFileInfoGroupList) {
            for (int i = fileInfoList.size() - 1; i >= 0; i--) {
                boolean checked = fileInfoList.get(i).isChecked();
                final FileInfo fileInfo = fileInfoList.get(i);
                if (checked) {
                    GlobalDataManager.getInstance().getThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            File file = new File(fileInfo.getPath());
                            file.delete();
                        }
                    });

                    fileInfoList.remove(i);
                }


            }
        }

    }

}
