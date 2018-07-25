package co.uk.squishling.dragon.objects;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class MenuButton {
	private double x;
	private double y;
	private double w;
	private double h;
	private Paint color;
	private String text;
	private Font font;
	
	private boolean greyedOut = false;
	
	public MenuButton(double x, double y, double w, double h, Paint color, String text, Font font) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h= h;
		this.color = color;
		this.text = text;
		this.font = font;
	}
	
	public boolean clicked(double x, double y) {
		if (!greyedOut && x > this.x && x < this.x + w && y > this.y && y < this.y + h) {
			return true;
		}
		
		return false;
	}
	
	public void setGreyedOut(boolean greyedOut) {
		this.greyedOut = greyedOut;
	}
	
	public boolean getGreyedOut() {
		return greyedOut;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void draw(GraphicsContext gc) {
		gc.setTextBaseline(VPos.CENTER);
		gc.setFont(Font.font(font.getFamily(), h / 2));
		
		if (!greyedOut) {
			gc.setFill(color);
		} else {
			gc.setFill(Color.LIGHTGRAY);
		}
		
		gc.fillRect(x, y, w, h);
		
		if (!greyedOut) {
			gc.setFill(Color.WHITE);
		} else {
			gc.setFill(Color.GRAY);
		}
		
		gc.fillText(text, x + (w / 2), y + (h / 2));
	}
}
