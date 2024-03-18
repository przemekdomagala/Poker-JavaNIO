package pz1.poker.model;

import org.junit.jupiter.api.Test;
import pz1.poker.common.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    @Test
    void testGame(){
        Game game = new Game();
        game.setNext(-1);
        game.setGameDeck(new Deck());
        game.setFoldCounter(0);
        game.setDealer(new Dealer());
        game.setPot(0);
        List<Player> players = new ArrayList<>();
        players.add(new Dealer());
        Player player = new Player();
        Card[] cards = new Card[5];
        cards[0] = new Card(Suit.SPADES, Rank.ACE);
        cards[1] = new Card(Suit.SPADES, Rank.KING);
        cards[2] = new Card(Suit.SPADES, Rank.JACK);
        cards[3] = new Card(Suit.SPADES, Rank.QUEEN);
        cards[4] = new Card(Suit.SPADES, Rank.TEN);
        player.setId(2);
        players.add(player);
        game.setPlayers(players);
        game.setNextRoundCounter(0);
        game.setCurrentBet(0);
        game.setWinnerRes(ResultType.HIGH_CARD);
        game.setWinnerID(1);
        game.setStartingBalance(1);
        game.anteHandler(1);
        List<Integer> buffer = new ArrayList<>();
        buffer.add(1);
        buffer.add(2);
        buffer.add(3);
        game.tradeHandler(buffer, 1);
        game.dealerHandler();
        game.checkHandler(1);
        game.betHandler(100, 1);
        game.roundOfBetsFinished(1);
        game.allPlayersFinishedBets();
        game.playerFinishedBets(1);
        game.evaluateHand(2);
        game.chooseWinner();
        assertFalse(game.roundOfBetsFinished(1));
        int a = game.getPot();
        assertEquals(0, a);
        a = game.getNext();
        assertNotEquals(0, a);
        a = game.getCurrentBet();
        assertEquals(0, a);
        a = game.getWinnerID();
        assertEquals(2, a);
        a = game.getNextRoundCounter();
        assertEquals(0, a);
        a = game.getFoldCounter();
        assertEquals(0, a);
    }
}