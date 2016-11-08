package assignment5;

import java.io.File;
import java.lang.reflect.Method;
import java.util.*;

import assignment5.Critter;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {
	// screen width and height factors
	private final static int screenWidthScalingFactor = 1;
	private final static int screenHeightScalingFactor = 1;
	public final static int screenWidth = Params.world_width * screenWidthScalingFactor;
	public final static int screenHeight = Params.world_height * screenHeightScalingFactor;

	// grid, stage, scene for the critters
	static GridPane layer_critterWorld = new GridPane();
	static Stage critterStage = new Stage();
	static Scene critterScene = new Scene(layer_critterWorld, screenWidth, screenHeight);

	// grid, stage, scene for the user
	static GridPane userGrid = new GridPane();
	static Stage userStage = new Stage();
	static Scene userScene = new Scene(userGrid, screenWidth, screenHeight);

	// vertical box, grid for the buttons
	static VBox buttons = new VBox();
	static GridPane buttonsGrid = new GridPane();

	// stack containing the layers for the critter world
	static Group critterWorldStack = new Group();

	// constants used for display
	private static final int numCrittersPerRow = 2;
	public static final int critterBoxSize =
		(int) Math.sqrt(Params.world_height * Params.world_width /
						(numCrittersPerRow * numCrittersPerRow));
	private static final String myPackage = Critter.class.getPackage().toString().split(" ")[1];

	private static String getExtension(File file) {
		String fileName = file.getName();
		int i = fileName.lastIndexOf('.');
		if (i > -1) { return fileName.substring(i+1); }
		else { return ""; }
	}

	private static String getCritterType(File file) {
		String fileName = file.getName();
		int i = fileName.lastIndexOf('.');
		if (i > -1) { return fileName.substring(0,i); }
		else { return ""; }
	}

	private static ArrayList<String> getAllCritterSubclasses() throws Exception {
		// Look through all .class files and grab all that are subclasses of critter
		File folder = new File("./src/assignment5");
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> validCritterTypes = new ArrayList<String>();
		for (File file : listOfFiles) {
			if (getExtension(file).equals("java")) { // if a java .class file
				String critterType = getCritterType(file);
				if (Critter.isCritterInstance(critterType)) { // if a concrete critter instance
					validCritterTypes.add(critterType);
				}
			}
		}
		return validCritterTypes;
	}

	private static VBox makeErrorMessage(String msg) {
		VBox dialogVbox = new VBox(20);
        dialogVbox.setAlignment(Pos.CENTER);
        Text errorMessage = new Text(msg);
        errorMessage.setFill(Color.RED);
        errorMessage.setFont(
        	Font.font(Font.getDefault().toString(), FontWeight.BOLD, 20));
        dialogVbox.getChildren().add(errorMessage);
        return dialogVbox;
	}

	private static void showErrorMessage(String msg, int boxSizeX, int boxSizeY) {
		final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(userStage);
        VBox dialogVbox = makeErrorMessage(msg);
        Scene dialogScene = new Scene(dialogVbox, boxSizeX, boxSizeY);
        dialog.setScene(dialogScene);
        dialog.show();
	}

	private static VBox makeStatsMessage(String msg) {
		VBox dialogVbox = new VBox(30);
        dialogVbox.setAlignment(Pos.CENTER);
        Text statsMessage = new Text(msg);
        statsMessage.setFill(Color.BLACK);
        statsMessage.setFont(
        	Font.font(Font.getDefault().toString(), 15));
        dialogVbox.getChildren().add(statsMessage);
        return dialogVbox;
	}

	private static void showStatsMessage(String cls, String msg, int boxSizeX, int boxSizeY) {
		final Stage dialog = new Stage();
        dialog.initModality(Modality.NONE);
        dialog.initOwner(userStage);
        VBox dialogVbox = makeStatsMessage(msg);
        Scene dialogScene = new Scene(dialogVbox, boxSizeX, boxSizeY);
        dialog.setScene(dialogScene);
        dialog.setTitle(cls + " Stats");
        dialog.show();
	}
	private static void placeNumTimeStepsOption() {
		// Set the label and tool tip for executing time steps
		Label label_numTimeSteps = new Label("Number of Time Steps: ");
		userGrid.add(label_numTimeSteps, 0, 0);
		TextField textField_numTimeSteps = new TextField();
		textField_numTimeSteps.setTooltip(
			new Tooltip("Number of time steps to execute"));
		userGrid.add(textField_numTimeSteps, 1, 0);

		// Add the button for executing time steps
		Button button_timeSteps = new Button("Execute Time Steps");
		button_timeSteps.setMaxWidth(Double.MAX_VALUE);
		buttons.getChildren().add(button_timeSteps);

		// Make the button actually execute time steps
		button_timeSteps.setOnAction((event) -> {
			try{
			    int numSteps = Integer.parseInt(textField_numTimeSteps.getText());
			    if (numSteps < 0) { throw new NumberFormatException(); }
			    for(int i = 0; i < numSteps; i++){ Critter.worldTimeStep(); }
            } catch(Exception e){
            	String msg = "Please enter a positive integer greater than or equal to zero.";
            	showErrorMessage(msg, 600, 100);
            }
		});
	}

	private static void placeAddCrittersOption() throws Exception {
		// Place the "Add Critter(s)" label and text fields
		Label label_addCritter = new Label("Number and Type of Critters: ");
		userGrid.add(label_addCritter , 0, 1);
		TextField textField_addNumCritter = new TextField();
		textField_addNumCritter.setTooltip(
				new Tooltip("Number of critters to add"));
		userGrid.add(textField_addNumCritter, 1, 1);

		// Look through all .class files and grab all that are subclasses of critter
		ArrayList<String> validCritterTypes = getAllCritterSubclasses();

		// Set the pull down menu for the types of critters one can add to the world
		ChoiceBox<String> choiceBox_addCritter = new ChoiceBox<String>();
		choiceBox_addCritter.setItems(FXCollections.observableList(validCritterTypes));
		choiceBox_addCritter.setTooltip(
				new Tooltip("Type of critters to add"));
		userGrid.add(choiceBox_addCritter, 2, 1);

		// Add the button for adding critters
		Button button_addCritters = new Button("Add Critters");
		button_addCritters.setMaxWidth(Double.MAX_VALUE);
		buttons.getChildren().add(button_addCritters);

		// Make the button actually add critters
		button_addCritters.setOnAction((event) -> {
			try{
				String critterChoice = choiceBox_addCritter.getSelectionModel().getSelectedItem();
			    int numSteps = Integer.parseInt(textField_addNumCritter.getText());
			    if (numSteps < 0) { throw new NumberFormatException(); }
			    for(int i = 0; i < numSteps; i++){ Critter.makeCritter(critterChoice); }
			    Critter.displayWorld();
            } catch(Exception e){
            	if (e instanceof InvalidCritterException) {
            		String msg = "Please choose one of the available critters.";
                	showErrorMessage(msg, 500, 100);
            	} else if (e instanceof NumberFormatException) {
            		String msg = "Please enter a positive integer greater than or equal to zero.";
                    showErrorMessage(msg, 600, 100);
            	}
            }
		});
	}

	private static void addInvisibleButton() {
		Button button_buffer = new Button();
		button_buffer.setVisible(false);
		button_buffer.setMinHeight(13);
		button_buffer.setMaxHeight(13);
		buttons.getChildren().add(button_buffer);
	}

	private static void placeRunStatsOption() throws Exception {
		// Place the "Run Stats" label and text fields
		Label label_runStats = new Label("Type of Critter(s): ");
		userGrid.add(label_runStats, 0, 2);

		// Look through all .class files and grab all that are subclasses of critter
		ArrayList<String> validCritterTypes = getAllCritterSubclasses();

		// Set the list menu for types of critters one can run stats on
		ListView<String> list_runStats = new ListView<String>();
		list_runStats.setItems(FXCollections.observableList(validCritterTypes));
		list_runStats.setTooltip(new Tooltip(
				"Type of critters to view stats on.\n"
				+ "You can select more than one option by holding the 'ctrl' button"));
		userGrid.add(list_runStats, 2, 2);

		// Configure settings for the list
		list_runStats.setPrefHeight(70);
		list_runStats.setPrefWidth(70);
		list_runStats.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		// Add the button for obtaining stats on specified critters
		// Add an invisible buffer button so that the actual button is centered
		Button button_runStats = new Button("Run Stats");
		button_runStats.setMaxWidth(Double.MAX_VALUE);
		addInvisibleButton();
		buttons.getChildren().add(3, button_runStats);

		// Make the button actually run stats
		button_runStats.setOnAction((event) -> {
			try{
				ObservableList<String> critterChoice = list_runStats.getSelectionModel().getSelectedItems();
				List<Critter> critterList = new ArrayList<Critter>();

				String className = critterChoice.get(0); //  TODO: do me
                String fullClassName = myPackage + "." + className;
//                for (String s : critterChoice) {
//                	critterList.add(Critter.getInstance(s));
//                }
                critterList = Critter.getInstances(className);
                Class<?> cls = Class.forName(fullClassName);
                Method runStats = cls.getMethod("runStats", List.class);
                String str = (String) runStats.invoke(cls, critterList);
                showStatsMessage(className, str, 600, 100);
            } catch(Exception e){
        		String msg = "Please choose one or more of the available critters.";
            	showErrorMessage(msg, 600, 100);
            }
		});
	}

	private static void addGridLines() {
		for (int i = 0; i < 2000; i += critterBoxSize) {
			if (i <= Params.world_width) {
			    Line line_vertical = new Line(i, 0, i, Params.world_height);
			    line_vertical.setStroke(Color.LIGHTGREY);
			    critterWorldStack.getChildren().add(line_vertical);
			}
		    if (i <= Params.world_height) {
		    	Line line_horizontal = new Line(0, i, Params.world_width, i);
		    	line_horizontal.setStroke(Color.LIGHTGREY);
			    critterWorldStack.getChildren().add(line_horizontal);
		    }
		}
	}

	private static void placeQuitOption() {
		// Add the quit button
		Button button_quit = new Button("Quit");
		button_quit.setMaxWidth(Double.MAX_VALUE);
		button_quit.setTextFill(Color.RED);
		addInvisibleButton();
		buttons.getChildren().add(button_quit);

		// Make the button actually quit the simulation
		button_quit.setOnAction((event) -> { System.exit(0); });
	}

	private static void placeButtons() {
		buttons.setSpacing(10);
		buttons.setPadding(new Insets(25, 25, 25, 0));
		buttonsGrid.setHgap(10);
		buttonsGrid.setVgap(10);
		buttonsGrid.add(buttons, 0, 0);
		buttonsGrid.setStyle("-fx-background-color: #FFFFFF;");
	}

	private static void setCritterGrid() {
    	layer_critterWorld.setAlignment(Pos.TOP_LEFT);
		layer_critterWorld.setHgap(5);
		layer_critterWorld.setVgap(5);
		layer_critterWorld.setPadding(new Insets(20, 20, 20, 20));
		layer_critterWorld.setStyle("-fx-background-color: #FFFFFF;");
	}

	private static void setUserGrid() {
    	userGrid.setAlignment(Pos.TOP_LEFT);
		userGrid.setHgap(10);
		userGrid.setVgap(10);
		userGrid.setPadding(new Insets(25, 10, 25, 25));
		userGrid.setStyle("-fx-background-color: #FFFFFF;");
	}

	private static void setCritterStage() {
		critterStage.setTitle("Critter World");
		critterStage.setX(710); // TODO: generalize this for every computer screen
		critterStage.setY(0);// TODO: generalize this for every computer screen
	}

	private static void setUserStage() {
		userStage.setTitle("User Interface");
		userStage.setX(-10);// TODO: generalize this for every computer screen
		userStage.setY(0);// TODO: generalize this for every computer screen
	}

	private static void setUserScene() {
		// put the buttons and user grid into a box
		HBox hbox = new HBox();
		hbox.getChildren().addAll(userGrid, buttonsGrid);
		userScene = new Scene(hbox, screenWidth, screenHeight);
	}

	private static void setCritterScene() {
		critterScene = new Scene(critterWorldStack, screenWidth, screenHeight);
	}

	// TODO: idk how scaling grid sizes for different computers works
	// TODO: looks promising http://www.java2s.com/Code/Java/JavaFX/Setstagexandyaccordingtoscreensize.htm
	// TODO: prob get rid of layer_CritterWorld

	@Override
	public void start(Stage primaryStage) {
		try {
			// set the stage and grid options
			setCritterStage(); setCritterGrid(); addGridLines();
			setUserStage(); setUserGrid();

			// toggle these debug options to see grid lines
//			layer_critterWorld.setGridLinesVisible(true);
			userGrid.setGridLinesVisible(true);

			// place all of the buttons and labels for the user interface
			placeNumTimeStepsOption();
			placeAddCrittersOption();
			placeRunStatsOption();
			placeQuitOption();
			placeButtons();

		    setUserScene();
		    setCritterScene();
			Critter.displayWorld();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
