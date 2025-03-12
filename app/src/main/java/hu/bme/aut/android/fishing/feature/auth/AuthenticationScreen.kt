/*package hu.bme.aut.android.fishing.feature.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.bme.aut.android.fishing.R
import hu.bme.aut.android.fishing.ui.common.EmailTextField
import hu.bme.aut.android.fishing.ui.common.PasswordTextField
import hu.bme.aut.android.fishing.ui.model.toUiText
import hu.bme.aut.android.fishing.util.UiEvent
import kotlinx.coroutines.launch

@Composable
fun AuthenticationScreen(
    viewModel: AuthenticationViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = SnackbarHostState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.checkSignInState()
        viewModel.loadUserProfile()
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Success -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = event.message?.asString(context) ?: ""
                        )
                    }
                }

                is UiEvent.Failure -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = event.message.asString(context)
                        )
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                if (state.isLoadingUserProfile) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center)
                    )
                } else if(state.isError) {
                    Text(
                        text = state.error?.toUiText()?.asString(context) ?: stringResource(id = R.string.some_error_message),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    Column {
                        if (state.isLoggedIn) {
                            Column(
                                modifier = Modifier
                                    .padding(bottom = 8.dp)
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                IconButton( // Edit button
                                    onClick = {
                                        viewModel.onEvent(AuthenticationEvent.ProfileEditButtonClicked)
                                    },
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Profile name textfield
                                EmailTextField(
                                    value = state.user?.name.orEmpty(),
                                    label = stringResource(id = R.string.user_name),
                                    onValueChange = { newName -> viewModel.onEvent(AuthenticationEvent.UserNameChanged(newName)) },
                                    onDone = {},
                                    imeAction = ImeAction.Done,
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = null
                                        )
                                    },
                                    enabled = state.isInEditUserProfileMode,
                                    modifier = Modifier
                                        .padding(bottom = 8.dp)
                                        .width(TextFieldDefaults.MinWidth)
                                        .height(64.dp)
                                )

                                // Profile email textfield
                                EmailTextField(
                                    value = state.user?.email.orEmpty(),
                                    label = stringResource(id = R.string.textfield_label_email),
                                    onValueChange = {},
                                    onDone = {},
                                    imeAction = ImeAction.Next,
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Email,
                                            contentDescription = null
                                        )
                                    },
                                    enabled = false,
                                    modifier = Modifier
                                        .padding(bottom = 8.dp)
                                        .width(TextFieldDefaults.MinWidth)
                                        .height(64.dp)
                                )

                                // birthdate datepicker


                                Button( // Logout button
                                    shape = RoundedCornerShape(8.dp),
                                    onClick = { viewModel.onEvent(AuthenticationEvent.LogoutButtonClicked) },
                                    modifier = Modifier
                                        .padding(bottom = 8.dp)
                                        .width(TextFieldDefaults.MinWidth)
                                        .height(64.dp)
                                ) {
                                    Text(text = stringResource(R.string.button_text_sign_out))
                                }
                            }

                        } else {
                            EmailTextField(
                                value = state.email,
                                label = stringResource(id = R.string.textfield_label_email),
                                onValueChange = { viewModel.onEvent(AuthenticationEvent.EmailChanged(it)) },
                                onDone = {},
                                imeAction = ImeAction.Next,
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Email,
                                        contentDescription = null
                                    )
                                },
                                modifier = Modifier
                                    .padding(bottom = 8.dp)
                                    .width(TextFieldDefaults.MinWidth)
                                    .height(64.dp)
                            )
                            PasswordTextField(
                                value = state.password,
                                label = stringResource(id = R.string.textfield_label_password),
                                onValueChange = { viewModel.onEvent(AuthenticationEvent.PasswordChanged(it)) },
                                onDone = {},
                                modifier = Modifier
                                    .padding(bottom = 8.dp)
                                    .width(TextFieldDefaults.MinWidth)
                                    .height(64.dp),
                                isVisible = state.passwordVisibility,
                                onVisibilityChanged = { viewModel.onEvent(AuthenticationEvent.PasswordVisibilityChanged) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Key,
                                        contentDescription = null
                                    )
                                }
                            )
                            Button(
                                onClick = { viewModel.onEvent(AuthenticationEvent.ForgotPasswordButtonClicked) },
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .width(TextFieldDefaults.MinWidth)
                                    .height(64.dp)
                            ) {
                                Text(text = stringResource(id = R.string.button_text_forgot_password))
                            }
                            AnimatedVisibility(visible = state.isInRegisterMode) {
                                PasswordTextField(
                                    value = state.confirmPassword,
                                    label = stringResource(id = R.string.textfield_label_confirm_password),
                                    onValueChange = {
                                        viewModel.onEvent(
                                            AuthenticationEvent.ConfirmPasswordChanged(
                                                it
                                            )
                                        )
                                    },
                                    onDone = {},
                                    modifier = Modifier
                                        .padding(bottom = 8.dp, top = 8.dp)
                                        .width(TextFieldDefaults.MinWidth)
                                        .height(64.dp),
                                    isVisible = state.confirmPasswordVisibility,
                                    onVisibilityChanged = { viewModel.onEvent(AuthenticationEvent.ConfirmPasswordVisibilityChanged) },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Key,
                                            contentDescription = null
                                        )
                                    }
                                )
                            }
                            AnimatedVisibility(visible = !state.isInRegisterMode) {

                                Spacer(
                                    modifier = Modifier
                                        .padding(bottom = 8.dp)
                                        .height(64.dp)
                                )
                            }
                            Spacer(
                                modifier = Modifier
                                    .height(24.dp)
                            )
                            Button(
                                shape = RoundedCornerShape(8.dp),
                                onClick = { viewModel.onEvent(AuthenticationEvent.AuthenticationButtonClicked) },
                                modifier = Modifier
                                    .padding(bottom = 8.dp)
                                    .width(TextFieldDefaults.MinWidth)
                                    .height(64.dp)
                            ) {
                                Text(
                                    text =
                                    if (state.isInRegisterMode)
                                        stringResource(id = R.string.button_text_sign_up)
                                    else
                                        stringResource(id = R.string.button_text_sign_in)
                                )
                            }
                            OutlinedButton(
                                shape = RoundedCornerShape(8.dp),
                                onClick = { viewModel.onEvent(AuthenticationEvent.ChangeMode) },
                                modifier = Modifier
                                    .padding(bottom = 8.dp)
                                    .width(TextFieldDefaults.MinWidth)
                                    .height(64.dp)
                            ) {
                                Text(
                                    text =
                                    if (state.isInRegisterMode)
                                        stringResource(id = R.string.button_text_back_to_login)
                                    else
                                        stringResource(id = R.string.button_text_no_account)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}*/

package hu.bme.aut.android.fishing.feature.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.bme.aut.android.fishing.R
import hu.bme.aut.android.fishing.ui.common.EmailTextField
import hu.bme.aut.android.fishing.ui.common.PasswordTextField
import hu.bme.aut.android.fishing.util.UiEvent
import kotlinx.coroutines.launch

@Composable
fun AuthenticationScreen(
    viewModel: AuthenticationViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = SnackbarHostState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.checkSignInState()
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Success -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = event.message?.asString(context) ?: ""
                        )
                    }
                }

                is UiEvent.Failure -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = event.message.asString(context)
                        )
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.isLoggedIn) {
                Button(
                    shape = RoundedCornerShape(8.dp),
                    onClick = { viewModel.onEvent(AuthenticationEvent.LogoutButtonClicked) },
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .width(TextFieldDefaults.MinWidth)
                        .height(64.dp)
                ) {
                    Text(text = stringResource(R.string.button_text_sign_out))
                }

            } else {
                EmailTextField(
                    value = state.email,
                    label = stringResource(id = R.string.textfield_label_email),
                    onValueChange = { viewModel.onEvent(AuthenticationEvent.EmailChanged(it)) },
                    onDone = {},
                    imeAction = ImeAction.Next,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .width(TextFieldDefaults.MinWidth)
                        .height(64.dp)
                )
                PasswordTextField(
                    value = state.password,
                    label = stringResource(id = R.string.textfield_label_password),
                    onValueChange = { viewModel.onEvent(AuthenticationEvent.PasswordChanged(it)) },
                    onDone = {},
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .width(TextFieldDefaults.MinWidth)
                        .height(64.dp),
                    isVisible = state.passwordVisibility,
                    onVisibilityChanged = { viewModel.onEvent(AuthenticationEvent.PasswordVisibilityChanged) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Key,
                            contentDescription = null
                        )
                    }
                )
                Button(
                    onClick = { viewModel.onEvent(AuthenticationEvent.ForgotPasswordButtonClicked) },
                    modifier = Modifier.padding(top = 8.dp)
                        .width(TextFieldDefaults.MinWidth)
                        .height(64.dp)
                ) {
                    Text(text = stringResource(id = R.string.button_text_forgot_password))
                }
                AnimatedVisibility(visible = state.isInRegisterMode) {
                    PasswordTextField(
                        value = state.confirmPassword,
                        label = stringResource(id = R.string.textfield_label_confirm_password),
                        onValueChange = {
                            viewModel.onEvent(
                                AuthenticationEvent.ConfirmPasswordChanged(
                                    it
                                )
                            )
                        },
                        onDone = {},
                        modifier = Modifier
                            .padding(bottom = 8.dp, top = 8.dp)
                            .width(TextFieldDefaults.MinWidth)
                            .height(64.dp),
                        isVisible = state.confirmPasswordVisibility,
                        onVisibilityChanged = { viewModel.onEvent(AuthenticationEvent.ConfirmPasswordVisibilityChanged) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Key,
                                contentDescription = null
                            )
                        }
                    )
                }
                AnimatedVisibility(visible = !state.isInRegisterMode) {

                    Spacer(
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .height(64.dp)
                    )
                }
                Spacer(
                    modifier = Modifier
                        .height(24.dp)
                )
                Button(
                    shape = RoundedCornerShape(8.dp),
                    onClick = { viewModel.onEvent(AuthenticationEvent.AuthenticationButtonClicked) },
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .width(TextFieldDefaults.MinWidth)
                        .height(64.dp)
                ) {
                    Text(
                        text =
                        if (state.isInRegisterMode)
                            stringResource(id = R.string.button_text_sign_up)
                        else
                            stringResource(id = R.string.button_text_sign_in)
                    )
                }
                OutlinedButton(
                    shape = RoundedCornerShape(8.dp),
                    onClick = { viewModel.onEvent(AuthenticationEvent.ChangeMode) },
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .width(TextFieldDefaults.MinWidth)
                        .height(64.dp)
                ) {
                    Text(
                        text =
                        if (state.isInRegisterMode)
                            stringResource(id = R.string.button_text_back_to_login)
                        else
                            stringResource(id = R.string.button_text_no_account)
                    )
                }
            }
        }
    }
}