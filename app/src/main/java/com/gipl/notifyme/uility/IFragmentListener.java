package com.gipl.notifyme.uility;

import android.os.Bundle;
import android.os.Parcelable;

public interface IFragmentListener extends Parcelable {
    void onActivityResult(Bundle bundle);
}
