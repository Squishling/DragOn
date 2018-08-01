package co.uk.squishling.dragon;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import javax.imageio.ImageIO;

import co.uk.squishling.dragon.objects.DeathMenu;
import co.uk.squishling.dragon.objects.Gear;
import co.uk.squishling.dragon.objects.MainMenu;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Main extends Application {
	
	private double WIDTH;
	private double HEIGHT;
	
	private Pane root;
	private static Scene scene;
	private Canvas canvas;
	
	private static int x = 0;
	
	private boolean acceleratorHeld = false;
	private boolean brakeHeld = false;
	private boolean clutchHeld = false;
	
	private static float speed = 0;
	
	private ArrayList<Gear> gears = new ArrayList<Gear>();
	private static int gear = 1;
	
	private BufferedImage carBlue = null;
	private BufferedImage carOrange = null;
	private BufferedImage carGreen = null;
	private BufferedImage carYellow = null;
	private int carWidth;
	private int carHeight;
	
	private ArrayList<BufferedImage> backgrounds = new ArrayList<BufferedImage>();
	private int backgroundWidth;
	private int backgroundHeight;
	
	private double stripLengthMiles = 1;
	private double mile;
	
	private static int menu = 0;
	
	public static int MAIN_MENU = 0;
	public static int GAME = 1;
	public static int DEATH_MENU = 2;
	
	private static MainMenu mainMenu;
	private static DeathMenu deathMenu;
	
	private Font font;
	
	private static int time = 0;
	private static int targetTime = 1657;
	
	private static String showTime = "";
	private static String showTargetTime = "";
	
	private int backgroundWeightTotal = 0;
	
	private int carColor = 0;
	private boolean isKph = false;
	
	private static double health = 100;
	
	public void start(Stage stage) {
		try {
			root = new Pane();
			
			scene = new Scene(root, 1500, 400);
			
			stage.setScene(scene);
			stage.setTitle("DragOn");
			stage.setFullScreen(true);
			stage.setResizable(false);
			stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
			
			stage.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent e) -> {
		        if (KeyCode.ESCAPE == e.getCode()) {
		            stage.close();
		        }
		    });
			
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		WIDTH = root.getWidth();
		HEIGHT = root.getHeight();
		
		font = Font.loadFont("file:res/DTM-Sans.otf", 50);
		
		mainMenu = new MainMenu((int) WIDTH, (int) HEIGHT, font);
		deathMenu = new DeathMenu((int) WIDTH, (int) HEIGHT, font);
		
		try {
			carBlue = ImageIO.read(new File("res/CarHD.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			carOrange = ImageIO.read(new File("res/CarHD_2.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			carGreen = ImageIO.read(new File("res/CarHD_3.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			carYellow = ImageIO.read(new File("res/CarHD_4.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		carHeight = (int) (HEIGHT / 2);
		carWidth = (int) (carBlue.getWidth() / ((float) carBlue.getHeight() / carHeight));
		
		mile = carWidth * 360.843238394;
		
		String content = "";
	    try {
	        content = new String (Files.readAllBytes(Paths.get("res/options.txt")));
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    String[] lines = content.split("\n");
	    
	    if (lines[0].split(":")[1].trim().equals("blue")) {
	    	carColor = 0;
	    }
	    
	    if (lines[0].split(":")[1].trim().equals("orange")) {
	    	carColor = 1;
	    }
	    
	    if (lines[0].split(":")[1].trim().equals("green")) {
	    	carColor = 2;
	    }
	    
	    if (lines[0].split(":")[1].trim().equals("yellow")) {
	    	carColor = 3;
	    }
	    
	    if (lines[1].split(":")[1].trim().equals("mph")) {
	    	isKph = false;
	    }
	    
	    if (lines[1].split(":")[1].trim().equals("k/h")) {
	    	isKph = true;
	    }
		
		try {
			backgrounds.add(ImageIO.read(new File("res/background_variation_0.png")));
			backgrounds.add(ImageIO.read(new File("res/background_variation_2.png")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		gears.add(new Gear((float) -0.9, (float) mphToPxpt(-50), (float) 3.13));
		gears.add(new Gear(0, 0, 0));
		gears.add(new Gear((float) 0.9, (float) mphToPxpt(40), (float) 3.13));
		gears.add(new Gear((float) 0.85, (float) mphToPxpt(70), (float) 3.59));
		gears.add(new Gear((float) 0.8, (float) mphToPxpt(100), (float) 1.96));
		gears.add(new Gear((float) 0.75, (float) mphToPxpt(120), (float) 1.25));
		gears.add(new Gear((float) 0.7, (float) mphToPxpt(140), (float) 0.98));
		gears.add(new Gear((float) 0.65, (float) mphToPxpt(180), (float) 0.98));
		gears.add(new Gear((float) 0.6, (float) mphToPxpt(200), (float) 0.84));
		
		backgroundHeight = (int) HEIGHT;
		backgroundWidth = (int) (backgrounds.get(0).getWidth() / ((float) backgrounds.get(0).getHeight() / backgroundHeight));
		
		showTargetTime = calcDisplayTime(targetTime);
		
		canvas = new Canvas(scene.getWidth(), scene.getHeight());
		canvas.setFocusTraversable(true);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		new AnimationTimer()
	    {
	        public void handle(long currentNanoTime)
	        {	
	        	update();
		        draw(gc);
	        }
	    }.start();
	    
	    canvas.setOnKeyPressed((KeyEvent e) -> {
	    	if (e.getCode() == KeyCode.SPACE) {
	    		acceleratorHeld = true;
	    	}
	    	
	    	if (e.getCode() == KeyCode.CONTROL) {
	    		brakeHeld = true;
	    	}
	    	
	    	if (e.getCode() == KeyCode.SHIFT) {
	    		clutchHeld = true;
	    	}
	    	
	    	if (menu == GAME) {
	    		if (e.getCode() == KeyCode.UP) {
		    		if (clutchHeld && gear < gears.size() - 1) {
		    			gear++;
		    		}
		    	}
		    	
		    	if (e.getCode() == KeyCode.DOWN) {
		    		if (clutchHeld && gear > 0) {
		    			gear--;
		    		}
		    	}
	    	}
	    });
	    
	    canvas.setOnKeyReleased((KeyEvent e) -> {
	    	if (e.getCode() == KeyCode.SPACE) {
	    		acceleratorHeld = false;
	    	}
	    	
	    	if (e.getCode() == KeyCode.CONTROL) {
	    		brakeHeld = false;
	    	}
	    	
	    	if (e.getCode() == KeyCode.SHIFT) {
	    		clutchHeld = false;
	    	}
	    });
	    
	    canvas.setOnMouseClicked((MouseEvent e) -> {
	    	if (menu == MAIN_MENU) {
	    		mainMenu.clicked(e.getSceneX(), e.getSceneY());
	    	}
	    	
	    	if (menu == DEATH_MENU) {
	    		deathMenu.clicked(e.getSceneX(), e.getSceneY());
	    	}
	    });
	    
	    root.getChildren().add(canvas);
	}
	
	public static void setMenu(int newMenu) {
		menu = newMenu;
	}
	
	public static void setTime(int newTime) {
		time = newTime;
	}
	
	public static void exit() {
		Platform.exit();
	}
	
	public static String calcDisplayTime(int time) {
		int mins = (int) Math.floor((double) time / 3600);
		double secs = (double) (time / 60) - (mins * 60);
		double ms = (double) ((((double) time / 60) * 1000) - (secs * 1000)) - (mins * 60000);
		
		return new DecimalFormat("00").format(mins) + ":" + new DecimalFormat("00").format(secs) + "." + new DecimalFormat("000").format(ms);
	}
	
	public static void die() {
		deathMenu.reset();
		setMenu(DEATH_MENU);
		x = 0;
		gear = 1;
		speed = 0;
		health = 100;
	}
	
	public double pxptToMph(double pxpt) {
		return (pxpt / mile) * 216000;
	}
	
	public double mphToPxpt(double mph) {
		return (mph * mile) / 216000;
	}
	
	public void draw(GraphicsContext gc) {
		if (menu == MAIN_MENU) {
			mainMenu.draw(gc);
		}
		
		if (menu == GAME) {
			// Background			
			// Because MATHS!
			for (int i = 0; i <= (WIDTH - x) / backgroundWidth; i++) {
				// Prevents insecure images from being drawn, for performance reasons.
				// Is true when an image is found which needs to be displayed within the range of the canvas.
				// Naturally, this could be more than one image (dependent on screen size).
				if (x + (i * backgroundWidth) > backgroundWidth * -1 && x + (i * backgroundWidth) < WIDTH) {
					if (i == 0) {
						gc.drawImage(SwingFXUtils.toFXImage(backgrounds.get(0), null), x + (i * backgroundWidth), 0, (float) backgroundWidth, (float) backgroundHeight);
					} else if (i == 8) {
						gc.drawImage(SwingFXUtils.toFXImage(backgrounds.get(0), null), x + (i * backgroundWidth), 0, (float) backgroundWidth, (float) backgroundHeight);
					} else {
						gc.drawImage(SwingFXUtils.toFXImage(backgrounds.get(1), null), x + (i * backgroundWidth), 0, (float) backgroundWidth, (float) backgroundHeight);
					}
				}
			}
			
			// Car	
			if (carColor == 0) {
				gc.drawImage(SwingFXUtils.toFXImage(carBlue, null), (WIDTH / 2) - (carWidth / 2), (HEIGHT / 2) - (carHeight / 2), (float) carWidth, (float) carHeight);
			}
			
			if (carColor == 1) {
				gc.drawImage(SwingFXUtils.toFXImage(carOrange, null), (WIDTH / 2) - (carWidth / 2), (HEIGHT / 2) - (carHeight / 2), (float) carWidth, (float) carHeight);
			}

			if (carColor == 2) {
				gc.drawImage(SwingFXUtils.toFXImage(carGreen, null), (WIDTH / 2) - (carWidth / 2), (HEIGHT / 2) - (carHeight / 2), (float) carWidth, (float) carHeight);
			}
			
			if (carColor == 3) {
				gc.drawImage(SwingFXUtils.toFXImage(carYellow, null), (WIDTH / 2) - (carWidth / 2), (HEIGHT / 2) - (carHeight / 2), (float) carWidth, (float) carHeight);
			}
			
			
			
			// Speed and gear
			int showSpeed = (int) pxptToMph(speed);
			
			String showGear = Integer.toString(gear - 1).replace("-1", "R").replace("0", "N");
			
			if (speed < 0) {
				showSpeed *= -1;
			}
			
			// Time completion, and health
			int showCompletion = (int) (x / ((mile * stripLengthMiles)) * -100);
			
			if (showCompletion < 0) {
				showCompletion = 0;
			}
			
			if (showCompletion > 100) {
				showCompletion = 100;
			}
			
			gc.setFill(Color.WHITE);
			gc.setTextAlign(TextAlignment.LEFT);
			gc.setTextBaseline(VPos.TOP);
			gc.setFont(Font.font(font.getFamily(), HEIGHT / 20));
			gc.fillText("Completion: " + showCompletion + "%", 10, 10);
			gc.fillText("Time: " + showTime + "/" + showTargetTime, 10, (HEIGHT / 20) + 10);
			

			// Clutch Health
			gc.setStroke(Color.BLACK);
			gc.setLineWidth(5);
			gc.strokeRect((WIDTH - (10 + (100 * (HEIGHT / 200)))) - 2.5, 7.5, 100 * (HEIGHT / 200) + 5, (HEIGHT / 20) + 5);
			
			
			double green = (health / 100) * 2;
			double red = (1 - (health / 100)) * 2;
			
			if (health < 50) {
				red = 1;
			}
			
			if (health > 50) {
				green = 1;
			}
			
			gc.setFill(new Color(red, green, 0, 1));
			gc.fillRect(WIDTH - (10 + (health * (HEIGHT / 200))), 10, health * (HEIGHT / 200), HEIGHT / 20);
			
			if (health > 10) {
				gc.setFill(Color.BLACK);
				gc.setFont(Font.font(font.getFamily(), HEIGHT / 30));
				gc.setTextAlign(TextAlignment.CENTER);
				gc.setTextBaseline(VPos.CENTER);
				gc.fillText((int) health + "%", WIDTH - (10 + (health * (HEIGHT / 400))), 10 + (HEIGHT / 40));
			}
			
			
			// Gear
			gc.setFill(Color.WHITE);
			gc.setFont(Font.font(font.getFamily(), HEIGHT / 10));
			gc.setTextAlign(TextAlignment.CENTER);
			gc.setTextBaseline(VPos.BOTTOM);
			gc.fillText(showGear, WIDTH / 2, HEIGHT - (HEIGHT / 250));
			
			// Speedometer
			// ===========
			
			// Background
			gc.setFill(Color.BLACK);
			gc.setGlobalAlpha(0.5);
			gc.fillArc(-(HEIGHT / 4.8) + HEIGHT / 50 - HEIGHT / 200, (HEIGHT - ((HEIGHT / 4.8) + (HEIGHT / 50))) + HEIGHT / 200, HEIGHT / 2.4, HEIGHT / 2.4, 0, 90, ArcType.ROUND);
			
			// Unit
			gc.setGlobalAlpha(1);
			gc.setFont(Font.font(font.getFamily(), HEIGHT / 30));
			gc.setTextAlign(TextAlignment.CENTER);
			gc.setTextBaseline(VPos.CENTER);
			gc.setFill(Color.WHITE);
			gc.fillText("MPH", (0 + ((HEIGHT / 20) + (HEIGHT / 50))), (HEIGHT - ((HEIGHT / 20) + (HEIGHT / 50))));
			
			// Pointer Base
			gc.setFill(Color.RED);
			gc.fillArc((HEIGHT / 200), (HEIGHT - ((HEIGHT / 100) + (HEIGHT / 50))) + HEIGHT / 200, HEIGHT / 50, HEIGHT / 50, 0, 90, ArcType.ROUND);
			
			// Pointer
			double max = 225;
			double angle = (double) (((pxptToMph(speed) / max) * 90) * -1);
			
			if (angle < 0) {
				angle *= -1;
			}
			
			gc.setStroke(Color.RED);
			gc.setLineWidth(HEIGHT / 250);
			gc.strokeLine(HEIGHT / 50, HEIGHT - (HEIGHT / 50), (HEIGHT / 50) + (Math.cos(Math.toRadians(angle)) * (HEIGHT / 5)), (HEIGHT - (HEIGHT / 50)) - (Math.sin(Math.toRadians(angle)) * (HEIGHT / 5)));
			
			// Outside Arc
			gc.setStroke(Color.WHITE);
			gc.setLineWidth(HEIGHT / 100);
			gc.strokeArc(-(HEIGHT / 5) + (HEIGHT / 50), HEIGHT - ((HEIGHT / 5) + (HEIGHT / 50)), HEIGHT / 2.5, HEIGHT / 2.5, 0, 90, ArcType.OPEN);
			
			// Interval lines
			for (int i = 0; i < 5; i ++) {
				double lineAngle = (90 / 4) * i;
				gc.setStroke(Color.WHITE);
				gc.setLineWidth(HEIGHT / 250);
				gc.strokeLine(((HEIGHT / 50) + (Math.cos(Math.toRadians(lineAngle)) * (HEIGHT / 5.5))), (HEIGHT - (HEIGHT / 50)) - (Math.sin(Math.toRadians(lineAngle)) * (HEIGHT / 5.5)), (HEIGHT / 50) + (Math.cos(Math.toRadians(lineAngle)) * (HEIGHT / 5)), (HEIGHT - (HEIGHT / 50)) - (Math.sin(Math.toRadians(lineAngle)) * (HEIGHT / 5)));
				
				gc.setTextAlign(TextAlignment.CENTER);
				gc.setTextBaseline(VPos.CENTER);
				gc.setFill(Color.WHITE);
				gc.setFont(Font.font(font.getFamily(), HEIGHT / 100));
				gc.fillText(Integer.toString((int) (((double) (max / 4)) * i)), (HEIGHT / 50) + (Math.cos(Math.toRadians(lineAngle)) * (HEIGHT / 6)), (HEIGHT - (HEIGHT / 50)) - (Math.sin(Math.toRadians(lineAngle)) * (HEIGHT / 6)));
			}
			
			for (int i = 0; i < 9; i ++) {
				double lineAngle = (90 / 8) * i;
				gc.setLineWidth(HEIGHT / 500);
				gc.strokeLine(((HEIGHT / 50) + (Math.cos(Math.toRadians(lineAngle)) * (HEIGHT / 5.5))), (HEIGHT - (HEIGHT / 50)) - (Math.sin(Math.toRadians(lineAngle)) * (HEIGHT / 5.5)), (HEIGHT / 50) + (Math.cos(Math.toRadians(lineAngle)) * (HEIGHT / 5)), (HEIGHT - (HEIGHT / 50)) - (Math.sin(Math.toRadians(lineAngle)) * (HEIGHT / 5)));
			}
			
			// RPM
			// ===========
		
			// Background
			gc.setFill(Color.BLACK);
			gc.setGlobalAlpha(0.5);
			gc.fillArc((WIDTH - ((HEIGHT / 4.8) + (HEIGHT / 50))) + HEIGHT / 200, (HEIGHT - ((HEIGHT / 4.8) + (HEIGHT / 50))) + HEIGHT / 200, HEIGHT / 2.4, HEIGHT / 2.4, 90, 90, ArcType.ROUND);
			
			// Unit
			gc.setGlobalAlpha(1);
			gc.setFont(Font.font(font.getFamily(), HEIGHT / 30));
			gc.setTextAlign(TextAlignment.CENTER);
			gc.setTextBaseline(VPos.CENTER);
			gc.setFill(Color.WHITE);
			gc.fillText("RPM", (WIDTH - ((HEIGHT / 20) + (HEIGHT / 50))), (HEIGHT - ((HEIGHT / 20) + (HEIGHT / 50))));
					
			// Pointer Base
			gc.setFill(Color.RED);
			gc.fillArc((WIDTH - ((HEIGHT / 100) + (HEIGHT / 50))) + HEIGHT / 200, (HEIGHT - ((HEIGHT / 100) + (HEIGHT / 50))) + HEIGHT / 200, HEIGHT / 50, HEIGHT / 50, 90, 90, ArcType.ROUND);
			
			// Pointer
			double rpmMax = 8.5;
			double rpmAngle = (double) (((((pxptToMph(speed) * gears.get(gear).getGearRatio() * 336) / 20) / 1000) / rpmMax) * 90);
			
			if (rpmAngle < 0) {
				rpmAngle *= -1;
			}
			
			gc.setStroke(Color.RED);
			gc.setLineWidth(HEIGHT / 250);
			gc.strokeLine(WIDTH - (HEIGHT / 50), HEIGHT - (HEIGHT / 50), (WIDTH - (HEIGHT / 50)) - (Math.cos(Math.toRadians(rpmAngle)) * (HEIGHT / 5)), (HEIGHT - (HEIGHT / 50)) - (Math.sin(Math.toRadians(rpmAngle)) * (HEIGHT / 5)));
			
			// Outside Arc
			gc.setStroke(Color.WHITE);
			gc.setLineWidth(HEIGHT / 100);
			gc.strokeArc(WIDTH - ((HEIGHT / 5) + (HEIGHT / 50)), HEIGHT - ((HEIGHT / 5) + (HEIGHT / 50)), HEIGHT / 2.5, HEIGHT / 2.5, 90, 90, ArcType.OPEN);
			
			// Interval lines
			for (int i = 0; i < 5; i++) {
				double lineAngle = (90 / 4) * i;
				gc.setStroke(Color.WHITE);
				gc.setLineWidth(HEIGHT / 250);
				gc.strokeLine((WIDTH - (HEIGHT / 50)) - (Math.cos(Math.toRadians(lineAngle)) * (HEIGHT / 5.5)), (HEIGHT - (HEIGHT / 50)) - (Math.sin(Math.toRadians(lineAngle)) * (HEIGHT / 5.5)), (WIDTH - (HEIGHT / 50)) - (Math.cos(Math.toRadians(lineAngle)) * (HEIGHT / 5)), (HEIGHT - (HEIGHT / 50)) - (Math.sin(Math.toRadians(lineAngle)) * (HEIGHT / 5)));
						
				gc.setTextAlign(TextAlignment.CENTER);
				gc.setTextBaseline(VPos.CENTER);
				gc.setFill(Color.WHITE);
				gc.setFont(Font.font(font.getFamily(), HEIGHT / 100));
				gc.fillText(Integer.toString((int) (((double) (rpmMax / 4)) * i)), (WIDTH - (HEIGHT / 50)) - (Math.cos(Math.toRadians(lineAngle)) * (HEIGHT / 6)), (HEIGHT - (HEIGHT / 50)) - (Math.sin(Math.toRadians(lineAngle)) * (HEIGHT / 6)));
			}
						
			for (int i = 0; i < 9; i ++) {
				double lineAngle = (90 / 8) * i;
				gc.setLineWidth(HEIGHT / 500);
				gc.strokeLine((WIDTH - (HEIGHT / 50)) - (Math.cos(Math.toRadians(lineAngle)) * (HEIGHT / 5.25)), (HEIGHT - (HEIGHT / 50)) - (Math.sin(Math.toRadians(lineAngle)) * (HEIGHT / 5.25)), (WIDTH - (HEIGHT / 50)) - (Math.cos(Math.toRadians(lineAngle)) * (HEIGHT / 5)), (HEIGHT - (HEIGHT / 50)) - (Math.sin(Math.toRadians(lineAngle)) * (HEIGHT / 5)));
			}
		}
		
		if (menu == DEATH_MENU) {
			deathMenu.draw(gc, time, targetTime, showTime, showTargetTime);
		}
	}
	
	public void update() {
		if (menu == GAME) {
			if (!(x < (int) ((mile * stripLengthMiles) * -1))) {
				time++;
				showTime = calcDisplayTime(time);
			}
			
			x -= (WIDTH / 1920) * speed;
			
			if (x < (int) ((mile * stripLengthMiles) * -1) && speed <= 0) {
				die();
			}
			
			if (x > 0) {
				x = 0;
			}
			
			if (speed < 0) {
				speed += 0.5;
				
				if (speed > 0) {
					speed = 0;
				}
			} else if (speed > 0) {
				speed -= 0.5;
				
				if (speed < 0) {
					speed = 0;
				}
			}
			
			if (health > 0 && acceleratorHeld && clutchHeld) {
				health -= 1;
				
				if (health < 0) {
					health = 0;
				}
			}
			
			if (brakeHeld && speed < 0) {
				speed += 1.5;
				
				if (speed > 0) {
					speed = 0;
				}
			} else if (brakeHeld && speed > 0) {
				speed -= 1.5;
				
				if (speed < 0) {
					speed = 0;
				}
			}
			
			if (health > 0) {
				if (gear != 0) {
					if (x > (int) ((mile * stripLengthMiles) * -1) && speed < gears.get(gear).getTopSpeed() && acceleratorHeld && !brakeHeld && !clutchHeld) {
						speed += gears.get(gear).getAcceleration();
						
						if (speed > gears.get(gear).getTopSpeed()) {
							speed = gears.get(gear).getTopSpeed();
						}
					}
				} else {
					if (speed > gears.get(0).getTopSpeed() && acceleratorHeld && !brakeHeld && !clutchHeld) {
						speed += gears.get(0).getAcceleration();
						
						if (speed < gears.get(gear).getTopSpeed()) {
							speed = gears.get(gear).getTopSpeed();
						}
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
