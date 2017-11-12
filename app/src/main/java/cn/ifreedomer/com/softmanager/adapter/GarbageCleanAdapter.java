package cn.ifreedomer.com.softmanager.adapter;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.activity.clean.GarbageActivity;
import cn.ifreedomer.com.softmanager.bean.EmptyFolder;
import cn.ifreedomer.com.softmanager.bean.GarbageInfo;
import cn.ifreedomer.com.softmanager.bean.clean.ClearItem;
import cn.ifreedomer.com.softmanager.bean.clean.GarbageGroupTitle;
import cn.ifreedomer.com.softmanager.manager.PermissionManager;
import cn.ifreedomer.com.softmanager.model.AppInfo;
import cn.ifreedomer.com.softmanager.util.DataTypeUtil;
import cn.ifreedomer.com.softmanager.util.FileUtil;
import cn.ifreedomer.com.softmanager.util.LogUtil;

/**
 * @author:eavawu
 * @since: 30/10/2017.
 * TODO:
 */

public class GarbageCleanAdapter extends BaseExpandableListAdapter {
    private static final String TAG = GarbageCleanAdapter.class.getSimpleName();
    private List<GarbageGroupTitle> mTitleList;
    private List<List<GarbageInfo>> mGarbageInfoGroupList;
    private Context mContext;
    private List<Boolean> checkState = new ArrayList<>();
    private Handler mHandler = null;

    public GarbageCleanAdapter(Context context, List<GarbageGroupTitle> titleList, List<List<GarbageInfo>> garbageInfoGroupList) {
        Log.e(TAG, "titleList size = " + titleList);
        this.mTitleList = titleList;
        this.mGarbageInfoGroupList = garbageInfoGroupList;
        this.mContext = context;
        for (int i = 0; i < mTitleList.size(); i++) {
            checkState.add(false);
        }

    }

    @Override
    public int getGroupCount() {
        return mTitleList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mGarbageInfoGroupList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGarbageInfoGroupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mGarbageInfoGroupList.get(groupPosition).get(childPosition);
    }


    @Override
    public int getChildType(int groupPosition, int childPosition) {
        return groupPosition;
    }

    @Override
    public int getChildTypeCount() {
        return mTitleList.size();
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
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        LogUtil.e(TAG, "mTitleList =" + mTitleList.toString());
        View groupView = View.inflate(mContext, R.layout.item_garbage_group, null);
        ImageView iconIv = (ImageView) groupView.findViewById(R.id.iv_icon);
        TextView titleTv = (TextView) groupView.findViewById(R.id.tv_title);
        GarbageGroupTitle garbageGroupInfo = mTitleList.get(groupPosition);
        titleTv.setText(garbageGroupInfo.getTitle());

        final CheckBox cb = (CheckBox) groupView.findViewById(R.id.cb);
        cb.setChecked(garbageGroupInfo.isChecked());
        cb.setOnClickListener(v -> {
            garbageGroupInfo.setChecked(!garbageGroupInfo.isChecked());
            List<GarbageInfo> garbageInfos = mGarbageInfoGroupList.get(groupPosition);
            for (int i = 0; i < garbageInfos.size(); i++) {
                garbageInfos.get(i).setChecked(cb.isChecked());
            }
            notifyDataSetChanged();
        });


        if (isExpanded) {
            iconIv.setBackgroundResource(R.mipmap.bottom_arrow);
        } else {
            iconIv.setBackgroundResource(R.mipmap.top_arrow);
        }


        return groupView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LogUtil.e(TAG, "group size = " + mTitleList.size() + "  group_pos=" + groupPosition);
        final GarbageInfo garbageInfo = mGarbageInfoGroupList.get(groupPosition).get(childPosition);
        View childView = null;
        CheckBox cb = null;
        switch (garbageInfo.getType()) {
            case GarbageInfo.TYPE_APP_CACHE:
                childView = View.inflate(mContext, R.layout.item_appcache_child, null);
                TextView nameTv = (TextView) childView.findViewById(R.id.tv_name);
                TextView sizeTv = (TextView) childView.findViewById(R.id.tv_size);
                ImageView iconIv = (ImageView) childView.findViewById(R.id.iv_icon);
                TextView categoryTv = (TextView) childView.findViewById(R.id.tv_category);
                cb = (CheckBox) childView.findViewById(R.id.cb);
                AppInfo data = (AppInfo) garbageInfo.getData();
                nameTv.setText(data.getAppName());
                sizeTv.setText(DataTypeUtil.getTextBySize(data.getCacheSize()));
                iconIv.setImageDrawable(data.getAppIcon());
                categoryTv.setText(R.string.cache_garbage);
                cb.setChecked(garbageInfo.isChecked());
                break;
            case GarbageInfo.TYPE_AD_GARBAGE:
                ClearItem clearItem = (ClearItem) garbageInfo.getData();
                childView = View.inflate(mContext, R.layout.item_appcache_child, null);
                nameTv = (TextView) childView.findViewById(R.id.tv_name);
                sizeTv = (TextView) childView.findViewById(R.id.tv_size);
                categoryTv = (TextView) childView.findViewById(R.id.tv_category);

                cb = (CheckBox) childView.findViewById(R.id.cb);
                nameTv.setText(clearItem.getName());
                sizeTv.setText(DataTypeUtil.getTextBySize(clearItem.getFileSize()));
                iconIv = (ImageView) childView.findViewById(R.id.iv_icon);
                iconIv.setImageResource(R.mipmap.ad);
                cb.setChecked(garbageInfo.isChecked());
                categoryTv.setText(clearItem.getFilePath());
                break;
            case GarbageInfo.TYPE_SYSTEM_GARBAGE:
                childView = View.inflate(mContext, R.layout.item_appcache_child, null);

                break;
            case GarbageInfo.TYPE_EMPTY_FILE:
                childView = View.inflate(mContext, R.layout.item_appcache_child, null);
                categoryTv = (TextView) childView.findViewById(R.id.tv_category);
                nameTv = (TextView) childView.findViewById(R.id.tv_name);
                nameTv.setText(mContext.getString(R.string.use_less_folder));
                iconIv = (ImageView) childView.findViewById(R.id.iv_icon);

                EmptyFolder emptyFolderInfo = (EmptyFolder) garbageInfo.getData();
                cb = (CheckBox) childView.findViewById(R.id.cb);
                categoryTv.setText(mContext.getString(R.string.empty_file_count) + emptyFolderInfo.getPathList().size());
                cb.setChecked(garbageInfo.isChecked());
                sizeTv = (TextView) childView.findViewById(R.id.tv_size);
                sizeTv.setText(DataTypeUtil.getTextBySize(emptyFolderInfo.getTotalSize()));
                iconIv.setImageResource(R.mipmap.empty_folder);
                break;
        }
        if (cb != null) {
            cb.setOnClickListener(v -> {
                garbageInfo.setChecked(!garbageInfo.isChecked());
                if (!garbageInfo.isChecked()) {
                    mTitleList.get(groupPosition).setChecked(false);
                }
                notifyDataSetChanged();
            });
        }


        return childView;

//        TextView nameTv = (TextView) childView.findViewById(R.id.tv_name);
//        nameTv.setText(garbageInfo.getName());
//        TextView sizeTv = (TextView) childView.findViewById(R.id.tv_size);
//        String sizeStr = DataTypeUtil.getTwoFloat(garbageInfo.getSize() / FileUtil.MB) + " MB";
//        sizeTv.setText(sizeStr);
//        ImageView fileIv = (ImageView) childView.findViewById(R.id.iv_icon);
//        fileIv.setImageDrawable(garbageInfo.getDrawable());
//
//        final CheckBox cb = (CheckBox) childView.findViewById(R.id.cb);
//        cb.setChecked(garbageInfo.isChecked());

//
//
//        return childView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void removeCheckedItems(Handler handler) {
        this.mHandler = handler;
        for (int i = 0; i < mTitleList.size(); i++) {
            int type = mTitleList.get(i).getType();
            if (type == GarbageInfo.TYPE_APP_CACHE) {
                sendProcessTip(mContext.getString(R.string.cleaning_app_cache));
                deleteAppCache(i);
            } else if (type == GarbageInfo.TYPE_AD_GARBAGE) {
                sendProcessTip(mContext.getString(R.string.cleanning_ad_garbage));
                deleteAdGarbage(i);
            } else if (type == GarbageInfo.TYPE_EMPTY_FILE) {
                sendProcessTip(mContext.getString(R.string.cleanning_empty_folder));
                deleteEmptyFolder(i);
            }
            sendProcessTip(mContext.getString(R.string.clean_finished));
        }
    }

    private void sendProcessTip(String tip) {
        Message message = new Message();
        message.obj = tip;
        message.what = GarbageActivity.MSG_CLEAN_PROCESSING;
        mHandler.sendMessage(message);
    }

    private void deleteEmptyFolder(int pos) {
        LogUtil.e(TAG, "deleteEmptyFolder = " + pos);

        GarbageInfo garbageInfo = mGarbageInfoGroupList.get(pos).get(0);

        EmptyFolder emptyFolder = (EmptyFolder) garbageInfo.getData();
        for (int i = 0; i < emptyFolder.getPathList().size(); i++) {
            if (garbageInfo.isChecked()) {
                LogUtil.e(TAG, "delete empty folder " + emptyFolder.getPathList().get(i) + "  state =" + FileUtil.deleteDir(new File(emptyFolder.getPathList().get(i))));
            }
        }
    }

    private void deleteAdGarbage(int pos) {
        LogUtil.e(TAG, "deleteAdGarbage = " + pos);

        List<GarbageInfo> garbageInfos;
        GarbageInfo garbageInfo;//清理广告垃圾
        garbageInfos = mGarbageInfoGroupList.get(pos);
        for (int i = 0; i < garbageInfos.size(); i++) {
            garbageInfo = garbageInfos.get(i);
            if (garbageInfo.isChecked()) {
                ClearItem clearItem = (ClearItem) garbageInfo.getData();
                File file = new File(clearItem.getFilePath());
                LogUtil.e(TAG, "delete ad path " + file.getPath() + "  state =" + FileUtil.deleteDir(file));
            }
        }
    }

    private void deleteAppCache(int pos) {
        LogUtil.e(TAG, "deleteAppCache = " + pos);
        List<GarbageInfo> garbageInfos = mGarbageInfoGroupList.get(pos);
        for (int i = 0; i < garbageInfos.size(); i++) {
            GarbageInfo garbageInfo = garbageInfos.get(i);
            if (garbageInfo.isChecked()) {
                AppInfo appInfo = (AppInfo) garbageInfo.getData();
                deleteCache(appInfo.getPackname());
                appInfo.setCacheSize(0);
            }
        }
    }


    private void deleteCache(String packageName) {
        LogUtil.e(TAG, "delete " + packageName + " cache");
        String dataCachePath;
        if (PermissionManager.getInstance().checkOrRequestedRootPermission()) {
            LogUtil.e(TAG, "has root");
            dataCachePath = "data/data/" + packageName + "/cache";

            LogUtil.e(TAG, "delete dataCachePath path " + dataCachePath + "  state =" + FileUtil.deleteFolderByRoot(dataCachePath).toString());


            String webViewCachePath = "data/data/" + packageName + "/database/webview.db";
            LogUtil.e(TAG, "delete webViewCachePath path " + dataCachePath + "  state =" + FileUtil.deleteFolderByRoot(webViewCachePath).toString());


            String webViewCachePath1 = "data/data/" + packageName + "/database/webviewCache.db";
            LogUtil.e(TAG, "delete webViewCachePath1 path " + dataCachePath + "  state =" + FileUtil.deleteFolderByRoot(webViewCachePath1).toString());


        }
        LogUtil.e(TAG, "delete sdcard cache");
        String sdCachePath = Environment.getExternalStorageDirectory().getPath() + File.separator + packageName + "/cache";
        LogUtil.e(TAG, "delete  sdCachePath path " + sdCachePath + "  state =" + FileUtil.deleteDir(new File(sdCachePath)));

    }

}



