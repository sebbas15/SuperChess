package piece;

import main.model.GameState;

public class Rook extends Piece{

    public Rook(int color, int col, int row) {
        super(color, col, row);
        
        type = Type.ROOK;


        if(color == GameState.WHITE){
            image = getImage("/piece/WRook");
        }
        else{
            image = getImage("/piece/BRook");

        }
    }
    public boolean canMove(int targetCol, int targetRow) {
    	
    	if(isWithinBoard(targetCol,targetRow) && isSameSquare(targetCol,targetRow) == false) {
    		
    		//Rook can move as long as either its col or row is the same
    		if(targetCol == preCol || targetRow == preRow) {
    			if(isValidSquare(targetCol,targetRow) && pieceIsOnStrightLine(targetCol,targetRow) == false) {
    				return true;
    			}
    			
    		}
    	}
    	
    	return false;
    }
    
}
