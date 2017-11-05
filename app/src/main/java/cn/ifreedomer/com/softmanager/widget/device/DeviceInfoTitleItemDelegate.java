package cn.ifreedomer.com.softmanager.widget.device;

import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.bean.DeviceInfoWrap;
import cn.ifreedomer.com.softmanager.bean.DeviceTitle;

/**
 * @author:eavawu
 * @since: 25/10/2017.
 * TODO:
 */

public class DeviceInfoTitleItemDelegate implements ItemViewDelegate<DeviceInfoWrap<DeviceTitle>> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.deviceinfo_title_view;
    }

    @Override
    public boolean isForViewType(DeviceInfoWrap<DeviceTitle> item, int position) {
        return item.getType() == DeviceInfoWrap.TITLE;
    }

    @Override
    public void convert(ViewHolder holder, DeviceInfoWrap<DeviceTitle> titleDeviceInfoWrap, int position) {
        holder.setText(R.id.tv_title, titleDeviceInfoWrap.getData().getTitle());
    }
}
