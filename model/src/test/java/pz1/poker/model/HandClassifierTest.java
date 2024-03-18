package pz1.poker.model;

import org.junit.jupiter.api.Test;
import pz1.poker.common.Card;
import pz1.poker.common.Rank;
import pz1.poker.common.Suit;

import static org.junit.jupiter.api.Assertions.*;

class HandClassifierTest {
    @Test
    void handClassifierTest(){
        Card[] royalFlush = new Card[5];
        royalFlush[0] = new Card(Suit.SPADES, Rank.ACE);
        royalFlush[1] = new Card(Suit.SPADES, Rank.KING);
        royalFlush[2] = new Card(Suit.SPADES, Rank.QUEEN);
        royalFlush[3] = new Card(Suit.SPADES, Rank.JACK);
        royalFlush[4] = new Card(Suit.SPADES, Rank.TEN);
        assertEquals(ResultType.ROYAL_FLUSH, HandClassifier.handEvaluator(royalFlush));
        Card[] straightFlush = new Card[5];
        straightFlush[0] = new Card(Suit.SPADES, Rank.KING);
        straightFlush[1] = new Card(Suit.SPADES, Rank.QUEEN);
        straightFlush[2] = new Card(Suit.SPADES, Rank.JACK);
        straightFlush[3] = new Card(Suit.SPADES, Rank.TEN);
        straightFlush[4] = new Card(Suit.SPADES, Rank.NINE);
        assertEquals(ResultType.STRAIGHT_FLUSH, HandClassifier.handEvaluator(straightFlush));
        Card[] fourOfAKind = new Card[5];
        fourOfAKind[0] = new Card(Suit.SPADES, Rank.TWO);
        fourOfAKind[1] = new Card(Suit.HEARTS, Rank.TWO);
        fourOfAKind[2] = new Card(Suit.CLUBS, Rank.TWO);
        fourOfAKind[3] = new Card(Suit.DIAMONDS, Rank.TWO);
        fourOfAKind[4] = new Card(Suit.SPADES, Rank.THREE);
        assertEquals(ResultType.FOUR_OF_A_KIND, HandClassifier.handEvaluator(fourOfAKind));
        Card[] fullHouse = new Card[5];
        fullHouse[0] = new Card(Suit.SPADES, Rank.TWO);
        fullHouse[1] = new Card(Suit.CLUBS, Rank.TWO);
        fullHouse[2] = new Card(Suit.DIAMONDS, Rank.TWO);
        fullHouse[3] = new Card(Suit.SPADES, Rank.THREE);
        fullHouse[4] = new Card(Suit.CLUBS, Rank.THREE);
        assertEquals(ResultType.FULL_HOUSE, HandClassifier.handEvaluator(fullHouse));
        Card[] flush = new Card[5];
        flush[0] = new Card(Suit.SPADES, Rank.TWO);
        flush[1] = new Card(Suit.SPADES, Rank.FIVE);
        flush[2] = new Card(Suit.SPADES, Rank.ACE);
        flush[3] = new Card(Suit.SPADES, Rank.KING);
        flush[4] = new Card(Suit.SPADES, Rank.FOUR);
        assertEquals(ResultType.FLUSH, HandClassifier.handEvaluator(flush));
        Card[] straight = new Card[5];
        straight[0] = new Card(Suit.SPADES, Rank.TWO);
        straight[1] = new Card(Suit.HEARTS, Rank.THREE);
        straight[2] = new Card(Suit.CLUBS, Rank.FOUR);
        straight[3] = new Card(Suit.DIAMONDS, Rank.FIVE);
        straight[4] = new Card(Suit.SPADES, Rank.SIX);
        assertEquals(ResultType.STRAIGHT, HandClassifier.handEvaluator(straight));
        Card[] threeOfAKind = new Card[5];
        threeOfAKind[0] = new Card(Suit.SPADES, Rank.THREE);
        threeOfAKind[1] = new Card(Suit.CLUBS, Rank.THREE);
        threeOfAKind[2] = new Card(Suit.DIAMONDS, Rank.THREE);
        threeOfAKind[3] = new Card(Suit.SPADES, Rank.TWO);
        threeOfAKind[4] = new Card(Suit.HEARTS, Rank.TEN);
        assertEquals(ResultType.THREE_OF_A_KIND, HandClassifier.handEvaluator(threeOfAKind));
        Card[] twoPairs = new Card[5];
        twoPairs[0] = new Card(Suit.SPADES, Rank.TWO);
        twoPairs[1] = new Card(Suit.HEARTS, Rank.TWO);
        twoPairs[2] = new Card(Suit.SPADES, Rank.THREE);
        twoPairs[3] = new Card(Suit.DIAMONDS, Rank.THREE);
        twoPairs[4] = new Card(Suit.CLUBS, Rank.TEN);
        assertEquals(ResultType.TWO_PAIRS, HandClassifier.handEvaluator(twoPairs));
        Card[] pair;
        pair = twoPairs;
        pair[2].setRank(Rank.ACE);
        assertEquals(ResultType.PAIR, HandClassifier.handEvaluator(pair));
        Card[] highCard;
        highCard = pair;
        highCard[0].setRank(Rank.JACK);
        assertEquals(ResultType.HIGH_CARD, HandClassifier.handEvaluator(highCard));
    }
}