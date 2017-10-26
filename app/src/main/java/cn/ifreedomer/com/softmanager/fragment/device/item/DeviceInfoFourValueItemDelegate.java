package cn.ifreedomer.com.softmanager.fragment.device.item;

import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.bean.DeviceInfoWrap;
import cn.ifreedomer.com.softmanager.bean.FourValue;

/**
 * @author:eavawu
 * @since: 25/10/2017.
 * TODO:
 */

public class DeviceInfoFourValueItemDelegate implements ItemViewDelegate<DeviceInfoWrap<FourValue>> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.deviceinfo_four_view;
    }

    @Override
    public boolean isForViewType(DeviceInfoWrap<FourValue> item, int position) {
        return item.getType() == DeviceInfoWrap.FOUR_VALUE;
    }

    @Override
    public void convert(ViewHolder holder, DeviceInfoWrap<FourValue> fourValueDeviceInfoWrap, int position) {
        holder.setText(R.id.tv_title, fourValueDeviceInfoWrap.getData().getTitle());
        holder.setText(R.id.tv_subtitle, fourValueDeviceInfoWrap.getData().getSubTitle());
        holder.setText(R.id.tv_value, fourValueDeviceInfoWrap.getData().getValue());
        holder.setText(R.id.tv_subvalue, fourValueDeviceInfoWrap.getData().getSubValue());
    }
}
