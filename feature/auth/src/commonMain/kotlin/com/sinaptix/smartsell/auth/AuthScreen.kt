package com.sinaptix.smartsell.auth

import ContentWithMessageBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.mmk.kmpauth.firebase.google.GoogleButtonUiContainerFirebase
import com.sinaptix.smartsell.auth.component.GoogleButton
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

@Composable
fun AuthScreen(
    navigateToHome: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val viewModel = koinViewModel<AuthViewModel>()
    val messageBarState = rememberMessageBarState()
    var loadingState by remember { mutableStateOf(false) }

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
                    .padding(all = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.Center,
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
                GoogleButtonUiContainerFirebase(
                    linkAccount = false,
                    onResult = { result ->
                        result.onSuccess { firebaseUser ->
                            viewModel.createCustomer(
                                user = firebaseUser,
                                onSuccess = {
                                    scope.launch {
                                        messageBarState.addSuccess(authSuccessMessage)
                                        delay(2000)
                                        navigateToHome()
                                    }
                                },
                                onError = { errorMessage ->
                                    messageBarState.addError(errorMessage)
                                }
                            )
                            loadingState = false
                        }
                        result.onFailure { error ->
                            val message = when {
                                error.message?.contains("A network error") == true -> networkErrorMessage
                                error.message?.contains("IdToken is null") == true -> signInCanceledMessage
                                else -> error.message ?: unknownErrorMessage
                            }
                            messageBarState.addError(message)

                            loadingState = false
                        }
                    }
                ) {
                    GoogleButton(
                        loading = loadingState,
                        onClick = {
                            loadingState = true
                            this@GoogleButtonUiContainerFirebase.onClick()
                        }
                    )
                }
            }
        }
    }
}