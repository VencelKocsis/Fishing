package hu.bme.aut.android.fishing.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.android.fishing.data.catches.CatchService
import hu.bme.aut.android.fishing.data.catches.firebase.FirebaseCatchService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CatchesServiceModule {

    @Binds
    @Singleton
    abstract fun bindFirebaseCatchesService(
        firebaseCatchService: FirebaseCatchService
    ): CatchService
}