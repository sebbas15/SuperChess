package main.ui;

import java.awt.*;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import main.controller.InputHandler;
import main.model.Board;
import main.model.GameState;
import piece.Piece;

public class GamePanel extends JPanel {
    private static final long serialVersionUID = 1L;

	public static final int WIDTH = 1100, HEIGHT = 795;

    private final GameState model;
    private final Board board = new Board();
    
    private Piece teleportingPiece;
    private JLabel turnLabel;
    public static Color backgroundColor = Color.DARK_GRAY;
    // initialize once here
    private final AbilityMenuPanel abilityMenu =
        new AbilityMenuPanel(this::onTeleportChosen);

    public GamePanel(GameState model) {
        this.model = model;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setLayout(null);

        abilityMenu.setBounds(0, 0, 150, 80);
        add(abilityMenu);
        
        ///used for turn panel
        turnLabel = new JLabel("White's Turn", SwingConstants.CENTER);
        turnLabel.setFont(new Font("Arial", Font.BOLD, 24));
        turnLabel.setForeground(Color.WHITE);
        turnLabel.setBounds(850, 50, 200, 50); // adjust position: x, y, width, height
        add(turnLabel);


        //wire up mouse input
        InputHandler input = new InputHandler(model, this);
        addMouseListener(input);
        addMouseMotionListener(input);
        
        // ensure the panel is focusable so it gets mouse events
        setFocusable(true);
        requestFocusInWindow();
    }
    
    //Juan display players turns
    public void updateTurnLabel() {
        if (model.getCurrentPlayer() == GameState.WHITE) {
            turnLabel.setText("White's Turn");
        } else {
            turnLabel.setText("Black's Turn");
        }
    }
    
    private void onTeleportChosen() {
        Piece queen = model.getActivePiece();
        
        javax.swing.SwingUtilities.invokeLater(() -> {
            model.setActivePiece(queen);
            model.setTeleportModeActive(true);
            System.out.println("Teleport mode activated.");
        });

        abilityMenu.setVisible(false);
    }

    
    public void showAbilityMenu(int x, int y) {
        //stash the Queen before anything else can clear activePiece
        teleportingPiece = model.getActivePiece();
        
        abilityMenu.setLocation(x, y);
        abilityMenu.setVisible(true);
        repaint();
    }
    public static void changeBackgroundColor(Color c) {
    	backgroundColor = c;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        //If in promotion mode, draw only the promo choices
        if (model.isPromotion()) {
            // darken the background
            g2.setColor(new Color(0, 0, 0, 180));
            g2.fillRect(0, 0, getWidth(), getHeight());

            // prompt text
            g2.setFont(new Font("Arial", Font.BOLD, 28));
            g2.setColor(Color.WHITE);
            g2.drawString("Promote your pawn:", 50, 50);

            //draw the 4 promo pieces
            for (Piece p : model.getPromoOptions()) {
                p.draw(g2);
                
            }
            
            return;  //skip all the normal drawing
        }


        //draw the board
        board.draw(g2);

        //highlight valid moves 
        List<Point> valid = model.getValidMoves();
        if (!valid.isEmpty()) {
            g2.setColor(new Color(255, 255, 255, 128));
            for (Point sq : valid) {
                int x = sq.x * Board.SQUARE_SIZE;
                int y = sq.y * Board.SQUARE_SIZE;
                g2.fillRect(x, y, Board.SQUARE_SIZE, Board.SQUARE_SIZE);
            }
        }

        //draw all the pieces
        for (Piece p : model.getSimPieces()) {
            p.draw(g2);
        }
                

    }

       
 }