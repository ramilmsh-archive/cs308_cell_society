import exceptions.XMLParseException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.Config;
import util.XMLParser;

import java.io.File;
import java.util.ResourceBundle;

public class Simulation extends Application {
	public static final String DEFAULT_RESOURCE_PACKAGE = "resources/";
	public static final int HORIZONTAL_DISTANCE_BETWEEN_BUTTONS = 25;
	public static final int VERTICAL_DISTANCE_BETWEEN_BUTTONS = 25;
	public static final int BUTTON_SIZE = 100;
	public static final int TEXT_INPUT_BAR_HEIGHT = 30;
	public static final int SCREEN_SIZE = 500;
	public static final int TITLE_FONT = 35;
	public static final int NORMAL_FONT = 12;
	public static final int SMALL_FONT = 10;
	public static final int GRID_TOO_SMALL = 5;
	public static final int GRID_TOO_BIG = 150;
	public static final int GAME_BUTTON_XLOCATION = 50;
	public static final int GAME_BUTTON_YLOCATION = 450;
	public static final int GAME_BUTTON_SPACE = 100;
	public static final double SPEED_INCREASE = 2;
    private static final double FPS = 3.;
	public static String language = "English";
    private static String DATA_DIRECTORY = "resources/data/";
    private static String XML_SCHEMA = DATA_DIRECTORY + "Society.xsd";
    private Config config;
    private Stage stage;
    private Scene scene, scene1;
    private CellSociety cellSociety;
    private Timeline animation;
    private double speed = 1;
    private int gridSize = 30;
    private GridPane startScreen;
    private TextField test;
    private ResourceBundle myResources;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    /**
     * Creates the opening screen where the user can choose what game they want to play
     * and some rules for the game.
     */
    public void start(Stage primaryStage) {
    	myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + language);
    	stage = primaryStage;
    	startScreen = new GridPane();
    	startScreen.setHgap(HORIZONTAL_DISTANCE_BETWEEN_BUTTONS);
    	startScreen.setVgap(VERTICAL_DISTANCE_BETWEEN_BUTTONS);
    	scene1 = new Scene(startScreen, SCREEN_SIZE, SCREEN_SIZE);
    	createBeginningButtons();
    	stage.show();
    }
    
    /**
     * Creates a button template that is used to create every button in the
     * opening scene.
     * @param columnindex
     * @param rowindex
     * @param label
     * @param eventHandler
     */
    public void createGenericButton(int columnindex, int rowindex, String label,
    		EventHandler<MouseEvent> eventHandler) {
    	Button button = new Button(label);
    	button.setFont(Font.font(null, FontWeight.EXTRA_BOLD, NORMAL_FONT));   
    	button.setTextFill(Color.BLUE);
    	button.setPrefSize(BUTTON_SIZE, BUTTON_SIZE);
    	stage.setScene(scene1);
    	startScreen.add(button, columnindex, rowindex);
    	scene1.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
    	button.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
    }
    
    /**
     * Create the buttons that allow the user to control what simulations
     * they want to run and how using generic button template above
     */
    private void createBeginningButtons() {
	    createStartButtons(myResources.getString("game1"), 0, 0);
		createStartButtons(myResources.getString("game2"), 0, 1);
		createStartButtons(myResources.getString("game3"), 0, 2);
		createStartButtons(myResources.getString("game4"), 0, 3);
		createStartButtons(myResources.getString("game5"), 0, 4);
		createInstructionButton();
		createStartingLabel();
		createGridSizeButton();
    }
    
    /**
     * Creates start button so that when it is called it begins a certain simulation
     * @param gametype
     * @param columnindex
     * @param rowindex
     */
    public void createStartButtons(String gametype, int columnindex, int rowindex) {
    	EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent e) {
	        	begin(gametype);}
        };
    	createGenericButton(columnindex, rowindex, gametype, eventHandler);
    }
    
    /**
     * Creates grid size button, when pressed this creates a text bar where the 
     * user can input number of rows/columns they want
     */
    private void createGridSizeButton() {
		EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent e) {
	        	inputGridSize();}
        };
    	createGenericButton(2, 1, myResources.getString("userinput"), eventHandler);
    }
    
    /**
     * Allows the user to choose the size of the grid
     */
    private void inputGridSize() {
    	test = new TextField();
    	test.setPrefSize(BUTTON_SIZE,  TEXT_INPUT_BAR_HEIGHT);
    	startScreen.add(test, 2, 2);
    }
    
    /**
     * Creates instruction button - when pressed brings instruction text into screen
     */
    public void createInstructionButton() {
    	EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent e) {
	        	showInstructions();}
        };
    	createGenericButton(2, 0, myResources.getString("guide"), eventHandler);
    }
    
   /**
    * General template for creating a label (used for instructions, title etc.)
    * @param title
    * @param row
    * @param col
    * @param fontsize
    */
    private void createLabel(String title, int row, int col, int fontsize) {
    	Label label1 = new Label(title);
    	label1.setFont(Font.font(myResources.getString("font"), 
    			FontWeight.EXTRA_BOLD, FontPosture.ITALIC, fontsize));
    	label1.setTextFill(Color.RED);
    	startScreen.add(label1, row, col);
    }
    
    /**
     * Creates the title "Cell Society"
     */
    private void createStartingLabel() {
    	createLabel(myResources.getString("title"), 1, 0, TITLE_FONT);
    }
    
    /**
     * Displays the instructions of how to run simulations to the reader
     */
    public void showInstructions() {
    	createLabel(myResources.getString("whatgame"), 1, 1, NORMAL_FONT);
    	createLabel(myResources.getString("rightkey"), 1, 2, NORMAL_FONT);
    	createLabel(myResources.getString("esckey"), 1, 3, NORMAL_FONT);
    	createLabel(myResources.getString("buttons"), 1, 4, NORMAL_FONT);
    }
    
    
    
    /**
     * Beginning of a certain simulation (depending on which one the user selected)
     * @param gametype
     */
    public void begin(String gametype) {	
        try {
            config = new Config(
                XMLParser.parse(new File(DATA_DIRECTORY + gametype + ".xml"),
                        XMLParser.getValidator(XML_SCHEMA)));
			cellSociety = new CellSociety(config, gridSize);
        } catch (XMLParseException e) {
            return;
        }
        createGameButtons();
        scene = new Scene(cellSociety.getGroup(), config.width, config.height);
        stage.setScene(scene);
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        setAnimation();
        stage.show();
    }

    /**
     * Creates the animation
     */
    private void setAnimation() {
        KeyFrame frame = new KeyFrame(Duration.seconds(1 / FPS),
                e -> step(1 / FPS));
        animation = new Timeline();
        animation.setRate(speed);
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    private void step(double dt) {
        cellSociety.next();
    }
    
    /**
     * Takes the user back to the main menu
     */
    private void restart() {
    	animation.stop();
    	speed = 1;
    	start(stage);
    }
    
    /**
     * Right = step, Esc = main menu, Enter for user input of rows
     * @param code
     */
    private void handleKeyInput (KeyCode code) {
    	if (code == KeyCode.RIGHT) {
    		cellSociety.next();
    	} else if (code == KeyCode.ESCAPE) {
    		restart();
    	} else if (code == KeyCode.ENTER) {
    		if (gridSize > GRID_TOO_SMALL && gridSize < GRID_TOO_BIG) {
    			gridSize = Integer.valueOf(test.getText());
    		}
    	}
    }
    
    /**
     * New Generic Button method needed for game buttons as they are very 
     * different to buttons in main screen. Either updates speed or 
     * starts/stops the simulation.
     * @param string
     * @param multiplier
     * @param xLocation
     * @param yLocation
     */
    public void createGameButton(String string, double multiplier,
    		int xLocation, int yLocation) {
    	Button button = new Button(string);
        button.setPrefSize(BUTTON_SIZE/2, BUTTON_SIZE/2);
        button.setLayoutX(xLocation);
        button.setLayoutY(yLocation);
        cellSociety.getGroup().getChildren().addAll(button);
        button.addEventHandler(MouseEvent.MOUSE_CLICKED,
    		    new EventHandler<MouseEvent>() {
	        @Override public void handle(MouseEvent e) {
	        	updateSpeed(speed, multiplier);
	        }
        });
    }
    
    /**
     * Decides if the simulation should be stopped/started/sped/slowed
     * @param speed
     * @param multiplier
     */
    public void updateSpeed(double speed, double multiplier) {
    	if (speed != 0) {
    		speed *= multiplier;
    	} else if (multiplier == 1) {
    		speed = multiplier;
    	}
    	animation.setRate(speed);
    }
    
    /**
     * Creates speed up, slow down, start and stop buttons
     */
    public void createGameButtons() {
    	createGameButton(myResources.getString("startcommand"), 1, GAME_BUTTON_XLOCATION, GAME_BUTTON_YLOCATION);
    	createGameButton(myResources.getString("stopcommand"), 0, GAME_BUTTON_XLOCATION * 3, GAME_BUTTON_YLOCATION);
    	createGameButton(myResources.getString("speedup"), SPEED_INCREASE, GAME_BUTTON_XLOCATION*5, GAME_BUTTON_YLOCATION);
    	createGameButton(myResources.getString("slowdown"), 1/SPEED_INCREASE, GAME_BUTTON_XLOCATION*7, GAME_BUTTON_YLOCATION);
    }
    
}
    
    

