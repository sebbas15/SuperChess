package piece;

import java.util.List;
import main.model.GameState;

public class King extends Piece {
    public King(int color, int col, int row) {
        super(color, col, row);
        type = Type.KING;
        if (color == GameState.WHITE) {
            image = getImage("/piece/WKing");
        } else {
            image = getImage("/piece/BKing");
        }
    }

    @Override
    public boolean canMove(int targetCol, int targetRow) {
        GameState state = GameState.getInstance();
        List<Piece> sim = state.getSimPieces();

        //must stay on the board
        if (!isWithinBoard(targetCol, targetRow)) {
            return false;
        }

        int dx = Math.abs(targetCol - preCol);
        int dy = Math.abs(targetRow - preRow);

        //normal king move one square any direction
        if ((dx + dy == 1) || (dx == 1 && dy == 1)) {
            return isValidSquare(targetCol, targetRow);
        }

        //castling king hasn't moved, two squares horizontally, path is clear
        if (!moved && targetRow == preRow) {
            //Kingside castling
            if (targetCol == preCol + 2
             && !pieceIsOnStrightLine(targetCol, targetRow)) {
                for (Piece p : sim) {
                    if (p instanceof Rook
                     && p.col == preCol + 3
                     && p.row == preRow
                     && !p.moved) {
                        state.setCastlingPiece(p);
                        return true;
                    }
                }
            }
            //Queenside castling
            if (targetCol == preCol - 2
             && !pieceIsOnStrightLine(targetCol, targetRow)) {
                for (Piece p : sim) {
                    if (p instanceof Rook
                     && p.col == preCol - 4
                     && p.row == preRow
                     && !p.moved) {
                        state.setCastlingPiece(p);
                        return true;
                    }
                }
            }
        }

        //else if illegal for the king
        return false;
    }
}