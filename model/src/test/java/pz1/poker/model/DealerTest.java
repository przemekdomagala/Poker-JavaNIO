package pz1.poker.model;

import org.junit.jupiter.api.Test;
import pz1.poker.common.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DealerTest {

    @Test
    void dealCards() {
        Deck deck = new Deck();
        List<Player> playerList = new ArrayList<>();
        Dealer dealer = new Dealer();
        dealer.setId(1);
        playerList.add(dealer);
        playerList.add(new Player());
        playerList.add(new Player());
        playerList.add(new Player());
        dealer.dealCards(playerList, deck);
        List<Card> buffor = new ArrayList<>();
        buffor.add(new Card(Suit.SPADES, Rank.ACE));
        buffor.add(new Card(Suit.SPADES, Rank.JACK));
        buffor.add(new Card(Suit.SPADES, Rank.KING));
        List<Integer> buffer = new ArrayList<>();
        buffer.add(1);
        buffer.add(3);
        buffer.add(5);
        dealer.handleTrade(buffer, dealer, buffor, deck);
        assertEquals(1, dealer.getId());
    }
}