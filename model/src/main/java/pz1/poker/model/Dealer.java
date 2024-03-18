package pz1.poker.model;

import pz1.poker.common.Card;
import pz1.poker.common.Deck;
import pz1.poker.common.Player;
import pz1.poker.common.PlayerType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**Class representing Game's Dealer.**/
public class Dealer extends Player {

    /**Default constructor assigning correct Role.**/
    Dealer(){
        this.role = PlayerType.DEALER;
    }

    /**Method used to deal cards between Players.
     * @param deck Deck of cards used in Game.
     * @param players List of Players playing Game.**/
    protected void dealCards(List<Player> players, Deck deck) {
        List<Card> bufferDeck = new ArrayList<>();
        Collections.addAll(bufferDeck, deck.getCards());
        for (int i = 0; i < players.get(0).getPlayerHand().length * players.size(); i += players.size()) {
            for (int j = 0; j < players.size(); j++) {
                players.get(j).getPlayerHand()[i / players.size()] = deck.getCards()[i + j];
                bufferDeck.remove(deck.getCards()[i + j]);
            }
        }
        Card[] newDeck = new Card[bufferDeck.size()];
        for (int i = 0; i < bufferDeck.size(); i++) {
            newDeck[i] = bufferDeck.get(i);
        }
        deck.setCards(newDeck);
    }

    /**Method used to trade Cards between Deck and Player.
     * @param buffer Integer containing number of Cards on hand that player want to trade.
     * @param deck Deck used in game.
     * @param player Player that trade is handled.
     * @param cardsTradeBuffer Buffer used in method**/
    protected void handleTrade(List<Integer> buffer, Player player, List<Card> cardsTradeBuffer, Deck deck){
        List<Card> handBuffer = new ArrayList<>();
        for(int i=0; i<player.getPlayerHand().length; i++){
            for (Integer integer : buffer) {
                if (i == integer - 1 && !cardsTradeBuffer.contains(player.getPlayerHand()[integer-1])) {
                    cardsTradeBuffer.add(player.getPlayerHand()[integer-1]);
                }
            }
        }
        for(int i=0; i<player.getPlayerHand().length; i++){
            if(!cardsTradeBuffer.contains(player.getPlayerHand()[i])){
                handBuffer.add(player.getPlayerHand()[i]);
            }
        }
        handBuffer.addAll(Arrays.asList(deck.getCards()).subList(0, player.getPlayerHand().length - handBuffer.size()));
        Card[] newHand = new Card[5];
        for(int i=0; i<player.getPlayerHand().length; i++){
            newHand[i] = handBuffer.get(i);
        }
        player.setPlayerHand(newHand);
        cardsTradeBuffer.clear();
    }
}
