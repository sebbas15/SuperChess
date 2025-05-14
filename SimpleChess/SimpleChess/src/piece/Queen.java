package piece;

import java.awt.Point;
import main.model.Board;
import main.model.GameState;

public class Queen extends Piece {
    private int teleportCount = 2;
    //check how many teleports remain
    public int getTeleportCount() {
        return teleportCount;
    }
    public Queen(int color, int col, int row) {
        super(color, col, row);
        type = Type.QUEEN;
        if (color == GameState.WHITE) {
            image = getImage("/piece/WQueen");
        } else {
            image = getImage("/piece/BQueen");
        }
    }
    
    //if uses remain active teleportation
     //sets the global GameState into teleport mode.
    public void activateTeleport() {
        if (teleportCount > 0) {
            GameState state = GameState.getInstance();
            state.setTeleportModeActive(true);
            System.out.println("Teleport mode activated.");
            teleportCount--;
        } else {
            System.out.println("No teleports remaining!");
        }
    }
    
   
     //Perform the teleport to the clicked square
     //Captures any enemy piece at the destination, updates position,
     //decrements counts and teleport mode
   

    public void teleport(Point pixelTarget) {
        GameState state = GameState.getInstance();
        if (!state.isTeleportModeActive()) return;

        int tc = (pixelTarget.x + Board.HALF_SQUARE_SIZE) / Board.SQUARE_SIZE;
        int tr = (pixelTarget.y + Board.HALF_SQUARE_SIZE) / Board.SQUARE_SIZE;

        //find a real target
        Piece realTarget = null;
        for (Piece p : state.getPieces()) {
            if (p.col == tc && p.row == tr && p.color != this.color) {
                realTarget = p;
                break;
            }
        }

        //ban King
        if (realTarget != null && realTarget.type == Type.KING) {
            System.out.println("Cannot teleport to capture the King!");
            state.setTeleportModeActive(false);
            return;
        }

        //capture any other and maybe play sound
        if (realTarget != null) {
            playCaptureSound();            
            state.removePiece(realTarget);
        }

        //move
        this.col = tc;
        this.row = tr;
        updatePosition();

        state.setTeleportModeActive(false);
        state.changePlayer();
        System.out.println("Queen teleported to: " + tc + "," + tr);
    }


     //If teleport mode is active, any destination is legal.
     //Otherwise fall back to normal queen movement rules.
    @Override
    public boolean canMove(int targetCol, int targetRow) {
        GameState state = GameState.getInstance();
        //teleport mode bypasses all checks
        if (state.isTeleportModeActive()) return true;
        
        int fromCol = this.col;
        int fromRow = this.row;
        int dcol    = targetCol - fromCol;
        int drow    = targetRow - fromRow;
        int absCol  = Math.abs(dcol);
        int absRow  = Math.abs(drow);

        //must move somewhere
        if (absCol == 0 && absRow == 0) return false;

        //must be straight or diagonal
        boolean straight = (dcol == 0 || drow == 0);
        boolean diagonal = (absCol == absRow);
        if (!straight && !diagonal) return false;

        //step vector
        int stepCol = Integer.compare(dcol, 0); 
        int stepRow = Integer.compare(drow, 0);

        //raycast from (fromCol,fromRow) towards (targetCol,targetRow),
        //stopping just before destination square
        int c = fromCol + stepCol, r = fromRow + stepRow;
        while (c != targetCol || r != targetRow) {
            if (state.getPieceAt(c, r) != null) {
                return false;  //blocked by some piece
            }
            c += stepCol;
            r += stepRow;
        }

        //now check destination empty/enemy
        Piece dest = state.getPieceAt(targetCol,targetRow);
        if (dest!=null && dest.color!=this.color) {
          this.hittingP = dest;
        }
        return dest==null||dest.color!=this.color;
    }

}
