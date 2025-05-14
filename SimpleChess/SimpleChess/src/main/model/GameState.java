package main.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import main.ui.TurnPanel;
import piece.Bishop;
import piece.King;
import piece.Knight;
import piece.Pawn;
import piece.Piece;
import piece.Queen;
import piece.Rook;
import piece.Type;

/**
 * Central game state & logic for SimpleChess.
 */
public class GameState {
    public static final int WHITE = 0;
    public static final int BLACK = 1;

    //single reference
    private static GameState instance;

    //data of the board
    private final ArrayList<Piece> pieces    = new ArrayList<>();
    private final ArrayList<Piece> simPieces = new ArrayList<>();
    private final ArrayList<Piece> promoList = new ArrayList<>();

    //active pices and flags
    private Piece activePiece;
    private Piece checkingPiece;
    private Piece castlingPiece;
    

    private Piece promotionPawn;

    private int currentPlayer = WHITE;
    private boolean gameOver, stalemate;
    private boolean teleportModeActive = false;
    private boolean promotion = false;

    //mouse input by pixels
    private int mouseX, mouseY;
    private boolean mousePressed;
    
//    //turn panel
    private static TurnPanel turnPanel;
//    //Juan added this
    public static void setTurnPanel(TurnPanel panel) {
        turnPanel = panel;
		    }
    private boolean turnChanged = false;
    //used to keep track of which player has changed Juan
    public boolean hasTurnChanged() {
        return turnChanged;
    }

    public void resetTurnChanged() {
        turnChanged = false;
    }
    
    
    //constructor
    public GameState() {
        instance = this;
        initPieces();
        copyPieces(pieces, simPieces);
    }

    //get the GameState
    public static GameState getInstance() {
        return instance;
    }
    
    public List<Piece> getSimPieces() {
        return simPieces;
    }
    
    
    //Juan added this
    //expose a way to end the game
       public void endGame() {
    	   gameOver = true;
    	   if (turnPanel != null) {
    	        turnPanel.updateTurn(); //in case it needs to update
    	    }
       }  
    //removes piece if captured and gameOver if king is captured Juan added this
    public void removePiece(Piece p) {
        pieces.remove(p);
        simPieces.remove(p);
        if (p.getType() == Type.KING) {
        	System.out.println("GAME OVER");
            endGame();   //instantly end the game if a king was captured
        }
    }
    	  
    public List<Piece> getPieces() {
        return pieces;
    }

    //initializing pieces
    private void initPieces() {
        //white pawns
        for (int c = 0; c < 8; c++) {
            pieces.add(new Pawn(WHITE, c, 6));
        }
        //other white pieces
        pieces.add(new Rook  (WHITE, 0, 7));
        pieces.add(new Rook  (WHITE, 7, 7));
        pieces.add(new Knight(WHITE, 1, 7));
        pieces.add(new Knight(WHITE, 6, 7));
        pieces.add(new Bishop(WHITE, 2, 7));
        pieces.add(new Bishop(WHITE, 5, 7));
        pieces.add(new Queen (WHITE, 3, 7));
        pieces.add(new King  (WHITE, 4, 7));

        //black pawns
        for (int c = 0; c < 8; c++) {
            pieces.add(new Pawn(BLACK, c, 1));
        }
        //other pieces
        pieces.add(new Rook  (BLACK, 0, 0));
        pieces.add(new Rook  (BLACK, 7, 0));
        pieces.add(new Knight(BLACK, 1, 0));
        pieces.add(new Knight(BLACK, 6, 0));
        pieces.add(new Bishop(BLACK, 2, 0));
        pieces.add(new Bishop(BLACK, 5, 0));
        pieces.add(new Queen (BLACK, 3, 0));
        pieces.add(new King  (BLACK, 4, 0));
    }

    /** Utility to copy the master list into the simulation list */
    public void copyPieces(List<Piece> src, List<Piece> dst) {
        dst.clear();
        dst.addAll(src);
    }

    //input accesors
    public void setMousePosition(int x, int y) { mouseX = x; mouseY = y; }
    public void setMousePressed(boolean p)      { mousePressed = p; }
    public int  getMouseX()                     { return mouseX; }
    public int  getMouseY()                     { return mouseY; }
    public boolean isMousePressed()             { return mousePressed; }

    //active piece
    public Piece getActivePiece()               { return activePiece; }
    public void  setActivePiece(Piece p)        { activePiece = p; }

    //telport flags
    public boolean isTeleportModeActive()           { return teleportModeActive; }
    public void    setTeleportModeActive(boolean f) { teleportModeActive = f; }

    //castling
    public void setCastlingPiece(Piece p)       { castlingPiece = p; }
    public Piece getCastlingPiece()             { return castlingPiece; }

    //the game status
    public int  getCurrentPlayer()               { return currentPlayer; }
    public boolean isPromotion()                 { return promotion; }
    public boolean isGameOver()                  { return gameOver; }
    public boolean isKingInCheck() {
    	//reset the flag
    	checkingPiece = null;
        //opponentCanCaptureKing() will set checkingPiece if there is an attack
        return opponentCanCaptureKing();
    }

    /** Swap white↔black and reset two‑step flags */
    public void changePlayer() {
        currentPlayer = (currentPlayer == WHITE ? BLACK : WHITE);
        for (Piece p : pieces) {
            if (p.color == currentPlayer) p.twoStepped = false;
        }
        activePiece = null;
        //JUAN added this: keeps track of each turn
        turnChanged = true;
        //JUAN added this
        if (turnPanel != null) {
            turnPanel.updateTurn(); //refresh the GUI turn text
        }
    }
    
    public List<Point> getValidMoves() {
        List<Point> moves = new ArrayList<>();
        Piece active = getActivePiece();
        if (active == null) return moves;
        for (int c = 0; c < 8; c++) {
            for (int r = 0; r < 8; r++) {
                if (active.canMove(c, r)) {
                    moves.add(new Point(c, r));
                }
            }
        }
        return moves;
    }

    //main game loop update
    public void update() {
        if (gameOver || promotion || stalemate) return;

        //when mouse pressed pick up piece
        if (mousePressed) {
            if (activePiece == null) {
                int c = mouseX / Board.SQUARE_SIZE;
                int r = mouseY / Board.SQUARE_SIZE;
                for (Piece p : simPieces) {
                    if (p.color == currentPlayer && p.col == c && p.row == r) {
                        activePiece = p;
                        if (p.type == Type.QUEEN) ((Queen)p).setOriginalPosition();
                        break;
                    }
                }
            }
            return;
        }
        //mouse released drop and move
        if (activePiece != null) {
            int destCol = mouseX / Board.SQUARE_SIZE;
            int destRow = mouseY / Board.SQUARE_SIZE;

            //if teleport ability then teleport
            if (activePiece.type == Type.QUEEN && isTeleportModeActive()) {
                Point destPx = new Point(
                    mouseX - Board.HALF_SQUARE_SIZE,
                    mouseY - Board.HALF_SQUARE_SIZE
                );
                ((Queen)activePiece).teleport(destPx);
            }
            else if (activePiece.canMove(destCol, destRow)) {
                //move the pawn piece
                activePiece.moveTo(destCol, destRow);

                //checking if pawn promotion
                if (activePiece.getType() == Type.PAWN &&
                    ((activePiece.color == WHITE && destRow == 0) ||
                     (activePiece.color == BLACK && destRow == 7))) 
                {
                    //remember the pawn that is being promoted
                    promotionPawn = activePiece;
                    //show the promition pieces options
                    makePromoOptions();
                    //copy move into simPieces
                    copyPieces(pieces, simPieces);
                    return;
                }

                //else no promotion needed finish move
                copyPieces(pieces, simPieces);
                //end‐game inside moveTo/removePiece
                if (!gameOver) changePlayer();
            } else {
                //bounce back
                activePiece.resetPosition();
            }

            activePiece = null;
        }
    }
  
     //Returns the piece occupying (col,row), or null if empty.
    public Piece getPieceAt(int col, int row) {
        for (Piece p : simPieces) {
            if (p.col == col && p.row == row) return p;
        }
        return null;
    }

    //check blocking and for illegal moves
    private boolean isIllegalKingMove(Piece king) {
        if (king.type != Type.KING) return false;
        for (Piece p : simPieces) {
            if (p != king && p.color != king.color && p.canMove(king.col, king.row)) {
                return true;
            }
        }
        return false;
    }

   
    private boolean opponentCanCaptureKing() {
        Piece king = getKing(false);
        if (king == null) return false;

        for (Piece p : simPieces) {
            // enemy → our king’s color
            if (p.color != king.color) {
                // if p.canMove says it can go there, that’s a check
                if (p.canMove(king.col, king.row)) {
                    checkingPiece = p;  // record who’s checking
                    return true;
                }
            }
        }
        return false;
    }

    //find the king
    private Piece getKing(boolean opponent) {
        for (Piece p : simPieces) {
            if (p.type == Type.KING
             && (opponent ? p.color != currentPlayer : p.color == currentPlayer)) {
                return p;
            }
        }
        return null;
    }
    //Juan added this 
    //checking for checkmate
    private boolean isCheckmate() {
        //if king not in check, no mate
        if (!isKingInCheck()) return false;

        //check if the king can move
        Piece king = getKing(false);
        if (kingCanMove(king)) return false;

        //check if an allied piece can block or capture
        for (Piece p : simPieces) {
            if (p.color != currentPlayer) continue;
            for (int c = 0; c < 8; c++) {
                for (int r = 0; r < 8; r++) {
                    copyPieces(pieces, simPieces);
                    if (!p.canMove(c, r)) continue;
                    //simulate
                    Piece target = p.getHittingP(c, r);
                    if (target != null && target.color != p.color) {
                        simPieces.remove(target.getIndex());
                    }
                    p.col = c; p.row = r;
                    if (!opponentCanCaptureKing()) {
                        copyPieces(pieces, simPieces);
                        return false;  //found a defense
                    }
                }
            }
        }
        return true;  //no defense found
    }



    private boolean kingCanMove(Piece king) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx==0 && dy==0) continue;
                if (isValidKingMove(king, dx, dy)) return true;
            }
        }
        return false;
    }

    private boolean isValidKingMove(Piece king, int dc, int dr) {
        king.col += dc; king.row += dr;
        boolean ok = false;
        if (king.canMove(king.col, king.row)) {
            if (king.hittingP != null) {
                simPieces.remove(king.hittingP.getIndex());
            }
            if (!isIllegalKingMove(king)) {
                ok = true;
            }
        }
        king.resetPosition();
        copyPieces(pieces, simPieces);
        return ok;
    }

    private boolean isStalemate() {
        int oppCount = 0;
        for (Piece p : simPieces) if (p.color != currentPlayer) oppCount++;
        return oppCount==1 && !kingCanMove(getKing(true));
    }

    //castling path check ────────────────
    private void checkCastling() {
        if (castlingPiece == null) return;
        if (castlingPiece.col == 0) {
            castlingPiece.col += 3;
        } else if (castlingPiece.col == 7) {
            castlingPiece.col -= 2;
        }
        castlingPiece.x = castlingPiece.getX(castlingPiece.col);
    }

    //promotion method
    private boolean canPromote() {
    	if (!(promotionPawn instanceof Pawn)) return false;
        int r = promotionPawn.row;
        return (currentPlayer==WHITE && r==0) || (currentPlayer==BLACK && r==7);
    }

    private void makePromoOptions() {
        promoList.clear();
        promoList.add(new Rook  (currentPlayer,  9, 2));
        promoList.add(new Knight(currentPlayer,  9, 3));
        promoList.add(new Bishop(currentPlayer,  9, 4));
        promotion = true;
    }

    public List<Piece> getPromoOptions() {
        return promoList;
    }

    public void completePromotion(Piece chosen) {
    	//remove the old pawn
        int idx = promotionPawn.getIndex();
        simPieces.remove(idx);

        //then move the chosen piece onto the pawn’s position
        chosen.col = promotionPawn.preCol;
        chosen.row = promotionPawn.preRow;
        chosen.updatePosition();

        //add it into the list of pieces
        simPieces.add(chosen);
        copyPieces(simPieces, pieces);

        //finish promotion state and change turns
        promotionPawn = null;
        promotion     = false;
        changePlayer();
    }
}