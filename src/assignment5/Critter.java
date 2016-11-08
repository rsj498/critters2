package assignment5;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public abstract class Critter {
	// ==========================================================================
	// ============================== NEW STUFF =================================

	/* the default color is white, which I hope makes critters invisible by default
	 * If you change the background color of your View component, then update the default
	 * color to be the same as you background
	 *
	 * critters must override at least one of the following three methods, it is not
	 * proper for critters to remain invisible in the view
	 *
	 * If a critter only overrides the outline color, then it will look like a non-filled
	 * shape, at least, that's the intent. You can edit these default methods however you
	 * need to, but please preserve that intent as you implement them.
	 */

	public enum CritterShape {
		CIRCLE,
		SQUARE,
		TRIANGLE,
		DIAMOND,
		STAR
	}

	public Color viewColor()        { return Color.WHITE; }
	public Color viewOutlineColor() { return viewColor(); }
	public Color viewFillColor()    { return viewColor(); }

	public abstract CritterShape viewShape();

	// ==========================================================================
	// ==========================================================================

	public static final int critterSize = (int) (Main.critterBoxSize * 0.6);
	private static String myPackage;
	private	static List<Critter> population = new java.util.ArrayList<Critter>();
	private static List<Critter> babies = new java.util.ArrayList<Critter>();
	private static int[][] critterCount = new int[Params.world_height][Params.world_width];
	private static HashMap<Point, String> oldCritterCount = new HashMap<Point, String>();
    private static boolean isFightingPhase = false;
    private boolean hasMoved = false;
    private int energy = 0;
    public int x_coord; // TODO: put this back to private
	public int y_coord;

	public abstract void doTimeStep();
	public abstract boolean fight(String oponent);

	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}

	// TODO: when to remove dead critters (see pdf)

	// TODO: test look()
	protected String look(int direction, boolean steps) {
		energy -= Params.look_energy_cost;
        if (energy <= 0) { critterCount[y_coord][x_coord] -= 1; }
		int numSteps = (steps ? 2 : 1);
		Point resultingLoc = adjLoc(direction, numSteps);

		if (isFightingPhase) {
			if (critterCount[resultingLoc.y][resultingLoc.x] == 0) {
				return null;
			} else {
				for (Critter c : population) {
					if (c.x_coord == resultingLoc.y  &&  c.y_coord == resultingLoc.x) {
						return c.toString();
					}
				}
				System.err.println("Look went wrong. OHNOOOESSSS");
				return ""; // TODO: remove me
			}
		} else {
			return oldCritterCount.get(resultingLoc);
		}
	}

	protected final void paint(Group g) {
		Shape s = new Polygon();
		double[] circleCoordinates = new double[] {
			(critterSize/2) * Math.cos(90),   (critterSize/2) * Math.sin(90),
			(critterSize/2) * Math.cos(85),   (critterSize/2) * Math.sin(85),
			(critterSize/2) * Math.cos(80),   (critterSize/2) * Math.sin(80),
			(critterSize/2) * Math.cos(75),   (critterSize/2) * Math.sin(75),
			(critterSize/2) * Math.cos(70),   (critterSize/2) * Math.sin(70),
			(critterSize/2) * Math.cos(65),   (critterSize/2) * Math.sin(65),
			(critterSize/2) * Math.cos(60),   (critterSize/2) * Math.sin(60),
			(critterSize/2) * Math.cos(55),   (critterSize/2) * Math.sin(55),
			(critterSize/2) * Math.cos(50),   (critterSize/2) * Math.sin(50),
			(critterSize/2) * Math.cos(45),   (critterSize/2) * Math.sin(45),
			(critterSize/2) * Math.cos(40),   (critterSize/2) * Math.sin(40),
			(critterSize/2) * Math.cos(35),   (critterSize/2) * Math.sin(35),
			(critterSize/2) * Math.cos(30),   (critterSize/2) * Math.sin(30),
			(critterSize/2) * Math.cos(25),   (critterSize/2) * Math.sin(25),
			(critterSize/2) * Math.cos(20),   (critterSize/2) * Math.sin(20),
			(critterSize/2) * Math.cos(15),   (critterSize/2) * Math.sin(15),
			(critterSize/2) * Math.cos(10),   (critterSize/2) * Math.sin(10),
			(critterSize/2) * Math.cos(5),    (critterSize/2) * Math.sin(5),
			(critterSize/2) * Math.cos(0),    (critterSize/2) * Math.sin(0),
			(critterSize/2) * Math.cos(355),  (critterSize/2) * Math.sin(355),
			(critterSize/2) * Math.cos(350),  (critterSize/2) * Math.sin(350),
			(critterSize/2) * Math.cos(345),  (critterSize/2) * Math.sin(345),
			(critterSize/2) * Math.cos(340),  (critterSize/2) * Math.sin(340),
			(critterSize/2) * Math.cos(335),  (critterSize/2) * Math.sin(335),
			(critterSize/2) * Math.cos(330),  (critterSize/2) * Math.sin(330),
			(critterSize/2) * Math.cos(325),  (critterSize/2) * Math.sin(325),
			(critterSize/2) * Math.cos(320),  (critterSize/2) * Math.sin(320),
			(critterSize/2) * Math.cos(315),  (critterSize/2) * Math.sin(315),
			(critterSize/2) * Math.cos(310),  (critterSize/2) * Math.sin(310),
			(critterSize/2) * Math.cos(305),  (critterSize/2) * Math.sin(305),
			(critterSize/2) * Math.cos(300),  (critterSize/2) * Math.sin(300),
			(critterSize/2) * Math.cos(295),  (critterSize/2) * Math.sin(295),
			(critterSize/2) * Math.cos(290),  (critterSize/2) * Math.sin(290),
			(critterSize/2) * Math.cos(285),  (critterSize/2) * Math.sin(285),
			(critterSize/2) * Math.cos(280),  (critterSize/2) * Math.sin(280),
			(critterSize/2) * Math.cos(275),  (critterSize/2) * Math.sin(275),
			(critterSize/2) * Math.cos(270),  (critterSize/2) * Math.sin(270),
			(critterSize/2) * Math.cos(265),  (critterSize/2) * Math.sin(265),
			(critterSize/2) * Math.cos(260),  (critterSize/2) * Math.sin(260),
			(critterSize/2) * Math.cos(255),  (critterSize/2) * Math.sin(255),
			(critterSize/2) * Math.cos(250),  (critterSize/2) * Math.sin(250),
			(critterSize/2) * Math.cos(245),  (critterSize/2) * Math.sin(245),
			(critterSize/2) * Math.cos(240),  (critterSize/2) * Math.sin(240),
			(critterSize/2) * Math.cos(235),  (critterSize/2) * Math.sin(235),
			(critterSize/2) * Math.cos(230),  (critterSize/2) * Math.sin(230),
			(critterSize/2) * Math.cos(225),  (critterSize/2) * Math.sin(225),
			(critterSize/2) * Math.cos(220),  (critterSize/2) * Math.sin(220),
			(critterSize/2) * Math.cos(215),  (critterSize/2) * Math.sin(215),
			(critterSize/2) * Math.cos(210),  (critterSize/2) * Math.sin(210),
			(critterSize/2) * Math.cos(205),  (critterSize/2) * Math.sin(205),
			(critterSize/2) * Math.cos(200),  (critterSize/2) * Math.sin(200),
			(critterSize/2) * Math.cos(195),  (critterSize/2) * Math.sin(195),
			(critterSize/2) * Math.cos(190),  (critterSize/2) * Math.sin(190),
			(critterSize/2) * Math.cos(185),  (critterSize/2) * Math.sin(185),
			(critterSize/2) * Math.cos(180),  (critterSize/2) * Math.sin(180),
			(critterSize/2) * Math.cos(175),  (critterSize/2) * Math.sin(175),
			(critterSize/2) * Math.cos(170),  (critterSize/2) * Math.sin(170),
			(critterSize/2) * Math.cos(165),  (critterSize/2) * Math.sin(165),
			(critterSize/2) * Math.cos(160),  (critterSize/2) * Math.sin(160),
			(critterSize/2) * Math.cos(155),  (critterSize/2) * Math.sin(155),
			(critterSize/2) * Math.cos(150),  (critterSize/2) * Math.sin(150),
			(critterSize/2) * Math.cos(145),  (critterSize/2) * Math.sin(145),
			(critterSize/2) * Math.cos(140),  (critterSize/2) * Math.sin(140),
			(critterSize/2) * Math.cos(135),  (critterSize/2) * Math.sin(135),
			(critterSize/2) * Math.cos(130),  (critterSize/2) * Math.sin(130),
			(critterSize/2) * Math.cos(125),  (critterSize/2) * Math.sin(125),
			(critterSize/2) * Math.cos(120),  (critterSize/2) * Math.sin(120),
			(critterSize/2) * Math.cos(115),  (critterSize/2) * Math.sin(115),
			(critterSize/2) * Math.cos(110),  (critterSize/2) * Math.sin(110),
			(critterSize/2) * Math.cos(105),  (critterSize/2) * Math.sin(105),
			(critterSize/2) * Math.cos(100),  (critterSize/2) * Math.sin(100),
			(critterSize/2) * Math.cos(95),   (critterSize/2) * Math.sin(95)
		};

		double[] squareCoordinates = new double[] {
            0,             0,
            critterSize,   0,
            critterSize,   critterSize,
            0,             critterSize
        };

		double[] diamondCoordinates = new double[] {
            critterSize/2,     0,
            critterSize,       critterSize/2,
            critterSize/2,     critterSize,
            0,                 critterSize/2
        };

		double[] starCoordinates = new double[] {
            critterSize/2,      0,
            critterSize*0.85,   critterSize,
            0,                  critterSize/3,
            critterSize,        critterSize/3,
            critterSize*0.15,   critterSize
        };

		double[] triangleCoordinates = new double[] {
            critterSize/2,      0,
            critterSize,        critterSize,
            0,                  critterSize,
        };

		switch (viewShape()) {
			case CIRCLE:   s = new Circle(critterSize / 2);      break;
			case SQUARE:   s = new Polygon(squareCoordinates);   break;
            case DIAMOND:  s = new Polygon(diamondCoordinates);  break;
			case STAR:     s = new Polygon(starCoordinates);     break;
			case TRIANGLE: s = new Polygon(triangleCoordinates); break;
			default:       break;
		}

		// set appropriate colors
		s.setFill(viewFillColor());
		s.setStroke(viewOutlineColor());

		// adjust the critter to the middle of the box
		int placementAdjustment = ((Main.critterBoxSize - critterSize) / 2);

		// do some math to find the correct box for the critter
//		double boxNumX = Math.floor(x_coord / Main.critterBoxSize);
//		double boxNumY = Math.floor(y_coord / Main.critterBoxSize);
		s.relocate(x_coord * Main.critterBoxSize + placementAdjustment,
				   y_coord * Main.critterBoxSize + placementAdjustment);
		g.getChildren().add(s);
	}

	// ==========================================================================
	// ============================== OLD STUFF =================================

	private static java.util.Random rand = new java.util.Random();
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}

	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}


	/* a one-character long string that visually depicts your critter in the ASCII interface */
	@Override
	public String toString() { return ""; }

	protected int getEnergy() { return energy; }

    // Set size of critterCount matrix according to changes in Params
    {
    	if (critterCount.length != Params.world_height
    		  ||  critterCount[0].length != Params.world_width) {
    		critterCount = new int[Params.world_height][Params.world_width];
    	}
    }

    /**
     * Simple nested class for helping keep track of locations.
     * Instead of x,y coordinates, now we can use "points"
     */
    private class Point {
        private int x;
        private int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Walking involves moving the critter once.
     * @param direction the direction in which the critter will move
     * @return none
     */
    protected final void walk(int direction) {
        energy -= Params.walk_energy_cost;
        move(direction, 1); // walk only moves the critter once
        if (energy <= 0) { critterCount[y_coord][x_coord] -= 1; }
    }

    /**
     * Running involves moving the critter twice.
     * @param direction the direction in which the critter will move
     * @return none
     */
    protected final void run(int direction) {
        energy -= Params.run_energy_cost;
        move(direction, 2); // run moves the critter twice
        if (energy <= 0) { critterCount[y_coord][x_coord] -= 1; }
    }

    /**
     * Helper function for walking and running.
     * Adds an extra field to make walking and running more reusable.
     * @param direction the direction in which the critter will move
     * @param numMoves the number of steps the critter will take
     * @return none
     */
    private void move(int direction, int numMoves) {
        if (hasMoved) { return; } // can't move more than once per time step

        Point newLoc = adjLoc(direction, numMoves);
        if (isFightingPhase  &&  critterCount[newLoc.y][newLoc.x] > 0) {
            return; // can't move onto another critter during fighting phase
        }

        // can actually move to new adjacent location
        critterCount[y_coord][x_coord] -= 1;
        x_coord = newLoc.x;
        y_coord = newLoc.y;
        critterCount[y_coord][x_coord] += 1;

        hasMoved = true;
    }

    /**
     * Call this function to give birth to new life.
     * @param offspring the child Critter being created
     * @param direction the direction the child will initially face
     * @return none
     */
    protected final void reproduce(Critter offspring, int direction) {
        // Invalid reproduction
        if (energy < Params.min_reproduce_energy  ||  ! isAlive()) { return; }

        // Update energies
        energy = (int) Math.ceil(energy / 2.0);
        offspring.energy = energy / 2;

        // Account for world wrap
        Point newLoc = adjLoc(direction, 1);
        offspring.x_coord = newLoc.x;
        offspring.y_coord = newLoc.y;

        // Add babies and remove parent (if it died)
        babies.add(offspring);
        if (energy <= 0) { critterCount[y_coord][x_coord] -= 1; }
    }

    /**
     * Helper function for finding the adjacent location based on
     *   a direction and a number of times to move.
     * @param dir the direction to move in
     * @param numMoves the number of movements in that direction
     * @return a new Point indicating where to move to
     */
   Point adjLoc(int dir, int numMoves) {
       int newX = 0; int newY = 0;
       int worldWidth = Params.world_width;
       int worldHeight = Params.world_height;

       // Find the adjacent location based on the direction
       switch (dir) {
           case 0: newX = x_coord + numMoves; newY = y_coord;            break;
           case 1: newX = x_coord + numMoves; newY = y_coord - numMoves; break;
           case 2: newX = x_coord;            newY = y_coord - numMoves; break;
           case 3: newX = x_coord - numMoves; newY = y_coord - numMoves; break;
           case 4: newX = x_coord - numMoves; newY = y_coord;            break;
           case 5: newX = x_coord - numMoves; newY = y_coord + numMoves; break;
           case 6: newX = x_coord;            newY = y_coord + numMoves; break;
           case 7: newX = x_coord + numMoves; newY = y_coord + numMoves; break;
           default: break;
       }

       // Fix x,y wrap around
       if (newX < 0)                 { newX = worldWidth + newX; }
       else if (newX >= worldWidth)  { newX = newX - worldWidth; }
       if (newY < 0)                 { newY = worldHeight + newY; }
       else if (newY >= worldHeight) { newY = newY - worldHeight; }
       return new Point(newX, newY);
   }

	/**
     * Helper function for the overloaded fight method (more natural).
     * @return true if the critter wants to fight
     */
    private boolean fight(Critter c) {
        return fight(c.toString());
    }

	/**
     * create and initialize a Critter subclass.
     * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
     * an InvalidCritterException must be thrown.
     * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
     * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
     * an Exception.)
     * @param critter_class_name
     * @throws InvalidCritterException
     */
    public static void makeCritter(String critter_class_name) throws InvalidCritterException {
        String fullClassName = myPackage + "." + critter_class_name;
        try {
            Object obj = Class.forName(fullClassName).newInstance();
            if (obj instanceof Critter) { // valid critter --> put into the world
                int randX = getRandomInt(Params.world_width - 1);
                int randY = getRandomInt(Params.world_height - 1);

                Critter c = (Critter) obj;
                c.energy = Params.start_energy;
                c.x_coord = randX;
                c.y_coord = randY;

                population.add(c);
                critterCount[c.y_coord][c.x_coord] += 1;

                Point p = c.new Point(randX, randY);
                oldCritterCount.put(p, c.toString());
            }
        } catch (ClassNotFoundException e) {
            throw new InvalidCritterException(critter_class_name);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new InvalidCritterException(critter_class_name);
        }
    }

	/**
     * Gets a list of critters of a specific type.
     * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
     * @return List of Critters.
     * @throws InvalidCritterException
     */
    public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
        List<Critter> res = new java.util.ArrayList<Critter>();
        String fullClassName = myPackage + "." + critter_class_name;
        try{
        	for(Critter c: population){
        		if(Class.forName(fullClassName).isInstance(c))
        			res.add(c);
        	}
        } catch (ClassNotFoundException e) {
            throw new InvalidCritterException(critter_class_name);
        }
        return res;
    }

    public static Critter getInstance(String critter_class_name) throws InvalidCritterException {
        String fullClassName = myPackage + "." + critter_class_name;
        try{
        	for(Critter c: population){
        		if(Class.forName(fullClassName).isInstance(c))
        			return c;
        	}
        } catch (ClassNotFoundException e) {
            throw new InvalidCritterException(critter_class_name);
        }
        return null;
    }

    /**
     * Gets a list of critters of a specific type.
     * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
     * @param searchList the list to search through for the given instances
     * @return List of Critters.
     * @throws InvalidCritterException
     */
    // TODO: test this
    public static boolean isCritterInstance(String critter_class_name) throws InvalidCritterException {
        String fullClassName = myPackage + "." + critter_class_name;
        try{
        	Object obj = Class.forName(fullClassName).newInstance();
        	if (obj instanceof Critter) { return true; }
        	return false;
        } catch (ClassNotFoundException | InstantiationException e) {
            return false;
        } catch (IllegalAccessException e) {
        	throw new InvalidCritterException(critter_class_name);
        }
    }

	/**
     * Prints out how many Critters of each type there are on the board.
     * @param critters List of Critters.
     */
    public static void runStats(List<Critter> critters) {
        System.out.print("" + critters.size() + " critters as follows -- ");
        java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
        for (Critter crit : critters) {
            String crit_string = crit.toString();
            Integer old_count = critter_count.get(crit_string);
            if (old_count == null) {
                critter_count.put(crit_string,  1);
            } else {
                critter_count.put(crit_string, old_count.intValue() + 1);
            }
        }
        String prefix = "";
        for (String s : critter_count.keySet()) {
            System.out.print(prefix + s + ":" + critter_count.get(s));
            prefix = ", ";
        }
        System.out.println();
    }

	/* the TestCritter class allows some critters to "cheat". If you want to
	 * create tests of your Critter model, you can create subclasses of this class
	 * and then use the setter functions contained here.
	 *
	 * NOTE: you must make sure that the setter functions work with your implementation
	 * of Critter. That means, if you're recording the positions of your critters
	 * using some sort of external grid or some other data structure in addition
	 * to the x_coord and y_coord functions, then you MUST update these setter functions
	 * so that they correctly update your grid/data structure.
	 */
	static abstract class TestCritter extends Critter {
		protected void setEnergy(int new_energy_value) {
			super.energy = new_energy_value;
            if (new_energy_value <= 0) {
            	critterCount[getY_coord()][getX_coord()] -= 1;
            }
		}

		protected void setX_coord(int new_x_coord) {
			critterCount[getY_coord()][getX_coord()] -= 1;
            super.x_coord = new_x_coord;
        	critterCount[getY_coord()][new_x_coord] += 1;
		}

		protected void setY_coord(int new_y_coord) {
			critterCount[getY_coord()][getX_coord()] -= 1;
            super.y_coord = new_y_coord;
        	critterCount[new_y_coord][getX_coord()] += 1;
		}

		protected int getX_coord() {
			return super.x_coord;
		}

		protected int getY_coord() {
			return super.y_coord;
		}


		/*
		 * This method getPopulation has to be modified by you if you are not using the population
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.
		 */
		protected static List<Critter> getPopulation() {
			return population;
		}

		/*
		 * This method getBabies has to be modified by you if you are not using the babies
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.  Babies should be added to the general population
		 * at either the beginning OR the end of every timestep.
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}

	/**
	 * Clear the world of all critters, dead and alive
	 */
	public static void clearWorld() {
        population = new java.util.ArrayList<Critter>();
        critterCount = new int[Params.world_height][Params.world_width];
        oldCritterCount = new HashMap<Point, String>();
        isFightingPhase = false;
    }

    /**
     * Test to see if the critter is alive
     * @return true if the critter's energy is above zero, false otherwise
     */
    private boolean isAlive() {
        return (energy > 0);
    }

    /**
     * Helper method for worldTimeStep(). Performs the individual time steps.
     * @return none
     */
    private static void doIndividTimeSteps() {
        Iterator<Critter> itr = population.iterator();
        while (itr.hasNext()) {
            Critter c = itr.next();
            if (c.isAlive()) { c.doTimeStep(); }
        }
    }

    /**
     * Test to see if the two critters are in the same location.
     * @param critter1 the first critter
     * @param critter2 the second critter
     * @return true if the critters are in the same location, else false
     */
    private static boolean areInSameLoc(Critter critter1, Critter critter2) {
        return (critter1.x_coord == critter2.x_coord  &&
                critter1.y_coord == critter2.y_coord);
    }

    /**
     * Helper method for worldTimeStep(). Resolve encounters.
     * @return none
     */
    private static void resolveEncounters() {
        // Iterate over each combination of critters
        for (int i = 0; i < population.size(); ++i) {
            Critter c1 = population.get(i);
            if (! c1.isAlive()) { continue; }
            for (int j = i+1; j < population.size(); ++j) {
                Critter c2 = population.get(j);
                if (! c2.isAlive()) { continue; }

                // Invoke both fight methods to determine if there is indeed a fight
                if (areInSameLoc(c1, c2)) {
                    isFightingPhase = true;
                    boolean c1WantsToFight = c1.fight(c2);
                    boolean c2WantsToFight = c2.fight(c1);

                    // Check to see if either critter died from invoking their fight method
                    if (! c1.isAlive()) {
                        critterCount[c1.y_coord][c1.x_coord] -= 1;
                        if (! c2.isAlive()) { critterCount[c2.y_coord][c2.x_coord] -= 1; }
                        break;
                    }
                    if (! c2.isAlive()) { // needs to be here
                        critterCount[c2.y_coord][c2.x_coord] -= 1;
                        continue;
                    }

                    // Both still alive and in same location --> time to fight
                    if (areInSameLoc(c1, c2)) {
                        int c1WinningChance; int c2WinningChance;
                        if (! c1WantsToFight) { c1WinningChance = 0; }
                        else { c1WinningChance = getRandomInt(c1.energy + 1); }
                        if (! c2WantsToFight) { c2WinningChance = 0; }
                        else { c2WinningChance = getRandomInt(c2.energy + 1); }

                        if (c1WinningChance >= c2WinningChance) { // Critter 1 stays alive
                            c1.energy += c2.energy / 2;
                            c2.energy = 0;
                            critterCount[c2.y_coord][c2.x_coord] -= 1;
                        } else { // Critter 2 stays alive
                            c2.energy += c1.energy / 2;
                            c1.energy = 0;
                            critterCount[c1.y_coord][c1.x_coord] -= 1;
                            break;
                        }
                    }
                }
            }
        }
        isFightingPhase = false;
    }


    /**
     * Helper method for worldTimeStep().
     * Subtract rest_energy and do final check to remove dead critters.
     * @return none
     */
    private static void finalCheckForDeadCritters() {
        Iterator<Critter> itr = population.iterator();
        while (itr.hasNext()) {
            Critter c = itr.next();
            if (! c.isAlive()) { itr.remove(); continue; }
            c.energy -= Params.rest_energy_cost;
            if (! c.isAlive()) { // died because of rest_energy cost
                critterCount[c.y_coord][c.x_coord] -= 1;
                itr.remove();
            }
        }
    }

    /**
     * Helper method for worldTimeStep().
     * Add babies to the population of critters.
     * @return none
     */
    private static void addBabies() {
        for (Critter c : babies) {
            population.add(c);
            critterCount[c.y_coord][c.x_coord] += 1;
        }
        babies.clear();
    }

    /**
     * Helper method for worldTimeStep().
     * Adds algae to the world.
     * @return none
     */
    private static void addAlgae() {
        for (int i = 0; i < Params.refresh_algae_count; ++i) {
            Critter al = new Algae();
            al.energy = Params.start_energy;
            al.x_coord = getRandomInt(Params.world_width);
            al.y_coord = getRandomInt(Params.world_height);
            critterCount[al.y_coord][al.x_coord] += 1;
            population.add(al);
        }
    }

    /**
     * Helper method for worldTimeStep().
     * Resets the hasMoved flags for each critter in the time step.
     * Resets the isFightingPhase flag.
     * Updates oldCritterCount to be aligned with the current time step.
     */
    private static void resetFlags() {
        for (Critter c : population) { c.hasMoved = false; }
        isFightingPhase = false;
        oldCritterCount = new HashMap<Point, String>();
        for (Critter c : population) {
        	Point p = c.new Point(c.x_coord, c.y_coord);
        	oldCritterCount.put(p, c.toString());
        }
    }

    /**
     * Simulates one time step for every critter still alive.
     * Resolves all encounters (at the end of each time step),
     *   until each location has at most one critter.
     * All dead critters should be removed by the end of this function.
     *
     * @return none
     */
    public static void worldTimeStep() {
        doIndividTimeSteps();
        resolveEncounters();
        finalCheckForDeadCritters();
        addBabies();
        addAlgae();
        resetFlags();
    }

    /**
     * Display the critter world grid and user interface.
     */
    public static void displayWorld() {
    	Main.critterWorldStack = new Group();
    	Main.addGridLines();
    	for (Critter c : population) { c.paint(Main.critterWorldStack); }
    	Main.setCritterScene();

		Main.critterStage.setScene(Main.critterScene);
		Main.critterStage.show();

		Main.userStage.setScene(Main.userScene);
		Main.userStage.show();
    }
}
