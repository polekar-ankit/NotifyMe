package com.gipl.swayam.uility;

import android.os.Bundle;
import android.os.Parcelable;

/**
 * this interface is use to send result back to fragment from fragment
 */
public interface IFragmentListener extends Parcelable {
    void onActivityResult(Bundle bundle);
}
