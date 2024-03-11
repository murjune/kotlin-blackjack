package blackjack.model.participant

import blackjack.model.card.Card
import blackjack.state.State

class Dealer(state: State) : Participant(DEALER_NAME, state) {
    fun play(
        onDraw: () -> Card,
        onDone: () -> Unit,
    ) {
        if (state.sumScore() < HIT_CONDITION) {
            state = State.Running(hand).hit(onDraw())
            onDone()
            play(onDraw, onDone)
        } else {
            state = State.Running(hand).stay()
            onDone()
        }
    }

    override fun judge(other: Participant): GameResult =
        when {
            other.isBust() -> GameResult.WIN
            isBust() -> GameResult.LOSE
            isBlackJack() && other.isBlackJack() -> GameResult.DRAW
            isBlackJack() -> GameResult.WIN
            other.isBlackJack() -> GameResult.LOSE
            else -> {
                val compared = sumScore() compareTo other.sumScore()
                GameResult.from(compared)
            }
        }

    fun judge(other: List<Participant>): Map<GameResult, Int> {
        return other.map { judge(it) }
            .groupingBy { it }
            .eachCount()
    }

    companion object {
        private const val DEALER_NAME = "딜러"
        private const val HIT_CONDITION = 17
    }
}
