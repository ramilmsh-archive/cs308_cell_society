package util;

import actions.Action;
import interfaces.Updatable;
import javafx.scene.shape.Rectangle;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Vector;

/**
 * Class that implements cell functionality
 */
public class Cell extends Rectangle implements Updatable<Cell.Update> {
    public static class Update {
        int priority;
        int x;
        int y;
        double status;
        boolean remove;

        public Update(int x, int y, double status, int priority, boolean remove) {
            this.priority = priority;
            this.x = x;
            this.y = y;
            this.status = status;
            this.remove = remove;
        }

        public Update(int x, int y, double status, int priority) {
            this(x, y, status, priority, false);
        }

        public Update(int x, int y, int priority) {
            this(x, y, -1, priority);
        }

        public Update(int x, int y, double status) {
            this(x, y, status, -1);
        }

        public Update(int x, int y) {
            this(x, y, -1, -1);
        }

        public Update(Vector2D pos) {
            this(pos.x, pos.y);
        }

        public Update(double status) {
            this(-1, -1, status, -1);
        }

        public Update(boolean remove) {
            this.remove = remove;
        }
    }

    private class UpdatePriorityComparator implements Comparator<Update> {
        @Override
        public int compare(Cell.Update o1, Cell.Update o2) {
            return o2.priority - o1.priority;
        }
    }

    private int x;
    private int y;
    // TODO create abstract Status class to describe status
    private double status;
    private Config.CellType type;

    private Grid grid;
    private Action action;
    private PriorityQueue<Update> updates;
    private Vector2D position;


    public Cell(Grid grid, Config.CellType type) {
        super(grid.getWidth() / grid.getCols(), grid.getHeight() / grid.getRows());
        this.action = type.getAction();
        for (Config.FillType fillType : type.fills)
            setFill(fillType.paint);
        this.grid = grid;
        this.type = type;
        updates = new PriorityQueue<>(new UpdatePriorityComparator());
    }

    /**
     * Creates an instance of a cell
     */
    public Cell(int x, int y, Grid grid, Config.CellType type) {
        this(grid, type);
        setPosition(x, y);
    }

    public Cell(Vector2D pos, Grid grid, Config.CellType type) {
        this(pos.x, pos.y, grid, type);
    }

    @Override
    public void add(Update update) {
        updates.add(update);
    }

    @Override
    public void applyUpdates() {
        for (Update update : updates) {
            if (update.remove) {
                grid.remove(this);
                return;
            }
            setPosition(update.x, update.y);
            grid.move(this);
            status = update.status;
        }
        updates.clear();
    }

    public double getStatus() {
        return status;
    }

    public void setStatus(double status) {
        this.status = status;
    }

    public Config.CellType getType() {
        return type;
    }

    public boolean sameType(Character symbol) {
        return type.symbol.equals(symbol);
    }

    public boolean sameType(Cell other) {
        return other == null || type.symbol.equals(other.getType().symbol);
    }

    public Vector2D getPosition() {
        return position;
    }

    public String toString() {
        return "[" + x + ", " + y + "]";
    }

    void setPosition(int x, int y) {
        grid.clear(this.x, this.y);
        position = new Vector2D(x, y);
        this.x = x == -1 ? this.x : x;
        this.y = y == -1 ? this.y : y;
        setX(this.x * getWidth());
        setY(this.y * getHeight());
    }

    void execute(Cell cell, Grid grid) {
        action.execute(cell, grid);
    }
}
