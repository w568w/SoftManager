package cn.ifreedomer.com.softmanager.fragment.device.item;

import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.bean.DeviceInfoWrap;
import cn.ifreedomer.com.softmanager.bean.Title;

/**
 * @author:eavawu
 * @since: 25/10/2017.
 * TODO:
 */

public class DeviceInfoTitleItemDelegate implements ItemViewDelegate<DeviceInfoWrap<Title>> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.deviceinfo_title_view;
    }

    @Override
    public boolean isForViewType(DeviceInfoWrap<Title> item, int position) {
        return item.getType() == DeviceInfoWrap.TITLE;
    }

    @Override
    public void convert(ViewHolder holder, DeviceInfoWrap<Title> titleDeviceInfoWrap, int position) {
        holder.setText(R.id.tv_title, titleDeviceInfoWrap.getData().getTitle());
    }
}
