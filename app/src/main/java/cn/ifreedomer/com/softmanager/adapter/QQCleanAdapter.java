package cn.ifreedomer.com.softmanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.activity.clean.QQCleanActivity;
import cn.ifreedomer.com.softmanager.bean.FileInfo;
import cn.ifreedomer.com.softmanager.bean.clean.QQGroupTitle;
import cn.ifreedomer.com.softmanager.manager.GlobalDataManager;
import cn.ifreedomer.com.softmanager.util.DataTypeUtil;

/**
 * @author:eavawu
 * @since: 30/10/2017.
 * TODO:
 */

public class QQCleanAdapter extends BaseExpandableListAdapter {
    private List<QQGroupTitle> mTitleList;
    private List<List<FileInfo>> mFileInfoGroupList;
    private Context mContext;
    private HashMap<String, Integer> mimeTypeMap = new HashMap<>();

    public QQCleanAdapter(Context context, List<QQGroupTitle> titles, List<List<FileInfo>> fileInfoGroupList) {
        this.mTitleList = titles;
        this.mFileInfoGroupList = fileInfoGroupList;
        this.mContext = context;
        mimeTypeMap.put("application/octet-stream", R.mipmap.unknow_file);
        mimeTypeMap.put("application/vnd.android.package-archive", R.mipmap.apk_file);
        mimeTypeMap.put("audio/mpeg", R.mipmap.music_file);
        mimeTypeMap.put("video/mp4", R.mipmap.video_file);
        mimeTypeMap.put("application/zip", R.mipmap.zip);
        mimeTypeMap.put("application/rar", R.mipmap.zip);

    }

    @Override
    public int getGroupCount() {
        return mTitleList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (mFileInfoGroupList.size() <= groupPosition) {
            return 0;
        }
        return mFileInfoGroupList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mTitleList.get(groupPosition);
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
        View groupView = View.inflate(mContext, R.layout.item_garbage_group, null);
        ImageView iconIv = (ImageView) groupView.findViewById(R.id.iv_icon);
        TextView titleTv = (TextView) groupView.findViewById(R.id.tv_title);
        titleTv.setText(mTitleList.get(groupPosition).getTitle());

        selectChildren(groupPosition, groupView);
        if (isExpanded) {
            iconIv.setBackgroundResource(R.mipmap.bottom_arrow);
        } else {
            iconIv.setBackgroundResource(R.mipmap.top_arrow);
        }

        return groupView;
    }

    private void selectChildren(int groupPosition, View groupView) {
        final CheckBox cb = (CheckBox) groupView.findViewById(R.id.cb);
        QQGroupTitle qqGroupTitle = mTitleList.get(groupPosition);
        cb.setChecked(qqGroupTitle.isChecked());
        cb.setOnClickListener(v -> {
            qqGroupTitle.setChecked(!qqGroupTitle.isChecked());
            if (mFileInfoGroupList == null || mFileInfoGroupList.size() == 0) {
                notifyDataSetChanged();
                return;
            }
            if (mFileInfoGroupList.size() <= groupPosition) {
                return;
            }
            List<FileInfo> garbageInfos = mFileInfoGroupList.get(groupPosition);
            for (int i = 0; i < garbageInfos.size(); i++) {
                garbageInfos.get(i).setChecked(cb.isChecked());
            }
            notifyDataSetChanged();

        });
    }

    private void unSelectGroup(int groupPosition, FileInfo fileInfo, CheckBox cb) {
        cb.setChecked(fileInfo.isChecked());
        if (cb != null) {
            cb.setOnClickListener(v -> {
                fileInfo.setChecked(!fileInfo.isChecked());
                if (!fileInfo.isChecked()) {
                    mTitleList.get(groupPosition).setChecked(false);
                }
                notifyDataSetChanged();
            });
        }
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final FileInfo fileInfo = mFileInfoGroupList.get(groupPosition).get(childPosition);
        View childView = convertView;
        if (childView == null) {
            childView = View.inflate(mContext, R.layout.item_appcache_child, null);
        }


        TextView nameTv = (TextView) childView.findViewById(R.id.tv_name);
        CheckBox cb = (CheckBox) childView.findViewById(R.id.cb);
        nameTv.setText(fileInfo.getName());
        TextView sizeTv = (TextView) childView.findViewById(R.id.tv_size);
        sizeTv.setText(DataTypeUtil.getTextBySize(fileInfo.getSize()));
        childView.findViewById(R.id.tv_category).setVisibility(View.GONE);
        int iconRes = R.mipmap.unknow_file;
        if (!TextUtils.isEmpty(fileInfo.getType())) {
            iconRes = mimeTypeMap.get(fileInfo.getType()) == null ? R.mipmap.unknow_file : mimeTypeMap.get(fileInfo.getType());
        }
        ImageView fileIv = (ImageView) childView.findViewById(R.id.iv_icon);

        if ("image/jpeg".equals(fileInfo.getType())) {
            try {
                if (fileInfo.getPhoto() == null) {
                    fileInfo.setPhoto(BitmapFactory.decodeStream(new FileInputStream(fileInfo.getPath())));
                }
                fileIv.setImageBitmap(fileInfo.getPhoto());
                childView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
//action还可以通过set方法进行设置
                        intent.setAction(Intent.ACTION_VIEW);
//既然是查看图片，就要指定打开哪张图片，通过setData，传入图片的uri
                        intent.setData(Uri.parse(fileInfo.getPath()));
//然后指定打开文件的类型，图片的话可用用image/*
                        intent.setType("image/*");//这两句也可以用intent.setDataAndType();来一句话搞定
//最后调用startActivity,系统就会根据条件找到具体能执行这个intent的activity
                        mContext.startActivity(intent);
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            fileIv.setImageResource(iconRes);
        }

        unSelectGroup(groupPosition, fileInfo, cb);


        return childView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void removeCheckedItems(Handler handler) {
        Runnable runnable = () -> {
            for (List<FileInfo> fileInfoList : mFileInfoGroupList) {
                for (int i = fileInfoList.size() - 1; i >= 0; i--) {
                    boolean checked = fileInfoList.get(i).isChecked();
                    final FileInfo fileInfo = fileInfoList.get(i);
                    if (checked) {
                        File file = new File(fileInfo.getPath());
                        file.delete();
                        fileInfoList.remove(i);
                    }
                }
            }
            handler.sendEmptyMessage(QQCleanActivity.MSG_CLEAN_FINISH);
        };
        GlobalDataManager.getInstance().getThreadPool().execute(runnable);


    }

}
