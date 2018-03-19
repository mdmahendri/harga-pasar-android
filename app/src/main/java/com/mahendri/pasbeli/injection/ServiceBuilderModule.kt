package com.mahendri.pasbeli.injection

import com.mahendri.pasbeli.repository.SyncJobService
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * @author Mahendri
 */

@Module
abstract class ServiceBuilderModule {

    @ContributesAndroidInjector
    abstract fun bindSyncJobService(): SyncJobService

}