package com.mahendri.pasbeli.ui.splash

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.mahendri.pasbeli.R
import com.mahendri.pasbeli.databinding.ActivitySplashBinding
import com.mahendri.pasbeli.ui.main.MainActivity
import dagger.android.AndroidInjection
import timber.log.Timber
import javax.inject.Inject

/**
 * @author Mahendri
 */
class SplashActivity : AppCompatActivity() {

    @Inject lateinit var signInClient: GoogleSignInClient
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        val binding: ActivitySplashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        splashViewModel = ViewModelProviders.of(this, viewModelFactory).get(SplashViewModel::class.java)
        binding.viewmodel = splashViewModel

        initSubscribe()
    }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        updateUI(account)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SIGN_IN_REQUEST) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            updateUI(account)
        } catch (e: ApiException) {
            Timber.e("Sign in fail code %s", e.statusCode)
            updateUI(null)
        }
    }

    private fun initSubscribe() {
        splashViewModel.goSignIn.observe(this, Observer {toAuth ->
            if (toAuth == true) {
                val signIn = signInClient.signInIntent
                startActivityForResult(signIn, SIGN_IN_REQUEST)
                splashViewModel.goSignIn.postValue(false)
            }
        })

        splashViewModel.goMain.observe(this, Observer {
            if (it != null && it.length > 0) {
                Timber.i("menuju menu utama")
                splashViewModel.goMain.postValue(null)
                val toMain = Intent(this, MainActivity::class.java)
                toMain.putExtra(EXTRA_MAIL, it)
                startActivity(toMain)
                finish()
            }
        })
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        splashViewModel.updateViewState(account)
    }

    companion object {
        const val EXTRA_MAIL = "MAIL_USER"
        private const val SIGN_IN_REQUEST = 1
    }
}