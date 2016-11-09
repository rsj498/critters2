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

/* MamaCraigCritter
 * This critter loves giving birth to CraigCritters.
 * She will continuously give birth to CraigCritters until she can't give no more.
 */

package assignment5;

import assignment5.Critter.CritterShape;
import javafx.scene.paint.Color;

public class Critter2 extends Critter {

	@Override
	public void doTimeStep() {
		while (getEnergy() >= Params.min_reproduce_energy) {
			Craig child = new Craig();
			reproduce(child, Critter.getRandomInt(8));
		}
	}

	@Override
	public boolean fight(String opponent) {
		look(0,true);
		return false; // Moms don't like to fight their babies
	}
	
	public String toString() {
		return "2";
	}
	@Override
	public CritterShape viewShape() { return CritterShape.TRIANGLE; }
	@Override
	public javafx.scene.paint.Color viewOutlineColor() { return javafx.scene.paint.Color.SPRINGGREEN; }

}
