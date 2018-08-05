package cn.ifreedomer.com.softmanager.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

import cn.ifreedomer.com.softmanager.R;


/**
 * @author eavawu
 * @since 06/05/2018.
 */

public class DialogUtil {
    public static void showConfirmDialog(Activity activity, String title, String msg, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(activity).setTitle(title).setMessage(msg).setNegativeButton(R.string.confirm, onClickListener).setNeutralButton(R.string.cancel, null).show();
    }

    public static void showEditDialog(String title, Context context, DialogContentCallback dialogContentCallback) {
        final EditText edit = new EditText(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);    //设置对话框标题
        builder.setView(edit);
        builder.setPositiveButton(R.string.confirm, (dialog, which) -> {
            if (dialogContentCallback != null) {
                dialogContentCallback.onEditContent(edit.getText().toString());
            }
        });
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
        });
        builder.show();
    }


    public static void showDialog(String title,String content, Context context, DialogCallback dialogContentCallback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);    //设置对话框标题
        builder.setMessage(content);
        builder.setPositiveButton(R.string.confirm, (dialog, which) -> {
            if (dialogContentCallback != null) {
                dialogContentCallback.onConfirm();
            }
        });
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
            if (dialogContentCallback != null) {
                dialogContentCallback.onCancel();
            }

        });
        builder.show();
    }





    public interface DialogContentCallback {
        void onEditContent(String content);
    }

    public interface DialogCallback{
        void onConfirm();
        void onCancel();
    }
}
