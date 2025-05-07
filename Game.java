import characters.Turret;
import characters.Alien;
import characters.Base;
import javafx.animation.Animation.Status;
import javafx.animation.PathTransition;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.ImageCursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Button;
import models.GameState;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
/**
 * This is the driver class for the TowerDefense Game
 * @author Bilal Mawji
 * @version 1.0
 */
public class Game extends Application {
    private GameState gameState;
    private Text text;
    private Stage mainStage;
    private Scene gameScene;
    private Scene startScene;
    private Scene gameOverScene;
    
    @Override
    public void start(Stage stage) {
        mainStage = stage;
        createStartScene();
        createGameOverScene();
        
        mainStage.setScene(startScene);
        mainStage.setTitle("TowerDefense!!");
        mainStage.setResizable(false);
        mainStage.show();
    }
    
    private void createStartScene() {
        Pane startRoot = new Pane();
        startRoot.setStyle("-fx-background-color: black;");
        startRoot.setMinSize(1376, 736);
        
        Text titleText = new Text("Tower Defense");
        titleText.setFont(Font.loadFont("file:./provided/res/RetroComputer.ttf", 80));
        titleText.setFill(Color.WHITE);
        titleText.setX(400);
        titleText.setY(300);
        
        Button startButton = new Button("Empezar");
        startButton.setStyle("-fx-font-size: 40px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        startButton.setLayoutX(600);
        startButton.setLayoutY(400);
        startButton.setOnAction(e -> startGame());
        
        startRoot.getChildren().addAll(titleText, startButton);
        startScene = new Scene(startRoot);
    }
    
    private void createGameOverScene() {
        Pane gameOverRoot = new Pane();
        gameOverRoot.setStyle("-fx-background-color: black;");
        gameOverRoot.setMinSize(1376, 736);
        
        Text gameOverText = new Text("Game Over");
        gameOverText.setFont(Font.loadFont("file:./provided/res/RetroComputer.ttf", 80));
        gameOverText.setFill(Color.RED);
        gameOverText.setX(500);
        gameOverText.setY(300);
        
        Button restartButton = new Button("Volver a Empezar");
        restartButton.setStyle("-fx-font-size: 40px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        restartButton.setLayoutX(550);
        restartButton.setLayoutY(400);
        restartButton.setOnAction(e -> startGame());
        
        gameOverRoot.getChildren().addAll(gameOverText, restartButton);
        gameOverScene = new Scene(gameOverRoot);
    }
    
    private void startGame() {
        gameState = new GameState();
        createGameScene();  // Recrear la escena del juego con el nuevo estado
        mainStage.setScene(gameScene);
    }
    
    private void createGameScene() {
        Pane root = new Pane();
        Pane pane1 = new Pane();
        Pane pane2 = new Pane();
        
        // Usar gameState para acceder a los elementos del juego
        Base base = gameState.getBase();
        Alien[] aliens = gameState.getAliens();
        
        // Background
        ImageView imageBG = new ImageView(gameState.getBackgroundImage());
        
        // Covered Areas
        ImageView covered2 = new ImageView(gameState.getCoveredAreaImage());
        
        // Turret Areas
        Image areas = gameState.getPlacementAreaImage();
        ImageView areas2 = new ImageView(areas);
        
        // ImageCursor
        ImageCursor greenCursor = new ImageCursor(gameState.getGreenCursorImage());
        ImageCursor redCursor = new ImageCursor(gameState.getRedCursorImage());
        
        // Aliens ImageViews
        ImageView alien2 = aliens[0].getImageView();
        ImageView alien3 = aliens[1].getImageView();
        
        // Text
        // Modificar la declaración existente de text
        text = new Text("Health: " + base.getHealth() + "\nScore: " + base.getScore());
        text.setBoundsType(TextBoundsType.VISUAL);
        text.setFont(Font.loadFont("file:./provided/res/RetroComputer.ttf", 50));
        
        // Path
        Path p1 = createPath();
        
        // Root
        root.setMinSize(1376, 736);
        root.getChildren().addAll(imageBG, pane1);
        
        // Pane1
        pane1.setMinSize(1376, 736);
        pane1.getChildren().addAll(text, alien2, alien3, covered2, pane2);
        text.relocate(325, 20);
        
        // Pane2
        areas2.setFitWidth(1376);
        areas2.setFitHeight(736);
        pane2.setMinSize(1376, 736);
        
        Scene scene = new Scene(root);
        pane2.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                int mouseX = ((int) event.getX());
                int mouseY = ((int) event.getY());
                PixelReader placePixels = areas.getPixelReader();
                try {
                    if ((1376 - mouseX) < 1376 && (736 - mouseY) < 736) {
                        if (!placePixels.getColor(mouseX, mouseY)
                            .equals(Color.BLACK)) {
                            if (!placePixels.getColor(mouseX + 5, mouseY + 5)
                                .equals(Color.BLACK)) {
                                if (!placePixels.getColor(mouseX, mouseY + 5)
                                    .equals(Color.BLACK)) {
                                    if (!placePixels
                                        .getColor(mouseX + 5, mouseY)
                                        .equals(Color.BLACK)) {
                                        scene
                                        .setCursor(greenCursor);
                                    }
                                }
                            }
                            } else {
                                scene.setCursor(redCursor);
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Out of bounds");
                }
            }
        });
        pane2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                int mouseX = ((int) event.getX());
                int mouseY = ((int) event.getY());
                if (scene.getCursor().equals(greenCursor) && base.haveScore()) {
                    Turret t = new Turret();
                    t.getImageView().setX(mouseX);
                    t.getImageView().setY(mouseY);
                    t.getBombImageView().setX(mouseX);
                    t.getBombImageView().setY(mouseY);
                    pane2.getChildren().addAll(t.getImageView(),
                        t.getBombImageView());
                    
                    // Encontrar el alien más cercano y mantener una referencia final
                    final Alien[] currentTarget = new Alien[1];
                    double minDistance = Double.MAX_VALUE;
                    for (Alien alien : aliens) {
                        if (t.isInRange(alien)) {
                            double distance = t.calculateDistance(alien);
                            if (distance < minDistance) {
                                minDistance = distance;
                                currentTarget[0] = alien;
                            }
                        }
                    }
                    
                    // Si hay un alien en rango, dispararle
                    if (currentTarget[0] != null) {
                        PathTransition bombTransition = t.pathTransitionBomb(currentTarget[0]);
                        bombTransition.setDuration(Duration.millis(2000));
                        bombTransition.play();
                        
                        // Timeline para actualizar el objetivo
                        final Timeline updateTarget = new Timeline();
                        KeyFrame keyFrame = new KeyFrame(Duration.millis(500), _ -> {
                            if (currentTarget[0] != null && !currentTarget[0].isDead() && t.isInRange(currentTarget[0])) {
                                bombTransition.setPath(t.createPathBombs(currentTarget[0]));
                            } else {
                                // Buscar nuevo objetivo si el actual murió o está fuera de rango
                                Alien newTarget = findClosestAlien(t, aliens);
                                if (newTarget != null) {
                                    currentTarget[0] = newTarget;
                                    bombTransition.setPath(t.createPathBombs(newTarget));
                                } else {
                                    updateTarget.stop();
                                    bombTransition.stop();
                                    t.getBombImageView().setVisible(false);
                                }
                            }
                        });
                        updateTarget.getKeyFrames().add(keyFrame);
                        updateTarget.setCycleCount(Timeline.INDEFINITE);
                        updateTarget.play();
                    }
                    
                    base.buyTurret();
                    text.setText("Health: " + base.getHealth() + "\nScore: "
                        + base.getScore());
                }
            }
        });
        // PathTransition
        PauseTransition pause = new PauseTransition(Duration.millis(5000));
        PathTransition pt = new PathTransition(Duration.millis(20000), p1, alien2);
        PathTransition pt2 = new PathTransition(Duration.millis(35000), p1, alien3);
        
        pt.statusProperty().addListener(new ChangeListener<Status>() {
            @Override
            public void changed(ObservableValue<? extends Status> observableValue, 
                              Status oldValue, Status newValue) {
                if (newValue == Status.STOPPED) {
                    base.subHealth(aliens[0].getAttack());
                    text.setText("Health: " + base.getHealth() + "\nScore: " + base.getScore());
                    checkGameOver();
                }
            }
        });
        
        pt2.statusProperty().addListener(new ChangeListener<Status>() {
            @Override
            public void changed(ObservableValue<? extends Status> observableValue, 
                              Status oldValue, Status newValue) {
                if (newValue == Status.STOPPED) {
                    base.subHealth(aliens[1].getAttack());
                    text.setText("Health: " + base.getHealth() + "\nScore: " + base.getScore());
                    checkGameOver();
                }
            }
        });
        
        pt.play();
        pause.play();
        pt2.play();
        
        // Modificar la creación del botón de siguiente oleada y el texto
        Button nextWaveButton = new Button("Siguiente Oleada");
        nextWaveButton.setStyle("-fx-font-size: 20px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        nextWaveButton.setOnAction(_ -> {
            if (gameState.isWaveComplete()) {
                gameState.startNextWave();
                updateWaveDisplay();
                
                // Reiniciar las animaciones para los nuevos aliens
                Alien[] currentWaveAliens = gameState.getAliens();
                for (int i = 0; i < currentWaveAliens.length; i++) {
                    PathTransition alienPath = new PathTransition(
                        Duration.millis(20000 + (i * 15000)), 
                        createPath(), 
                        currentWaveAliens[i].getImageView()
                    );
                    
                    final int alienIndex = i; // Crear una variable final para el índice
                    alienPath.statusProperty().addListener((_, _, newVal) -> {
                        if (newVal == Status.STOPPED) {
                            base.subHealth(currentWaveAliens[alienIndex].getAttack());
                            updateWaveDisplay();
                        }
                    });
                    
                    alienPath.play();
                }
            }
        });
        
        // Actualizar el texto para mostrar la información de la oleada
        Text waveText = new Text("Oleada: " + gameState.getCurrentWaveNumber());
        waveText.setFont(Font.loadFont("file:./provided/res/RetroComputer.ttf", 30));
        
        // Posicionar y agregar los elementos al layout
        nextWaveButton.setLayoutX(325);
        nextWaveButton.setLayoutY(100);
        waveText.setLayoutX(325);
        waveText.setLayoutY(150);
        
        pane1.getChildren().addAll(nextWaveButton, waveText);
        pt.play();
        pause.play();
        pt2.play();
    }

    /**
     * Makes a path for Aliens to travel on
     * @return Path Path for Aliens
     */
    public Path createPath() {
        Path path = new Path();
        MoveTo spawn = new MoveTo(6.0, 430.0);
        LineTo line1 = new LineTo(85.0, 430.0);
        LineTo line2 = new LineTo(85.0, 80.0);
        LineTo line3 = new LineTo(265.0, 80.0);
        LineTo line4 = new LineTo(265.0, 682.5);
        LineTo line5 = new LineTo(350.0, 682.5);
        LineTo line6 = new LineTo(350.0, 600.0);
        LineTo line7 = new LineTo(800.0, 600.0);
        LineTo line8 = new LineTo(800.0, 425.5);
        LineTo line9 = new LineTo(1121.0, 425.5);
        LineTo line10 = new LineTo(1121.0, 300.0);
        path.getElements().addAll(spawn, line1, line2, line3, line4, line5,
            line6, line7, line8, line9, line10);
        return path;
    }
    
    private void updateWaveDisplay() {
        text.setText(String.format("Health: %d\nScore: %d\nOleada: %d", 
            gameState.getBase().getHealth(),
            gameState.getBase().getScore(),
            gameState.getCurrentWaveNumber()
        ));
    }
    
    // Agregar este método auxiliar
    private Alien findClosestAlien(Turret turret, Alien[] aliens) {
        double minDistance = Double.MAX_VALUE;
        Alien closest = null;
        for (Alien alien : aliens) {
            if (!alien.isDead() && turret.isInRange(alien)) {
                double distance = turret.calculateDistance(alien);
                if (distance < minDistance) {
                    minDistance = distance;
                    closest = alien;
                }
            }
        }
        return closest;
    }
    
    private void checkGameOver() {
        if (gameState.getBase().getHealth() <= 0) {
            mainStage.setScene(gameOverScene);
        }
    }
}
