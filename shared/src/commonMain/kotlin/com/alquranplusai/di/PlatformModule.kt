package com.alquranplusai.di

import org.koin.dsl.module

// Platform-specific module - implemented in androidMain and iosMain
expect val platformModule: org.koin.core.module.Module
