package co.uk.squishling.dragon.objects;

import co.uk.squishling.dragon.Main;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class MainMenu {
	private boolean drawn;
	
	private MenuButton playButton;
	private MenuButton exitButton;
	
	private int WIDTH;
	private int HEIGHT;
	
	private Font font;
	
	public MainMenu(int WIDTH, int HEIGHT, Font font) {
		this.WIDTH = WIDTH;
		this.HEIGHT = HEIGHT;
		this.font = font;
		
		playButton = new MenuButton(WIDTH / 4, HEIGHT / 3, WIDTH / 2, HEIGHT / 5, Color.GOLD, "PLAY", font);
		exitButton = new MenuButton(WIDTH / 4, (HEIGHT / 6) * 3.5, WIDTH / 2, HEIGHT / 5, Color.GOLD, "EXIT", font);
	}
	
	public void reset() {
		drawn = false;
	}
	
	public void draw(GraphicsContext gc) {
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, WIDTH, HEIGHT);
		
		gc.setFill(Color.WHITE);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.BASELINE);
		gc.setFont(Font.font(font.getFamily(), HEIGHT / 5));
		gc.fillText("DragOn", WIDTH / 2, HEIGHT / 4);
		
		playButton.draw(gc);
		exitButton.draw(gc);
		
		drawn = true;
	}
	
	public void clicked(double x, double y) {
		if (playButton.clicked(x, y)) {
			reset();
			Main.setTime(0);
			Main.setMenu(Main.GAME);
		}
		
		if (exitButton.clicked(x, y)) {
			Main.exit();
		}
	}
	
	public void update() {
		
	}
}
