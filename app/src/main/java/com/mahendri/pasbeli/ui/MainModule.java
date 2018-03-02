package com.mahendri.pasbeli.ui;

import android.widget.ProgressBar;

import dagger.Module;
import dagger.Provides;

/**
 * @author Mahendri
 */

@Module
public class MainModule {

    @Provides
    ProgressBar provideProgressBar (MainActivity mainActivity) {
        return new ProgressBar(mainActivity, null, android.R.attr.progressBarStyle);
    }

}
