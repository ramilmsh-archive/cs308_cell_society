package actions;

import util.Cell;
import util.Grid;
import util.Tuple;
import util.Vector2D;

public class Test extends Action {
    @Override
    public void execute(Cell cell, Grid grid) {
        Vector2D pos = cell.getPosition();
        cell.add(new Cell.Update(pos.x, pos.y + 1));
    }
}
