package models;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

import main.Main;
import main.Move;

public class Player {

	    // Player VS Player (start)
	    Scanner input = new Scanner(System.in);    
	    boolean isWhite;

	    public Player(boolean isWhite)
	    {
			this.isWhite = isWhite;
	    }

	    public Board getMove(Board board)
	    {        
	        displayBoard(board, null);
	        Move[] possibleMoves;
	        while (true)
	        {
	            Token TokenMoving = getTokenFromUser(board);

	            if (TokenMoving == null)
	                return board;

	            possibleMoves = TokenMoving.getAllPossibleMoves(board);
	                       
	            if (possibleMoves == null)
	                System.out.println("This token can't move, you need to choose another:");
	            else
	            {
	                displayBoard(board, possibleMoves);
	                Move move = getMoveFromUser(possibleMoves);
	                if (move != null)
	                {
	                    board.applyMoveToBoard(move, TokenMoving);
	                    return board;
	                }
	            }
	        } 
	    }
	    private void displayBoard(Board board, Move[] possibleMoves)
	    {
	        Main.clearScreen();
	        for (int y = -1; y < board.size; y++)
	        {   
	            for (int x = -1; x < board.size; x++)
	            {
	                if (y == -1) 
	                {
	                    if (x != -1)
	                        System.out.print((char)(x + 65) + " _ ");
	                    else
	                        System.out.print("     ");
	                }
	                else if (x == -1)
	                {
	                    if (y != -1)
	                        System.out.print(" "+(y + 1) + " ");
	                }
	                else
	                {
	                    Token thisToken = board.getValue(x, y);
	                    
	                    if (possibleMoves != null)
	                    {
	                        boolean moveFound = false;
	                        
	                        for (int i = 0; i < possibleMoves.length; i++)
	                        {
	                            int[] move = possibleMoves[i].getEndingPosition();
	                            if (move[0] == x && move[1] == y)
	                            {
	                                System.out.print("| " + Integer.toString(i+1) + " ");
	                                moveFound = true;
	                            }
	                        }
	                        if (moveFound)
	                            continue;
	                    }
	                    if (thisToken != null)
	                        System.out.print("| " + thisToken.getString());
	                    else if (board.checkerboardSpace(x, y))
	                        System.out.print("| . ");
	                    else
	                        System.out.print("|   ");
	                    if(x==8) {
	                    	System.out.print("|   ");
	                    }
	                }
	            }
	            System.out.println();
	        }
	    }
	        private Token getTokenFromUser(Board board)
	    {
	        while (true)
	        {       
	            String raw;
	            
	            System.out.println(getColor() + ", please select a token to move [letter][number]:");
	            try
	            {    
	                raw = input.nextLine().toLowerCase();
	                if (raw.equalsIgnoreCase("exit"))
	                {
	                    Main.endGameNow();
	                    return null;
	                }
	                else if (raw.length() < 2)
	                    throw new Exception();
	                
	                char letterChar = raw.charAt(0);
	                char numberChar = raw.charAt(1);
	                if (letterChar < 97)
	                {
	                    letterChar = numberChar;
	                    numberChar = raw.charAt(0);
	                } 
	                int x = letterChar - 97;
	                int y = numberChar - 48 - 1;
	                if (board.outOfBoard(x, y))
	                    throw new Exception();              
	                Token userToken = board.getValue(x, y);
	                if (userToken == null)
	                    System.out.println("Bad coordinate choose another!\n");
	                else if (userToken.isWhite != this.isWhite)
	                    System.out.println("Is not your token\n");
	                else
	                    return userToken;  
	            }
	            catch (Exception e)
	            {
	               System.out.println("Enter a coordinate like'[letter][number]'");
	               continue;
	            }
	        }
	    }
	    private Move getMoveFromUser(Move[] possibleMoves)
	    {
	        int moveNum;
	        
	        while (true)
	        {       
	            System.out.println(getColor() + ", please select a move the its number (enter 0 to go back):");
	            try 
	            {
	                moveNum = input.nextInt();
	                input.nextLine();
	                if (moveNum == 0)
	                {
	                    return null;
	                }
	                else if (moveNum > possibleMoves.length)
	                    throw new Exception();                    
	                return possibleMoves[moveNum - 1];
	            }
	            catch (Exception e)
	            {
	               System.out.println("Please enter one of the numbers");
	               input.nextLine();
	            }
	        }
	    }
	    
	    private String getColor()
	    {
	        return isWhite ? "White" : "Black";
	    }
	        // Player VS Player (end)
	        
	        // Player VS IA (start)
	    
	        public Board getMoveIa(Board board)
	        {
	            HashMap<Token, Move[]> possibleChoices = new HashMap<Token, Move[]>();
	            for (int x = 0; x < board.size; x++)
	            {
	                for (int y = 0; y < board.size; y++)
	                {
	                    Token Token = board.getValue(x, y);
	                    if (Token != null && Token.isWhite == this.isWhite)
	                    {
	                        Move[] possibleMoves = Token.getAllPossibleMoves(board);
	                        if (possibleMoves != null)
	                            possibleChoices.put(Token, possibleMoves);
	                    }
	                }
	            }
	                   
	            Token furthestBackwardToken = possibleChoices.keySet().toArray(new Token[1])[0];
	            Token furthestForwardToken = possibleChoices.keySet().toArray(new Token[1])[0];
	            
	            HashMap<Move, Token> bestMovesPerToken = new HashMap<Move, Token>();
	            for (Token Token : possibleChoices.keySet())
	            {                
	                int thisTokenY = Token.getCoordinates()[1];
	                if (thisTokenY > furthestForwardToken.getCoordinates()[1])
	                {
	                    if (isWhite)
	                        furthestForwardToken = Token;
	                    else
	                        furthestBackwardToken = Token;
	                }
	                else if (thisTokenY < furthestBackwardToken.getCoordinates()[1])
	                {
	                    if (isWhite)
	                        furthestBackwardToken = Token;
	                    else
	                        furthestForwardToken = Token;
	                }
	                
	                Move[] possibleMoves = possibleChoices.get(Token);
	                Move maxJumpMove = possibleMoves[0];
	                int maxJumpMoveLength = 0;
	                for (int i = 0; i < possibleMoves.length; i++)
	                {
	                    Token[] jumpedTokens = possibleMoves[i].getJumpedTokens(board);
	                    if (jumpedTokens != null)
	                    {
	                        int jumpLength = jumpedTokens.length;
	                    
	                        if (jumpLength >= maxJumpMoveLength)
	                        {
	                            maxJumpMoveLength = jumpLength;
	                            maxJumpMove = possibleMoves[i];
	                        }
	                    }
	                }
	                bestMovesPerToken.put(maxJumpMove, Token);
	            }
	            
	            Move absoluteBestMove = bestMovesPerToken.keySet().toArray(new Move[1])[0];
	            int absoluteBestMoveJumpLength = 0;
	            for (Move move : bestMovesPerToken.keySet())
	            {
	                Token[] jumpedTokens = move.getJumpedTokens(board);
	                if (jumpedTokens != null)
	                {
	                    int thisBestMoveJumpLength = jumpedTokens.length;
	                    if (thisBestMoveJumpLength >= absoluteBestMoveJumpLength)
	                    {
	                        absoluteBestMoveJumpLength = thisBestMoveJumpLength;
	                        absoluteBestMove = move;
	                    }
	                }
	            }
	            
	            if (absoluteBestMoveJumpLength > 0)
	            {
	                board.applyMoveToBoard(absoluteBestMove, bestMovesPerToken.get(absoluteBestMove));
	            }
	            else
	            {
	                int randomNum = new Random().nextInt(2);
	                if (randomNum == 0)
	                {
	                    board.applyMoveToBoard(getKeyByValue(bestMovesPerToken, furthestBackwardToken), furthestBackwardToken);
	                }
	                else
	                {
	                    board.applyMoveToBoard(getKeyByValue(bestMovesPerToken, furthestForwardToken), furthestForwardToken);
	                }  
	            }
	            
	            return board;
	        }
	     
	        private <T, E> T getKeyByValue(HashMap<T, E> map, E value) 
	        {
	            for (Entry<T, E> entry : map.entrySet()) 
	            {
	                if (Objects.equals(value, entry.getValue())) 
	                {
	                    return entry.getKey();
	                }
	            }
	            return null;
	        }
	        
	    // Player VS IA (end)
}
