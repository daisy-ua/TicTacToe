package com.daisy.tictactoe.client.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.daisy.tictactoe.client.core.utils.LocalDimens
import com.daisy.tictactoe.client.presentation.GameAction
import com.daisy.tictactoe.client.presentation.GameUiState
import org.jetbrains.compose.resources.stringResource
import tictactoeclient.composeapp.generated.resources.Res
import tictactoeclient.composeapp.generated.resources.battle
import tictactoeclient.composeapp.generated.resources.connecting_to_server
import tictactoeclient.composeapp.generated.resources.create_invitation
import tictactoeclient.composeapp.generated.resources.invite_with_code


@Composable
fun InvitationContent(
    state: GameUiState,
    onAction: (GameAction) -> Unit,
    modifier: Modifier = Modifier
) {
    var passcode by remember {
        mutableStateOf(state.roomId ?: "")
    }

    val dimens = LocalDimens.current

    Surface(
        modifier = modifier,
    ) {
        when {
            state.isConnecting -> {
                LoadingContent(
                    text = stringResource(Res.string.connecting_to_server),
                    modifier = Modifier.fillMaxSize(),
                )
            }

            else -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(Res.string.invite_with_code),
                        style = MaterialTheme.typography.headlineMedium
                    )

                    OutlinedTextField(
                        value = passcode,
                        onValueChange = {
                            passcode = it
                        },
                        enabled = state.message == null,
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(dimens.buttonSpacing),
                        modifier = Modifier
                            .fillMaxWidth()
                            .alpha(if (state.message == null) 1f else 0f),
                    ) {
                        OutlinedButton(
                            onClick = { onAction(GameAction.CreateRoom) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            Text(text = stringResource(Res.string.create_invitation))
                        }

                        Button(
                            onClick = { onAction(GameAction.JoinRoom(passcode)) },
                            enabled = passcode.isNotEmpty(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            Text(text = stringResource(Res.string.battle))
                        }
                    }

                    state.message?.let {
                        Text(
                            text = it.asString(),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}