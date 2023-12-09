// Player.java for Palace by Sohum Berry
public class Player {
    private String name;
    private Deck hand;
    private Deck hiddenHand;
    private Deck topHand;
    // Holds the player's phase. 1=mainGame, 2=topGame, 3=endGame
    // Main game is when the deck hasn't been exhausted
    // Top game is when the deck has been depleted and a player is down to the face-up cards on the table
    // End game is when the top hand has been used and the player is down to blind guessing
    private int gamePhase;

    // Constructor creates empty Decks for each type of hand
    public Player(String name) {
        this.name = name;
        this.hand = new Deck(false);
        this.topHand = new Deck(false);
        this.hiddenHand = new Deck(false);
        this.gamePhase = 1;
    }

    // Getters
    public String getName() {
        return name;
    }
    public Deck getHand() {
        return hand;
    }
    public Deck getHiddenHand() {
        return hiddenHand;
    }
    public Deck getTopHand() {
        return topHand;
    }
    // Return the hand depending on which game phase the player is in
    public Deck getCurrentHand() {
        if (gamePhase == 1) {
            return hand;
        } else if (gamePhase == 2) {
            return topHand;
        } else if (gamePhase == 3) {
            return hiddenHand;
        }
        return null;
    }
    public int getGamePhase() {
        return gamePhase;
    }

    // Setters
    public void setTopPhase() {
        gamePhase = 2;
    }
    public void setEndPhase() {
        gamePhase = 3;
    }

    // Hides decks that are invisible while still showing the number of cards
    public String asterize(int count) {
        String out = "";
        for (int i = 0; i < count; i++) {
            out += "* ";
        }
        return out;
    }

    // Print each type of hand depending on it's visibility
    @Override
    public String toString() {
        String out = name + ":\n";
        out += "Hand:\n";
        if (!hand.isEmpty() && hand.isVisible()) {
            out += hand.toString();
        }
        else {
            out += asterize(hand.size());
        }
        out += "\nPalace:\n";
        if (!topHand.isEmpty() && topHand.isVisible()) {
            out += topHand.toString();
        }
        else {
            out += asterize(topHand.size());
        }
        out += "\n";
        if (!hiddenHand.isEmpty() && hiddenHand.isVisible()) {
            out += hiddenHand.toString();
        }
        else {
            out += asterize(hiddenHand.size());
        }
        return out;
    }
}
