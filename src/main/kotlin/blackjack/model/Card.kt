package blackjack.model

data class Card(val suit: Suit, val rank: Rank) {
    fun isAce() = (rank == Rank.ACE)
}
