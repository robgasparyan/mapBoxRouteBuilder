package com.mapbox.mapboxroutebuilder.DI

import com.mapbox.mapboxroutebuilder.ViewModels.BoxViewModel
import org.koin.androidx.experimental.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val viewModels: Module = module {
    viewModel<BoxViewModel>()
}
