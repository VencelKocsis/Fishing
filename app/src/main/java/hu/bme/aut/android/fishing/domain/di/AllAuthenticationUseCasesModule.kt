package hu.bme.aut.android.fishing.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.android.fishing.data.auth.AuthService
import hu.bme.aut.android.fishing.domain.usecases.auth.AllAuthenticationUseCases
import hu.bme.aut.android.fishing.domain.usecases.auth.CreateUserProfileUseCase
import hu.bme.aut.android.fishing.domain.usecases.auth.CurrentUserIdUseCase
import hu.bme.aut.android.fishing.domain.usecases.auth.CurrentUserUseCase
import hu.bme.aut.android.fishing.domain.usecases.auth.DeleteAccountUseCase
import hu.bme.aut.android.fishing.domain.usecases.auth.GetUserProfileUseCase
import hu.bme.aut.android.fishing.domain.usecases.auth.HasUserUseCase
import hu.bme.aut.android.fishing.domain.usecases.auth.IsEmailValidUseCase
import hu.bme.aut.android.fishing.domain.usecases.auth.PasswordsMatchUseCase
import hu.bme.aut.android.fishing.domain.usecases.auth.SendRecoveryEmailUseCase
import hu.bme.aut.android.fishing.domain.usecases.auth.SignInUseCase
import hu.bme.aut.android.fishing.domain.usecases.auth.SignOutUseCase
import hu.bme.aut.android.fishing.domain.usecases.auth.SignUpUseCase
import hu.bme.aut.android.fishing.domain.usecases.auth.UpdateUserProfileUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AllAuthenticationUseCasesModule {

    @Provides
    @Singleton
    fun provideCurrentUserIdUseCase(repository: AuthService): CurrentUserIdUseCase =
        CurrentUserIdUseCase(repository)

    @Provides
    @Singleton
    fun provideCurrentUserUseCase(repository: AuthService): CurrentUserUseCase =
        CurrentUserUseCase(repository)

    @Provides
    @Singleton
    fun provideHasUserUseCase(repository: AuthService): HasUserUseCase =
        HasUserUseCase(repository)

    @Provides
    @Singleton
    fun provideSignUpUseCase(repository: AuthService): SignUpUseCase =
        SignUpUseCase(repository)

    @Provides
    @Singleton
    fun provideLoginUseCase(repository: AuthService): SignInUseCase =
        SignInUseCase(repository)

    @Provides
    @Singleton
    fun provideSendRecoveryEmailUseCase(repository: AuthService):
            SendRecoveryEmailUseCase =
        SendRecoveryEmailUseCase(repository)

    @Provides
    @Singleton
    fun provideDeleteAccountUseCase(repository: AuthService): DeleteAccountUseCase =
        DeleteAccountUseCase(repository)

    @Provides
    @Singleton
    fun provideSignOutUseCase(repository: AuthService): SignOutUseCase =
        SignOutUseCase(repository)

    @Provides
    @Singleton
    fun provideIsEmailValidUseCase(): IsEmailValidUseCase = IsEmailValidUseCase()

    @Provides
    @Singleton
    fun providePasswordsMatchUseCase(): PasswordsMatchUseCase = PasswordsMatchUseCase()

    @Provides
    @Singleton
    fun provideGetUserProfileUseCase(repository: AuthService): GetUserProfileUseCase =
        GetUserProfileUseCase(repository)

    @Provides
    @Singleton
    fun provideUpdateUserProfileUseCase(repository: AuthService): UpdateUserProfileUseCase =
        UpdateUserProfileUseCase(repository)

    @Provides
    @Singleton
    fun provideCreateUserProfileUseCase(repository: AuthService): CreateUserProfileUseCase =
        CreateUserProfileUseCase(repository)

    @Provides
    @Singleton
    fun provideAuthenticationUseCases(
        repository: AuthService,
        currentUserIdUseCase: CurrentUserIdUseCase,
        currentUserUseCase: CurrentUserUseCase,
        hasUserUseCase: HasUserUseCase,
        signUpUseCase: SignUpUseCase,
        signInUseCase: SignInUseCase,
        sendRecoveryEmailUseCase: SendRecoveryEmailUseCase,
        deleteAccountUseCase: DeleteAccountUseCase,
        signOutUseCase: SignOutUseCase,
        isEmailValidUseCase: IsEmailValidUseCase,
        passwordsMatchUseCase: PasswordsMatchUseCase,
        getUserProfileUseCase: GetUserProfileUseCase,
        updateUserProfileUseCase: UpdateUserProfileUseCase,
        createUserProfileUseCase: CreateUserProfileUseCase
    ): AllAuthenticationUseCases = AllAuthenticationUseCases(
        repository,
        currentUserIdUseCase,
        currentUserUseCase,
        hasUserUseCase,
        signUpUseCase,
        signInUseCase,
        sendRecoveryEmailUseCase,
        deleteAccountUseCase,
        signOutUseCase,
        isEmailValidUseCase,
        passwordsMatchUseCase,
        getUserProfileUseCase,
        updateUserProfileUseCase,
        createUserProfileUseCase
    )
}
