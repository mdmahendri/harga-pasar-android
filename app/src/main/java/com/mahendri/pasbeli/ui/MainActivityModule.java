package com.mahendri.pasbeli.ui;

import android.content.Context;
import android.widget.ProgressBar;

import dagger.Module;
import dagger.Provides;

/**
 * @author Mahendri
 */

@Module
public class MainActivityModule {

    @Provides
    ProgressBar provideProgressBar (Context context) {
        return new ProgressBar(context, null, android.R.attr.progressBarStyle);
    }

}
