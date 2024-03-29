// Deck.java for Palace by Sohum Berry
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Deck {
    private ArrayList<Card> cards;
    private boolean isVisible;
    int originX;
    int originY;

    // Constructor
    public Deck(int[] points, String[] suits, String[] ranks) {
        cards = new ArrayList<Card>();
        for (int i = 0; i < ranks.length; i++) {
            for (int j = 0; j < suits.length; j++) {
//                System.out.println("Resources/Cards/" + (int) ((i*4)+j+1) + ".png");
                if (points[i] == 2) {
                    cards.add(new SpecialCard(suits[j], ranks[i], points[i],
                            new ImageIcon("Resources/Cards/" + (int) ((i*4)+j+1) + ".png").getImage(),
                            false));
                } else if (points[i] == 10) {
                    cards.add(new SpecialCard(suits[j], ranks[i], points[i],
                            new ImageIcon("Resources/Cards/" + (int) ((i*4)+j+1) + ".png").getImage(),
                            true));
                }
                else {
                    cards.add(new Card(suits[j], ranks[i], points[i],
                            new ImageIcon("Resources/Cards/" + (int) ((i*4)+j+1) + ".png").getImage()));
                }
            }
        }
        isVisible = true;
    }
    // Secondary constructor for an empty deck, usually a hand
    public Deck(boolean isVisible, int originX, int originY) {
        cards = new ArrayList<Card>();
        this.isVisible = isVisible;
        this.originX = originX;
        this.originY = originY;
    }

    // Getters
    public boolean isVisible() {
        return isVisible;
    }
    public int getCardsLeft() {
        return cards.size();
    }
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public int size() {
        return cards.size();
    }

    // Change visibility of decks
    public void makeVisible() {
        isVisible = true;
    }
    public void makeInvisible() {
        isVisible = false;
    }

    // Return specific card, either by index or last in the deck
    public Card getCard() {
        return cards.isEmpty() ? null : cards.get(cards.size()-1);
    }
    public Card getCard(int index) {
        return cards.isEmpty() ? null : cards.get(index);
    }

    // Determines if a card is in this deck
    public boolean hasCard(Card other) {
        for (Card card : cards) {
            if (other.equals(card)) {
                return true;
            }
        }
        return false;
    }

    // Adds a card to the deck
    public void addCard(Card card) {
        cards.add(card);
    }

    // Clears the deck and starts it with new
    public void clearDeck() {
        cards.clear();
    }


    // Takes a card from end of deck, at an index, or at the end of the deck and returns it
    public Card deal(Card card) {
        if (cards.isEmpty()) { return null; }
        cards.remove(card);
        return card;
    }
    public Card deal(int index) {
        if (cards.isEmpty()) { return null; }
        return cards.remove(index);
    }
    public Card deal() {
        if (isEmpty()) {
            return null;
        }
        return cards.remove(cards.size()-1);
    }

    // Randomly shuffles the deck
    public void shuffle() {
        int cardsLeft = cards.size();
        for (int i = cardsLeft - 1; i >= 0; i--) {
            int r = (int) (Math.random() * cardsLeft - 1);
            cards.add(i > r ? r : r + 1, cards.remove(i));
        }
    }

    public void draw(Graphics g, GameViewer viewer) {
        for (int i = 0; i < cards.size(); i++) {
            cards.get(i).draw(g, isVisible, originX + i*15, originY, viewer);
        }
    }

    public void draw(Graphics g, GameViewer viewer, boolean isVisible) {
        for (int i = 0; i < cards.size(); i++) {
            cards.get(i).draw(g, isVisible, originX + i*15, originY, viewer);
        }
    }

    // Prints each rank in the deck in a line separated by commas
    public String toString() {
        String out = "";
        for (Card card : cards) {
            out += card.toString();
            out += ", ";
        }
        // Omit the last two characters to remove the last " ,"
        return out.substring(0, out.length()-2);
    }


}