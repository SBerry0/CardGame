// Game.java for Palace by Sohum Berry

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Scanner;

public class Game {
    public static final int UNFINISHED = 0;
    public static final Image back = new ImageIcon("Resources/Cards/back.png").getImage();
    public static final int CARD_HEIGHT = 100,
                            CARD_WIDTH = 71;

    public static final int DECK_PADDING = 20,
                            HAND_X_PADDING_SHORT = (GameViewer.WINDOW_WIDTH / 2 - DECK_PADDING - CARD_WIDTH)/2 - 40,
                            HAND_Y_PADDING_LONG = GameViewer.WINDOW_HEIGHT/2 - CARD_HEIGHT + 20,
                            HAND_X_PADDING_LONG = GameViewer.WINDOW_WIDTH/2  - CARD_WIDTH/2 - 40,
                            HAND_Y_PADDING_SHORT = 70;
    public static final int GAME_DECK_X = GameViewer.WINDOW_WIDTH / 2 + DECK_PADDING,
                            GAME_DECK_Y = GameViewer.WINDOW_HEIGHT / 2 - CARD_HEIGHT + 20,
                            PILE_X = GameViewer.WINDOW_WIDTH / 2 - DECK_PADDING - CARD_WIDTH,
                            PILE_Y = GameViewer.WINDOW_HEIGHT / 2 - CARD_HEIGHT + 20;


    private Deck gameDeck;
    private Deck pile;
    private Player[] players;
    private int numPlayers;
    private Scanner s;
    private GameViewer viewer;
    private int wonState;

    // Constructor that creates a full deck of cards and a Player for each player
    public Game() {
        wonState = UNFINISHED;
        s = new Scanner(System.in);
        pile = new Deck(true, PILE_X, PILE_Y);
        gameDeck = new Deck(new int[] {14, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13},
                new String[] {"Spades", "Heart", "Diamonds", "Clubs"},
                new String[] {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"}
//                new String[] {"2", "3", "4", "5", "6", "7"}
        );
        gameDeck.shuffle();
        do {
            System.out.println("How many people are playing?");
            numPlayers = s.nextInt();
            s.nextLine();
        } while (numPlayers < 2 || numPlayers > 4);
        // Create each player with their own specific x and y values depending on how many
        players = new Player[numPlayers];
        String[] ordering = {"first", "second", "third", "fourth"};
        System.out.println("What's the " + ordering[0] + " player's name?");
        String name = s.nextLine();
        players[0] = new Player(name, HAND_X_PADDING_LONG,
                GameViewer.WINDOW_HEIGHT-HAND_Y_PADDING_SHORT-CARD_HEIGHT, true);
        if (numPlayers == 2) {
            System.out.println("What's the " + ordering[1] + " player's name?");
            String name2 = s.nextLine();
            players[1] = new Player(name2, HAND_X_PADDING_LONG, HAND_Y_PADDING_SHORT, false);
        } else if (numPlayers >= 3) {
            System.out.println("What's the " + ordering[1] + " player's name?");
            String name3 = s.nextLine();
            players[1] = new Player(name3, HAND_X_PADDING_SHORT, HAND_Y_PADDING_LONG, false);
            System.out.println("What's the " + ordering[2] + " player's name?");
            String name4 = s.nextLine();
            players[2] = new Player(name4, HAND_X_PADDING_LONG, HAND_Y_PADDING_SHORT, false);
            if (numPlayers == 4) {
                System.out.println("What's the " + ordering[3] + " player's name?");
                String name5 = s.nextLine();
                players[3] = new Player(name5,
                        GameViewer.WINDOW_WIDTH-HAND_X_PADDING_SHORT-80, HAND_Y_PADDING_LONG, false);
            }
        }
        viewer = new GameViewer(this);
    }

    // Deals a card from the game deck into the hand and hidden hand to start the game
    public void deal() {
        for (int i = 0; i < numPlayers; i++) {
            gameDeck.shuffle();
            gameDeck.shuffle();
            for (int j = 0; j < 6; j++) {
                players[i].getHand().addCard(gameDeck.deal());
            }
            for (int j = 0; j < 3; j++) {
                players[i].getHiddenHand().addCard(gameDeck.deal());
            }
        }
    }

    // Shuffles the array of players by using a temp player
    public void shufflePlayers() {
        for (int i = 0; i < numPlayers; i++) {
            int remaining = numPlayers - i;

            int r = (int) (Math.random() * remaining);
            if (r == remaining) {
                break;
            }
            Player temp = players[i];
            players[i] = players[r];
            players[r] = temp;
        }
    }

    // Determines if any card in a player's hand is higher than the top card
    public boolean canPlayCard(Deck hand) {
        for (int i = 0; i < hand.size(); i++) {
            if (hand.getCard(i).isGreater(pile.getCard()) || hand.getCard(i) instanceof SpecialCard) {
                return true;
            }
        }
        return false;
    }

    public void printWin(Player player) {
        System.out.println(player.getName() + " won!");
    }

    // Print the fancy Palace and directions
    public void printInstructions() {
        System.out.println(
                """
                        .------..------..------..------..------..------.
                        |P.--. ||A.--. ||L.--. ||A.--. ||C.--. ||E.--. |
                        | :/\\: || (\\/) || :/\\: || (\\/) || :/\\: || (\\/) |
                        | (__) || :\\/: || (__) || :\\/: || :\\/: || :\\/: |
                        | '--'P|| '--'A|| '--'L|| '--'A|| '--'C|| '--'E|
                        `------'`------'`------'`------'`------'`------'
                        """
        );
        System.out.println("https://www.wikihow.com/Play-the-Palace-Card-Game\n");
    }

    // Determines if all the cards in the hand at each index are the same rank
    public boolean isSameRank(Deck hand, int[] indices) {
        for (int i = 0; i < indices.length; i++) {
            if (!hand.getCard(indices[i]).equals(hand.getCard(indices[0]))) {
                return false;
            }
        }
        return true;
    }

    // If the hand given has less than 3 cards, draw until they do
    public void replenish(Deck hand) {
        while (hand.size() < 3 && !gameDeck.isEmpty()) {
            hand.addCard(gameDeck.deal());
        }
    }

    // Prints the whole game, only showing visible decks.
    // I chose to print it all at once so that each player could see how many the other had left
    //and what their top cards were
    public void printGame() {
        System.out.println("___________________________________");
        // OR, in the actual terminal to clear screen:
        // System.out.print("\033[H\033[2J");
        // System.out.flush();
        for (int i = 0; i < numPlayers - 1; i++) {
            System.out.println("-----------------------------------");
            System.out.println(players[i]);
        }
        // In order to have the right spacing
        System.out.println("-----------------------------------");
        System.out.println(players[players.length - 1]);
        System.out.println("___________________________________");
        System.out.println("\n\nTop Card: " + (pile.getCard() == null ? "Empty" : pile.getCard()));
        System.out.println("Cards left in deck: " + gameDeck.getCardsLeft());
    }
    // Prints an individual player for the initialization
    public void printGame(Player player) {
        System.out.println("___________________________________");
        System.out.println(player);
        System.out.println("\n\nTop Card: " + (pile.getCard() == null ? "Empty" : pile.getCard()));
    }

    // Repeats each player's turn in each stage until someone gets rid of all their cards
    public void playGame() {
        while (wonState == UNFINISHED) {
            for (int i = 0; i < numPlayers; i++) {
                Player player = players[i];
                int phase = player.getGamePhase();
                playGamePhase(player);
                if (!player.getHand().isEmpty()) {
                    player.setStartPhase();
                }
                if (gameDeck.isEmpty() && player.getHand().isEmpty() && phase == 1) {
                    player.setTopPhase();
                } else if (gameDeck.isEmpty() && player.getTopHand().isEmpty() && phase == 2) {
                    player.setEndPhase();
                } else if (gameDeck.isEmpty() && player.getHand().isEmpty() && player.getTopHand().isEmpty()
                        && player.getHiddenHand().isEmpty() && phase == 3) {
                    wonState = i + 1;
                    printWin(player);
                    break;
                }
            }
        }
    }

    public int getWonState() {
        return wonState;
    }

    public int[] filterSpecials(int[] indices, Deck hand) {
        boolean isTwo = false;
        boolean isTen = false;
        for (int i = 0; i < indices.length; i++) {
            if (hand.getCard(i) instanceof SpecialCard) {
                if (((SpecialCard) (hand.getCard(i))).isDoesClear()) {
                    if (!isTen) {
                        indices[i] = -1;
                        hand.deal(i--);
                    }
                    isTen = true;
                }
                else {
                    if (!isTwo) {
                        indices[i] = -1;
                        hand.deal(i--);
                    }
                    isTwo = true;
                }
            }
        }
        if (isTen && isTwo) {
            for (int i = 0; i < indices.length; i++) {
                if (hand.getCard(i) instanceof SpecialCard && !((SpecialCard) hand.getCard(i)).isDoesClear()) {
                    indices[i] = -1;
                }
            }
        }
        int numEmpty = 0;
        for (int i : indices) {
            if (i == -1) {
                numEmpty++;
            }
        }
        int[] out = new int[indices.length - numEmpty];
        int count = 0;
        for (int i = 0; i < indices.length; i++) {
            if (indices[i] != -1) {
                out[count++] = indices[i];
            }
        }
        return out;
    }

    public Deck getGameDeck() {
        return gameDeck;
    }

    public Deck getPile() {
        return pile;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void playGamePhase(Player player) {
        for (int i = 0; i < 1; i++) {
            System.out.println(player.getName() + ", are you ready?");
            s.nextLine();
            Deck hand = player.getCurrentHand();
            if (player.getGamePhase() != 3) {
                hand.makeVisible();
            }
            viewer.repaint();
            printGame();
            if (player.getGamePhase() != 3) {
                if (canPlayCard(hand)) {
                    int[] indices = getIndices(hand, true);
                    for (int j = 0; j < indices.length; j++) {
                        int result = playCard(player, hand.getCard(indices[j]));
                        for (int k = 0; k < indices.length; k++) {
                            indices[k]--;
                        }
                        if (result == 1) {
                            System.out.println("You can't play that card! Try again.");
                            i--;
                        } else if (result == 2) {
                            System.out.println("You get to go again!");
                            if (player.getGamePhase() == 1) {
                                replenish(hand);
                            }
                            i--;
                        } else {
                            replenish(hand);
                        }
                    }
                    System.out.println("Your hand is now:\n" + player);
                    viewer.repaint();
                } else {
                    System.out.println("You cannot play a card greater than the top card!");
                    while (!pile.isEmpty()) {
                        player.getCurrentHand().addCard(pile.deal(pile.size() - 1));
                    }
                    System.out.println("You get the pile! You now have " + hand.size() + " cards");
                }
            }
            else {
                // The final stage of the game.
                boolean first = true;
                int index = 0;
                do {
                    if (!first) {

                        System.out.println("Invalid indices. Choose cards from 1 to " + hand.getCardsLeft() + ".");
                    }
                    System.out.println("Which card do you choose to play? (1-" + hand.getCardsLeft() + ")");
                    index = s.nextInt();
                    first = false;
                } while (index < 0 || index >= hand.getCardsLeft());
                int result = playCard(player, hand.getCard(index));
                if (result == 1) {
                    System.out.println("Uh Oh! You get a hand again!");
                    player.setStartPhase();
                    player.getCurrentHand().addCard(hand.getCard(index));
                    while (!pile.isEmpty()) {
                        player.getCurrentHand().addCard(pile.deal(pile.size() - 1));
                    }

                } else if (result == 2) {
                    System.out.println("You get to go again!");
                    i--;
                }
                viewer.repaint();
                System.out.println("Your hand is now:\n" + player);
            }
            hand.makeInvisible();
        }
    }

    // Asks the user for the indices of the cards they want to play in a hand
    // It only allows the same cards if beSame is true.
    private int[] getIndices(Deck hand, boolean beSame) {
        String[] indicesArray;
        int[] indices;
        boolean first = true;
        do {
            if (!first && beSame) {

                System.out.println("Invalid indices. Multiple cards must be of the same rank.");
            }
            if (!first && !beSame) {
                System.out.println("You must choose three cards to create your top hand." +
                        "(I recommend choosing your better cards)");
            }
            System.out.println("Which card(s) do you choose" + (!beSame ? " for your top hand" : " to play") +
                    "? (1-" + hand.getCardsLeft() + ")");
            String indicesString = s.nextLine();
            indicesArray = indicesString.split(", ");
            indices = new int[indicesArray.length];
            for (int j = 0; j < indicesArray.length; j++) {
                indices[j] = Integer.valueOf(indicesArray[j]) - 1;
            }
            first = false;
        } while (
                indicesArray.length > hand.getCardsLeft() ||
                        (beSame && !isSameRank(hand, indices)) ||
                        indices[0] < 0 ||
                        (!beSame && indicesArray.length != 3)
        );
        Arrays.sort(indices);
        return filterSpecials(indices, hand);
    }

    //  Asks each player to make their top hand
    public void initGame() {
        deal();
        // Go through each player and ask them to choose the three for the top cards
        System.out.println("Shuffling order...");
        shufflePlayers();
        for (int i = 0; i < numPlayers; i++) {
            Player player = players[i];
            System.out.println(player.getName() + ", are you ready?");
            s.nextLine();
            player.getHand().makeVisible();
            printGame(player);
            viewer.repaint();
            int[] indices = getIndices(player.getHand(), false);
            for (int j = 0; j < indices.length; j++) {
                player.getTopHand().addCard(player.getHand().deal(indices[j]));
                for (int k = 0; k < indices.length; k++) {
                    indices[k]--;
                }
            }
            player.getTopHand().makeVisible();
            System.out.println("Your hand is now:");
            System.out.println(player);
            viewer.repaint();
            player.getHand().makeInvisible();
        }
    }

    public int playCard(Player player, Card card) {
        // Take from player's hand and make it the top card after checking for special cases
        if (player.getCurrentHand().hasCard(card)) {
            // Check for special cards 2 and 10
            if (card instanceof SpecialCard) {
                pile.addCard(player.getCurrentHand().deal(card));
                if (((SpecialCard) card).isDoesClear()) {
                    pile.clearDeck();
                }
                return 2;
            }
            if (card.isGreater(pile.getCard())) {
                pile.addCard(player.getCurrentHand().deal(card));
                return 0;
            }
        }
        return 1;
    }

    public void play() {
        printInstructions();
        shufflePlayers();
        initGame();
        playGame();
    }
    public static void main(String[] args) {
        Game g = new Game();
        g.play();
    }
}