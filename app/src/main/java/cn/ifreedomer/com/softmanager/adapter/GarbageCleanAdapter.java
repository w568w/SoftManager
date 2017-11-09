package cn.ifreedomer.com.softmanager.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.bean.GarbageInfo;
import cn.ifreedomer.com.softmanager.bean.clean.AppCacheInfo;
import cn.ifreedomer.com.softmanager.util.LogUtil;

/**
 * @author:eavawu
 * @since: 30/10/2017.
 * TODO:
 */

public class GarbageCleanAdapter extends BaseExpandableListAdapter {
    private static final String TAG = GarbageCleanAdapter.class.getSimpleName();
    private String[] mTitles;
    private List<List<GarbageInfo>> mGarbageInfoGroupList;
    private Context mContext;
    private List<Boolean> checkState = new ArrayList<>();

    public GarbageCleanAdapter(Context context, String[] titles, List<List<GarbageInfo>> garbageInfoGroupList) {
        this.mTitles = titles;
        this.mGarbageInfoGroupList = garbageInfoGroupList;
        this.mContext = context;
        for (int i = 0; i < mTitles.length; i++) {
            checkState.add(false);
        }

    }

    @Override
    public int getGroupCount() {
        return mGarbageInfoGroupList.size();
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
        return mTitles.length;
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
        View groupView = View.inflate(mContext, R.layout.item_garbage_group, null);
        ImageView iconIv = (ImageView) groupView.findViewById(R.id.iv_icon);
        TextView titleTv = (TextView) groupView.findViewById(R.id.tv_title);
        titleTv.setText(mTitles[groupPosition]);

        final CheckBox cb = (CheckBox) groupView.findViewById(R.id.cb);
        cb.setChecked(checkState.get(groupPosition));
        cb.setOnClickListener(v -> {
            checkState.set(groupPosition, !checkState.get(groupPosition));
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
        final GarbageInfo garbageInfo = mGarbageInfoGroupList.get(groupPosition).get(childPosition);
        View childView = null;
        switch (groupPosition) {
            case GarbageInfo.TYPE_APP_CACHE:
                childView = View.inflate(mContext, R.layout.item_appcache_child, null);
                TextView nameTv = (TextView) childView.findViewById(R.id.tv_name);
                TextView sizeTv = (TextView) childView.findViewById(R.id.tv_size);
                ImageView iconIv = (ImageView) childView.findViewById(R.id.iv_icon);
                AppCacheInfo data = (AppCacheInfo) garbageInfo.getData();
                nameTv.setText(data.getName());
                sizeTv.setText(data.getSize() + " kB");
                iconIv.setImageDrawable(data.getDrawable());

                break;
            case GarbageInfo.TYPE_AD_GARBAGE:
                childView = View.inflate(mContext, R.layout.item_appcache_child, null);

                break;
            case GarbageInfo.TYPE_SYSTEM_GARBAGE:
                childView = View.inflate(mContext, R.layout.item_appcache_child, null);

                break;
            case GarbageInfo.TYPE_EMPTY_FILE:
                childView = View.inflate(mContext, R.layout.item_appcache_child, null);

                break;
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
//        cb.setOnClickListener(v -> {
//            garbageInfo.setChecked(!garbageInfo.isChecked());
//            notifyDataSetChanged();
//        });
//
//
//        return childView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void removeCheckedItems() {
        LogUtil.e(TAG, "removeCheckedItems");
//        List<String> packageNameList = new ArrayList<>();
//        for (List<GarbageInfo<AppCacheInfo>> garbageInfoList : mGarbageInfoGroupList) {
//            for (int i = garbageInfoList.size() - 1; i >= 0; i--) {
//                boolean checked = garbageInfoList.get(i).isChecked();
//                final GarbageInfo garbageInfo = garbageInfoList.get(i);
//                if (garbageInfo.getType() == GarbageInfo.TYPE_APP_CACHE) {
//                    packageNameList.add(garbageInfo.getPackageName());
//                }
//                if (checked) {
//                    garbageInfoList.remove(i);
//                }
//            }
//        }
//        notifyDataSetChanged();
//        GlobalDataManager.getInstance().getThreadPool().execute(() ->
//        {
//            for (int i = 0; i < packageNameList.size(); i++) {
//                PackageInfoManager.getInstance().clearCache(packageNameList.get(i));
//
//            }
//        });

    }

}



