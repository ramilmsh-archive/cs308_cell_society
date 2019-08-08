package actions.wator;

import util.Cell;
import util.Grid;
import util.Vector2D;

import java.util.ArrayList;

public class Prey extends Actor {
    public static final int priority = 0;
    public static final double breeding = 40.0;

    @Override
    public void execute(Cell cell, Grid grid) {
        local = 0;
        Vector2D pos = cell.getPosition();
        if (pos == null)
            return;
        Vector2D offset = getOffset(pos, grid);
        if (offset != null) {
            grid.reserve(pos.add(offset), cell);
            cell.add(new Cell.Update(pos.add(offset)));
        }

        if (breeding <= cell.getStatus()) {
            Vector2D childOffset = getOffset(pos, grid);
            if (childOffset != null) {
                ArrayList<Cell> cells = new ArrayList<>();
                Cell child = new Cell(grid, cell.getType());
                cells.add(child);

                Vector2D pos2 = pos.add(childOffset);
                grid.add(new Grid.Update(cells));
                child.add(new Cell.Update(pos2));
                grid.reserve(pos2, child);

                cell.setStatus(0);
            } else
                cell.setStatus(cell.getStatus() + 1);
        }
    }

    private Vector2D getOffset(Vector2D pos, Grid grid) {
        Vector2D offset;
        do {
            offset = randomNeighbour();
            local = local | (1 << ((1 + offset.y) * 3 + 1 + offset.x));
            if (local == 0x1ff)
                return null;
        } while (grid.reserved(pos.add(offset)) != null);
        return offset;
    }
}
