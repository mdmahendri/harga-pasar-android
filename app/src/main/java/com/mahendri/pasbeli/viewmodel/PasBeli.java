package com.mahendri.pasbeli.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.app.Service;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.mahendri.pasbeli.BuildConfig;
import com.mahendri.pasbeli.injection.AppInjector;
import com.mahendri.pasbeli.repository.SyncJobService;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasServiceInjector;
import timber.log.Timber;

/**
 * @author Mahendri
 */

public class PasBeli extends Application implements HasActivityInjector, HasServiceInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingActivityInjector;
    @Inject
    DispatchingAndroidInjector<Service> dispatchingServiceInjector;
    @Inject
    FirebaseJobDispatcher dispatcher;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        AppInjector.init(this);

        Job syncJob = syncJobBuild();
        dispatcher.mustSchedule(syncJob);
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingActivityInjector;
    }

    @Override
    public AndroidInjector<Service> serviceInjector() {
        return dispatchingServiceInjector;
    }

    private Job syncJobBuild() {
        final int periodicity = (int) TimeUnit.HOURS.toSeconds(3);
        final int tolerance = (int) TimeUnit.MINUTES.toSeconds(30);

        return dispatcher.newJobBuilder()
                .setService(SyncJobService.class)
                .setTag("sync-job")
                .setRecurring(true)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(periodicity, periodicity + tolerance))
                .setReplaceCurrent(false)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .build();
    }
}
