package main.model;

import java.awt.Color;
import java.awt.Graphics2D;

public class Board {
    
    final int MAX_COL = 8;
    final int MAX_ROW = 8;
    public static final int SQUARE_SIZE = 100;
    public static final int HALF_SQUARE_SIZE = SQUARE_SIZE/2;
    
    public static Color whiteSpaceColor = new Color(210,165,125);
    public static Color blackSpaceColor = new Color(175,115,70);

    public void draw(Graphics2D g2) {
    	
        int c = 0;

        for(int row = 0; row < MAX_ROW; row++) {

            for(int col = 0; col < MAX_COL; col++){

                if(c == 0){
                    g2.setColor(whiteSpaceColor);
                    c = 1;
                }
                else {
                    g2.setColor(blackSpaceColor);
                    c = 0;
                }

                g2.fillRect(col*SQUARE_SIZE, row*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }

            if(c == 0){
                c = 1;
            }
            else {
                c = 0;
            }
        }
    }
    
    public static void setWhiteSpaceColor (Color c) {    	
    	whiteSpaceColor = c;
    }
    public static void setBlackSpaceColor (Color c) {
    	blackSpaceColor = c;
    }
}

