import java.util.ArrayList;

public class Deck {
    private ArrayList<Card> cards;
    private int cardsLeft;
    private boolean isVisible;

    public Deck(int[] points, String[] suits, String[] ranks) {
        cards = new ArrayList<Card>();
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < suits.length; j++) {
                cards.add(new Card(suits[j], ranks[i], points[i]));
            }
        }
        this.cardsLeft = cards.size();
        isVisible = true;
    }

    public Deck(boolean isVisible) {
        cards = new ArrayList<Card>();
        cardsLeft = 0;
        this.isVisible = isVisible;
    }

    public Card getCard() {
        return cardsLeft == 0 ? null : cards.get(cardsLeft-1);
    }

    public Card getCard(int index) {
        return cardsLeft == 0 ? null : cards.get(index);
    }

    public boolean isEmpty() {
        return cardsLeft == 0;
    }

    public int getCardsLeft() {
        return cardsLeft;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void makeVisible() {
        isVisible = true;
    }

    public void makeInvisible() {
        isVisible = false;
    }

    public int size() {
        return cards.size();
    }

    public boolean hasCard(Card other) {
        for (Card card : cards) {
            if (other.equals(card)) {
                return true;
            }
        }
        return false;
    }

    public void addCard(Card card) {
        cards.add(card);
        cardsLeft++;
    }

    public Card takeCard(Card card) {
        if (cardsLeft == 0) { return null; }
        cards.remove(card);
        cardsLeft--;
        return card;
    }
    public Card takeCard(int index) {
        if (cardsLeft == 0) { return null; }
        cardsLeft--;
        return cards.remove(index);
    }

    public Card deal() {
        if (isEmpty()) {
            return null;
        }
        cardsLeft--;
        return cards.remove(cardsLeft);
    }

    public void shuffle() {
        cardsLeft = cards.size();
        for (int i = cardsLeft - 1; i >= 0; i--) {
            int r = (int) (Math.random() * cardsLeft - 1);
            cards.add(i > r ? r : r + 1, cards.remove(i));
        }
    }

    public String toString() {
        String out = "";
        for (Card card :
                cards) {
            out += card.toString();
            out += ", ";
        }
        return out.substring(0, out.length()-2);
    }
}