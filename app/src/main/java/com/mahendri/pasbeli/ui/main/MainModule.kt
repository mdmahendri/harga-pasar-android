package com.mahendri.pasbeli.ui.main

import android.widget.ProgressBar

import dagger.Module
import dagger.Provides

/**
 * @author Mahendri
 */

@Module
class MainModule {

    @Provides
    internal fun provideProgressBar(mainActivity: MainActivity): ProgressBar {
        return ProgressBar(mainActivity, null, android.R.attr.progressBarStyle)
    }

}
