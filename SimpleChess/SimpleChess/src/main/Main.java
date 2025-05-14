package main;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.*;

import main.model.Board;
import main.model.GameState;
import main.ui.GamePanel;
import main.ui.TurnPanel;
import main.controller.GameController;

public class Main {
    public static void main(String[] args) {
        GameState model = new GameState();
        GamePanel view = new GamePanel(model);
        TurnPanel turnPanel = new TurnPanel();

        GameState.setTurnPanel(turnPanel);
        turnPanel.updateTurn();

        JFrame window = new JFrame("Super Chess");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        //menu
        JMenuBar menuBar = new JMenuBar();

        JMenu customizeMenu = new JMenu("Customize");

        JMenu WhiteSpaceMenu = new JMenu("White Space Color");
        JMenu BlackSpaceMenu = new JMenu("Black Space Color");

        customizeMenu.add(WhiteSpaceMenu);
        customizeMenu.add(BlackSpaceMenu);
        customizeMenu.addSeparator();


        //whitespace options
        JMenuItem randomItemW = new JMenuItem("Random");
        JMenuItem defaultItemW = new JMenuItem("Default");
        JMenuItem whiteItemW = new JMenuItem("White");
        JMenuItem redItemW = new JMenuItem("Red");
        JMenuItem orangeItemW = new JMenuItem("Orange");
        JMenuItem yellowItemW = new JMenuItem("Yellow");
        JMenuItem greenItemW = new JMenuItem("Green");
        JMenuItem blueItemW = new JMenuItem("Blue");
        JMenuItem pinkItemW = new JMenuItem("Pink");
        WhiteSpaceMenu.add(randomItemW);
        WhiteSpaceMenu.add(defaultItemW);
        WhiteSpaceMenu.add(whiteItemW);
        WhiteSpaceMenu.add(redItemW);
        WhiteSpaceMenu.add(orangeItemW);
        WhiteSpaceMenu.add(yellowItemW);
        WhiteSpaceMenu.add(greenItemW);
        WhiteSpaceMenu.add(blueItemW);
        WhiteSpaceMenu.add(pinkItemW);

        //blackspace options
        JMenuItem randomItemB = new JMenuItem("Random");
        JMenuItem defaultItemB = new JMenuItem("Default");
        JMenuItem blackItemB = new JMenuItem("Black");
        JMenuItem redItemB = new JMenuItem("Red");
        JMenuItem orangeItemB = new JMenuItem("Orange");
        JMenuItem yellowItemB = new JMenuItem("Yellow");
        JMenuItem greenItemB = new JMenuItem("Green");
        JMenuItem blueItemB = new JMenuItem("Blue");
        JMenuItem pinkItemB = new JMenuItem("Pink");
        BlackSpaceMenu.add(randomItemB);
        BlackSpaceMenu.add(defaultItemB);
        BlackSpaceMenu.add(blackItemB);
        BlackSpaceMenu.add(redItemB);
        BlackSpaceMenu.add(orangeItemB);
        BlackSpaceMenu.add(yellowItemB);
        BlackSpaceMenu.add(greenItemB);
        BlackSpaceMenu.add(blueItemB);
        BlackSpaceMenu.add(pinkItemB);

        //background options
        JMenuItem randomItem = new JMenuItem("Random");
        JMenuItem grayItem = new JMenuItem("Gray");
        JMenuItem whiteItem = new JMenuItem("White");
        JMenuItem blackItem = new JMenuItem("Black");
        JMenuItem redItem = new JMenuItem("Red");
        JMenuItem orangeItem = new JMenuItem("Orange");
        JMenuItem yellowItem = new JMenuItem("Yellow");
        JMenuItem greenItem = new JMenuItem("Green");
        JMenuItem blueItem = new JMenuItem("Blue");
        JMenuItem pinkItem = new JMenuItem("Pink");

        menuBar.add(customizeMenu);
        window.setJMenuBar(menuBar);

        //action listeners for menu items
        randomItemW.addActionListener(e -> Board.setWhiteSpaceColor(randomColor()));
        defaultItemW.addActionListener(e -> Board.setWhiteSpaceColor(new Color(210, 165, 125)));
        whiteItemW.addActionListener(e -> Board.setWhiteSpaceColor(Color.white));
        redItemW.addActionListener(e -> Board.setWhiteSpaceColor(Color.red));
        orangeItemW.addActionListener(e -> Board.setWhiteSpaceColor(Color.orange));
        yellowItemW.addActionListener(e -> Board.setWhiteSpaceColor(Color.yellow));
        greenItemW.addActionListener(e -> Board.setWhiteSpaceColor(Color.green));
        blueItemW.addActionListener(e -> Board.setWhiteSpaceColor(Color.blue));
        pinkItemW.addActionListener(e -> Board.setWhiteSpaceColor(Color.pink));

        //Black space
        randomItemB.addActionListener(e -> Board.setBlackSpaceColor(randomColor()));
        defaultItemB.addActionListener(e -> Board.setBlackSpaceColor(new Color(175, 115, 70)));
        blackItemB.addActionListener(e -> Board.setBlackSpaceColor(Color.black));
        redItemB.addActionListener(e -> Board.setBlackSpaceColor(Color.red));
        orangeItemB.addActionListener(e -> Board.setBlackSpaceColor(Color.orange));
        yellowItemB.addActionListener(e -> Board.setBlackSpaceColor(Color.yellow));
        greenItemB.addActionListener(e -> Board.setBlackSpaceColor(Color.green));
        blueItemB.addActionListener(e -> Board.setBlackSpaceColor(Color.blue));
        pinkItemB.addActionListener(e -> Board.setBlackSpaceColor(Color.pink));

        //Background
        randomItem.addActionListener(e -> GamePanel.changeBackgroundColor(randomColor()));
        grayItem.addActionListener(e -> GamePanel.changeBackgroundColor(Color.DARK_GRAY));
        whiteItem.addActionListener(e -> GamePanel.changeBackgroundColor(Color.white));
        blackItem.addActionListener(e -> GamePanel.changeBackgroundColor(Color.black));
        redItem.addActionListener(e -> GamePanel.changeBackgroundColor(Color.red));
        orangeItem.addActionListener(e -> GamePanel.changeBackgroundColor(Color.orange));
        yellowItem.addActionListener(e -> GamePanel.changeBackgroundColor(Color.yellow));
        greenItem.addActionListener(e -> GamePanel.changeBackgroundColor(Color.green));
        blueItem.addActionListener(e -> GamePanel.changeBackgroundColor(Color.blue));
        pinkItem.addActionListener(e -> GamePanel.changeBackgroundColor(Color.pink));

        //main game and turnpanel
        window.add(view, BorderLayout.CENTER);
        window.add(turnPanel, BorderLayout.WEST);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        new GameController(model, view).start();
    }

    private static Color randomColor() {
        return new Color((int)(Math.random() * 256),
                         (int)(Math.random() * 256),
                         (int)(Math.random() * 256));
    }
}

