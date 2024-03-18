package pz1.poker.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    @Test
    void testDeck() {
        Deck deck = new Deck();
        Card[] cards = new Card[52];
        Deck deck1 = new Deck();
        deck1.setCards(cards);
        deck.shuffle();
        assertNotEquals(deck, deck1);
        assertNotNull(deck1.getCards());
    }
}