package pz1.poker.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PassedTokensTest {
    @Test
    void testPassedTokens(){
        PassedTokens passedTokens1 = new PassedTokens();
        PassedTokens passedTokens = new PassedTokens(2, ActionType.SHOW_RESULT, "");
        passedTokens1.setPlayerID(2);
        passedTokens1.setActionType(ActionType.SHOW_RESULT);
        passedTokens1.setActionParameters("");
        assertEquals(passedTokens, passedTokens1);

    }
}