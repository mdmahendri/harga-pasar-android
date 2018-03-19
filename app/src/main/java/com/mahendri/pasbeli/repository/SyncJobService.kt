package com.mahendri.pasbeli.repository

import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService
import com.firebase.jobdispatcher.SimpleJobService
import dagger.android.AndroidInjection
import javax.inject.Inject

/**
 * @author Mahendri
 */
class SyncJobService : SimpleJobService() {

    @Inject
    lateinit var mapRepository: MapRepository

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onRunJob(job: JobParameters?): Int {
        val success = mapRepository.syncListPasar()
        return if (success) JobService.RESULT_SUCCESS else JobService.RESULT_FAIL_NORETRY;
    }

}