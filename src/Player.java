import java.awt.*;

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
    boolean isInverted;
    int originX;
    int originY;

    // Constructor creates empty Decks for each type of hand
    public Player(String name, int originX, int originY, boolean isInverted) {
        this.name = name;
        this.hand = new Deck(false, originX + 25, originY);
        this.topHand = new Deck(false,
                originX + 25, isInverted ? originY-20-Game.CARD_HEIGHT : originY+20 + Game.CARD_HEIGHT);
        this.hiddenHand = new Deck(false,
                originX + 25, isInverted ? originY-30-Game.CARD_HEIGHT : originY+30 + Game.CARD_HEIGHT);
        this.gamePhase = 1;
        this.isInverted = isInverted;
        this.originX = originX;
        this.originY = originY;
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
    public void setStartPhase() {
        gamePhase = 1;
    }

    public void setTopPhase() {
        gamePhase = 2;
    }
    public void setEndPhase() {
        gamePhase = 3;
    }

    public void draw(Graphics g, GameViewer viewer) {
        if (!hiddenHand.isEmpty() && hiddenHand.isVisible()) {
            hiddenHand.draw(g, viewer, true);
        }
        else if (!hiddenHand.isEmpty()) {
            hiddenHand.draw(g, viewer, false);
        }
        if (!topHand.isEmpty() && topHand.isVisible()) {
            topHand.draw(g, viewer, true);
        }
        else if (!topHand.isEmpty()){
            topHand.draw(g, viewer,false);
        }
        if (!hand.isEmpty() && hand.isVisible()) {
            hand.draw(g, viewer, true);
        }
        else if (!hand.isEmpty()) {
            hand.draw(g, viewer, false);
        }
        g.drawString(name, originX + 50, isInverted ? originY+Game.CARD_HEIGHT+20 : originY-20);
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
        if (!hand.isEmpty() && hand.isVisible()) {
            out += "Hand:\n";
            out += hand.toString();
        }
        else if (!hand.isEmpty()) {
            out += "Hand:\n";
            out += asterize(hand.size());
        }
        out += "\nPalace:\n";
        if (!topHand.isEmpty() && topHand.isVisible()) {
            out += topHand.toString();
        }
        else if (!topHand.isEmpty()){
            out += asterize(topHand.size());
        }
        out += "\n";
        if (!hiddenHand.isEmpty() && hiddenHand.isVisible()) {
            out += hiddenHand.toString();
        }
        else if (!hiddenHand.isEmpty()) {
            out += asterize(hiddenHand.size());
        }
        return out;
    }
}
