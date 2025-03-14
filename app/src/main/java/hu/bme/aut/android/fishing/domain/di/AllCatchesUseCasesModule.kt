package hu.bme.aut.android.fishing.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.android.fishing.data.catches.CatchService
import hu.bme.aut.android.fishing.domain.usecases.catches.AddCatchUseCase
import hu.bme.aut.android.fishing.domain.usecases.catches.AllCatchesUseCases
import hu.bme.aut.android.fishing.domain.usecases.catches.CatchesUseCase
import hu.bme.aut.android.fishing.domain.usecases.catches.DeleteCatchUseCase
import hu.bme.aut.android.fishing.domain.usecases.catches.GetCatchByIdUseCase
import hu.bme.aut.android.fishing.domain.usecases.catches.GetCatchesByNameUseCase
import hu.bme.aut.android.fishing.domain.usecases.catches.UpdateCatchUseCase
import hu.bme.aut.android.fishing.domain.usecases.catches.UploadImageUseCase
import hu.bme.aut.android.fishing.domain.usecases.catches.UserCatchesUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AllCatchesUseCasesModule {
    @Provides
    @Singleton
    fun provideCatchesUseCase(repository: CatchService): CatchesUseCase =
        CatchesUseCase(repository)

    @Provides
    @Singleton
    fun provideUserCatchesUseCase(repository: CatchService): UserCatchesUseCase =
        UserCatchesUseCase(repository)

    @Provides
    @Singleton
    fun provideGetCatchesByNameUseCase(repository: CatchService): GetCatchesByNameUseCase =
        GetCatchesByNameUseCase(repository)

    @Provides
    @Singleton
    fun provideGetCatchByIdUseCase(repository: CatchService): GetCatchByIdUseCase =
        GetCatchByIdUseCase(repository)

    @Provides
    @Singleton
    fun provideAddCatchUseCase(repository: CatchService): AddCatchUseCase =
        AddCatchUseCase(repository)

    @Provides
    @Singleton
    fun provideUpdateCatchUseCase(repository: CatchService): UpdateCatchUseCase =
        UpdateCatchUseCase(repository)

    @Provides
    @Singleton
    fun provideDeleteCatchUseCase(repository: CatchService): DeleteCatchUseCase =
        DeleteCatchUseCase(repository)

    @Provides
    @Singleton
    fun provideUploadImageUseCase(repository: CatchService): UploadImageUseCase =
        UploadImageUseCase(repository)


    @Provides
    @Singleton
    fun provideAllCatchesUseCases(
        repository: CatchService,
        catches: CatchesUseCase,
        userCatches: UserCatchesUseCase,
        getCatchesByName: GetCatchesByNameUseCase,
        getCatchById: GetCatchByIdUseCase,
        addCatch: AddCatchUseCase,
        updateCatch: UpdateCatchUseCase,
        deleteCatch: DeleteCatchUseCase,
        uploadImage: UploadImageUseCase
    ): AllCatchesUseCases = AllCatchesUseCases(
        repository,
        catches,
        userCatches,
        getCatchesByName,
        getCatchById,
        addCatch,
        updateCatch,
        deleteCatch,
        uploadImage
    )
}