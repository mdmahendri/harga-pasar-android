package com.mahendri.pasbeli.injection;

import com.mahendri.pasbeli.viewmodel.PasBeli;

/**
 * @author Mahendri
 */

public class AppInjector {

    private AppInjector() {}

    public static void init(PasBeli pasBeli) {
        DaggerAppComponent.builder().application(pasBeli).build().inject(pasBeli);
    }
}
