package piece;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;

import main.Sound;
import main.model.Board;
import main.model.GameState;

public class Piece {
    
    public Type type;
    public BufferedImage image;
    public int x, y;
    public int col, row, preCol, preRow;
    public int color;
    public Piece hittingP;
    public boolean moved, twoStepped;
    Sound sound = new Sound();
    Sound moveSound = new Sound();
    

    public Piece(int color, int col, int row) {
        this.color = color;
        this.col   = col;
        this.row   = row;
        this.x     = getX(col);
        this.y     = getY(row);
        this.preCol = col;
        this.preRow = row;    
    }
    
    
    
    
    //Return the path to the capture sound on the classpath
    protected int getCaptureSoundIndex() {
 
    	type = getType();
    	switch (type) {
    	    case PAWN:
    	        return color == GameState.WHITE ? 0 : 1; //0 = white_pawn, 1 = black_pawn
    	    case ROOK:
    	        return color == GameState.WHITE ? 2 : 3; //2 = white_rook, 3 = black_rook
    	    case KNIGHT:
    	        return color == GameState.WHITE ? 4 : 5; //etc...
    	    case BISHOP:
    	        return color == GameState.WHITE ? 6 : 7;
    	    case QUEEN:
    	        return color == GameState.WHITE ? 8 : 9;
    	    case KING:
    	        return color == GameState.WHITE ? 10 : 11;
    	    default:
    	        System.out.println("NO SOUND INDEX FOUND");
    	        return -1;
    	}
    }

    
    public void playSE(int i) {
    	sound.setFile(i);
    	sound.play();
    }
    
    public void playCaptureSound() {
        int index = getCaptureSoundIndex();
        if (index >= 0) {
            sound.setFile(index);
            sound.play();
        } 
        else {
            System.out.println("Invalid capture sound index: " + index);
        }
    }
    
    //play the move sound when moving without capturing */
    public void playMoveSound() {
        moveSound.setFile(20); //or any other empty slot you assign for "move sound"
        moveSound.play();
    }


    
    public void moveTo(int targetCol, int targetRow) {
        //find target in the REAL pieces list
        GameState state = GameState.getInstance();
        Piece target = null;
        for (Piece p : state.getPieces()) {
            if (p.col == targetCol && p.row == targetRow && p.color != this.color) {
                target = p;
                break;
            }
        }

        //if there was a real target, play its sound & remove it
        if (target != null) {
            playCaptureSound();
            state.removePiece(target);
        } else {
            playMoveSound();
        }

        //now move this piece
        this.col = targetCol;
        this.row = targetRow;
        updatePosition();
    }


    /** Remember the current square before dragging */
    public void setOriginalPosition() {
        this.preCol = this.col;
        this.preRow = this.row;
    }
    
    /** Load a PNG from the resources folder */
    public BufferedImage getImage(String imagePath) {    
        try {
            return ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public int getX(int col) {
        return col * Board.SQUARE_SIZE;
    }
    
    public int getY(int row) {
        return row * Board.SQUARE_SIZE;
    }
    
    public int getCol(int x) {
        return (x + Board.HALF_SQUARE_SIZE) / Board.SQUARE_SIZE;
    }
    
    public int getRow(int y) {
        return (y + Board.HALF_SQUARE_SIZE) / Board.SQUARE_SIZE;
    }
    
    /** Find this piece’s index in the simulation list */
    public int getIndex() {
        List<Piece> sim = GameState.getInstance().getSimPieces();
        for (int i = 0; i < sim.size(); i++) {
            if (sim.get(i) == this) {
                return i;
            }
        }
        return -1;
    }
    
    /** After a successful move, update pixel & board coords */
    public void updatePosition() {
        if (type == Type.PAWN && Math.abs(row - preRow) == 2) {
            twoStepped = true;
        }
        x = getX(col);
        y = getY(row);
        preCol = getCol(x);
        preRow = getRow(y);
        moved  = true;
    }
    
    /** Revert to the original square */
    public void resetPosition() {
        col = preCol;
        row = preRow;
        x   = getX(col);
        y   = getY(row);
    }
    
    /** Override in subclasses */
    public boolean canMove(int targetCol, int targetRow) {
        return false;
    }
    
    public boolean isWithinBoard(int targetCol, int targetRow) {
        return targetCol >= 0 && targetCol <= 7 && targetRow >= 0 && targetRow <= 7;
    }
    
    public boolean isSameSquare(int targetCol, int targetRow) {
        return targetCol == preCol && targetRow == preRow;
    }
    
    /** Return a piece occupying the target square, or null */
    public Piece getHittingP(int targetCol, int targetRow) {
        for (Piece p : GameState.getInstance().getSimPieces()) {
            if (p.col == targetCol && p.row == targetRow && p != this) {
                return p;
            }
        }
        return null;
    }
    
    /** True if the square is empty or holds an enemy piece */
    public boolean isValidSquare(int targetCol, int targetRow) {
        hittingP = getHittingP(targetCol, targetRow);
        if (hittingP == null) {
            return true;
        } else if (hittingP.color != this.color) {
            return true;
        } else {
            hittingP = null;
            return false;
        }
    }
    
    /** Check for blockers along a straight (rook-like) path */
    public boolean pieceIsOnStrightLine(int targetCol, int targetRow) {
        List<Piece> sim = GameState.getInstance().getSimPieces();
        // left
        for (int c = preCol - 1; c > targetCol; c--) {
            for (Piece p : sim) {
                if (p.col == c && p.row == targetRow) {
                    hittingP = p;
                    return true;
                }
            }
        }
        // right
        for (int c = preCol + 1; c < targetCol; c++) {
            for (Piece p : sim) {
                if (p.col == c && p.row == targetRow) {
                    hittingP = p;
                    return true;
                }
            }
        }
        // up
        for (int r = preRow - 1; r > targetRow; r--) {
            for (Piece p : sim) {
                if (p.col == targetCol && p.row == r) {
                    hittingP = p;
                    return true;
                }
            }
        }
        // down
        for (int r = preRow + 1; r < targetRow; r++) {
            for (Piece p : sim) {
                if (p.col == targetCol && p.row == r) {
                    hittingP = p;
                    return true;
                }
            }
        }
        return false;
    }
    
    /** Check for blockers along a diagonal (bishop-like) path */
    public boolean pieceIsOnDiagonalLine(int targetCol, int targetRow) {
        List<Piece> sim = GameState.getInstance().getSimPieces();
        // up-left & up-right
        if (targetRow < preRow) {
            for (int c = preCol - 1; c > targetCol; c--) {
                int diff = Math.abs(c - preCol);
                for (Piece p : sim) {
                    if (p.col == c && p.row == preRow - diff) {
                        hittingP = p;
                        return true;
                    }
                }
            }
            for (int c = preCol + 1; c < targetCol; c++) {
                int diff = Math.abs(c - preCol);
                for (Piece p : sim) {
                    if (p.col == c && p.row == preRow - diff) {
                        hittingP = p;
                        return true;
                    }
                }
            }
        }
        // down-left & down-right
        if (targetRow > preRow) {
            for (int c = preCol - 1; c > targetCol; c--) {
                int diff = Math.abs(c - preCol);
                for (Piece p : sim) {
                    if (p.col == c && p.row == preRow + diff) {
                        hittingP = p;
                        return true;
                    }
                }
            }
            for (int c = preCol + 1; c < targetCol; c++) {
                int diff = Math.abs(c - preCol);
                for (Piece p : sim) {
                    if (p.col == c && p.row == preRow + diff) {
                        hittingP = p;
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /** Getter for the piece type */
    public Type getType() {
        return type;
    }

    /** Draw this piece at its current pixel coords */
    public void draw(Graphics2D g2) {
        g2.drawImage(image, x, y, Board.SQUARE_SIZE, Board.SQUARE_SIZE, null);
    }
    
   //teleport helpers

    ///Pixel position getter for teleport logic
    public Point getPosition() {
        return new Point(x, y);
    }
    
    //Teleport setter for pixel position
    public void setPosition(Point newPos) {
        this.col = getCol(newPos.x);
        this.row = getRow(newPos.y);
        this.x   = getX(col);
        this.y   = getY(row);
    }
}