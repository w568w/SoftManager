package cn.ifreedomer.com.softmanager.widget.permission;

import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.bean.PermissionTitle;
import cn.ifreedomer.com.softmanager.bean.PermissionWrap;

/**
 * @author:eavawu
 * @since: 05/11/2017.
 * TODO:
 */

public class PermissionTitleItemDelegate implements ItemViewDelegate<PermissionWrap<PermissionTitle>> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_permission_title;
    }

    @Override
    public boolean isForViewType(PermissionWrap<PermissionTitle> item, int position) {
        return item.getType() == PermissionWrap.TITLE_TYPE;
    }

    @Override
    public void convert(ViewHolder holder, PermissionWrap<PermissionTitle> permissionTitlePermissionWrap, int position) {
        PermissionTitle permissionTitle = permissionTitlePermissionWrap.getData();
        boolean showPadding = permissionTitle.isShowPadding();
        holder.setVisible(R.id.view_header, showPadding);
        holder.setText(R.id.tv_title, permissionTitle.getTitle());
    }
}
