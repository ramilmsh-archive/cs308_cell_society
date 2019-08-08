import javafx.scene.Group;
import util.Config;
import util.Grid;

/**
 * Main game class runner
 */
class CellSociety {
    private Config config;
    private Grid grid;
    private int gridSize;
    /**
     * Creates an instance of the game
     *
     * @param config: configuration
     */
    CellSociety(Config config, int inputGridSize) {
    	gridSize = inputGridSize;
        update(config);
    }

    void update(Config config) {
        this.config = config;
        grid = new Grid(config, gridSize);
    }

    Group getGroup() {
        return grid.getGroup();
    }

    /**
     * Calculates the grid  at given generation
     *
     * @param generation: generation number
     * @return grid of the new generation
     */
    void calculate(int generation) {
    }

    void next() {
        grid.next();
    }
    

    /**
     * Calculate all of the cell's actions
     */
    private void calculate() {

    }
}