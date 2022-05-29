package projekt;

import java.util.ArrayList;
import java.util.Random;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Raumschiff extends Application {

	Canvas canvas = new Canvas(600, 600);
	Group group = new Group();
	Scene scene = new Scene(group, 600, 600);
	Stage stage = new Stage();

	ImageView ship = new ImageView("file:\\E:\\Java Profekts\\Bilder\\Ship.png");
	Image meteorImage = new Image("file:\\E:\\Java Profekts\\Bilder\\Meteor.png");
	Image background = new Image("file:\\E:\\Java Profekts\\Bilder\\Background.gif");
	Image begin = new Image("file:\\E:\\Java Profekts\\Bilder\\Start.png");
//	Image Endpicture = new Image("file:\\E:\\Java Profekts\\Bilder\\Start.png");

	int shipX = 250;
	int shipY = 480;
	Random NR = new Random();
	ArrayList<Meteor> meteorlist = new ArrayList<Meteor>();
	int meteorSize = 50;
	double invincibleTime = 3000;
	int meteorX, meteorX2, velX, velY, shipSize = 100;
	int spawnrate = 2;

	PathTransition transition = new PathTransition();
	Path path = new Path();
	boolean enableDeath = true;
	ColorAdjust hitColorAdjust = new ColorAdjust();
	ColorAdjust notHitColorAdjust = new ColorAdjust();

	Text counterText = new Text("0");
	int counter = 0;
	int live = 3;
	Text liveText = new Text();
	boolean launch = false;

	Text beginText = new Text("Space To Start");
	Text beginText2 = new Text("W / A / S / D = Move");


	FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), beginText);

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		stage.setScene(scene);
		stage.setTitle("SpaceShip");

		GraphicsContext gc = canvas.getGraphicsContext2D();

		gc.drawImage(begin, 0, 0);
		ship.setVisible(false);
		counterText.setVisible(false);

		group.getChildren().addAll(canvas, ship, counterText, liveText, beginText, beginText2);

		ship.setX(shipX);
		ship.setY(shipY);
		ship.setFitHeight(shipSize);
		ship.setFitWidth(shipSize);

		hitColorAdjust.setBrightness(0.7);
		hitColorAdjust.setSaturation(1);
		hitColorAdjust.setSaturation(0);

		notHitColorAdjust.setBrightness(0);

//		startbutton.setLayoutX(270);
//		startbutton.setLayoutY(290);
//		startbutton.setText("Play");
//		startbutton.setOnAction(this);
//		startbutton.setStyle("-fx-font-size: 20px;"
////							+"-fx-background-color: blue;"
//				+ "-fx-text-fill:RED;" + "-fx-text-size: 20px;"
////							+"-fx-border-color: #FFFF00 ;" 
////							+"-fx-border-width: 2px ;" 
//		);

		counterText.setTextAlignment(TextAlignment.LEFT);
		counterText.setY(20);
		counterText.setStrokeWidth(100);
		counterText.setStyle("-fx-font: 20 arial; -fx-font-weight: bold;");
		counterText.setFill(Color.PINK);

		liveText.setTextAlignment(TextAlignment.RIGHT);
		liveText.setY(20);
		liveText.setStrokeWidth(100);
		liveText.setStyle("-fx-font: 20 arial; -fx-font-weight: bold;");
		liveText.setFill(Color.PINK);

		beginText.setTextAlignment(TextAlignment.CENTER);
		beginText.setStrokeWidth(200);
		beginText.setFont(Font.font("Verdana", FontWeight.BOLD, 40));
		beginText.setFill(Color.DARKSLATEBLUE);
		beginText.setX((canvas.getWidth() / 2) - (beginText.getLayoutBounds().getWidth() / 2));
		beginText.setY((canvas.getHeight() / 2.1) + (beginText.getLayoutBounds().getHeight() / 2));
		
		beginText2.setX((canvas.getWidth() / 2.1) - (beginText.getLayoutBounds().getWidth() / 2));
		beginText2.setY((canvas.getHeight() / 1.8) + (beginText.getLayoutBounds().getHeight() / 2));
		beginText2.setStrokeWidth(100);
		beginText2.setFont(Font.font("Verdana", FontWeight.BOLD, 29));
		beginText2.setFill(Color.DARKSLATEBLUE);

		fadeTransition.setFromValue(1.0);
		fadeTransition.setToValue(0.05);
		fadeTransition.setAutoReverse(true);
		fadeTransition.setCycleCount(Animation.INDEFINITE);
		fadeTransition.setInterpolator(Interpolator.LINEAR);
		fadeTransition.play();

		stage.show();

		Task<Void> tick = new Task<Void>() {

			protected Void call() throws Exception {
				try {

					while (launch) {

						for (int j = 0; j < meteorlist.size(); j++) {
// hitted 
							if (ship.getBoundsInParent().intersects(meteorlist.get(j).imageView.getBoundsInParent())
									&& enableDeath) {
								enableDeath = false;
								if (counter > 300) {
									counter -= 300;
								} else {
									counter = 0;
								}
								live--;
								new Thread(new Runnable() {
									@Override
									public void run() {
										for (int j = 0; j < meteorlist.size(); j++) {
											meteorlist.get(j).imageView.setY(canvas.getHeight()
													+ meteorlist.get(j).imageView.getFitHeight() + 20);
											meteorlist.get(j).imageView.setX(0);
										}
//color change
										for (int j = 0; j < invincibleTime / 400; j++) {
											ship.setEffect(hitColorAdjust);
											try {
												Thread.sleep(400);
											} catch (InterruptedException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											ship.setEffect(notHitColorAdjust);
											try {
												Thread.sleep(400);
											} catch (InterruptedException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}

										}

										enableDeath = true;
									}
								}).start();

							}
						}

// Im Main Thrad ausgeführe (grafic)
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								gc.drawImage(background, 0, 0);

								if (0 < (ship.getX()) & velX < 0) {
									ship.setX(ship.getX() + velX);
								}
								if (canvas.getWidth() > (ship.getX() + shipSize) & velX > 0) {
									ship.setX(ship.getX() + velX);
								}
								if (ship.getY() > 0 & velY < 0) {
									ship.setY(ship.getY() + velY);
								}
								if ((ship.getY() + shipSize) < canvas.getHeight() & velY > 0) {
									ship.setY(ship.getY() + velY);
								}
							}
						});

						try {

							Thread.sleep(5);
						} catch (InterruptedException e) {
							System.out.println("Error2: " + e);
						}

					}
				} catch (Exception e) {
					System.out.println("Error: " + e);
				}

				return null;
			}

		};

		Thread Game = new Thread(tick);
		Game.setDaemon(true);
//		Game.start();

//meteor Erstellung
		for (int i = 0; i < 10; i++) {

			meteorlist.add(new Meteor(meteorImage));
			group.getChildren().add(meteorlist.get(i).imageView);

			meteorlist.get(i).imageView.setPreserveRatio(true);
			meteorlist.get(i).imageView.setFitWidth(meteorSize);
			meteorlist.get(i).imageView.setY(canvas.getHeight() + 20);

		}

		Task<Void> tick2 = new Task<Void>() {

			protected Void call() throws Exception {
				try {

					while (launch) {

						try {
							counter++;
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									counterText.setText(Integer.toString(counter));
									liveText.setText("Lives: " + live);
									counterText.setX(canvas.getWidth() - counterText.getLayoutBounds().getWidth());
								}
							});
							Thread.sleep(16);
						} catch (Exception e) {
							System.out.println("Error2: " + e);

						}

						if (NR.nextInt(spawnrate) == 1) {

							meteorX = NR.nextInt((int) (canvas.getWidth() - meteorSize) + 1);
							meteorX2 = NR.nextInt((int) (canvas.getWidth() - meteorSize) + 1);
							System.out.println("X: " + meteorX);
							System.out.println("X2: " + meteorX2);
							System.out.println(live);

							for (int i = 0; i < meteorlist.size(); i++) {
								if (meteorlist.get(i).valid) {
									meteorlist.get(i).path = new Path();
									meteorlist.get(i).path.getElements().addAll(new MoveTo(meteorX, 0),
											new LineTo(meteorX2, canvas.getHeight() + meteorSize));
									meteorlist.get(i).transition.setDuration(Duration.millis(2200));
									meteorlist.get(i).transition.setPath(meteorlist.get(i).path);
									meteorlist.get(i).valid = false;
									Platform.runLater(new MyRunnable(i) {
										@Override
										public void run() {
											meteorlist.get(i).transition.play();

										}
									});

									break;
								}

							}

						}

						try {
							Thread.sleep(25);
						} catch (InterruptedException e) {
							System.out.println("Error2: " + e);
						}

					}
				} catch (Exception e) {
					System.out.println("Error: " + e);
				}

				return null;
			}
		};

		Thread Game2 = new Thread(tick2);
		Game2.setDaemon(true);
//		Game2.start();

		scene.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.D || e.getCode() == KeyCode.RIGHT) {
				if (canvas.getWidth() > (ship.getX() + shipSize)) {
					velX = 2;
				}
			}
			if (e.getCode() == KeyCode.A || e.getCode() == KeyCode.LEFT) {
				if (0 < (ship.getX())) {
					velX = -2;
				}
			}
			if (e.getCode() == KeyCode.W || e.getCode() == KeyCode.UP) {
				if (0 < (ship.getY() + shipSize)) {
					velY = -2;
				}
			}
			if (e.getCode() == KeyCode.S || e.getCode() == KeyCode.DOWN) {
				if (canvas.getHeight() > (ship.getY() + shipSize)) {
					velY = 2;

				}
			}
			if (e.getCode() == KeyCode.SPACE) {

				launch = true;
				beginText.setVisible(false);
				beginText2.setVisible(false);
				Game.start();
				Game2.start();
				ship.setVisible(true);
				counterText.setVisible(true);

			}

		});
		scene.setOnKeyReleased(e -> {
			if ((e.getCode() == KeyCode.D || e.getCode() == KeyCode.RIGHT) && velX > 0) {

				if (canvas.getWidth() > (ship.getX() + shipSize)) {
					velX = 0;
				}
			}
			if ((e.getCode() == KeyCode.A || e.getCode() == KeyCode.LEFT) && velX < 0) {
				if (0 < (ship.getX())) {
					velX = 0;
				}
			}
			if ((e.getCode() == KeyCode.W || e.getCode() == KeyCode.UP) && velY < 0) {
				if (0 < (ship.getY() + shipSize)) {
					velY = 0;
				}
			}
			if ((e.getCode() == KeyCode.S || e.getCode() == KeyCode.DOWN) && velY < canvas.getHeight()) {
				if (canvas.getHeight() > (ship.getY() + shipSize)) {
					velY = 0;
				}
			}

		});

	}

	public void drawShapes(GraphicsContext gc) {

	}

	public class MyRunnable implements Runnable {

		int i;

		public MyRunnable(int i) {
			this.i = i;
		}

		public void run() {
		}
	}

}
