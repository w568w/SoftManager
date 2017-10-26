package cn.ifreedomer.com.softmanager.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.bean.CleanCardInfo;


public class ItemCardView extends RelativeLayout {

    private ImageView cardImage;
    private TextView cardName;

    private Context mContext;

    public ItemCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        mContext = context;


    }

    public ItemCardView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        View view = View.inflate(context, R.layout.item_card_view, this);
        cardImage = (ImageView) view.findViewById(R.id.card_image);
        cardImage.setImageResource(R.mipmap.clean);
        cardName = (TextView) view.findViewById(R.id.card_name);
        cardName.setText("QQ专清");
    }


    public void setData(CleanCardInfo cleanCardInfo) {
        cardImage.setImageResource(cleanCardInfo.getIcon());
        cardName.setText(cleanCardInfo.getTitle());
    }
}
