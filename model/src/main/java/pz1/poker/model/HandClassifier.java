package pz1.poker.model;

import pz1.poker.common.Rank;
import pz1.poker.common.Suit;
import pz1.poker.common.Card;

import java.util.*;

/**Class classifying player's hand and picking winner.**/
public class HandClassifier {

    /**Method used to check whether Player has RoyalFlush on hand.
     * @param hand Player's hand.
     * @return boolean true when Player has RoyalFlush on hand, False otherwise.**/
    private static boolean isRoyalFlush(Card[] hand) {
        boolean ten = false;
        boolean jack = false;
        boolean queen = false;
        boolean king = false;
        boolean ace = false;
        for (Card card : hand) {
            if (card.getRank().ordinal() < Rank.TEN.ordinal()) {
                return false;
            }
            if (card.getRank() == Rank.TEN) {
                ten = true;
            }
            if (card.getRank() == Rank.JACK) {
                jack = true;
            }
            if (card.getRank() == Rank.QUEEN) {
                queen = true;
            }
            if (card.getRank() == Rank.KING) {
                king = true;
            }
            if (card.getRank() == Rank.ACE) {
                ace = true;
            }
        }
        return (ten && jack && queen && king && ace);
    }

    /**Method used to check whether Player has StraightFlush on hand.
     * @param hand Player's hand.
     * @return boolean true when Player has StraightFlush on hand, False otherwise.**/
    private static boolean isStraightFlush(Card[] hand) {
        ArrayList<Integer> flushWithAce = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            flushWithAce.add(i);
        }
        flushWithAce.add(12);
        ArrayList<Integer> handOrdinal = new ArrayList<>();
        Suit comparingSuit = hand[0].getSuit();
        for (Card card : hand) {
            handOrdinal.add(card.getRank().ordinal());
            if (card.getSuit() != comparingSuit) {
                return false;
            }
        }
        return withAce(handOrdinal, flushWithAce);
    }

    /**Method used to check whether Player has Four of a kind on hand.
     * @param hand Player's hand.
     * @return boolean true when Player has Four of a kind on hand, False otherwise.**/
    private static boolean isFourOfAKind(Card[] hand) {
        int counterOne = 0;
        int counterTwo = 0;
        Card comparatorOne = hand[0];
        Card comparatorTwo = hand[1];
        for (Card card : hand) {
            if (card.getRank() == comparatorOne.getRank()) {
                counterOne++;
            }
            if (card.getRank() == comparatorTwo.getRank()) {
                counterTwo++;
            }
        }
        return (counterOne == 4 || counterTwo == 4);
    }

    /**Method used to check whether Player has FullHouse on hand.
     * @param hand Player's hand.
     * @return boolean true when Player has Full house on hand, False otherwise.**/
    private static boolean isFullHouse(Card[] hand) {
        ArrayList<Card> bufferOne = new ArrayList<>();
        ArrayList<Card> bufferTwo = new ArrayList<>();
        bufferOne.add(hand[0]);
        for (int i = 1; i < 5; i++) {
            if (hand[i].getRank() == bufferOne.get(0).getRank()) {
                bufferOne.add(hand[i]);
            }
        }
        if (bufferOne.size() < 2 || bufferOne.size() > 3) {
            return false;
        }
        for (Card card : hand) {
            if (!bufferOne.contains(card)) {
                bufferTwo.add(card);
            }
        }
        Rank comparingRank = bufferTwo.get(0).getRank();
        for (Card card : bufferTwo) {
            if (card.getRank() != comparingRank) {
                return false;
            }
        }
        return true;
    }

    /**Method used to check whether Player has Flush on hand.
     * @param hand Player's hand.
     * @return boolean true when Player has Flush on hand, False otherwise.**/
    private static boolean isFlush(Card[] hand) {
        Suit comparingSuit = hand[0].getSuit();
        for (Card card : hand) {
            if (card.getSuit() != comparingSuit) {
                return false;
            }
        }
        return true;
    }

    /**Method that helps to classify hand when Ace counts as Two.
     * @param handOrdinal Ordinals of Cards rank on hand
     * @param WithAce ArrayList helping to evaluate.
     * @return boolean true when hand is true (depends on when method was used).**/
    private static boolean withAce(ArrayList<Integer> handOrdinal, ArrayList<Integer> WithAce){
        Collections.sort(handOrdinal);
        Collections.sort(WithAce);
        return ((handOrdinal.equals(WithAce)) || (handOrdinal.get(0) + 1 == handOrdinal.get(1) &&
                handOrdinal.get(1) + 1 == handOrdinal.get(2) && handOrdinal.get(2) + 1 == handOrdinal.get(3) &&
                handOrdinal.get(3) + 1 == handOrdinal.get(4)));
    }

    /**Method used to check whether Player has Straight on hand.
     * @param hand Player's hand.
     * @return boolean true when Player has Straight on hand, False otherwise.**/
    private static boolean isStraight(Card[] hand) {
        ArrayList<Integer> StraightWithAce = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            StraightWithAce.add(i);
        }
        StraightWithAce.add(12);
        ArrayList<Integer> handOrdinal = new ArrayList<>();
        for (Card card : hand) {
            handOrdinal.add(card.getRank().ordinal());
        }
        return withAce(handOrdinal, StraightWithAce);
    }

    /**Method used to check whether Player has three of a kind on hand.
     * @param hand Player's hand.
     * @return boolean true when Player has three of a kind on hand, False otherwise.**/
    private static boolean isThreeOfAKind(Card[] hand) {
        List<Card> buffer = new ArrayList<>();
        Collections.addAll(buffer, hand);
        Collections.sort(buffer);
        //trio can either start from first or from third
        int counterOne = 0;
        int counterTwo = 0;
        Rank rankOne = buffer.get(0).getRank();
        Rank rankTwo = buffer.get(2).getRank();
        for (Card card : hand) {
            if (card.getRank() == rankOne) {
                counterOne++;
            }
            if (card.getRank() == rankTwo) {
                counterTwo++;
            }
        }
        return (counterOne == 3 || counterTwo == 3);
    }

    /**Method used to check whether Player has two pairs on hand.
     * @param hand Player's hand.
     * @return boolean true when Player has two pairs on hand, False otherwise.**/
    private static boolean isTwoPair(Card[] hand) {
        List<Card> buffer = new ArrayList<>(Arrays.asList(hand));
        Collections.sort(buffer);
        int counter = 0;
        for (int i = 0; i < buffer.size() - 1; i++) {
            if (buffer.get(i).getRank() == buffer.get(i + 1).getRank()) {
                counter++;
            }
        }
        return counter == 2;
    }

    /**Method used to check whether Player has Pair on hand.
     * @param hand Player's hand.
     * @return boolean true when Player has Pair on hand, False otherwise.**/
    private static boolean isPair(Card[] hand) {
        for (int i = 0; i < hand.length; i++) {
            for (int j = i + 1; j < hand.length; j++) {
                if (hand[i].getRank() == hand[j].getRank()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**Method returning what score player has on hand.
     * @param hand Player's hand.
     * @return ResultType is type of result player scored with his hand.**/
    public static ResultType handEvaluator(Card[] hand) {
        if (isRoyalFlush(hand)) return ResultType.ROYAL_FLUSH;
        if (isStraightFlush(hand)) return ResultType.STRAIGHT_FLUSH;
        if (isFourOfAKind(hand)) return ResultType.FOUR_OF_A_KIND;
        if (isFullHouse(hand)) return ResultType.FULL_HOUSE;
        if (isFlush(hand)) return ResultType.FLUSH;
        if (isStraight(hand)) return ResultType.STRAIGHT;
        if (isThreeOfAKind(hand)) return ResultType.THREE_OF_A_KIND;
        if (isTwoPair(hand)) return ResultType.TWO_PAIRS;
        if (isPair(hand)) return ResultType.PAIR;
        return ResultType.HIGH_CARD;
    }

    /**Method checking whether next checked Player has higher Hand that currently pick winner.
     * @param playerHand New Hand
     * @param winnerHand Current Winner's hand
     * @return int 1 when new player wins, 2 when winner stays**/
    public static int chooseWinner(Card[] playerHand, Card[] winnerHand) {
        Card highCard = new Card(Suit.HEARTS, Rank.TWO);
        int win = 0;
        for(Card card: playerHand){
            if(card.compareTo(highCard)>=0){
                highCard = card;
                win = 1;
            }
        }
        for(Card card: winnerHand){
            if(card.compareTo(highCard)>0){
                highCard = card;
                win = 2;
            }
        }
        return win;
    }
}