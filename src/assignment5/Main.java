package assignment5;

import java.io.File;
import java.lang.reflect.Method;
import java.util.*;

import assignment5.Critter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
	public static int userScreenWidth = 520;
	public static int userScreenHeight = 600;
	public static int critterScreenWidth = 660;
	public static int critterScreenHeight = 660;

	// constants used for display
	static int numCrittersPerRow = Params.world_width;
	static int numCrittersPerCol = numCrittersPerRow;
	public static int critterBoxSize =
			(int) Math.sqrt(critterScreenWidth * critterScreenHeight /
			(numCrittersPerRow * numCrittersPerCol));

	private static final String myPackage =
		Critter.class.getPackage().toString().split(" ")[1];

	// grid, stage, scene for the critters
	static GridPane layer_critterWorld = new GridPane();
	static Stage critterStage = new Stage();
	static Scene critterScene
		= new Scene(layer_critterWorld, critterScreenWidth, critterScreenHeight);

	// grid, stage, scene for the user
	static GridPane userGrid = new GridPane();
	static Stage userStage = new Stage();
	static Scene userScene = new Scene(userGrid, userScreenWidth, userScreenHeight);

	// vertical box, grid for the buttons
	static VBox buttons = new VBox();
	static GridPane buttonsGrid = new GridPane();

	// stack containing the layers for the critter world
	static Group critterWorldStack = new Group();

	// animation slider
    private static Slider slider = new Slider();

//	private static void adjustScreen() {
//		while (screenWidth < 600) { screenWidth += 10; }
//		while (screenHeight < 600) { screenHeight += 10; }
//	}

	// TODO: setDisable(true)

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

	private static VBox dialogVbox;
	private static VBox makeMessage(String msg, Color textColor) {
		dialogVbox = new VBox(20);
        dialogVbox.setAlignment(Pos.CENTER);
        Text message = new Text(msg);
        message.setFill(textColor);
        message.setFont(
        	Font.font(Font.getDefault().toString(), FontWeight.BOLD, 20));
        dialogVbox.getChildren().add(message);
        return dialogVbox;
	}

	private static final Modality UNCHANGEABLE = Modality.APPLICATION_MODAL;
	private static final Modality CHANGEABLE = Modality.NONE;

	private static void showMessage(
			String msg, int boxSizeX, int boxSizeY, Color textColor, Modality modality)
	{
		final Stage dialog = new Stage();
        dialog.initModality(modality);
        dialog.initOwner(userStage);
        VBox dialogVbox = makeMessage(msg, textColor);
        Scene dialogScene = new Scene(dialogVbox, boxSizeX, boxSizeY);
        dialog.setScene(dialogScene);
        dialog.show();
	}

	private static void showImage(
			Image image, Modality modality)
	{
		final Stage dialog = new Stage();
        dialog.initModality(modality);
        dialog.initOwner(userStage);

        GridPane gridpane = new GridPane();
        gridpane.add(new ImageView(image), 0, 0);
        Scene dialogScene = new Scene(gridpane);
        dialog.setScene(dialogScene);
        dialog.show();
	}

	private static void placeSeedOption() {
		// Set the label and tool tip for setting the seed
		Label label_seed = new Label("Seed Number: ");
		userGrid.add(label_seed, 0, 0);
		TextField textField_seed = new TextField();
		textField_seed.setTooltip(
			new Tooltip("This seed will help repoduce simulation behavior"));
		userGrid.add(textField_seed, 1, 0);

		// Add the button for executing time steps
		Button button_seed = new Button("Set Seed");
		button_seed.setMaxWidth(Double.MAX_VALUE);
		buttons.getChildren().add(button_seed);

		// Make the button actually execute time steps
		button_seed.setOnAction((event) -> {
			try{
			    int seed = Integer.parseInt(textField_seed.getText());
			    Critter.setSeed(seed);
            } catch(Exception e){
            	String msg = "Please enter a valid integer for the seed";
            	showMessage(msg, 600, 100, Color.RED, UNCHANGEABLE);
            }
		});
	}

	private static void placeNumCrittersOption() {
		Label label_numCritters = new Label("Set Number of Critters\nPer Row and Column");
		userGrid.add(label_numCritters, 0, 1);
		TextField textField_numCritters = new TextField();
		textField_numCritters.setTooltip(
			new Tooltip("This will set the number of critters\nper row and column"));
		userGrid.add(textField_numCritters, 1, 1);

		// Add the button for setting the number of critters per row and column
		Button button_numCritters = new Button("Set Num Critters");
		button_numCritters.setMaxWidth(Double.MAX_VALUE);
		buttons.getChildren().add(button_numCritters);

		// Make the button actually run stats
		button_numCritters.setOnAction((event) -> {
			try{
				int num = Integer.parseInt(textField_numCritters.getText());
				Params.world_width = num; Params.world_height = num;
				numCrittersPerRow = num; numCrittersPerCol = num;
				critterBoxSize =
					(int) Math.sqrt(critterScreenWidth * critterScreenHeight /
					(numCrittersPerRow * numCrittersPerCol));
				Critter.critterSize = (int) (Main.critterBoxSize * 0.6);
				Critter.displayWorld();
			} catch(Exception e){
				e.printStackTrace();
				String msg = "Please enter a valid integer.";
            	showMessage(msg, 600, 100, Color.RED, UNCHANGEABLE);
			}
		});
	}

	private static void placeAddCrittersOption() throws Exception {
		// Place the "Add Critter(s)" label and text fields
		Label label_addCritter = new Label("Number and Type of Critters: ");
		userGrid.add(label_addCritter , 0, 2);
		TextField textField_addNumCritter = new TextField();
		textField_addNumCritter.setTooltip(
				new Tooltip("Number of critters to add"));
		userGrid.add(textField_addNumCritter, 1, 2);

		// Look through all .class files and grab all that are subclasses of critter
		ArrayList<String> validCritterTypes = getAllCritterSubclasses();

		// Set the pull down menu for the types of critters one can add to the world
		ChoiceBox<String> choiceBox_addCritter = new ChoiceBox<String>();
		choiceBox_addCritter.setMaxWidth(Double.MAX_VALUE);
		choiceBox_addCritter.setItems(FXCollections.observableList(validCritterTypes));
		choiceBox_addCritter.setTooltip(
				new Tooltip("Type of critters to add"));
		userGrid.add(choiceBox_addCritter, 1, 3);

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
                	showMessage(msg, 500, 100, Color.RED, UNCHANGEABLE);
            	} else if (e instanceof NumberFormatException) {
            		String msg = "Please enter a positive integer greater than or equal to zero.";
                    showMessage(msg, 600, 100, Color.RED, UNCHANGEABLE);
            	}
            }
		});
	}

	private static void placeNumTimeStepsOption() {
		// Set the label and tool tip for executing time steps
		Label label_numTimeSteps = new Label("Number of Time Steps: ");
		userGrid.add(label_numTimeSteps, 0, 4);
		TextField textField_numTimeSteps = new TextField();
		textField_numTimeSteps.setTooltip(
			new Tooltip("Number of time steps to execute"));
		userGrid.add(textField_numTimeSteps, 1, 4);

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
			    Critter.displayWorld();
            } catch(Exception e){
            	String msg = "Please enter a positive integer greater than or equal to zero.";
            	showMessage(msg, 600, 100, Color.RED, UNCHANGEABLE);
            }
		});
	}

	private static Timer timer = new Timer();
    public void animation() {
    	timer = new Timer();
    	timer.scheduleAtFixedRate(new TimerTask() {
    		@Override
    		public void run() {
    			Platform.runLater(new Runnable() {
    				@Override
					public void run() {
    					try {
    						Critter.worldTimeStep();
//    						StatsOutput.clear();
//    						Method m = Class.forName(myPackage+"."+dropStats.getValue()).getMethod("runStats", List.class);
//    	            		m.invoke(null, Critter.getInstances(dropStats.getValue()));
    						Critter.displayWorld();
    					} catch (Exception e1) {

    					}
    				}
    			});

    			try { Thread.sleep(1); }
    			catch (InterruptedException e) {}
    		}
    	}, (long)(5000/(slider.getValue())),(long)(5000/(slider.getValue())));
    }

    private static boolean animationIsRunning = false;

	private void placeSlider() {
		// Set the label and tool tip for setting the seed
		Label label_animation = new Label("Animation Speed: ");
		userGrid.add(label_animation, 0, 5);

		slider.setMin(0);
		slider.setMax(100);
		slider.setValue(40);
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
		slider.setMajorTickUnit(50);
		slider.setMinorTickCount(5);
		slider.setBlockIncrement(10);
		userGrid.add(slider, 1, 5);

		Button sliderButton = new Button("Run Simulation");
		sliderButton.setMaxWidth(Double.MAX_VALUE);
		buttons.getChildren().add(sliderButton);
		Button stopButton = new Button("Stop Simulation");
		stopButton.setMaxWidth(Double.MAX_VALUE);
		buttons.getChildren().add(stopButton);

		slider.valueProperty().addListener((observable, oldValue, newValue) -> {
			 if(animationIsRunning){
				 try{
					 timer.cancel();
					 animation();
				 } catch (Exception e1) {

				 }
			 }
		 });

		sliderButton.setOnAction((event) -> {
			// disable buttons and get slider value
			for(Node b: buttons.getChildren()){ b.setDisable(true); }
			stopButton.setDisable(false);

			// do the animation
			animationIsRunning = true;
			animation();

			// re-enable the buttons and cancel animation once stop is pressed
			stopButton.setOnAction((event2) -> {
				for(Node b: buttons.getChildren()){  b.setDisable(false); }
				timer.cancel();
			});
		});
	}

	private static void placeInvisibleButton(int height) {
		Button button_buffer = new Button();
		button_buffer.setVisible(false);
		button_buffer.setMinHeight(height);
		button_buffer.setMaxHeight(height);
		buttons.getChildren().add(button_buffer);
	}

	private static void placeRunStatsOption() throws Exception {
		// Place the "Run Stats" label and text fields
		Label label_runStats = new Label("Type of Critter(s): ");
		userGrid.add(label_runStats, 0, 8);

		// Look through all .class files and grab all that are subclasses of critter
		ArrayList<String> validCritterTypes = getAllCritterSubclasses();

		// Set the list menu for types of critters one can run stats on
		ListView<String> list_runStats = new ListView<String>();
		list_runStats.setItems(FXCollections.observableList(validCritterTypes));
		list_runStats.setTooltip(new Tooltip(
				"Type of critters to view stats on.\n"));
//				+ "You can select more than one option by holding the 'ctrl' button"));
		userGrid.add(list_runStats, 1, 8);

		// Configure settings for the list
		list_runStats.setPrefHeight(70);
		list_runStats.setPrefWidth(70);
		list_runStats.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		// Add the button for obtaining stats on specified critters
		// Add an invisible buffer button so that the actual button is centered
		Button button_runStats = new Button("Run Stats");
		button_runStats.setMaxWidth(Double.MAX_VALUE);
		buttons.getChildren().add(button_runStats);

		Label actualCritterStats = new Label("");
		Label statsLabel = new Label("Run Stats:");
		userGrid.add(statsLabel, 0, 10);
		userGrid.add(actualCritterStats, 1, 10);

		// Make the button actually run stats
		button_runStats.setOnAction((event) -> {
			try{
				ObservableList<String> critterChoice = list_runStats.getSelectionModel().getSelectedItems();
				List<Critter> critterList = new ArrayList<Critter>();

				String className = critterChoice.get(0); //  TODO: change me
                String fullClassName = myPackage + "." + className;
//                for (String s : critterChoice) {
//                	critterList.add(Critter.getInstance(s));
//                }
                critterList = Critter.getInstances(className);
                Class<?> cls = Class.forName(fullClassName);
                Method runStats = cls.getMethod("runStats", List.class);
                String str = (String) runStats.invoke(cls, critterList);

                // show the stats text
                actualCritterStats.setText(str);
            } catch(Exception e){
        		String msg = "Please choose one of the available critters.";
            	showMessage(msg, 600, 100, Color.RED, UNCHANGEABLE);
            }
		});
	}

	public static void placeSpecialButton() {
		// Add the button for showing no face doge
		Button button_special = new Button("Special Button");
		button_special.setMaxWidth(Double.MAX_VALUE);
		buttons.getChildren().add(button_special);
		button_special.setTooltip(
				new Tooltip("Click here to see the best thing ever"));

		// Make the button actually display no face doge
		button_special.setOnAction((event) -> {
			try{
				File file = new File("./NoFace_Doge.jpg");
				Image image = new Image(file.toURI().toString());
			    showImage(image, UNCHANGEABLE);
            } catch(Exception e){

            }
		});
	}

	private static void placeQuitOption() {
		// Add the quit button
		Button button_quit = new Button("Quit");
		button_quit.setMaxWidth(Double.MAX_VALUE);
		button_quit.setTextFill(Color.RED);
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
	}

	public static void addGridLines() {
		for (int i = 0; i < 2000; i += critterBoxSize) {
			int lineLength = Math.min(critterScreenHeight, critterScreenWidth);
			lineLength = lineLength / critterBoxSize * critterBoxSize;
//			lineLength = (int) ((Math.ceil(lineLength / critterBoxSize) + 1) * critterBoxSize);

			if (i <= critterScreenWidth  &&  i <= critterScreenHeight) {
			    Line line_vertical = new Line(i, 0, i, lineLength);
			    line_vertical.setStroke(Color.LIGHTGREY);
			    critterWorldStack.getChildren().add(line_vertical);
			}
		    if (i <= critterScreenHeight  &&  i <= critterScreenWidth) {
		    	Line line_horizontal = new Line(0, i, lineLength, i);
		    	line_horizontal.setStroke(Color.LIGHTGREY);
			    critterWorldStack.getChildren().add(line_horizontal);
		    }
		}
	}

	private static void setCritterGrid() {
    	layer_critterWorld.setAlignment(Pos.TOP_LEFT);
		layer_critterWorld.setHgap(5);
		layer_critterWorld.setVgap(5);
		layer_critterWorld.setPadding(new Insets(20, 20, 20, 20));
		layer_critterWorld.setStyle("-fx-background-color: #FFFFFF;");
	}

	private static void setCritterStage() {
		critterStage.setTitle("Critter World");
		critterStage.setX(700); // TODO: generalize this for every computer screen
		critterStage.setY(0);   // TODO: generalize this for every computer screen
		critterStage.setMinHeight(critterScreenHeight + 40);
		critterStage.setMinWidth(critterScreenWidth + 20);
	}

	public static void setCritterScene() {
		critterScene =
			new Scene(critterWorldStack, critterScreenWidth, critterScreenHeight);
	}

	private static void setUserGrid() {
    	userGrid.setAlignment(Pos.TOP_LEFT);
		userGrid.setHgap(10);
		userGrid.setVgap(10);
		userGrid.setPadding(new Insets(25, 10, 25, 25));
	}

	private static void setUserStage() {
		userStage.setTitle("User Interface");
		userStage.setX(-10);// TODO: generalize this for every computer screen
		userStage.setY(0);// TODO: generalize this for every computer screen
		userStage.setMinHeight(userScreenHeight);
		userStage.setMinWidth(userScreenWidth);
	}

	private static void setUserScene() {
		// put the buttons and user grid into a box
		HBox hbox = new HBox();
		hbox.getChildren().addAll(userGrid, buttonsGrid);
		userScene = new Scene(hbox, userScreenWidth, userScreenHeight);
	}

	// TODO: idk how scaling grid sizes for different computers works
	// TODO: looks promising http://www.java2s.com/Code/Java/JavaFX/Setstagexandyaccordingtoscreensize.htm

	@Override
	public void start(Stage primaryStage) {
		try {
			// set the stage and grid options
			setCritterStage(); setCritterGrid();
			setUserStage(); setUserGrid();
			addGridLines();

			// toggle these debug options to see grid lines
//			layer_critterWorld.setGridLinesVisible(true);
//			userGrid.setGridLinesVisible(true);

			// place all of the buttons and labels for the user interface
			placeSeedOption();
			placeNumCrittersOption();
				placeInvisibleButton(0);
			placeAddCrittersOption();
				placeInvisibleButton(25);
			placeNumTimeStepsOption();
			placeSlider();
				placeInvisibleButton(10);
			placeRunStatsOption();
				placeInvisibleButton(130);
			placeSpecialButton();
			placeQuitOption();
			placeButtons();

		    setUserScene();
		    setCritterScene();
			Critter.displayWorld();
			Critter.displayUserWorld();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
