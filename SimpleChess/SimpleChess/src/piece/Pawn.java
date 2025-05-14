package piece;

import main.model.GameState;

public class Pawn extends Piece{

    public Pawn(int color, int col, int row){
        super(color, col, row);
        
        type = Type.PAWN;

        if(color == GameState.WHITE){
            image = getImage("/piece/WPawn");
        }
        else{
            image = getImage("/piece/BPawn");
            
        }
    }
    
    public boolean canMove(int targetCol, int targetRow) {
		
		if(isWithinBoard(targetCol,targetRow) && isSameSquare(targetCol,targetRow) == false) {
			
			
			//define the move value based on its color
			int moveValue;
			if(color == GameState.WHITE) {
				moveValue = -1;
			}
			else {
				moveValue = 1;
			}
			
			//check the hitting piece
			hittingP = getHittingP(targetCol,targetRow);
			
			//one square for the movement
			if(targetCol == preCol && targetRow == preRow + moveValue && hittingP == null) {
				return true;
			}
			//square movement
			if(targetCol == preCol && targetRow == preRow + moveValue*2 && hittingP == null & moved == false && 
					pieceIsOnStrightLine(targetCol,targetRow) == false) {
				return true;
			}
			
			//diagonal movement and capture(if a piece is on a square diagonally in front of it)
			if(Math.abs(targetCol - preCol) == 1 && targetRow == preRow + moveValue && hittingP != null &&
					hittingP.color != color) {
				return true;
			}
			
			//En Passant
			if(Math.abs(targetCol - preCol) == 1 && targetRow == preRow + moveValue) {
				for(Piece piece :  GameState.getInstance().getSimPieces()) {
					if(piece.col == targetCol && piece.row == preRow && piece.twoStepped == true) {
						hittingP = piece;
						return true;
					}
				}
			}
			
		}
		return false;
    }
}

