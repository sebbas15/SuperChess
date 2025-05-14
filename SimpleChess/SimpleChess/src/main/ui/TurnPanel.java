package main.ui;

import javax.swing.*;
import java.awt.*;

import main.model.GameState;

public class TurnPanel extends JPanel {
  
	private static final long serialVersionUID = 1L;
	private JLabel turnLabel;

    public TurnPanel() {
        setPreferredSize(new Dimension(200, 70));
        setBackground(Color.green);

        turnLabel = new JLabel("Game Started!", SwingConstants.CENTER);
        turnLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(turnLabel);
    }

    public void updateTurn() {
        GameState model = GameState.getInstance();

        if (model.isGameOver()) {
            setBackground(Color.red);
            turnLabel.setText("Game Over!");
        } else if (model.isKingInCheck()) {
            //check for White player in check
            if (model.getCurrentPlayer() == GameState.WHITE) {
                setBackground(Color.yellow);
                turnLabel.setText("White's in Check!");
            }
            //check for Black player in check
            else if (model.getCurrentPlayer() == GameState.BLACK) {
                setBackground(Color.yellow);
                turnLabel.setText("Black's in Check!");
            }
        }
        //if not in check the game continues changing color back to green
        else {
        	 if (turnLabel.getText().equals("Game Started!")) {
                 setBackground(Color.green);
                 turnLabel.setText("Game Started!");
             } else {
                 setBackground(Color.green);
                 turnLabel.setText("Game continues!");
             }
        }

        revalidate();
        repaint();
    }

}
