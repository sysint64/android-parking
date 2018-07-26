package ru.kabylin.andrey.parking.views

interface ItemTouchHelperActionCompletionContract {
    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean

    fun onItemDismiss(position: Int)
}
