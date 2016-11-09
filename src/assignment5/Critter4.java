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

/* MamaCritter1Critter
 * This critter loves giving birth to Critter1Critters.
 * She will continuously give birth to Critter1Critters until she can't give no more.
 */
package assignment5;

import assignment5.Critter.CritterShape;
import javafx.scene.paint.Color;

public class Critter4 extends Critter {

	@Override
	public void doTimeStep() {
        Critter1 child = new Critter1();
        reproduce(child, Critter.getRandomInt(8));
	}

	@Override
	public boolean fight(String opponent) {
		return false;
	}
	
	public String toString() {
		return "4";
	}
	
	@Override
	public CritterShape viewShape() { return CritterShape.DIAMOND; }
	@Override
	public Color viewColor() { return Color.DARKVIOLET; }
}
