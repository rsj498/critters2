/* CRITTERS Critter2.java
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

/* MamaAlgaeCritter
 * This critter loves giving birth to AlgaeCritters.
 * She will continuously give birth to AlgaeCritters until she can't give no more.
 */

package assignment5;

import assignment5.Critter.CritterShape;
import javafx.scene.paint.Color;

public class Critter3 extends Critter {

	@Override
	public void doTimeStep() {
		while (getEnergy() >= Params.min_reproduce_energy) {
			Algae child = new Algae();
			reproduce(child, Critter.getRandomInt(8));
		}
	}

	@Override
	public boolean fight(String opponent) {
		return false; // Moms don't like to fight their babies
	}
	
	public String toString() {
		return "3";
	}
	
	@Override
	public CritterShape viewShape() { return CritterShape.TRIANGLE; }
	@Override
	public Color viewColor() { return Color.ORANGE; }

}
