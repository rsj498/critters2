/* CRITTERS Critter1.java
 * EE422C Project 4 submission by
 * Justin Nguyen
 * jhn545
 * 16475
 * Rebecca Jiang
 * rsj498
 * 16470
 * Slip days used: <0>
 * Fall 2016
 */

/* CrossCountryCritter
 * This critter loves to run and always runs to the right.
 * This critter is always running and never has time to fight.
 * This critter never chooses to fight.
 */
package assignment5;

import assignment5.Critter.CritterShape;
import javafx.scene.paint.Color;

public class Critter1 extends Critter {

	@Override
	public void doTimeStep() {
		look(0, true);
		run(0);
	}

	@Override
	public boolean fight(String opponent) {
		return false;
	}
	
	public String toString() {
		return "1";
	}
	@Override
	public CritterShape viewShape() { return CritterShape.SQUARE; }
	@Override
	public Color viewColor() { return Color.RED; }
	
}
