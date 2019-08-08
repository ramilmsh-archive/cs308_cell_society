package actions.gameoflife;

import java.util.ArrayList;

import actions.Action;
import javafx.scene.paint.Color;
import util.Cell;
import util.Grid;

public class GameLife extends Action {
	
	private static final Color ALIVE = Color.BLACK;
    private static final Color DEAD = Color.WHITE;
    
    
	 @Override
	 public void execute(Cell cell, Grid grid) {
		 ArrayList<Cell> neighbours = grid.getNeighbours(cell);
		 int aliveNeighbors = 0;
		 for (Cell neighbour : neighbours) {
			 if (neighbour != null && neighbour.getFill() == ALIVE) aliveNeighbors++;
		 }
		 update(cell, aliveNeighbors);
		 
	 }

	 /**
	  * Decides whether a cell should live or die
	  * @param cell
	  * @param aliveNeighbors
	  */
	private void update(Cell cell, int aliveNeighbors) {
		if (cell.getFill() == ALIVE) {
			 if (aliveNeighbors < 2 || aliveNeighbors > 3) {
				 cell.setFill(DEAD);
			 }
		 } else if (aliveNeighbors == 3) {
			 cell.setFill(ALIVE);
		 }
	}
	 
 	


}






