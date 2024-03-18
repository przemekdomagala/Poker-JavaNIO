package pz1.poker.client;

import org.junit.jupiter.api.Test;
import pz1.poker.common.PlayerType;

import static org.junit.jupiter.api.Assertions.*;

class ClientManagerTest {

    @Test
    void extractPlayerRole() {
        assertEquals(PlayerType.PLAYER, ClientManager.extractPlayerRole("Your role: PLAYER"));
        assertEquals(PlayerType.DEALER, ClientManager.extractPlayerRole("Your role: DEALER"));
        assertNull(ClientManager.extractPlayerRole("dfsgfd"));
    }

    @Test
    void extract() {
        assertEquals("bet", ClientManager.extract("bet aaa"));
        assertEquals("trade", ClientManager.extract("trade aaa"));
        assertEquals("", ClientManager.extract("fghfghfg"));
    }

    @Test
    void amount() {
        assertEquals(100, ClientManager.amount("bet 100"));
        assertEquals(-1, ClientManager.amount("bet nonumberhere"));
    }
}