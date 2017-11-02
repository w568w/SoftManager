package cn.ifreedomer.com.softmanager.adapter;

import android.content.Context;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import cn.ifreedomer.com.softmanager.bean.CleanCardInfo;

/**
 * @author:eavawu
 * @since: 26/10/2017.
 * TODO: clean界面的四个卡片
 */

public class CleanCardAdapter  extends CommonAdapter<CleanCardInfo> {
    public CleanCardAdapter(Context context, int layoutId, List<CleanCardInfo> datas) {
        super(context, layoutId, datas);
    }


    @Override
    protected void convert(ViewHolder holder, CleanCardInfo cleanCardInfo, int position) {

    }
}
