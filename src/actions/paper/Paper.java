package actions.paper;

import java.util.ArrayList;
import java.util.Random;

import actions.Action;
import javafx.scene.paint.Color;
import util.Cell;
import util.Grid;

public class Paper extends Action {
	
	private static final int EATEN = -1;
	private static final int EAT = 1;
	private static final Color ROCK  = Color.BLUE;
    private static final Color PAPER = Color.GREEN;
    private static final Color SCISSOR = Color.RED;
    private final Random random = new Random();
    
	@Override
	 public void execute(Cell cell, Grid grid) {
		
		ArrayList<Cell> neighbours = grid.getNeighbours(cell);
		
		for (int i = 0; i<10; i++) {
			int index = random.nextInt(neighbours.size());
			Cell neighbour = neighbours.get(index);
			if ( cell!=null && !cell.sameType(neighbour)) {
				if (compare(cell, neighbour) == EAT) {
					neighbour.setFill(cell.getFill());
				}
				else cell.setFill(neighbour.getFill());
				break;
			}
		}
	}

	private int compare(Cell cell, Cell neighbour) {
		if ((cell.getFill() == ROCK && neighbour.getFill() == PAPER) || (cell.getFill() == PAPER && neighbour.getFill() == SCISSOR) || (cell.getFill() == SCISSOR && neighbour.getFill() == ROCK))
			return EATEN;
		//else if (cell.getFill() == PAPER && neighbour.getFill() == SCISSOR) return -1;
		//else if (cell.getFill() == SCISSOR && neighbour.getFill() == ROCK) return -1;
		else return EAT;
	}
}
