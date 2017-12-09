package cn.ifreedomer.com.softmanager.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.R;

/**
 * @author:eavawu
 * @since: 09/12/2017.
 * TODO:
 */

public class ContentDialog extends AlertDialog implements View.OnClickListener {
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tv_des)
    TextView tvDes;
    @InjectView(R.id.btn_ok)
    Button btnOk;

    public ContentDialog(@NonNull Context context) {
        super(context);
    }


    @Override
    public void onClick(View v) {
        dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_content);
        ButterKnife.inject(this);
        btnOk.setOnClickListener(this);
    }

    public void setData(String title, String content) {
        tvTitle.setText(title);
        tvDes.setText(content);
    }


}
