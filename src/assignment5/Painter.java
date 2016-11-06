/* CRITTERS Painter.java
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
package assignment5;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Painter {

	/*
	 * Returns a square or a circle, according to shapeIndex
	 */
	static Shape getIcon(int shapeIndex) {
		Shape s = null;
		int size = 100;

		switch(shapeIndex) {
		case 0: s = new Rectangle(size, size);
			s.setFill(javafx.scene.paint.Color.RED); break;
		case 1: s = new Circle(size/2);
			s.setFill(javafx.scene.paint.Color.GREEN); break;
		}
		// set the outline of the shape
		s.setStroke(javafx.scene.paint.Color.BLUE); // outline
		return s;
	}

	/*
	 * Paints the shape on a grid.
	 */
	public static void paint() {
		Main.userGrid.getChildren().clear(); // clean up grid.
		for (int i = 0; i <= 1; i++) {
			Shape s = getIcon(i);	// convert the index to an icon.
			Main.userGrid.add(s, i, i); // add the shape to the grid.
		}

	}
}
