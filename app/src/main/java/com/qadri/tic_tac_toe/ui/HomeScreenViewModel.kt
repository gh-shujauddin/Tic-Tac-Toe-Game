package com.qadri.tic_tac_toe.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.qadri.tic_tac_toe.data.BoardCellValue
import com.qadri.tic_tac_toe.data.GameState
import com.qadri.tic_tac_toe.data.UserActions
import com.qadri.tic_tac_toe.data.VictoryType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeScreenViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(GameState())
    val uiState = _uiState.asStateFlow()

    val boardItems: MutableMap<Int, BoardCellValue> = mutableMapOf(
        1 to BoardCellValue.NONE,
        2 to BoardCellValue.NONE,
        3 to BoardCellValue.NONE,
        4 to BoardCellValue.NONE,
        5 to BoardCellValue.NONE,
        6 to BoardCellValue.NONE,
        7 to BoardCellValue.NONE,
        8 to BoardCellValue.NONE,
        9 to BoardCellValue.NONE
    )

    fun onAction(action: UserActions) {
        when(action) {
            is UserActions.BoardTapped -> {
                addValueToBoard(action.cellNumber)
            }
            UserActions.PlayAgainButtonClicked -> {
                gameReset()
            }
        }
    }

    private fun gameReset() {
        boardItems.forEach{(i, _) ->
            boardItems[i] = BoardCellValue.NONE
        }
        _uiState.value = _uiState.value.copy(
            hintText = "Player O's turn",
            currentTurn = BoardCellValue.CIRCLE,
            victoryType = VictoryType.NONE,
            hasWon = false
        )
    }

    private fun addValueToBoard(cellNumber: Int) {
        if (boardItems[cellNumber] != BoardCellValue.NONE) {
            return
        }
        if (_uiState.value.currentTurn == BoardCellValue.CIRCLE) {
            boardItems[cellNumber] = BoardCellValue.CIRCLE
            if(checkForVictory(BoardCellValue.CIRCLE)) {
                _uiState.value = _uiState.value.copy(
                    hintText = "Player O Won",
                    hasWon = true,
                    playerCircleCount = _uiState.value.playerCircleCount + 1,
                    currentTurn = BoardCellValue.NONE
                )
            }
            else if (hasBoardFull()) {
                _uiState.value = _uiState.value.copy(
                    hintText = "Game Draw",
                    drawCount = _uiState.value.drawCount + 1
                )
            }else {
                _uiState.value = _uiState.value.copy(
                    hintText = "Player X's Turn",
                    currentTurn = BoardCellValue.CROSS
                )
            }
        } else if (_uiState.value.currentTurn == BoardCellValue.CROSS) {
            boardItems[cellNumber] = BoardCellValue.CROSS
            if(checkForVictory(BoardCellValue.CROSS)) {
                _uiState.value = _uiState.value.copy(
                    hintText = "Player X Won",
                    hasWon = true,
                    playerCrossCount = _uiState.value.playerCrossCount + 1,
                    currentTurn = BoardCellValue.NONE
                )
            }
            else if (hasBoardFull()) {
                _uiState.value = _uiState.value.copy(
                    hintText = "Game Draw",
                    drawCount = _uiState.value.drawCount + 1
                )
            }else {
                _uiState.value = _uiState.value.copy(
                    hintText = "Player O's Turn",
                    currentTurn = BoardCellValue.CIRCLE
                )
            }
        }
    }

    private fun checkForVictory(boardValue: BoardCellValue): Boolean {
        when {
            boardItems[1] == boardValue && boardItems[2] == boardValue && boardItems[3] == boardValue -> {
                _uiState.value = _uiState.value.copy(
                    victoryType = VictoryType.HORIZONTAL1
                )
                return true
            }
            boardItems[4] == boardValue && boardItems[5] == boardValue && boardItems[6] == boardValue -> {
                _uiState.value = _uiState.value.copy(
                    victoryType = VictoryType.HORIZONTAL2
                )
                return true
            }
            boardItems[7] == boardValue && boardItems[8] == boardValue && boardItems[9] == boardValue -> {
                _uiState.value = _uiState.value.copy(
                    victoryType = VictoryType.HORIZONTAL3
                )
                return true
            }
            boardItems[1] == boardValue && boardItems[4] == boardValue && boardItems[7] == boardValue -> {
                _uiState.value = _uiState.value.copy(
                    victoryType = VictoryType.VERTICAL1
                )
                return true
            }
            boardItems[2] == boardValue && boardItems[5] == boardValue && boardItems[8] == boardValue -> {
                _uiState.value = _uiState.value.copy(
                    victoryType = VictoryType.VERTICAL2
                )
                return true
            }
            boardItems[3] == boardValue && boardItems[6] == boardValue && boardItems[9] == boardValue -> {
                _uiState.value = _uiState.value.copy(
                    victoryType = VictoryType.VERTICAL3
                )
                return true
            }
            boardItems[1] == boardValue && boardItems[5] == boardValue && boardItems[9] == boardValue -> {
                _uiState.value = _uiState.value.copy(
                    victoryType = VictoryType.DIAGONAL1
                )
                return true
            }
            boardItems[3] == boardValue && boardItems[5] == boardValue && boardItems[7] == boardValue -> {
                _uiState.value = _uiState.value.copy(
                    victoryType = VictoryType.DIAGONAL2
                )
                return true
            }
            else -> {
                return false
            }
        }
    }

    private fun hasBoardFull(): Boolean {
        if (boardItems.containsValue(BoardCellValue.NONE)) return false
        return true
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                HomeScreenViewModel()
            }
        }
    }
}