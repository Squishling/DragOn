package co.uk.squishling.dragon.objects;

import co.uk.squishling.dragon.Main;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class DeathMenu {
	private boolean drawn;
	
	private MenuButton menuButton;
	
	private int WIDTH;
	private int HEIGHT;
	
	private Font font;
	
	public DeathMenu(int WIDTH, int HEIGHT, Font font) {
		this.WIDTH = WIDTH;
		this.HEIGHT = HEIGHT;
		this.font = font;
		
		menuButton = new MenuButton((WIDTH / 2) - (WIDTH / 8), (HEIGHT / 2) + (HEIGHT / 20), WIDTH / 4, HEIGHT / 15, Color.GOLD, "MENU", font);
	}
	
	public void reset() {
		drawn = false;
	}
	
	public void draw(GraphicsContext gc, int time, int targetTime, String showTime, String showTargetTime) {
		if (!drawn) {
			gc.setGlobalAlpha(0.5);
			gc.setFill(Color.BLACK);
			gc.fillRect(0, 0, WIDTH, HEIGHT);
			gc.setGlobalAlpha(1.0);
			
			gc.setFill(Color.WHITE);
			gc.setFont(Font.font(font.getFamily(), HEIGHT / 20));
			gc.setTextAlign(TextAlignment.CENTER);
			
			if (time < targetTime) {
				gc.fillText("Congratulations!", WIDTH / 2, ((HEIGHT / 5) * 2) - HEIGHT / 100);
			} else {
				gc.fillText("Try again next time!", WIDTH / 2, ((HEIGHT / 5) * 2) - HEIGHT / 100);
			}
			
			gc.fillText("Your Time: " + showTime + ", Target Time: " + showTargetTime, WIDTH / 2, ((HEIGHT / 5) * 2) + HEIGHT / 20);
			
			menuButton.draw(gc);
		}
		
		drawn = true;
	}
	
	public void clicked(double x, double y) {
		if (menuButton.clicked(x, y)) {
			Main.setMenu(Main.MAIN_MENU);
		}
	}
}
