package pz1.poker.common;

/**Enum containing all possible signal types sent between Client and Server**/
public enum ActionType { CONNECT, CHECK_HAND, FIRST_CHECK_HAND, DEAL, NOT_YOUR_TURN, DISCONNECT, BET, NEXT_ROUND, ANTE,
    TRADE, STAND_POT, SHOW_HAND, SHOW_RESULT, FOLD, WAIT, NEXT_ROUND_FOLDED, FINISHED, CHECK }
