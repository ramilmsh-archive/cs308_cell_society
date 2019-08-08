package actions;

import util.Cell;
import util.Grid;

public abstract class Action {
    /**
     * Calculates action of a given Cell, provided neighbouring Cells
     */
    abstract public void execute(Cell cell, Grid grid);
}