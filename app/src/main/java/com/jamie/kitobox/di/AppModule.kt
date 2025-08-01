package com.jamie.kitobox.di

import android.content.Context
import androidx.room.Room
import com.jamie.kitobox.MainViewModel
import com.jamie.kitobox.data.local.AppDatabase
import com.jamie.kitobox.data.repository.AnnotationRepository
import com.jamie.kitobox.data.repository.BookRepository
import com.jamie.kitobox.data.repository.UserPreferencesRepository
import com.jamie.kitobox.features.library.LibraryViewModel
import com.jamie.kitobox.features.notes.NotesSummaryViewModel
import com.jamie.kitobox.features.onboarding.OnboardingViewModel
import com.jamie.kitobox.features.settings.SettingsViewModel
import com.jamie.kitobox.features.reader.ReaderViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        androidApplication().getSharedPreferences("kitobox_prefs", Context.MODE_PRIVATE)
    }

    single {
        Room.databaseBuilder(
                androidApplication(),
                AppDatabase::class.java,
                "kitobox-db"
            )
            .fallbackToDestructiveMigration(false)
            .build()
    }

    single { get<AppDatabase>().bookDao() }
    single { get<AppDatabase>().annotationDao() }

    viewModel { MainViewModel(prefs = get(), userPreferencesRepository = get()) }

    viewModel { OnboardingViewModel(prefs = get()) }


    single { BookRepository(bookDao = get()) }
    single { UserPreferencesRepository(prefs = get()) }
    single { AnnotationRepository(annotationDao = get()) }

    viewModel { SettingsViewModel(userPreferencesRepository = get()) }
    viewModel { LibraryViewModel(bookRepository = get()) }
    viewModel { params ->
        ReaderViewModel(bookRepository = get(), annotationRepository = get(), savedStateHandle = params.get())
    }
    viewModel { params ->
        NotesSummaryViewModel(annotationRepository = get(), savedStateHandle = params.get())
    }
}