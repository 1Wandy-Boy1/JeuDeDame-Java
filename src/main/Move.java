package main;

import java.util.ArrayList;
import java.util.Arrays;
import models.Board;
import models.Token;

public class Move {

    int x1, y1, x2, y2;
    Move precedingMove;
    boolean isJump;
    
    public Move(int x1, int y1, int x2, int y2, Move precedingMove, boolean isJump)
    {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.precedingMove = precedingMove;
        this.isJump = isJump;
    }

    public int[] getStartingPosition()
    {
        int[] position = new int[2];
        position[0] = x1;
        position[1] = y1;
        return position;
    }
    
    public int[] getEndingPosition()
    {
        int[] position = new int[2];
        position[0] = x2;
        position[1] = y2;
        return position;
    }
    
    public Token[] getJumpedTokens(Board board)
    {
        if (isJump)
        {
            ArrayList<Token> Tokens = new ArrayList<Token>();
            
            int TokenX = (x1 + x2)/2;
            int TokenY = (y1 + y2)/2;
            
            Tokens.add(board.getValue(TokenX, TokenY));
            
            if (precedingMove != null)
            {
                Tokens.addAll(Arrays.asList(precedingMove.getJumpedTokens(board))); 
            }
            
            Tokens.trimToSize();
            return Tokens.toArray(new Token[1]);
        }
        else
            return null;
    }
}
