// Game.java for Palace by Sohum Berry
import java.util.Arrays;
import java.util.Scanner;

public class Game {
    private Deck gameDeck;
    private Deck pile;
    private Player[] players;
    private int numPlayers;
    private Scanner s = new Scanner(System.in);
    private boolean isWon;

    // Constructor that creates a full deck of cards and a Player for each player
    public Game() {
        isWon = false;
        pile = new Deck(true);
        gameDeck = new Deck(new int[] {15, 3, 4, 5, 6, 7, 8, 9, 15, 11, 12, 13, 14},
                new String[] {"Hearts", "Spades", "Diamonds", "Clubs"},
                new String[] {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"});
        gameDeck.shuffle();
        pile.addCard(gameDeck.deal());
        do {
            System.out.println("How many people are playing?");
            numPlayers = s.nextInt();
            s.nextLine();
        } while (numPlayers < 2 || numPlayers > 6);
        players = new Player[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            String[] ordering = {"first", "second", "third", "fourth", "fifth", "sixth"};
            System.out.println("What's the " + ordering[i] + " player's name?");
            String name = s.nextLine();
            players[i] = new Player(name);
        }
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
            if (hand.getCard(i).isGreater(pile.getCard())) {
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
        System.out.println("\n\nTop Card: " + pile.getCard());
        System.out.println("Cards left in deck: " + gameDeck.getCardsLeft());
    }
    // Prints an individual player for the initialization
    public void printGame(Player player) {
        System.out.println("___________________________________");
        System.out.println(player);
        System.out.println("\n\nTop Card: " + pile.getCard());
    }

    // Repeats each player's turn in each stage until someone gets rid of all their cards
    public void playGame() {
        while (!isWon) {
            for (Player player : players) {
                int phase = player.getGamePhase();
                playGamePhase(player, phase);
                if (gameDeck.isEmpty() && player.getHand().isEmpty() && phase == 1) {
                    player.setTopPhase();
                } else if (gameDeck.isEmpty() && player.getTopHand().isEmpty() && phase == 2) {
                    player.setEndPhase();
                } else if (gameDeck.isEmpty() && player.getHand().isEmpty() && player.getTopHand().isEmpty() && player.getHiddenHand().isEmpty() && phase == 3) {
                    isWon = true;
                    printWin(player);
                    break;
                }
            }
        }
    }

    public void playGamePhase(Player player, int gamePhase) {
        for (int i = 0; i < 1; i++) {
            System.out.println(player.getName() + ", are you ready?");
            s.nextLine();
            Deck hand = player.getCurrentHand();
            hand.makeVisible();
            printGame();
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
                        i--;
                    } else {
                        if (gamePhase == 1) { replenish(hand); }
                    }
                }
                System.out.println("Your hand is now:\n" + player);
            } else {
                System.out.println("You cannot play a card greater than the top card!");
                while (!pile.isEmpty()) {
                    player.getCurrentHand().addCard(pile.deal(pile.size() - 1));
                }
                pile.addCard(gameDeck.deal());
                System.out.println("You get the pile! You now have " + hand.size() + " cards");
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
                System.out.println("You must choose three cards to create your top hand. (I recommend choosing your better cards)");
            }
            System.out.println("Which card(s) do you choose" + (!beSame ? " for your top hand" : " to play") + "? (1-" + hand.getCardsLeft() + ")");
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
        return indices;
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
            int[] indices = getIndices(player.getHand(), false);
            for (int j = 0; j < 3; j++) {
                player.getTopHand().addCard(player.getHand().deal(indices[j]));
                for (int k = 0; k < indices.length; k++) {
                    indices[k]--;
                }
            }
            player.getTopHand().makeVisible();
            System.out.println("Your hand is now:");
            System.out.println(player);
            player.getHand().makeInvisible();
        }
    }

    public int playCard(Player player, Card card) {
        // Take from player's hand and make it the top card after checking for special cases
        if (player.getCurrentHand().hasCard(card)) {
            if (card.isGreater(pile.getCard())) {
                pile.addCard(player.getCurrentHand().deal(card));
                // Check for special cards 2 and 10
                if (pile.getCard().getRank().equals("2")) {
                    System.out.println("2 alert");
                    return 2;
                }
                else if (pile.getCard().getRank().equals("10")) {
                    System.out.println("10 alert");
                    pile.clearDeck();
                    return 2;
                }
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
