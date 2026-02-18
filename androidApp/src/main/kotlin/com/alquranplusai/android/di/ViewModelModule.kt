package com.alquranplusai.android.di

import com.alquranplusai.android.ui.viewmodels.DataLoadingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    // ViewModels registered in AndroidAppModule
    
    // Data Loading ViewModel - for initial Quran data loading
    viewModel { DataLoadingViewModel(get()) }
}
