package cn.ifreedomer.com.softmanager.adapter;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.activity.clean.GarbageActivity;
import cn.ifreedomer.com.softmanager.bean.GarbageInfo;
import cn.ifreedomer.com.softmanager.bean.clean.AppCacheItem;
import cn.ifreedomer.com.softmanager.bean.clean.ClearItem;
import cn.ifreedomer.com.softmanager.bean.clean.EmptyFileItem;
import cn.ifreedomer.com.softmanager.bean.clean.GarbageGroupTitle;
import cn.ifreedomer.com.softmanager.manager.GlobalDataManager;
import cn.ifreedomer.com.softmanager.manager.PermissionManager;
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
    private Handler mHandler = null;

    public GarbageCleanAdapter(Context context, List<GarbageGroupTitle> titleList, List<List<GarbageInfo>> garbageInfoGroupList) {
//        Log.e(TAG, "titleList size = " + titleList);
        this.mTitleList = titleList;
        this.mGarbageInfoGroupList = garbageInfoGroupList;
        this.mContext = context;


    }

    @Override
    public int getGroupCount() {
        return mGarbageInfoGroupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
//        LogUtil.e(TAG, "mTitleList size = " + mTitleList.size() + "   mGarbageInfoGroupList size = " + mGarbageInfoGroupList.size() + "   groupPosition = " + groupPosition);

        if (mGarbageInfoGroupList.size() <= groupPosition) {
            return 0;
        }
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
        return mGarbageInfoGroupList.size();
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
        LogUtil.e(TAG, "mTitleList =" + mTitleList.toString() + "  pos = " + groupPosition);
        View groupView = View.inflate(mContext, R.layout.item_garbage_group, null);
        ImageView iconIv = (ImageView) groupView.findViewById(R.id.iv_icon);
        TextView titleTv = (TextView) groupView.findViewById(R.id.tv_title);
        GarbageGroupTitle garbageGroupInfo = mTitleList.get(groupPosition);
        titleTv.setText(garbageGroupInfo.getTitle());

        selectChildren(groupPosition, groupView, garbageGroupInfo);
        if (isExpanded) {
            iconIv.setBackgroundResource(R.mipmap.bottom_arrow);
        } else {
            iconIv.setBackgroundResource(R.mipmap.top_arrow);
        }


        return groupView;
    }

    private void selectChildren(int groupPosition, View groupView, GarbageGroupTitle garbageGroupInfo) {
        final CheckBox cb = (CheckBox) groupView.findViewById(R.id.cb);
        cb.setChecked(garbageGroupInfo.isChecked());
        cb.setOnClickListener(v -> {

            garbageGroupInfo.setChecked(!garbageGroupInfo.isChecked());
            if (mGarbageInfoGroupList == null || mGarbageInfoGroupList.size() == 0) {
                notifyDataSetChanged();
                return;
            }
            if (mGarbageInfoGroupList.size() > groupPosition) {
                List<GarbageInfo> garbageInfos = mGarbageInfoGroupList.get(groupPosition);
                for (int i = 0; i < garbageInfos.size(); i++) {
                    garbageInfos.get(i).setChecked(cb.isChecked());
                }
                notifyDataSetChanged();
                return;
            }

        });
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
//        LogUtil.e(TAG, "group size = " + mTitleList.size() + "  group_pos=" + groupPosition);
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
                AppCacheItem data = (AppCacheItem) garbageInfo.getData();
                nameTv.setText(data.getAppName());
                sizeTv.setText(DataTypeUtil.getTextBySize(data.getSize()));
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

                EmptyFileItem emptyFolderInfo = (EmptyFileItem) garbageInfo.getData();
                cb = (CheckBox) childView.findViewById(R.id.cb);
                categoryTv.setText(mContext.getString(R.string.empty_file_count) + emptyFolderInfo.getSize());
                cb.setChecked(garbageInfo.isChecked());
                sizeTv = (TextView) childView.findViewById(R.id.tv_size);
                sizeTv.setText(DataTypeUtil.getTextBySize(emptyFolderInfo.getSize()));
                iconIv.setImageResource(R.mipmap.empty_folder);
                break;
        }
        unSelectGroup(groupPosition, garbageInfo, cb);


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
//        cb.setEnable(garbageInfo.isEnable());

//
//
//        return childView;

    }

    private void unSelectGroup(int groupPosition, GarbageInfo garbageInfo, CheckBox cb) {
        if (cb != null) {
            cb.setOnClickListener(v -> {
                garbageInfo.setChecked(!garbageInfo.isChecked());
                if (!garbageInfo.isChecked()) {
                    mTitleList.get(groupPosition).setChecked(false);
                }
                notifyDataSetChanged();
            });
        }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    public void removeCheckFiles(GarbageActivity.RemoveFinishCallback removeFinishCallback,Handler handler) {
        for (int i = 0; i < mGarbageInfoGroupList.size(); i++) {

            Iterator<GarbageInfo> iterator = mGarbageInfoGroupList.get(i).iterator();
            while (iterator.hasNext()) {
                GarbageInfo garbageInfo = iterator.next();
                if (!garbageInfo.isChecked()) {
                    continue;
                }
                //删除App缓存
                if (garbageInfo.getType() == GarbageInfo.TYPE_APP_CACHE) {
                    AppCacheItem appInfo = (AppCacheItem) garbageInfo.getData();
                    GlobalDataManager.getInstance().getThreadPool().execute(() -> deleteCache(appInfo.getPkgName()));
                    //删除广告缓存
                } else if (garbageInfo.getType() == GarbageInfo.TYPE_AD_GARBAGE) {
                    ClearItem clearItem = (ClearItem) garbageInfo.getData();
                    File forile = new File(clearItem.getFilePath());
                    GlobalDataManager.getInstance().getThreadPool().execute(() -> forile.delete());
                } else if (garbageInfo.getType() == GarbageInfo.TYPE_EMPTY_FILE) {
                    EmptyFileItem emptyFolder = (EmptyFileItem) garbageInfo.getData();
                    GlobalDataManager.getInstance().getThreadPool().execute(() -> FileUtil.deleteDir(new File(emptyFolder.getPath())));
                }
                iterator.remove();
                if (removeFinishCallback != null) {
                    float size = garbageInfo.getData().getSize();
                    removeFinishCallback.delete(size);

                }
                handler.sendEmptyMessage(GarbageActivity.MSG_UPDATE_UI);
            }
            if (removeFinishCallback != null) {
                removeFinishCallback.finish();
            }
        }
    }

//    public void sendProcessTip(String tip) {
//        Message message = new Message();
//        message.obj = tip;
//        message.what = GarbageActivity.MSG_CLEAN_PROCESSING;
//        mHandler.sendMessage(message);
//    }

    //    private void deleteEmptyFolder(int pos) {
//        LogUtil.e(TAG, "deleteEmptyFolder = " + pos);
//        if (mGarbageInfoGroupList.get(pos).size() <= 0) {
//            LogUtil.e(TAG, "deleteEmptyFolder end ");
//
//            return;
//        }
//        GarbageInfo garbageInfo = mGarbageInfoGroupList.get(pos).get(0);
//
//        EmptyFolder emptyFolder = (EmptyFolder) garbageInfo.getData();
//        for (int i = 0; i < emptyFolder.getPathList().size(); i++) {
//            if (garbageInfo.isChecked()) {
//                LogUtil.e(TAG, "emptyFolder pathlist = ");
//                LogUtil.e(TAG, "delete empty folder " + emptyFolder.getPathList().get(i) + "  state =" + FileUtil.deleteDir(new File(emptyFolder.getPathList().get(i))));
//            }
//        }
//        LogUtil.e(TAG, "deleteEmptyFolder end ");
//
//    }
//
//    private void deleteAdGarbage(int pos) {
//        LogUtil.e(TAG, "deleteAdGarbage = " + pos);
//
//        List<GarbageInfo> garbageInfos;
//        GarbageInfo garbageInfo;//清理广告垃圾
//        garbageInfos = mGarbageInfoGroupList.get(pos);
//        for (int i = 0; i < garbageInfos.size(); i++) {
//            garbageInfo = garbageInfos.get(i);
//            if (garbageInfo.isChecked()) {
//                ClearItem clearItem = (ClearItem) garbageInfo.getData();
//                File file = new File(clearItem.getFilePath());
//                LogUtil.e(TAG, "delete ad path " + file.getPath() + "  state =" + FileUtil.deleteDir(file));
//            }
//        }
//        LogUtil.e(TAG, "deleteAdGarbage = end");
//
//    }
//
//    private void deleteAppCache(int pos) {
//        LogUtil.e(TAG, "deleteAppCache = " + pos);
//        List<GarbageInfo> garbageInfos = mGarbageInfoGroupList.get(pos);
//        for (int i = 0; i < garbageInfos.size(); i++) {
//            GarbageInfo garbageInfo = garbageInfos.get(i);
//            if (garbageInfo.isChecked()) {
//                AppInfo appInfo = (AppInfo) garbageInfo.getData();
//                deleteCache(appInfo.getPackname());
//                appInfo.setCacheSize(0);
//            }
//        }
//        LogUtil.e(TAG, "deleteAppCache = end");
//
//    }
//
//
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



