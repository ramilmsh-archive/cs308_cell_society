package actions.segregation;

import actions.Action;
import util.Cell;
import util.Grid;
import util.Tuple;
import util.Vector2D;

import java.util.ArrayList;
import java.util.Random;

public class Agent extends Action {
    private static final double ratio = .7;
    private final Random random = new Random();
    @Override
    public void execute(Cell cell, Grid grid) {
        ArrayList<Cell> neighbours = grid.getNeighbours(cell);
        double same = 0., different = 0.;
        for (Cell neighbour : neighbours) {
            if (neighbour == null)
                continue;
            if (neighbour.sameType(cell))
                same += 1.;
            else
                different += 1.;
        }

        if ((different + same) != 0 && ratio > same / (different + same)) {
            int x, y;
            do {
                x = random.nextInt(grid.getCols());
                y = random.nextInt(grid.getRows());
            } while (grid.reserved(x, y) != null);
            Vector2D pos = cell.getPosition();
            grid.reserve(x, y, cell);
            cell.add(new Cell.Update(x, y));
        }
    }
}
