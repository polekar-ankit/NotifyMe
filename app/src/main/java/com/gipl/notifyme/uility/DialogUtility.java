package com.gipl.notifyme.uility;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gipl.notifyme.R;
import com.gipl.notifyme.exceptions.ErrorMessageFactory;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


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

        progressDialog.show();

//        if (progressDialog.getWindow() != null) {
//            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        }
        progressDialog.setContentView(R.layout.dialog_progress);
        TextView textView = progressDialog.findViewById(R.id.progress_text);
        if (messageId.length > 0)
            textView.setText(context.getString(messageId[0]));
        else
            textView.setText(context.getString(R.string.msg_default_wait));

        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        return progressDialog;
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    public static Snackbar showSnackbar(View view,String msg){
        Snackbar mySnackbar = Snackbar.make(view,
                msg,
                Snackbar.LENGTH_INDEFINITE);
        mySnackbar.setAction(view.getContext().getString(R.string.btn_ok), v -> mySnackbar.dismiss());
        mySnackbar.show();
        return mySnackbar;
    }

    public static DatePickerDialog getDatePickerDialog(Context context,
                                                       String sDefaultDate,
                                                       long nMaxDate,
                                                       long nMinDate,
                                                       DatePickerDialog.OnDateSetListener dateListener) {
        Date date;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(TimeUtility.ONLY_DATE_FORMAT, Locale.US);
            date = sdf.parse(sDefaultDate.isEmpty() ? TimeUtility.getTodayOnlyDateInDisplayFormat() : sDefaultDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            DatePickerDialog datePickerDialog = new DatePickerDialog(context, dateListener, calendar.get(Calendar.YEAR)
                    , calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

            if (nMaxDate != 0)
                datePickerDialog.getDatePicker().setMaxDate(nMaxDate);
            if (nMinDate != 0)
                datePickerDialog.getDatePicker().setMinDate(nMinDate);


//            datePickerDialog.show();
            return datePickerDialog;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
