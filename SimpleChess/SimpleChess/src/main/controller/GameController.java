package main.controller;

import main.model.GameState;
import main.ui.GamePanel;
import javax.swing.SwingUtilities;

public class GameController implements Runnable {
    private static final int FPS = 60;
    private final GameState model;
    private final GamePanel view;
    private Thread thread;

    public GameController(GameState model, GamePanel view) {
        this.model = model;
        this.view  = view;

        //wire up input
        InputHandler ih = new InputHandler(model, view);
        view.addMouseListener(ih);
        view.addMouseMotionListener(ih);
    }

    public void start() {
        thread = new Thread(this, "Game Loop");
        thread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1_000_000_000.0 / FPS;
        long lastTime = System.nanoTime(), currentTime;
        double delta = 0;

        while (thread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                model.update();            //game logic
                //added this if statement to update players turn
                if (model.hasTurnChanged()) {
                    SwingUtilities.invokeLater(() -> view.updateTurnLabel());
                    model.resetTurnChanged();
                }
               
                SwingUtilities.invokeLater(view::repaint);
                delta--;
            }
        }
    }
}
