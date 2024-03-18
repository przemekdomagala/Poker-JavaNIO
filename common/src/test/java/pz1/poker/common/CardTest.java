package pz1.poker.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {
    @Test
    void cardTest(){
        Card card = new Card(Suit.DIAMONDS, Rank.ACE);
        Card card2 = new Card(Suit.SPADES, Rank.TWO);
        card.show();
        Card card3 = new Card(Suit.DIAMONDS, Rank.ACE);
        assertEquals(card, card3);
        assertNotEquals(card, card2);
        assertEquals(1, card.compareTo(card2));
        assertEquals(-1, card2.compareTo(card));
        assertEquals(0, card.compareTo(card3));
        int x = card.hashCode();
        assertNotEquals(0, x);
    }
}