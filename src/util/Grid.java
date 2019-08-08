package util;

import interfaces.Updatable;
import javafx.scene.Group;

import java.util.*;

public class Grid implements Updatable<Grid.Update> {
    public static class Update {
        int priority;
        ArrayList<Cell> newCells;

        public Update(ArrayList<Cell> cells, int priority) {
            this.priority = priority;
            newCells = cells;
        }

        public Update(ArrayList<Cell> cells) {
            this(cells, -1);
        }
    }

    private class UpdatePriorityComparator implements Comparator<Grid.Update> {
        @Override
        public int compare(Grid.Update o1, Grid.Update o2) {
            return o2.priority - o1.priority;
        }
    }

    private Random random = new Random();

    private Cell[][] grid;
    private Cell[][] availability;
    private ArrayList<Cell> cells;
    private PriorityQueue<Update> updates;
    private Group group;
    

    private double width;
    private double height;

    private int rows;
    private int cols;

    public Grid(Config config, int gridSize) {
        rows = gridSize;
        cols = rows;

        width = config.width;
        height = config.height - 60;

        group = new Group();
        cells = new ArrayList<>();

        if (config.type == Config.Type.RANDOM)
            addFromDistribution(config);
        else
            addFromGrid(config);

        updates = new PriorityQueue<>(new UpdatePriorityComparator());
    }
    
    
    

    @Override
    public void add(Update update) {
        updates.add(update);
    }

    @Override
    public void applyUpdates() {
        for (Update update : updates) {
            for (Cell cell : update.newCells)
                add(cell);
        }
        updates.clear();
    }
    


    public void next() {
        for (Cell cell : cells) cell.execute(cell, this);

        Cell[] _cells = cells.toArray(new Cell[cells.size()]);
        for (Cell cell : _cells) cell.applyUpdates();

        applyUpdates();

        for (int i = 0; i < grid[0].length; ++i)
            for (int j = 0; j < grid.length; ++j)
                availability[i][j] = grid[i][j];
    }
    

    public Group getGroup() {
        return group;
    }

    public Cell at(int i, int j) {
        if (inBounds(i, j))
            return grid[i][j];
        return null;
    }

    public Cell at(Vector2D pos) {
        return at(pos.x, pos.y);
    }

    public ArrayList<Cell> getNeighbours(Cell cell) {
        ArrayList<Cell> list = new ArrayList<>();
        Vector2D pos = cell.getPosition();
        list.add(at(pos.x - 1, pos.y - 1));
        list.add(at(pos.x, pos.y - 1));
        list.add(at(pos.x + 1, pos.y - 1));
        list.add(at(pos.x + 1, pos.y));
        list.add(at(pos.x + 1, pos.y + 1));
        list.add(at(pos.x, pos.y + 1));
        list.add(at(pos.x - 1, pos.y + 1));
        list.add(at(pos.x - 1, pos.y));
        return list;
    }

    public Cell reserved(int i, int j) {
        return inBounds(i, j) ? availability[i][j] : null;
    }

    public void reserve(int i, int j, Cell cell) {
        if (inBounds(i, j)) {
            Vector2D pos = cell.getPosition();
            if (pos != null && inBounds(pos))
                availability[pos.x][pos.x] = null;
            availability[i][j] = cell;
        }
    }

    public Cell reserved(Vector2D pos) {
        return reserved(pos.x, pos.y);
    }

    public void reserve(Vector2D pos, Cell cell) {
        reserve(pos.x, pos.y, cell);
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    double getWidth() {
        return width;
    }

    double getHeight() {
        return height;
    }

    private void add(Cell cell) {
        if (move(cell)) {
            cells.add(cell);
            group.getChildren().add(cell);
        }
    }

    private void addRandom(Config.CellType cellType) {
        int x, y;
        do {
            x = random.nextInt(cols);
            y = random.nextInt(rows);
        } while (grid[x][y] != null);
        add(new Cell(x, y, this, cellType));
    }

    private void addFromDistribution(Config config) {
        grid = new Cell[rows][cols];
        availability = new Cell[rows][cols];
        int total = cols * rows;

        for (Config.CellType type : config.getCellTypes().values()) {
            int n = (int) Math.floor(type.ratio * total);
            for (int i = 0; i < n; ++i)
                addRandom(type);
        }
    }

    private void addFromGrid(Config config) {
        grid = new Cell[rows][cols];
        availability = new Cell[rows][cols];
        Config.CellType[][] cellTypes = config.getGrid();
        for (int i = 0; i < cellTypes.length; ++i)
            for (int j = 0; j < cellTypes[0].length; ++j)
                add(new Cell(i, j, this, cellTypes[i][j]));
    }

    boolean move(Cell cell) {
        Vector2D pos = cell.getPosition();
        if (pos != null) {
            if (!inBounds(pos.x, pos.y) || grid[pos.x][pos.y] != null)
                return false;
            grid[pos.x][pos.y] = cell;
        }
        return true;
    }

    public void clear(int i, int j) {
        if (inBounds(i, j)) {
            grid[i][j] = null;
        }
    }

    void remove(Cell cell) {
        Vector2D pos = cell.getPosition();
        if (inBounds(pos.x, pos.y)) {
            grid[pos.x][pos.y] = null;
        }
        cells.remove(cell);
        group.getChildren().remove(cell);
    }

    void prettyPrint() {
        for (Cell[] row : grid) {
            for (int j = 0; j < grid[0].length; ++j)
                System.out.printf("%c ", row[j] == null ? '_' : row[j].getType().symbol);
            System.out.println();
        }
        System.out.println("----------------------------");
    }

    private boolean inBounds(int i, int j) {
        return i >= 0 && j >= 0 && i < cols && j < rows;
    }

    private boolean inBounds(Vector2D pos) {
        return pos.x >= 0 && pos.y >= 0 && pos.x < cols && pos.y < rows;
    }
}
