package pz1.poker.common;

import lombok.Getter;
import lombok.Setter;

/**Class representing Game's Player**/
@Setter
@Getter
public class Player {
    /**Player's id.**/
    private int id;
    /**Cards that Player has on hand.**/
    private Card[] playerHand;
    /**Amount of coins on Player's account.**/
    private int coins;
    /**Highest Player's bet in this betting round.**/
    private int currentRoundBet = 0;
    /**Boolean informing whether Player has folded.**/
    protected boolean folded = false;
    /**Boolean informing whether Player has won**/
    protected boolean won = false;
    /**Boolean informing whether Player has checked in this betting round.**/
    protected boolean checked;
    /**Player's role (either PLAYER or DEALER).**/
    protected PlayerType role;

    /**Constructor used to create Player object.**/
    public Player() {
        checked = false;
        playerHand = new Card[5];
        role = PlayerType.PLAYER;
    }

    /**Method showing player's hand.
     * @return String containing all Cards on Player hand.**/
    public String showHand() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < this.getPlayerHand().length; i++) {
            result.append(i + 1).append(" ");
            result.append(this.getPlayerHand()[i].show()).append(" ");
        }
        return String.valueOf(result);
    }
}