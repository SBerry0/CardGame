import java.util.Scanner;

public class Game {
    private Deck gameDeck;
    private Deck pile;
    private Player[] players;
    private int numPlayers;
    private Scanner s = new Scanner(System.in);
    private boolean isWon;

    public Game() {
        isWon = false;
        pile = new Deck(true);
        gameDeck = new Deck(new int[] {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14},
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

    public boolean canPlayCard(Deck hand) {
        for (int i = 0; i < hand.size(); i++) {
            if (hand.getCard(i).isGreater(pile.getCard())) {
                return true;
            }
        }
        return false;
    }

    public void playGame() {
        System.out.println("Shuffling order...");
        shufflePlayers();
        while (!isWon) {
            for (Player player : players) {
                int phase = player.getGamePhase();
                playMainGame(player, phase);
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

    public void printWin(Player player) {
        System.out.println(player.getName() + " won!");
    }

    public void printInstructions() {
        System.out.println("Instructions...");
    }

    public void playMainGame(Player player, int gamePhase) {
        for (int i = 0; i < 1; i++) {
            System.out.println(player.getName() + ", are you ready?");
            String ans = s.nextLine();
            Deck hand = gamePhase == 1 ? player.getHand() : gamePhase == 2 ? player.getTopHand() : player.getHiddenHand();
            hand.makeVisible();
            printGame();
            if (canPlayCard(hand)) {
                int[] indices = getIndices(hand);
                for (int j = 0; j < indices.length; j++) {
                    boolean isPlayed = playCard(player, hand.getCard(indices[j]));
                    if (!isPlayed) {
                        System.out.println("You can't play that card! Try again.");
                        i--;
                    } else {
                        if (gamePhase == 1) { replenish(hand); }
                        System.out.println("Your hand is now: " + player);
                    }
                }
            } else {
                System.out.println("You cannot play a card greater than the top card!");
                while (!pile.isEmpty()) {
                    player.getHand().addCard(pile.takeCard(pile.size() - 1));
                }
                pile.addCard(gameDeck.deal());
                System.out.println("You get the pile! You now have " + hand.size() + " cards");
            }
            hand.makeInvisible();
        }
    }

    private int[] getIndices(Deck hand) {
        String[] indicesArray;
        int[] indices;
        boolean first = true;
        do {
            if (!first) {
                System.out.println("Invalid indices. Multiple cards must be of the same rank.");
            }
            System.out.println("Choose which card(s) you would like to play. (1-" + hand.getCardsLeft() + ")");
            String indicesString = s.nextLine();
            indicesArray = indicesString.split(", ");
            indices = new int[indicesArray.length];
            for (int j = 0; j < indicesArray.length; j++) {
                indices[j] = Integer.valueOf(indicesArray[j]) - 1;
            }
            first = false;
        } while (
                indicesArray.length > hand.getCardsLeft() ||
                        !isSameIndices(hand, indices) ||
                        indices[0] < 0
        );
        return indices;
    }

    public boolean isSameIndices(Deck hand, int[] indices) {
        for (int i = 0; i < indices.length; i++) {
            if (!hand.getCard(i).equals(hand.getCard(0))) {
                return false;
            }
        }
        return true;
    }

    public void replenish(Deck hand) {
        // If the hand given has less than 3 cards, draw until they do
        while (hand.size() < 3 && !gameDeck.isEmpty()) {
            hand.addCard(gameDeck.deal());
        }
    }

    public void printGame() {
        System.out.println("___________________________________");
        // OR, in the actual terminal to clear screen:
        // System.out.print("\033[H\033[2J");
        // System.out.flush();
        for (int i = 0; i < numPlayers - 1; i++) {
            System.out.println(players[i]);
        }
        // In order to have the right spacing
        System.out.println(players[players.length - 1]);
        System.out.println("\n\nTop Card: " + pile.getCard());
        System.out.println("Cards left in deck: " + gameDeck.getCardsLeft());
    }

    public void printGame(Player player) {
        System.out.println("___________________________________");
        System.out.println(player);
        System.out.println("\n\nTop Card: " + pile.getCard());
    }

    public void initGame() {
        // TODO: Go through each player and ask them to choose the three for the top cards
    }

    public boolean playCard(Player player, Card card) {
        // Take from player's hand and make it the top card after checking for special cases
        if (player.hasCard(card)) {
            if (card.isGreater(pile.getCard())) {
                player.getHand().takeCard(card);
                pile.addCard(new Card(card.getSuit(), card.getRank(), card.getPoint()));
                // TODO: Check for special cards 2 and 10
                return true;
            }
        }
        return false;
    }

    public void play() {
        printInstructions();
        deal();
        initGame();
        playGame();
    }
    public static void main(String[] args) {
        Game g = new Game();
        g.play();
    }
}
