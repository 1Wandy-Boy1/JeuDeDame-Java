package main;

import java.util.Scanner;
import models.Board;
import models.Player;
import models.Token;

public class Main {

    public static final int SIZE = 9;
    private static Scanner input = new Scanner(System.in);
    private static boolean isPlayer1 = true;
    private static boolean endGameNow = false;
    
    public static void main(String[] args)
    {
        Board board = new Board(SIZE);
        Player player1;
        Player player2;
        boolean IA = false; 
        if (askIfTwoPlayer())
        {
            player1 = new Player(true);
            player2 = new Player(false);
        }
        else 
        {         
            player1 = new Player(true);
            player2 = new Player(false);
            IA = true;
        }
        clearScreen();

        while ( !endGame(board) )
        {          
            if (isPlayer1)
            {
                board = player1.getMove(board);
            }
            else
            {
                if(IA == true){
                    board = player2.getMoveIa(board);    
                }else{
                    board = player2.getMove(board);
                }
            }
            isPlayer1 = !isPlayer1;
        }
    }
    private static boolean askIfTwoPlayer()
    {       
        while (true)
        {
            clearScreen();
            System.out.println("*******Welcome to Draughts Game!*******\n");
            System.out.println("[1] 1 Player Mode");
            System.out.println("[2] 2 Player Mode");
            System.out.println("\nEnter a number: ");
            String response = input.nextLine();
            switch (response.trim())
            {
                case "1":
                    return false;
                case "2":
                    return true;
                case "exit":
                    endGameNow();
                    return true;
            }
        }
    }
    private static boolean endGame(Board board)
    {
        if (endGameNow)
            return true;
        else
        {
            int movableWhiteNum = 0;
            int movableBlackNum = 0;
            for (int pos = 0; pos < board.size*board.size; pos++)
            {
                Token TokenHere = board.getValue(pos);
                if (TokenHere != null)
                {
                    Move[] movesHere = TokenHere.getAllPossibleMoves(board);
                    if (movesHere != null && movesHere.length > 0)
                    {
                        if (TokenHere.isWhite)
                            movableWhiteNum++;
                        else if (!TokenHere.isWhite)
                            movableBlackNum++;
                    }
                }
            }
            
            if (movableWhiteNum + movableBlackNum == 0)
                System.out.println("The game was a stalemate...");
            else if (movableWhiteNum == 0)
                System.out.println("Congratulations, Black, you win the game!");
            else if (movableBlackNum == 0)
                System.out.println("Congratulations, White, you win the game!");
            else
                return false;
            
            return true;
        }
    }
    public static void endGameNow()
    {
        endGameNow = true;
    }
    
    public static void clearScreen()
    {
        System.out.print("\n\n\n\n");
    }
}
