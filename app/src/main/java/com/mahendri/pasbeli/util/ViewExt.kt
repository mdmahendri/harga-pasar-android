package com.mahendri.pasbeli.util

import android.databinding.BindingAdapter
import com.google.android.gms.common.SignInButton
import com.mahendri.pasbeli.ui.splash.SplashViewModel

/**
 * @author Mahendri
 * extension dari view pada android
 */

@BindingAdapter("onClick")
fun SignInButton.setOnClick(viewmodel: SplashViewModel) {
    setOnClickListener { viewmodel.onSignInClick() }
}