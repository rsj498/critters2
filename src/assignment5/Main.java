package assignment5;

import java.io.File;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javafx.stage.Stage;

public class Main extends Application {
	// screen width and height factors
	private final static int screenWidthScalingFactor = 1;
	private final static int screenHeightScalingFactor = 1;
	private final static int screenWidth = Params.world_width * screenWidthScalingFactor;
	private final static int screenHeight = Params.world_height * screenHeightScalingFactor;

	// grid, stage, scene for the critters
	static GridPane critterGrid = new GridPane();
	static Stage critterStage = new Stage();
	static Scene critterScene = new Scene(critterGrid, screenWidth, screenHeight);

	// grid, stage, scene for the user
	static GridPane userGrid = new GridPane();
	static Stage userStage = new Stage();
	static Scene userScene = new Scene(userGrid, screenWidth, screenHeight);

	// vertical box, grid for the buttons
	static VBox buttons = new VBox();
	static GridPane buttonsGrid = new GridPane();

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

		// Enable an action upon choosing an option
		choiceBox_addCritter.getSelectionModel().selectedIndexProperty().addListener(
			new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observable,
									Number oldValue, Number newValue) {
					// TODO: fill this out
				}
			}
		);
		userGrid.add(choiceBox_addCritter, 2, 1);

		// Add the button for adding critters
		Button button_addCritters = new Button("Add Critters");
		button_addCritters.setMaxWidth(Double.MAX_VALUE);
		buttons.getChildren().add(button_addCritters);
	}

	private static void placeRunStatsOption() throws Exception {
		// Place the "Run Stats" label and text fields
		Label label_runStats = new Label("Type of Critter(s): ");
		userGrid.add(label_runStats, 0, 2);

		// Look through all .class files and grab all that are subclasses of critter
		ArrayList<String> validCritterTypes = getAllCritterSubclasses();

		// Set the pull down menu for the types of critters one can add to the world
//		ChoiceBox<String> choiceBox_runStats = new ChoiceBox<String>();
//		choiceBox_runStats.setItems(FXCollections.observableList(validCritterTypes));
//		choiceBox_runStats.setTooltip(new Tooltip(
//				"Type of critters to view stats on.\n"
//				+ "You can select more than one option"));
//		userGrid.add(choiceBox_runStats, 2, 2);

		// Set the list menu for types of critters one can run stats on
		ListView<String> list_runStats = new ListView<String>();
		list_runStats.setItems(FXCollections.observableList(validCritterTypes));
		list_runStats.setTooltip(new Tooltip(
				"Type of critters to view stats on.\n"
				+ "You can select more than one option"));
		userGrid.add(list_runStats, 2, 2);

		// Configure settings for the list
		list_runStats.setPrefHeight(70);
		list_runStats.setPrefWidth(70);
		list_runStats.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		// Enable an action upon choosing an option
		list_runStats.getSelectionModel().selectedIndexProperty().addListener(
			new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observable,
									Number oldValue, Number newValue) {
					// TODO: figure out what to do with
					//System.out.println(list_runStats.getSelectionModel().getSelectedItems());
					// TODO: probably call that ^ when someone clicks the runStats button
				}
			}
		);

		// Add the button for obtaining stats on specified critters
		Button button_runStats = new Button("Run Stats");
		button_runStats.setMaxWidth(Double.MAX_VALUE);

		buttons.getChildren().add(button_runStats);
	}

	// TODO: write this
	private static void placeQuitOption() {

	}

	private static void placeButtons() {
		buttons.setSpacing(10);
		buttons.setPadding(new Insets(25, 25, 25, 0));
		buttonsGrid.setHgap(10);
		buttonsGrid.setVgap(10);
		buttonsGrid.add(buttons, 0, 0);
	}

	private static void setCritterGrid() {
    	critterGrid.setAlignment(Pos.CENTER);
		critterGrid.setHgap(10);
		critterGrid.setVgap(10);
		critterGrid.setPadding(new Insets(25, 25, 25, 25));
	}

	private static void setUserGrid() {
    	userGrid.setAlignment(Pos.TOP_LEFT);
		userGrid.setHgap(10);
		userGrid.setVgap(10);
		userGrid.setPadding(new Insets(25, 10, 25, 25));
	}

	private static void setCritterStage() {
		critterStage.setTitle("Critter World");
		critterStage.setX(760); // TODO: generalize this for every computer screen
		critterStage.setY(0);// TODO: generalize this for every computer screen
	}

	private static void setUserStage() {
		userStage.setTitle("User Interface");
		userStage.setX(-10);// TODO: generalize this for every computer screen
		userStage.setY(0);// TODO: generalize this for every computer screen
	}

	// TODO: idk how scaling grid sizes for different computers works
	// TODO: looks promising http://www.java2s.com/Code/Java/JavaFX/Setstagexandyaccordingtoscreensize.htm

	@Override
	public void start(Stage primaryStage) {
		try {
			// set the stage and grid options
			setCritterStage(); setCritterGrid();
			setUserStage(); setUserGrid();

			// toggle these debug options to see grid lines
			critterGrid.setGridLinesVisible(true);
			userGrid.setGridLinesVisible(true);

			// place all of the buttons and labels for the user interface
			placeNumTimeStepsOption();
			placeAddCrittersOption();
			placeRunStatsOption();
			placeQuitOption();
			placeButtons();

			HBox hbox = new HBox();
			hbox.getChildren().addAll(userGrid, buttonsGrid);
			userScene = new Scene(hbox, screenWidth, screenHeight);

			Critter.displayWorld();
//			Painter.paint();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
