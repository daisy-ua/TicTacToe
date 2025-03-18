package com.daisy.tictactoe.client.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.daisy.tictactoe.client.domain.model.GameState
import com.daisy.tictactoe.client.domain.model.Player
import com.daisy.tictactoe.client.ui.theme.TicTacToeTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TicTacToeBoard(
    gameState: GameState,
    onCellClicked: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
    playerXColor: Color = Color.Red,
    playerOColor: Color = Color.Blue,
    boardColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    val markPaddingPx = with(LocalDensity.current) { 40.dp.toPx() }

    Canvas(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val cellSize = size.width / 3f
                    val row = (offset.y / cellSize).toInt()
                    val col = (offset.x / cellSize).toInt()
                    onCellClicked(row, col)
                }
            }
    ) {
        drawBoard(boardColor)

        val cellSize = size.width / 3f
        val markSize = cellSize - markPaddingPx

        gameState.board.forEachIndexed { row, cols ->
            cols.forEachIndexed { col, mark ->
                val center = Offset(
                    x = col * cellSize + cellSize / 2,
                    y = row * cellSize + cellSize / 2
                )

                when (mark) {
                    Player.X -> drawX(
                        color = playerXColor,
                        center = center,
                        size = Size(markSize, markSize)
                    )

                    Player.O -> drawO(
                        color = playerOColor,
                        center = center,
                        size = Size(markSize, markSize)
                    )

                    null -> Unit
                }
            }
        }
    }
}

private fun DrawScope.drawO(
    color: Color,
    center: Offset,
    size: Size = Size(50.dp.toPx(), 50.dp.toPx())
) {
    drawCircle(
        color = color,
        radius = size.width / 2,
        center = center,
        style = Stroke(
            width = 2.dp.toPx()
        )
    )
}

private fun DrawScope.drawX(
    color: Color,
    center: Offset,
    size: Size = Size(50.dp.toPx(), 50.dp.toPx())
) {
    drawLine(
        color = color,
        start = Offset(
            x = center.x - size.width / 2f,
            y = center.y - size.height / 2f
        ),
        end = Offset(
            x = center.x + size.width / 2f,
            y = center.y + size.height / 2f
        ),
        strokeWidth = 2.dp.toPx(),
        cap = StrokeCap.Round
    )

    drawLine(
        color = color,
        start = Offset(
            x = center.x - size.width / 2f,
            y = center.y + size.height / 2f
        ),
        end = Offset(
            x = center.x + size.width / 2f,
            y = center.y - size.height / 2f
        ),
        strokeWidth = 2.dp.toPx(),
        cap = StrokeCap.Round
    )

}

private fun DrawScope.drawBoard(color: Color) {
    val xOffset = size.width / 3f
    val yOffset = size.height / 3f

    drawVerticalLine(
        color = color,
        xOffset = xOffset
    )

    drawVerticalLine(
        color = color,
        xOffset = xOffset * 2
    )

    drawHorizontalLine(
        color = color,
        yOffset = yOffset
    )

    drawHorizontalLine(
        color = color,
        yOffset = yOffset * 2
    )
}

private fun DrawScope.drawVerticalLine(
    color: Color,
    xOffset: Float,
) {
    drawLine(
        color = color,
        start = Offset(
            x = xOffset,
            y = 0f
        ),
        end = Offset(
            x = xOffset,
            y = size.height
        ),
        strokeWidth = 2.dp.toPx(),
        cap = StrokeCap.Round
    )
}

private fun DrawScope.drawHorizontalLine(
    color: Color,
    yOffset: Float,
) {
    drawLine(
        color = color,
        start = Offset(
            x = 0f,
            y = yOffset
        ),
        end = Offset(
            x = size.width,
            y = yOffset
        ),
        strokeWidth = 2.dp.toPx(),
        cap = StrokeCap.Round
    )
}

@Preview()
@Composable
private fun TicTacToeScreenPreview() {
    TicTacToeTheme {
        TicTacToeBoard(
            gameState = GameState(
                board = Array(3) { Array<Player?>(3) { null } }.apply {
                    this[1][2] = Player.X
                    this[1][1] = Player.X
                    this[2][1] = Player.O
                    this[2][0] = Player.O
                }
            ),
            onCellClicked = { _, _ -> },
            modifier = Modifier.size(250.dp)
        )
    }
}