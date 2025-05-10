import characters.Turret;

import java.io.IOException;
import models.Wave;
import characters.Alien;
import characters.Base;
import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.GameState;

/**
 * Driver class for the TowerDefense Game
 */
public class Game extends Application {
    private GameState gameState;
    private Stage mainStage;
    private Scene startScene, gameScene, gameOverScene;
    private Text statusText; // shows health and score
    private Text enemiesText; // shows remaining enemies
    private Button nextWaveButton;
    private Text waveText;
    private Pane pane1, pane2;

    @Override
    public void start(Stage stage) {
        mainStage = stage;
        createStartScene();
        createGameOverScene();
        mainStage.setScene(startScene);
        mainStage.setTitle("TowerDefense!!");
        mainStage.setResizable(false);
        // Start in fullscreen
        mainStage.setMaximized(true);
        mainStage.setFullScreen(true);
        mainStage.show();
    }

    private void createStartScene() {
        Pane root = new Pane();
        root.setStyle("-fx-background-color: black;");
        root.setMinSize(1376, 736);
        Text title = new Text("Tower Defense");
        title.setFont(Font.loadFont("file:./provided/res/RetroComputer.ttf", 80));
        title.setFill(Color.WHITE);
        title.setX(400); title.setY(300);
        Button startButton = new Button("Empezar Juego");
        startButton.setStyle("-fx-font-size: 40px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        startButton.setLayoutX(600); startButton.setLayoutY(400);
        startButton.setOnAction(e -> startGame());
        root.getChildren().addAll(title, startButton);
        startScene = new Scene(root);
    }

    private void createGameOverScene() {
        Pane root = new Pane();
        root.setStyle("-fx-background-color: black;");
        root.setMinSize(1376, 736);
        Text gameOverText = new Text("Game Over");
        gameOverText.setFont(Font.loadFont("file:./provided/res/RetroComputer.ttf", 80));
        gameOverText.setFill(Color.RED);
        gameOverText.setX(500); gameOverText.setY(300);
        Button restart = new Button("Volver a Empezar");
        restart.setStyle("-fx-font-size: 40px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        restart.setLayoutX(550); restart.setLayoutY(400);
        restart.setOnAction(e -> startGame());
        root.getChildren().addAll(gameOverText, restart);
        gameOverScene = new Scene(root);
    }

    private void startGame() {
        gameState = new GameState();
        createGameScene();
        // Game update loop
        Timeline gameLoop = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            gameState.updateGame();
            updateStatusText();
            updateEnemiesText();
            if (gameState.isWaveCompletePhase()) {
                nextWaveButton.setDisable(false);
                nextWaveButton.setText("Siguiente Oleada");
            }
        }));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();
        mainStage.setScene(gameScene);
    }

    private void createGameScene() {
    Pane root = new Pane();
    pane1 = new Pane();
    pane2 = new Pane();
    // Background and UI
    ImageView bg = new ImageView(gameState.getBackgroundImage());
    ImageView covered = new ImageView(gameState.getCoveredAreaImage());
    statusText = new Text(); statusText.setFill(Color.WHITE);
    statusText.setBoundsType(TextBoundsType.VISUAL);
    statusText.setFont(Font.loadFont("file:./provided/res/RetroComputer.ttf", 50));
    waveText = new Text(); waveText.setFont(Font.loadFont("file:./provided/res/RetroComputer.ttf", 30));
    waveText.setFill(Color.WHITE); // Añadir color blanco si es necesario
    enemiesText = new Text(); enemiesText.setFill(Color.WHITE);
    enemiesText.setFont(Font.loadFont("file:./provided/res/RetroComputer.ttf", 30));
    updateStatusText(); updateWaveText();
    try { updateEnemiesText(); } catch (Exception ignored) {}
    root.setMinSize(1376, 736);
    root.getChildren().addAll(bg, pane1);
    pane1.setMinSize(1376, 736);
    pane1.getChildren().addAll(statusText, covered, pane2, waveText, enemiesText);
    statusText.setX(325); statusText.setY(40);
    // Posicionar waveText y enemiesText en la esquina inferior derecha
    waveText.setX(850); 
    waveText.setY(550);  // Ajustar Y para arriba del botón
    enemiesText.setX(850);
    enemiesText.setY(600); 
    pane2.setMinSize(1376, 736);
    // Mouse events
    Image placementMask = gameState.getPlacementAreaImage();
    Cursor green = createCursor(gameState.getGreenCursorImage(), "Green cursor load failed");
    Cursor red   = createCursor(gameState.getRedCursorImage(),   "Red cursor load failed");
    pane2.setOnMouseMoved(e -> handleMouseMove(e, placementMask, green, red));
    pane2.setOnMouseClicked(e -> handleMouseClick(e, green));
    // Next wave button
    nextWaveButton = new Button("Empezar Oleada");
    nextWaveButton.setStyle("-fx-font-size: 20px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
    nextWaveButton.layoutXProperty().bind(root.widthProperty().subtract(nextWaveButton.widthProperty()).subtract(20));
    nextWaveButton.layoutYProperty().bind(root.heightProperty().subtract(nextWaveButton.heightProperty()).subtract(20));
    nextWaveButton.setOnAction(e -> handleNextWaveAction());
    pane1.getChildren().add(nextWaveButton);
    root.setFocusTraversable(true); root.requestFocus();
    gameScene = new Scene(root);
}

    private void handleNextWaveAction() {
    if (gameState.isPlacingPhase()) {
        gameState.beginWave();
        spawnAliensFromWave(gameState.getWaveSystem().getCurrentWave());
        nextWaveButton.setDisable(true);
        nextWaveButton.setText("Siguiente oleada");
    } else if (gameState.isWaveCompletePhase()) {
        int numT = gameState.getTurrets().size();
        int refund = numT * 40;
        int bonus  = 40;
        System.out.println("[DEBUG] Torretas: " + numT + ", refund=" + refund + ", bonus=" + bonus);
        gameState.getBase().addScore(refund + bonus);
        
        // Limpiar elementos visuales de las torretas
        pane2.getChildren().clear();
        
        // Detener la lógica de targeting de las torretas antes de eliminarlas
        for (Turret t : gameState.getTurrets()) {
            t.stopTargeting();
        }
        gameState.getTurrets().clear();
        
        gameState.startNextWave();
        updateWaveText();
        updateEnemiesText();
        nextWaveButton.setDisable(false);
        nextWaveButton.setText("Empezar oleada");
    }
    }

    private void spawnAliensFromWave(Wave wave) {
    if (wave == null) return;
    System.out.println("[DEBUG] spawnAliensFromWave — hasMore=" + wave.hasMoreEnemies());
    gameState.getEnemyQueue().clear(); // Limpiar cola completamente
    Path path = createPath();
    int count = 0, idx = 0;
    Alien a;
    while ((a = wave.getNextEnemy()) != null) {
        final Alien alien = a;
        pane1.getChildren().add(alien.getImageView());
        gameState.getEnemyQueue().enqueue(alien);
        count++;
        PathTransition pt = new PathTransition(
            Duration.millis(20000 + (idx++) * 15000), path, alien.getImageView()
        );
        pt.setOnFinished(e -> {
            pane1.getChildren().remove(alien.getImageView());
            if (!alien.isDead()) {
                gameState.getBase().subHealth(alien.getAttack());
                updateStatusText();
                System.out.println("[DEBUG] Base damaged: " + alien.getAttack() + ", health=" + gameState.getBase().getHealth());
                if (gameState.getBase().getHealth() <= 0) mainStage.setScene(gameOverScene);
            } else {
                System.out.println("[DEBUG] Alien muerto antes de llegar");
            }
            gameState.getEnemyQueue().dequeue();
            updateEnemiesText();
        });
        pt.play();
    }
    System.out.println("[DEBUG] enemigos encolados = " + count);
}

    private void handleMouseClick(MouseEvent e, Cursor green) {
        try {
            if (gameScene.getCursor() == green && gameState.isPlacingPhase()) {
                int x = (int) e.getX(), y = (int) e.getY();
                gameState.addTurret(x, y);
                Turret t = gameState.getTurrets().get(gameState.getTurrets().size()-1);
                pane2.getChildren().addAll(t.getImageView(), t.getBombImageView());
                t.startTargeting(gameState.getEnemyQueue());
                updateStatusText();
                updateEnemiesText();
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private void handleMouseMove(MouseEvent e, Image mask, Cursor green, Cursor red) {
        if (mask==null) return;
        PixelReader pr = mask.getPixelReader();
        try { gameScene.setCursor(!pr.getColor((int)e.getX(),(int)e.getY()).equals(Color.BLACK)?green:red); }
        catch (IndexOutOfBoundsException ex) {}
    }

    private Cursor createCursor(Image img, String errMsg) {
        try { if (img==null||img.isError()) throw new Exception(errMsg); return new ImageCursor(img);} 
        catch (Exception ex) { return Cursor.DEFAULT; }
    }

    private void updateStatusText() {
        statusText.setText("Health: " + gameState.getBase().getHealth() + "\nScore: " + gameState.getBase().getScore());
    }

    private void updateEnemiesText() {
    try {
        Wave currentWave = gameState.getWaveSystem().getCurrentWave();
        int remaining = gameState.getEnemyQueue().getSize();
        
        // Solo contar los enemigos de la oleada si está activa
        if (currentWave != null && gameState.isWaveActive() && currentWave.hasMoreEnemies()) {
            remaining += currentWave.getEnemies().length;
        }
        enemiesText.setText("Enemigos vivos: " + remaining);
    } catch (Exception e) {
        enemiesText.setText("Enemigos restantes: N/A");
    }
}

    private void updateWaveText() {
        waveText.setText("Oleada: " + gameState.getCurrentWaveNumber());
    }

    private Path createPath() {
        Path path = new Path();
        path.getElements().addAll(
            new MoveTo(6,430), new LineTo(85,430), new LineTo(85,80),
            new LineTo(265,80), new LineTo(265,682.5), new LineTo(350,682.5),
            new LineTo(350,600), new LineTo(800,600), new LineTo(800,425.5),
            new LineTo(1121,425.5), new LineTo(1121,300)
        );
        return path;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
