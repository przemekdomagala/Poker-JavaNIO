package pz1.poker.common;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.*;

class MyJSONParserTest {

    @Test
    void testJSONParser() {
        PassedTokens passedTokens = new PassedTokens(2, ActionType.SHOW_HAND, "");
        assertNotEquals(ByteBuffer.wrap("PassedTokens(playerID=2, actionType=SHOW_HAND, actionParameters=)".getBytes()), MyJSONParser.stringify(passedTokens));
        assertNotEquals(passedTokens, MyJSONParser.parse("{PassedTokens(playerID=2)}"));
    }
}