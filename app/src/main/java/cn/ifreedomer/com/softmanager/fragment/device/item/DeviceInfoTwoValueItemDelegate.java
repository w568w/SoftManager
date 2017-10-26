package cn.ifreedomer.com.softmanager.fragment.device.item;

import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.bean.DeviceInfoWrap;
import cn.ifreedomer.com.softmanager.bean.TwoValue;

/**
 * @author:eavawu
 * @since: 25/10/2017.
 * TODO:
 */

public class DeviceInfoTwoValueItemDelegate implements ItemViewDelegate<DeviceInfoWrap<TwoValue>> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.deviceinfo_two_view;
    }

    @Override
    public boolean isForViewType(DeviceInfoWrap<TwoValue> item, int position) {
        return item.getType() == DeviceInfoWrap.TWO_VALUE;
    }

    @Override
    public void convert(ViewHolder holder, DeviceInfoWrap<TwoValue> deviceInfoTwoValueDeviceInfoWrap, int position) {
        holder.setText(R.id.tv_title, deviceInfoTwoValueDeviceInfoWrap.getData().getTitle());
        holder.setText(R.id.tv_value, deviceInfoTwoValueDeviceInfoWrap.getData().getValue());
    }
}
