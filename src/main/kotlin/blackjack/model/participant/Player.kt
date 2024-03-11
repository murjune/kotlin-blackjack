package blackjack.model.participant

import blackjack.model.card.Card
import blackjack.model.card.Hand

class Player(val name: String, hand: Hand) {
    private val handCards: MutableList<Card> = hand.cards.toMutableList()
    val hand: Hand get() = Hand(handCards.toList())

    constructor(pair: Pair<String, Hand>) : this(pair.first, pair.second)

    fun hit(card: Card) {
        handCards.add(card)
    }

    companion object {
        @JvmStatic
        fun createPlayers(
            names: List<String>,
            hands: List<Hand>,
        ): List<Player> {
            return names.zip(hands).map(::Player)
        }
    }
}