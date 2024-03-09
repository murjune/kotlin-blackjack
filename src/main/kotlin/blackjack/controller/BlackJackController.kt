package blackjack.controller

import blackjack.model.GameResult
import blackjack.model.card.Deck
import blackjack.model.card.ParticipantHands
import blackjack.model.participant.Dealer
import blackjack.model.participant.ParticipantModel
import blackjack.model.participant.Player
import blackjack.view.InputView
import blackjack.view.OutputView

class BlackJackController(
    private val inputView: InputView,
    private val outputView: OutputView,
) {
    fun start() {
        val playersNames: List<String> = inputView.inputPlayerNames()
        val deck: Deck = Deck.create()
        val (dealer, players) = spreadCards(deck, playersNames)
        hitParticipant(players, deck, dealer)
        showScoreBoard(players, dealer)
    }

    private fun hitParticipant(
        players: List<Player>,
        deck: Deck,
        dealer: Dealer,
    ) {
        hitPlayers(players, deck)
        hitDealer(dealer, deck)
        outputView.showDealerScore(dealer.hand.cards, dealer.hand.sumOptimized())
    }

    private fun showScoreBoard(
        players: List<Player>,
        dealer: Dealer,
    ) {
        players.forEach {
            val name = it.name
            val handCards = it.hand
            outputView.showPlayerScore(name, handCards.cards, handCards.sumOptimized())
        }
        val scoreBoard = GameResult(dealer, players).createScoreBoard()
        outputView.showScoreBoard(scoreBoard)
    }

    private fun hitDealer(
        dealer: Dealer,
        deck: Deck,
    ) {
        while (dealer.canHit()) {
            outputView.showDealerHitCard()
            dealer.hit(deck.draw())
        }
    }

    private fun hitPlayers(
        players: List<Player>,
        deck: Deck,
    ) {
        players.forEach { player ->
            hitPlayer(player, deck)
        }
    }

    private fun hitPlayer(
        player: Player,
        deck: Deck,
    ) {
        while (inputView.inputWhetherHit(player)) {
            player.hit(deck.draw())
            outputView.showPlayerHandCards(player)
        }
    }

    private fun spreadCards(
        deck: Deck,
        playersNames: List<String>,
    ): ParticipantModel {
        val (playerHand, dealerHand) =
            ParticipantHands.from(deck.drawMultiple(playersNames.size))
        val dealer = Dealer(dealerHand)
        val players: List<Player> = Player.createPlayers(playersNames, playerHand)
        outputView.showDivided(dealerHand.first(), players)
        return ParticipantModel(dealer, players)
    }
}
