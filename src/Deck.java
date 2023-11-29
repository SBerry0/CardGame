import java.util.ArrayList;

public class Deck {
    private ArrayList<Card> cards;
    private int cardsLeft;

    public Deck(int[] points, String[] ranks, String[] suits) {
        cards = new ArrayList<Card>();
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < suits.length; j++) {
                cards.add(new Card(suits[j], ranks[i], points[i]));
            }
        }
        this.cardsLeft = cards.size();
    }

    public boolean isEmpty() {
        return cardsLeft == 0;
    }

    public int getCardsLeft() {
        return cardsLeft;
    }

    public Card deal() {
        if (isEmpty()) {
            return null;
        }
        cardsLeft--;
        return cards.get(cardsLeft + 1);
    }

    public void shuffle() {
        cardsLeft = cards.size();
        for (int i = cardsLeft; i > 0; i--) {
            int r = (int) (Math.random() * cardsLeft);
            cards.add(i>r ? r : r+1, cards.remove(i));
        }
    }
}
