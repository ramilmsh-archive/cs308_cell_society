package actions.wator;

import util.Cell;
import util.Grid;
import util.Vector2D;

public class Predator extends Actor {
    @Override
    public void execute(Cell cell, Grid grid) {
        local = 0;
        Vector2D pos = cell.getPosition();
        if (pos == null)
            return;
        Vector2D offset = getOffset(cell, grid);
        if (offset != null) {
            Vector2D pos2 = pos.add(offset);
            grid.reserve(pos2, cell);
            cell.add(new Cell.Update(pos2));
        }
    }

    private Vector2D getOffset(Cell cell, Grid grid) {
        Vector2D pos = cell.getPosition();
        Vector2D pos2;
        Vector2D offset;
        do {
            offset = randomNeighbour();
            local = local | (1 << ((1 + offset.y) * 3 + 1 + offset.x));
            if (local == 0x1ff)
                return null;
            pos2 = pos.add(offset);
        } while ((grid.reserved(pos2) != null) && cell.sameType(grid.at(pos2)));
        return offset;
    }
}
