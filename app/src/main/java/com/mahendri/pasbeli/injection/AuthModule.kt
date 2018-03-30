package com.mahendri.pasbeli.injection

import android.app.Application
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Module
import dagger.Provides

/**
 * @author Mahendri
 */

@Module
class AuthModule {

    @Authenticate
    @Provides
    internal fun provideSignInOptions(): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
    }

    @Authenticate
    @Provides
    internal fun provideSignInClient(app: Application, options: GoogleSignInOptions)
            : GoogleSignInClient {
        return GoogleSignIn.getClient(app, options)
    }

}
