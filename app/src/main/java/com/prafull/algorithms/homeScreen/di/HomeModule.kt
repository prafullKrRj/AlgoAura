package com.prafull.algorithms.homeScreen.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.prafull.algorithms.homeScreen.data.HomeRepoImpl
import com.prafull.algorithms.homeScreen.domain.HomeRepo
import com.prafull.algorithms.homeScreen.folder.FolderViewModel
import com.prafull.algorithms.homeScreen.home.AlgoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val homeModule = module {
    single<HomeRepo> {
        HomeRepoImpl(get(), get())
    }
    single<FirebaseStorage> {
        FirebaseStorage.getInstance()
    }
    single<FirebaseFirestore> {
        FirebaseFirestore.getInstance()
    }

    viewModel {
        AlgoViewModel()
    }

    viewModel {
        FolderViewModel(get())
    }

}
