package com.marcoscg.movies.di

import com.marcoscg.movies.data.repository.MoviesLocalRepositoryImpl
import com.marcoscg.movies.data.repository.MoviesRemoteRepositoryImpl
import com.marcoscg.movies.data.sources.local.mapper.MoviesLocalMapper
import com.marcoscg.movies.data.sources.remote.mapper.MoviesRemoteMapper
import com.marcoscg.movies.domain.interactor.*
import com.marcoscg.movies.domain.repository.MoviesLocalRepository
import com.marcoscg.movies.domain.repository.MoviesRemoteRepository
import com.marcoscg.movies.ui.details.viewmodel.MovieDetailsViewModel
import com.marcoscg.movies.ui.home.master.MovieListAdapter
import com.marcoscg.movies.ui.home.viewmodel.FavoriteViewModel
import com.marcoscg.movies.ui.home.viewmodel.PopularViewModel
import com.marcoscg.movies.ui.home.viewmodel.UpcomingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    single { MoviesRemoteMapper() }
    single { MoviesLocalMapper() }
    factory<MoviesRemoteRepository> { MoviesRemoteRepositoryImpl(get()) }
    factory<MoviesLocalRepository> { MoviesLocalRepositoryImpl(androidContext(), get()) }
    factory { MovieListAdapter(androidContext()) }
}

val popularMoviesModule = module {
    factory { GetPopularMoviesUseCase(get()) }
    viewModel { PopularViewModel(get()) }
}

val upcomingMoviesModule = module {
    factory { GetUpcomingMoviesUseCase(get()) }
    viewModel { UpcomingViewModel(get()) }
}

val favoriteMoviesModule = module {
    factory { GetFavoriteMoviesUseCase(get()) }
    viewModel { FavoriteViewModel(get()) }
}

val movieDetailsModule = module {
    factory { GetSingleMovieUseCase(get()) }
    factory { AddFavoriteMovieUseCase(get()) }
    factory { DeleteFavoriteMovieUseCase(get()) }
    factory { UpdateFavoriteMovieUseCase(get()) }
    factory { GetFavoriteMovieUseCase(get()) }
    viewModel { MovieDetailsViewModel(get(), get(), get(), get(), get()) }
}