package actions.wator;

import actions.Action;
import util.Cell;
import util.Grid;
import util.Vector2D;

import java.util.Comparator;
import java.util.Random;

public class Actor extends Action {
    private static final Random random = new Random();
    int local;

    @Override
    public void execute(Cell cell, Grid grid) {
    }

    static Vector2D randomNeighbour() {
        return new Vector2D(1 - random.nextInt(3), 1 - random.nextInt(3));
    }
}
