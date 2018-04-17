package com.mahendri.pasbeli.util

import android.arch.lifecycle.LiveData
import android.databinding.BindingAdapter
import android.databinding.InverseBindingAdapter
import android.databinding.InverseBindingListener
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Spinner
import com.google.android.gms.common.SignInButton
import com.mahendri.pasbeli.R
import com.mahendri.pasbeli.entity.FetchStatus
import com.mahendri.pasbeli.entity.KualitasUnit
import com.mahendri.pasbeli.entity.Resource
import com.mahendri.pasbeli.ui.addharga.KualitasAdapter
import com.mahendri.pasbeli.ui.splash.SplashViewModel
import timber.log.Timber

/**
 * @author Mahendri
 * extension dari view pada android
 */

@BindingAdapter("onClick")
fun setOnClick(button: SignInButton, viewmodel: SplashViewModel) {
    button.setOnClickListener { viewmodel.onSignInClick() }
}

@BindingAdapter("list")
fun setList(textView: AutoCompleteTextView, list: LiveData<Resource<List<String>>>) {
    val resource = list.value
    if (resource?.fetchStatus == FetchStatus.SUCCESS) {
        resource.data?.apply {
            textView.setAdapter<ArrayAdapter<String>>(
                    ArrayAdapter(textView.context, android.R.layout.simple_dropdown_item_1line, this)
            )
            textView.validator = AutoValidator(this)
            textView.setOnFocusChangeListener { view, focus ->
                if (!focus && view.id == textView.id) textView.performValidation()
            }

        }
    }
}

@BindingAdapter("entry")
fun setEntries(spinner: Spinner, data: LiveData<List<KualitasUnit>>) {
    val res = data.value
    val isEnable = res != null && !res.isEmpty()
    Timber.i("kondisi list spinner: %s", isEnable)
    spinner.apply {
        isEnabled = isEnable
        adapter = if (isEnable) KualitasAdapter(spinner.context, R.layout.item_spinner, res) else null
    }
}