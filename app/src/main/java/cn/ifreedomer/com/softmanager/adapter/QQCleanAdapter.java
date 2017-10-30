package cn.ifreedomer.com.softmanager.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.List;

import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.bean.FileInfo;

/**
 * @author:eavawu
 * @since: 30/10/2017.
 * TODO:
 */

public class QQCleanAdapter extends BaseExpandableListAdapter {
    private String[] mTitles;
    private List<List<FileInfo>> mFileInfoGroupList;
    private Context mContext;

    public QQCleanAdapter(Context context, String[] titles, List<List<FileInfo>> fileInfoGroupList) {
        this.mTitles = titles;
        this.mFileInfoGroupList = fileInfoGroupList;
        this.mContext = context;
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
        return View.inflate(mContext, R.layout.item_qq_group_item, null);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return View.inflate(mContext, R.layout.item_qq_child_item, null);

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
