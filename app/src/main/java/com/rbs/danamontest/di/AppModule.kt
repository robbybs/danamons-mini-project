package com.rbs.danamontest.di

import com.rbs.danamontest.domain.usecase.HomeInteractor
import com.rbs.danamontest.domain.usecase.HomeUseCase
import com.rbs.danamontest.domain.usecase.UserInteractor
import com.rbs.danamontest.domain.usecase.UserUseCase
import com.rbs.danamontest.presentation.ui.home.HomeViewModel
import com.rbs.danamontest.presentation.ui.main.MainViewModel
import com.rbs.danamontest.presentation.ui.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val useCaseModule = module {
    factory<UserUseCase> { UserInteractor(get()) }
    factory<HomeUseCase> { HomeInteractor(get()) }
}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { HomeViewModel(get()) }
}