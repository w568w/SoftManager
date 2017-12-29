package cn.ifreedomer.com.softmanager.adapter;

import android.content.Context;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.bean.SettingInfo;

/**
 * @author eavawu
 * @since 27/12/2017.
 */

public class OperationAdapter extends CommonAdapter<SettingInfo> {
    public OperationAdapter(Context context, int layoutId, List<SettingInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, SettingInfo settingInfo, int position) {
        holder.setText(R.id.tv_name, settingInfo.getTitle());

    }
}
