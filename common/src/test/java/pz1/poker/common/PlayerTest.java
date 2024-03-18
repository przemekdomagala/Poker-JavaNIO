package pz1.poker.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void testPlayer() {
        Player player = new Player();
        Card[] card = new Card[5];
        card[0] = new Card(Suit.SPADES, Rank.TWO);
        card[1] = new Card(Suit.SPADES, Rank.FOUR);
        card[2] = new Card(Suit.SPADES, Rank.FIVE);
        card[3] = new Card(Suit.SPADES, Rank.SIX);
        card[4] = new Card(Suit.SPADES, Rank.SEVEN);
        player.setPlayerHand(card);
        player.setId(1);
        player.setChecked(false);
        player.setCoins(1000);
        player.setRole(PlayerType.PLAYER);
        player.setWon(false);
        player.setFolded(false);
        player.setCurrentRoundBet(150);
        assertEquals(1, player.getId());
        assertFalse(player.isChecked());
        assertEquals(1000, player.getCoins());
        assertEquals(PlayerType.PLAYER, player.getRole());
        assertEquals(150, player.getCurrentRoundBet());
        assertNotNull(player.showHand());
    }
}