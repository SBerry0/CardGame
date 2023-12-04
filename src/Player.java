import java.util.ArrayList;

public class Player {
    private String name;
    private Deck hand;
    private Deck hiddenHand;

    public Player(String name) {
        this.name = name;
        this.hand = new Deck(true);
        this.hiddenHand = new Deck(false);
    }

    public String getName() {
        return name;
    }

    public Deck getHand() {
        return hand;
    }

    public boolean hasCard(Card card) {
        if (hiddenHand.isVisible()) {
            return hiddenHand.hasCard(card) || hand.hasCard(card);
        }
        return hand.hasCard(card);
    }

    public void addCard(Deck deck) {
        hand.addCard(deck.deal());
    }

    public void addHiddenCard(Deck deck) {
        hiddenHand.addCard(deck.deal());
    }

    public void printHand() {
        System.out.print(name + ": ");
        if (!hand.isEmpty()) {
            System.out.println(hand);
        }
        if (!hiddenHand.isVisible()) {
            System.out.print("\t");
            for (int i = 0; i < hiddenHand.size(); i++) {
                System.out.print("* ");
            }
        }
        else {
            System.out.println(hiddenHand);
        }
    }

    @Override
    public String toString() {
        return name + "'s cards: " + hand;
    }
}
