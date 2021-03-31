package com.gipl.swayam.ui.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gipl.swayam.R;


import static com.gipl.swayam.ui.model.Status.ERROR;
import static com.gipl.swayam.ui.model.Status.LOADING;
import static com.gipl.swayam.ui.model.Status.SUCCESS;


/**
 * Creted by User on 24-Sep-18
 */
public class Response {

    public final Status status;

    @Nullable
    public final Object data;

    @Nullable
    public final Throwable error;


    private Response(Status status, @Nullable Object data, @Nullable Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static Response loading(int... progressMsgId) {
        if (progressMsgId.length > 0) return new Response(LOADING, progressMsgId[0], null);
        else return new Response(LOADING, R.string.msg_default_wait, null);
    }


    public static Response success(@NonNull Object data) {
        return new Response(SUCCESS, data, null);
    }

    public static Response error(@NonNull Throwable error) {
        return new Response(ERROR, null, error);
    }


}