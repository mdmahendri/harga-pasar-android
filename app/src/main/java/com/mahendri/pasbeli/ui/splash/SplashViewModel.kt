package com.mahendri.pasbeli.ui.splash

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.mahendri.pasbeli.repository.UserRepository
import javax.inject.Inject

/**
 * @author Mahendri
 */

class SplashViewModel @Inject constructor (
    private val userRepo: UserRepository
) : ViewModel() {

    val isLogin = ObservableBoolean(true)

    internal val goSignIn = MutableLiveData<Boolean>()
    internal val goMain = MutableLiveData<String>()

    internal fun updateViewState(account: GoogleSignInAccount?) {
        if (account != null) {
            userRepo.saveMail(account.email).subscribe({
                goMain.postValue(account.email)
            })
        } else {
            isLogin.set(false)
        }
    }

    fun onSignInClick() {
        goSignIn.postValue(true)
    }
}