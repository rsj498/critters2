package assignment5;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Painter {
	public static void paintGridSquare(int x, int y, GridPane g) {
		Shape s = new Rectangle(Critter.critterSize, Critter.critterSize);
		s.setFill(Color.WHITE);
		s.setStroke(Color.WHITE);
		g.add(s, x, y);
	}
}
