package com.gipl.notifyme.exceptions;

import android.content.Context;
import android.text.TextUtils;


import com.gipl.notifyme.R;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;


/**
 * Factory used to create error messages from an Exception as a condition.
 */
public class ErrorMessageFactory {

    private ErrorMessageFactory() {
        //empty
    }

    public static String create(Context context, Exception exception) {
        String message = "";
        if (exception.getCause() instanceof ConnectException
                || exception.getCause() instanceof TimeoutException
                || exception.getCause() instanceof SocketTimeoutException
        ) {
            message = context.getString(R.string.msg_no_internet_connectivity);
        } else if (exception.getCause() instanceof CustomException) {
            message = exception.getCause().getMessage();
        } else {
            if (exception.getCause() != null)
                message = exception.getCause().getMessage();
        }


        if (exception.getCause() instanceof UnknownHostException) {
            message = context.getString(R.string.unkonw_host_exception);
        }

        if (TextUtils.isEmpty(message)) {

            message = context.getString(R.string.exception_not_found);
        }

        return message;
    }
}