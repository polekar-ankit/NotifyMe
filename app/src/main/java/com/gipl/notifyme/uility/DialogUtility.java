package com.gipl.notifyme.uility;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gipl.notifyme.R;
import com.google.android.material.snackbar.Snackbar;


/**
 * This class manages application dialogs.
 */
public class DialogUtility {

    /**
     * This method returns a simple dialog.
     *
     * @param context
     * @return
     */
    public static Dialog getDialog(Context context, View view) {
        Dialog dialog = new Dialog(context, android.R.style.Theme_Holo_Dialog_NoActionBar);
        dialog.getWindow();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
//        dialog.setContentView(nLayoutId);
        dialog.setContentView(view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        if (!((AppCompatActivity) context).isFinishing())
            dialog.show();

        return dialog;
    }



    public static ProgressDialog showLoadingDialog(Context context, int... messageId) {

        ProgressDialog progressDialog = new ProgressDialog(context);
        if (messageId.length > 0)
            progressDialog.setMessage(context.getString(messageId[0]));
        else
            progressDialog.setMessage(context.getString(R.string.msg_default_wait));
        progressDialog.show();

//        if (progressDialog.getWindow() != null) {
//            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        }
//        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        return progressDialog;
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
