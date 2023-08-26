package com.qadri.tic_tac_toe.data

sealed class UserActions{
    object PlayAgainButtonClicked: UserActions()
    data class BoardTapped(val cellNumber: Int): UserActions()
}
