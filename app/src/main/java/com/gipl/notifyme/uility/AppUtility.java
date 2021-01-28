package com.gipl.notifyme.uility;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.gipl.notifyme.R;
import com.google.android.material.snackbar.Snackbar;

public class AppUtility {
    public static class INTENT_EXTRA {
        public static final String KEY_USER = "keyUserObj";
    }

    public static class LINK_TYPE {
        public static final String VIDEO = "Video";
        public static final String IMAGE = "Image";
        public static final String PDF = "Pdf";
    }

    public static void openPdf(Uri uri, Context context, View view){
        Intent intent = new Intent();
        intent.setDataAndType(uri, "application/pdf");
        // check if there is any app that can open pdf
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException activityNotFound) {
            // Tell user that the need to install a pdf viewer
            Snackbar mySnackbar = Snackbar.make(view, context.getString(R.string.error_pdf_viewer_not_installed), Snackbar.LENGTH_INDEFINITE);
            // Show snackbar
            mySnackbar.setAction(context.getString(R.string.btn_ok), v -> {
                // Redirect to Play Store
                Intent playIntent = new Intent(Intent.ACTION_VIEW);
                playIntent.setData(Uri.parse(
                        "http://play.google.com/store/search?q=pdfviewer&c=apps"));
                playIntent.setPackage("com.android.vending");
                context.startActivity(playIntent);
            });
            mySnackbar.show();
        }
    }
}
