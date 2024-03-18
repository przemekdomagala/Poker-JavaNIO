package pz1.poker.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServerOptionsTest {
    @Test
    void testServerOptions(){
        assertEquals(4, ServerOptions.MAX_ID);
        ServerOptions.connectedClients = 0;
        assertEquals(0, ServerOptions.connectedClients);
    }
}