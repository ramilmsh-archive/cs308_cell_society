package actions.fire;

import actions.Action;
import javafx.scene.paint.Color;
import util.Cell;
import util.Grid;
import util.Tuple;
import util.Vector2D;

import java.util.ArrayList;
import java.util.Random;


public class Spread extends Action {
    private static final double ratio = .9;
    private final Random random = new Random();
    private static final Color EMPTY = Color.YELLOW;
    private static final Color TREE = Color.GREEN;
    private static final Color FIRE = Color.RED;

    @Override
    public void execute(Cell cell, Grid grid) {
    	if (cell.getFill() == FIRE) {
    		cell.setFill(EMPTY);
    	} else if (cell.getFill() == TREE) {
    		ArrayList<Cell> neighbours = grid.getNeighbours(cell);
    		for (Cell neighbour : neighbours) {
    			if (neighbour != null && neighbour.getFill() == EMPTY) {
    				if (random.nextDouble() > ratio) {
    					cell.setFill(FIRE);
    				}
    			}
    		}
    	}
    }
}
