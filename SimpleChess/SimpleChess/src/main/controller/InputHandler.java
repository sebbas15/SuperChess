package main.controller;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;

import main.model.Board;
import main.model.GameState;
import main.ui.GamePanel;
import piece.Piece;
import piece.Queen;
import piece.Type;

public class InputHandler extends MouseAdapter {
    private final GameState model;
    private final GamePanel view;

    public InputHandler(GameState model, GamePanel view) {
        this.model = model;
        this.view  = view;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //handle promotion clicks first
        if (model.isPromotion()) {
            int col = e.getX() / Board.SQUARE_SIZE;
            int row = e.getY() / Board.SQUARE_SIZE;
            for (Piece p : model.getPromoOptions()) {
                if (p.col == col && p.row == row) {
                    model.completePromotion(p);
                    view.repaint();   //redraw after promotion
                    return;           //skip other click handling
                }
            }
            //ignore if not promo
            return;
        }

        //when right click select queen under cursor & show menu
        if (SwingUtilities.isRightMouseButton(e)) {
            int col = e.getX() / Board.SQUARE_SIZE;
            int row = e.getY() / Board.SQUARE_SIZE;
            Piece clicked = model.getPieceAt(col, row);
            if (clicked != null && clicked.getType() == Type.QUEEN) {
                model.setActivePiece(clicked);
                view.showAbilityMenu(e.getX(), e.getY());
            }
            return;  //if left click, ignore
        }

        //left click = normal logic
        if (SwingUtilities.isLeftMouseButton(e)) {
            model.setMousePressed(true);
        } else {
            model.setMousePressed(false);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (model.isTeleportModeActive()) {
            System.out.println("Teleport branch in mouseClicked!");
            Piece active = model.getActivePiece();
            if (active instanceof Queen) {
                Point dest = new Point(
                    e.getX() - Board.HALF_SQUARE_SIZE,
                    e.getY() - Board.HALF_SQUARE_SIZE
                );
                ((Queen)active).teleport(dest);
                //debugging purposes
                System.out.println("Queen teleported to: " + dest);
            }
            model.setTeleportModeActive(false);
            view.repaint();
            return;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        model.setMousePressed(false);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        model.setMousePosition(e.getX(), e.getY());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        model.setMousePosition(e.getX(), e.getY());
    }
}
