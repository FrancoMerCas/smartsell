package com.sinaptix.smartsell.auth

import ContentWithMessageBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.mmk.kmpauth.google.GoogleButtonUiContainer
import com.sinaptix.smartsell.auth.component.GoogleButton
import com.sinaptix.smartsell.shared.components.CustomePrimaryButton
import com.sinaptix.smartsell.shared.components.CustomeTextField
import com.sinaptix.smartsell.shared.resources.Alpha
import com.sinaptix.smartsell.shared.resources.AppStrings
import com.sinaptix.smartsell.shared.resources.FontSize.EXTRA_LARGE
import com.sinaptix.smartsell.shared.resources.FontSize.EXTRA_REGULAR
import com.sinaptix.smartsell.shared.resources.MadaBoldFont
import com.sinaptix.smartsell.shared.resources.MadaRegularFont
import com.sinaptix.smartsell.shared.resources.SurfaceBrand
import com.sinaptix.smartsell.shared.resources.SurfaceError
import com.sinaptix.smartsell.shared.resources.TextCreme
import com.sinaptix.smartsell.shared.resources.TextPrimary
import com.sinaptix.smartsell.shared.resources.TextSecondary
import com.sinaptix.smartsell.shared.resources.TextWhite
import com.sinaptix.smartsell.shared.util.asStringRes
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import rememberMessageBarState

private const val TAB_LOGIN = 0
private const val TAB_REGISTER = 1

@Composable
fun AuthScreen(
    navigateToHome: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val viewModel = koinViewModel<AuthViewModel>()
    val messageBarState = rememberMessageBarState()

    var selectedTab by remember { mutableIntStateOf(TAB_LOGIN) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var googleLoading by remember { mutableStateOf(false) }

    val screenState = viewModel.screenState
    val isLoading = screenState.isLoading()

    val authSuccessMessage = stringResource(AppStrings.Auth.authSuccess)
    val networkErrorMessage = stringResource(AppStrings.Errors.errorNotConnection)
    val signInCanceledMessage = stringResource(AppStrings.Errors.errorSigninCancel)
    val unknownErrorMessage = stringResource(AppStrings.Errors.errorUnknow)

    Scaffold { padding ->
        ContentWithMessageBar(
            modifier = Modifier
                .padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                ),
            messageBarState = messageBarState,
            errorMaxLines = 2,
            errorContainerColor = SurfaceError,
            errorContentColor = TextWhite,
            successContainerColor = SurfaceBrand,
            successContentColor = TextPrimary
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 24.dp, end = 24.dp, top = 24.dp)
                    .imePadding()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // ─── App name header ──────────────────────────────────────────
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = TextSecondary)) {
                                append(AppStrings.AppName.appNameFirst.asStringRes())
                            }
                            withStyle(style = SpanStyle(color = TextCreme)) {
                                append(AppStrings.AppName.appNameSecond.asStringRes())
                            }
                        },
                        textAlign = TextAlign.Center,
                        fontFamily = MadaBoldFont(),
                        fontSize = EXTRA_LARGE
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .alpha(Alpha.HALF),
                        text = AppStrings.Auth.authContinue.asStringRes(),
                        textAlign = TextAlign.Center,
                        fontFamily = MadaRegularFont(),
                        fontSize = EXTRA_REGULAR,
                        color = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // ─── Tab selector ─────────────────────────────────────────────
                TabRow(selectedTabIndex = selectedTab) {
                    Tab(
                        selected = selectedTab == TAB_LOGIN,
                        onClick = { selectedTab = TAB_LOGIN },
                        text = { Text("Login") }
                    )
                    Tab(
                        selected = selectedTab == TAB_REGISTER,
                        onClick = { selectedTab = TAB_REGISTER },
                        text = { Text("Register") }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // ─── Register-only fields ─────────────────────────────────────
                if (selectedTab == TAB_REGISTER) {
                    CustomeTextField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        placeHolder = AppStrings.PlaceHolder.placeholderFirstName.asStringRes()
                    )
                    CustomeTextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        placeHolder = AppStrings.PlaceHolder.placeholderLastName.asStringRes()
                    )
                }

                // ─── Email + Password ─────────────────────────────────────────
                CustomeTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeHolder = AppStrings.PlaceHolder.placeholderEmail.asStringRes(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                CustomeTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeHolder = "Password",
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                // ─── Submit button ────────────────────────────────────────────
                CustomePrimaryButton(
                    text = if (selectedTab == TAB_LOGIN) "Login" else "Register",
                    enabled = !isLoading,
                    onClick = {
                        if (selectedTab == TAB_LOGIN) {
                            viewModel.loginWithEmail(
                                email = email,
                                password = password,
                                onSuccess = {
                                    scope.launch {
                                        messageBarState.addSuccess(authSuccessMessage)
                                        delay(1500)
                                        navigateToHome()
                                    }
                                },
                                onError = { errorMessage ->
                                    messageBarState.addError(errorMessage)
                                }
                            )
                        } else {
                            viewModel.register(
                                firstName = firstName,
                                lastName = lastName,
                                email = email,
                                password = password,
                                onSuccess = {
                                    scope.launch {
                                        messageBarState.addSuccess(authSuccessMessage)
                                        delay(1500)
                                        navigateToHome()
                                    }
                                },
                                onError = { errorMessage ->
                                    messageBarState.addError(errorMessage)
                                }
                            )
                        }
                    }
                )

                // ─── Divider ──────────────────────────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f))
                    Text(text = "or", color = TextPrimary)
                    HorizontalDivider(modifier = Modifier.weight(1f))
                }

                // ─── Google Sign-In ───────────────────────────────────────────
                GoogleButtonUiContainer(
                    onGoogleSignInResult = { googleUser ->
                        val idToken = googleUser?.idToken
                        if (idToken != null) {
                            viewModel.loginWithGoogle(
                                idToken = idToken,
                                onSuccess = {
                                    scope.launch {
                                        messageBarState.addSuccess(authSuccessMessage)
                                        delay(1500)
                                        navigateToHome()
                                    }
                                    googleLoading = false
                                },
                                onError = { errorMessage ->
                                    messageBarState.addError(errorMessage)
                                    googleLoading = false
                                }
                            )
                        } else {
                            messageBarState.addError(signInCanceledMessage)
                            googleLoading = false
                        }
                    }
                ) {
                    GoogleButton(
                        loading = googleLoading,
                        onClick = {
                            googleLoading = true
                            this@GoogleButtonUiContainer.onClick()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}