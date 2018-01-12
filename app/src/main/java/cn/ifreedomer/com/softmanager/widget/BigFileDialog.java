package cn.ifreedomer.com.softmanager.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.util.DataTypeUtil;

/**
 * @author:eavawu
 * @since: 09/12/2017.
 * TODO:
 */

public class BigFileDialog extends AlertDialog implements View.OnClickListener {
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.et_content)
    EditText etDes;
    @InjectView(R.id.btn_ok)
    Button btnOk;
    private OnEditContent onEditContent;

    public BigFileDialog(@NonNull Context context) {
        super(context);
        if (context instanceof Activity) {
            setOwnerActivity((Activity) context);
        }
    }


    public EditText getEt(){
        return etDes;
    }


    @Override
    public void onClick(View v) {
        String content = etDes.getText().toString();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(getContext(), R.string.cannot_null, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!DataTypeUtil.isInteger(content)) {
            Toast.makeText(getContext(), R.string.not_number, Toast.LENGTH_SHORT).show();
            return;
        }
        if (onEditContent != null) {
            onEditContent.onContent(content);
        }
        dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bigfile);
        ButterKnife.inject(this);
        btnOk.setOnClickListener(this);
    }

    public void setData(int minSze) {
        tvTitle.setText(R.string.big_file_maxsize);
        etDes.setText((minSze)+"");
    }

//    /**
//     * EditText获取焦点并显示软键盘
//     */
//    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
//        editText.setFocusable(true);
//        editText.setFocusableInTouchMode(true);
//        editText.requestFocus();
//        if (activity!=null){
//            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//        }
//    }

    public OnEditContent getOnEditContent() {
        return onEditContent;
    }

    public void setOnEditContent(OnEditContent onEditContent) {
        this.onEditContent = onEditContent;
    }

    public interface OnEditContent {
        void onContent(String content);
    }

}
