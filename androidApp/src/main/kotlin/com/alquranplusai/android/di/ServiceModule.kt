package com.alquranplusai.android.di

import com.alquranplusai.android.services.*
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val serviceModule = module {
    
    // Services are typically started by Android system via Intent
    // We don't need to provide them in DI unless they have injectable dependencies
    
    // If services need dependencies, they can use Koin's androidContext() directly
    // Example: class MyService : Service() {
    //     private val repository: Repository by inject()
    // }
}
