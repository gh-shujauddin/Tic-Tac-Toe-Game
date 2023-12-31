@file:OptIn(ExperimentalMaterial3Api::class)

package com.qadri.tic_tac_toe.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.qadri.tic_tac_toe.R
import com.qadri.tic_tac_toe.data.BoardCellValue
import com.qadri.tic_tac_toe.data.GameState
import com.qadri.tic_tac_toe.data.UserActions
import com.qadri.tic_tac_toe.data.VictoryType
import com.qadri.tic_tac_toe.ui.theme.BlueCustom
import com.qadri.tic_tac_toe.ui.theme.GrayBackground

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = viewModel(factory = HomeScreenViewModel.factory)
) {
    val uiState = viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        fontSize = 30.sp,
                        fontFamily = FontFamily.Cursive,
                        color = BlueCustom
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(GrayBackground)
            )
        }
    ) { inner ->
        Box(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.weight(1f))
                PreviousData(
                    modifier = Modifier,
                    uiState = uiState.value
                )
                GameScreen(
                    modifier = Modifier,
                    viewModel = viewModel
                )
                Spacer(modifier = Modifier.weight(1f))
                PlayerTurn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    turn = uiState.value,
                    tryAgain = {
                        viewModel.onAction(UserActions.PlayAgainButtonClicked)
                    }
                )
            }
        }
    }
}

@Composable
fun PreviousData(
    modifier: Modifier,
    uiState: GameState
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Player 'O': ${uiState.playerCircleCount}",
            fontSize = 16.sp,
            color = Color.Black
        )
        Text(text = "Draw: ${uiState.drawCount}", fontSize = 16.sp, color = Color.Black)
        Text(
            text = "Player 'X': ${uiState.playerCrossCount}",
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel
) {
    val state = viewModel.uiState.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .clip(RoundedCornerShape(20.dp))
            .background(GrayBackground),
        contentAlignment = Alignment.Center
    ) {
        BoardBase()
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .aspectRatio(1f)
        ) {
            viewModel.boardItems.forEach { (cellNumber, boardCellValue) ->
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null
                            ) {
                                if (!state.value.hasWon) {
                                    viewModel.onAction(
                                        UserActions.BoardTapped(cellNumber = cellNumber)
                                    )
                                }
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        AnimatedVisibility(
                            visible = viewModel.boardItems[cellNumber] != BoardCellValue.NONE,
                            enter = scaleIn(tween(500))
                        ) {

                            if (boardCellValue == BoardCellValue.CIRCLE) {
                                Circle()
                            } else if (boardCellValue == BoardCellValue.CROSS) {
                                Cross()
                            }
                        }
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .aspectRatio(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(
                visible = state.value.hasWon,
                enter = fadeIn(tween(1000))

            ) {
                DrawVictoryLine(uiState = state.value)
            }
        }
    }
}

@Composable
fun DrawVictoryLine(uiState: GameState) {
    when (uiState.victoryType) {
        VictoryType.HORIZONTAL1 -> WinHorizontalLine1()
        VictoryType.HORIZONTAL2 -> WinHorizontalLine2()
        VictoryType.HORIZONTAL3 -> WinHorizontalLine3()
        VictoryType.VERTICAL1 -> WinVerticalLine1()
        VictoryType.VERTICAL2 -> WinVerticalLine2()
        VictoryType.VERTICAL3 -> WinVerticalLine3()
        VictoryType.DIAGONAL1 -> WinDiagonalLine1()
        VictoryType.DIAGONAL2 -> WinDiagonalLine2()
        VictoryType.NONE -> {}
    }
}

@Composable
fun PlayerTurn(
    modifier: Modifier = Modifier,
    turn: GameState,
    tryAgain: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = turn.hintText,
            fontStyle = FontStyle.Italic,
            fontSize = 24.sp,
            color = Color.Black
        )
        Button(
            onClick = tryAgain,
            shape = RoundedCornerShape(5.dp),
            elevation = ButtonDefaults.buttonElevation(5.dp),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = BlueCustom,
                contentColor = Color.White
            )
        ) {
            Text(text = "Play Again", fontSize = 16.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun preview() {
    HomeScreen()
}
