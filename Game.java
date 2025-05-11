import characters.Turret;
import models.Wave;
import characters.Alien;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.geometry.Pos;
import models.GameState;

/**
 * Driver class for the TowerDefense Game
 */
public class Game extends Application {
    private GameState gameState;
    private Stage mainStage;
    private Scene startScene, gameScene, gameOverScene, winScene;
    private Text statusText; // shows health and score
    private Text enemiesText; // shows remaining enemies
    private Button nextWaveButton;
    private Text waveText;
    private Pane pane1, pane2;
    private static final int COST_STANDARD = 40;
    private static final int COST_ELECTRIC = 60;
    private static final int COST_FIRE     = 100;

    // Variables para la selección de torres
    private String selectedTurretType = "standard"; // "standard", "electric" o "fire"
    private ImageView standardTurretButton;
    private ImageView electricTurretButton;
    private ImageView fireTurretButton;
    private Text standardCostText;
    private Text electricCostText;
    private Text fireCostText;
    private HBox turretSelectionBox;
    
    // Variables para visualización del rango de torre
    private Circle rangeIndicator;

    @Override
    public void start(Stage stage) {
        mainStage = stage;
        createStartScene();
        createGameOverScene();
        createWinScene();
        mainStage.setScene(startScene);
        mainStage.setTitle("TowerDefense!!");
        mainStage.setResizable(false);
        // Start in fullscreen
        mainStage.setMaximized(true);
        mainStage.setFullScreen(true);
        mainStage.show();
    }

    private void createStartScene() {
    VBox root = new VBox();
    root.setStyle("-fx-background-color: black;");
    root.setMinSize(1376, 736);
    root.setAlignment(Pos.CENTER);
    root.setSpacing(50);  // Espacio entre elementos
    
    Text title = new Text("Tower Defense");
    title.setFont(Font.loadFont("file:./provided/res/RetroComputer.ttf", 80));
    title.setFill(Color.WHITE);
    
    Button startButton = new Button("Empezar Juego");
    startButton.setStyle("-fx-font-size: 40px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
    startButton.setOnAction(e -> startGame());
    
    root.getChildren().addAll(title, startButton);
    startScene = new Scene(root);
    }

    private void createGameOverScene() {
    VBox root = new VBox();
    root.setStyle("-fx-background-color: black;");
    root.setMinSize(1376, 736);
    root.setAlignment(Pos.CENTER); // Centra vertical y horizontalmente
    root.setSpacing(50); // Espacio entre el texto y el botón

    Text gameOverText = new Text("Game Over");
    gameOverText.setFont(Font.loadFont("file:./provided/res/RetroComputer.ttf", 80));
    gameOverText.setFill(Color.RED);

    Button restart = new Button("Volver a empezar");
    restart.setStyle("-fx-font-size: 40px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
    restart.setOnAction(e -> startGame());

    root.getChildren().addAll(gameOverText, restart);
    gameOverScene = new Scene(root);
}

private void createWinScene() {
        VBox root = new VBox();
        root.setStyle("-fx-background-color: black;");
        root.setMinSize(1376, 736);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(50);

        Text winText = new Text("¡HAS GANADO!");
        winText.setFont(Font.loadFont("file:./provided/res/RetroComputer.ttf", 80));
        winText.setFill(Color.LIME);

        Button restart = new Button("Volver a jugar");
        restart.setStyle("-fx-font-size: 40px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        restart.setOnAction(e -> startGame());

        root.getChildren().addAll(winText, restart);
        winScene = new Scene(root);
    }

    private void startGame() {
    gameState = new GameState();
    createGameScene();

    // Game update loop
    Timeline gameLoop = new Timeline(new KeyFrame(Duration.millis(100), e -> {
        gameState.updateGame();
        updateStatusText();
        updateEnemiesText();

        // Si la fase es de oleada completa...
        if (gameState.isWaveCompletePhase()) {
            int currentWave = gameState.getCurrentWaveNumber();
            int remainingEnemies = gameState.getEnemyQueue().getSize();

            // Victoria al terminar la oleada 10 y no quedan enemigos
            if (currentWave == 10 && remainingEnemies == 0) {
                mainStage.setScene(winScene);
                return;
            }

            // Sino, habilitar siguiente oleada normalmente
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
        waveText.setFill(Color.WHITE);
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
        waveText.setX(910); 
        waveText.setY(550);
        enemiesText.setX(910);
        enemiesText.setY(600);
        pane2.setMinSize(1376, 736);
        
        // Crear selección de torres
        createTurretSelectionUI(root);
        
        // Crear indicador de rango (inicialmente invisible)
        rangeIndicator = new Circle();
        rangeIndicator.setFill(Color.TRANSPARENT);
        rangeIndicator.setStroke(Color.YELLOW);
        rangeIndicator.setStrokeWidth(2);
        rangeIndicator.setOpacity(0.7);
        rangeIndicator.setVisible(false);
        pane2.getChildren().add(rangeIndicator);
        
        // Mouse events
        Image placementMask = gameState.getPlacementAreaImage();
        Cursor green = createCursor(gameState.getGreenCursorImage(), "Green cursor load failed");
        Cursor red   = createCursor(gameState.getRedCursorImage(),   "Red cursor load failed");
        pane2.setOnMouseMoved(e -> {
            handleMouseMove(e, placementMask, green, red);
            updateRangeIndicator(e.getX(), e.getY());
        });
        pane2.setOnMouseClicked(e -> handleMouseClick(e, green));
        
        // Next wave button
        nextWaveButton = new Button("Empezar Oleada");
        nextWaveButton.setStyle("-fx-font-size: 20px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        nextWaveButton.layoutXProperty().bind(root.widthProperty().subtract(nextWaveButton.widthProperty()).subtract(50));
        nextWaveButton.layoutYProperty().bind(root.heightProperty().subtract(nextWaveButton.heightProperty()).subtract(40));
        nextWaveButton.setOnAction(e -> handleNextWaveAction());
        pane1.getChildren().add(nextWaveButton);
        root.setFocusTraversable(true); root.requestFocus();
        gameScene = new Scene(root);
    }
    
    // Método para actualizar el indicador de rango cuando el mouse se mueve
    private void updateRangeIndicator(double x, double y) {
        if (!gameState.isPlacingPhase()) {
            rangeIndicator.setVisible(false);
            return;
        }
        
        // Determinar el radio basado en el tipo de torre seleccionado
        double radius = 0;
        Color rangeColor = Color.YELLOW;
        
        switch (selectedTurretType) {
            case "standard":
                radius = 310; // Radio para torre estándar
                rangeColor = Color.YELLOW;
                break;
            case "electric":
                radius = 620; // Radio para torre eléctrica
                rangeColor = Color.CYAN;
                break;
            case "fire":
                radius = 310; // Radio para torre de fuego
                rangeColor = Color.ORANGE;
                break;
        }
        
        // Actualizar la posición y radio del círculo indicador
        rangeIndicator.setCenterX(x);
        rangeIndicator.setCenterY(y);
        rangeIndicator.setRadius(radius);
        rangeIndicator.setStroke(rangeColor);
        
        // Verificar si el cursor está en área válida y hay suficientes puntos
        PixelReader pr = gameState.getPlacementAreaImage().getPixelReader();
        try {
            boolean validPlacement = !pr.getColor((int)x, (int)y).equals(Color.BLACK);
            boolean canAfford = false;
            
            switch (selectedTurretType) {
                case "standard":
                    canAfford = gameState.getBase().getScore() >= COST_STANDARD;
                    break;
                case "electric":
                    canAfford = gameState.getBase().getScore() >= COST_ELECTRIC;
                    break;
                case "fire":
                    canAfford = gameState.getBase().getScore() >= COST_FIRE;
                    break;
            }
            
            if (validPlacement && canAfford) {
                rangeIndicator.setVisible(true);
                rangeIndicator.setOpacity(0.7);
            } else {
                rangeIndicator.setVisible(true);
                rangeIndicator.setOpacity(0.3); // Opacidad reducida si no se puede colocar
                rangeIndicator.setStroke(Color.RED);
            }
            
        } catch (IndexOutOfBoundsException ex) {
            rangeIndicator.setVisible(false);
        }
    }
    
    private void createTurretSelectionUI(Pane root) {
        // Crear contenedor para las opciones de torres
        turretSelectionBox = new HBox(30); // Espacio entre elementos de 30px
        turretSelectionBox.setAlignment(Pos.CENTER);
        
        // Cargar imágenes de torres
        Image standardImage = new Image("./provided/res/launcher.png");
        Image electricImage = new Image("./provided/res/electric_launcher.png");
        Image fireImage = new Image("./provided/res/fire_launcher.png");
        
        // Crear ImageViews para los botones de torres
        standardTurretButton = new ImageView(standardImage);
        electricTurretButton = new ImageView(electricImage);
        fireTurretButton = new ImageView(fireImage);
        
        // Ajustar tamaño de las imágenes si es necesario
        standardTurretButton.setFitHeight(60);
        standardTurretButton.setPreserveRatio(true);
        electricTurretButton.setFitHeight(60);
        electricTurretButton.setPreserveRatio(true);
        fireTurretButton.setFitHeight(60);
        fireTurretButton.setPreserveRatio(true);
        
        // Crear textos para los costos
        standardCostText = new Text("40");
        electricCostText = new Text("60");
        fireCostText = new Text("100");
        
        // Estilo para los textos
        standardCostText.setFill(Color.WHITE);
        standardCostText.setFont(Font.loadFont("file:./provided/res/RetroComputer.ttf", 16));
        electricCostText.setFill(Color.WHITE);
        electricCostText.setFont(Font.loadFont("file:./provided/res/RetroComputer.ttf", 16));
        fireCostText.setFill(Color.WHITE);
        fireCostText.setFont(Font.loadFont("file:./provided/res/RetroComputer.ttf", 16));
        
        // Crear VBox para cada opción (imagen + texto)
        VBox standardOption = new VBox(5, standardTurretButton, standardCostText);
        VBox electricOption = new VBox(5, electricTurretButton, electricCostText);
        VBox fireOption = new VBox(5, fireTurretButton, fireCostText);
        
        standardOption.setAlignment(Pos.CENTER);
        electricOption.setAlignment(Pos.CENTER);
        fireOption.setAlignment(Pos.CENTER);
        
        // Añadir estilo de selección al estándar por defecto
        standardOption.setStyle("-fx-border-color: yellow; -fx-border-width: 2; -fx-padding: 5;");
        
        // Añadir eventos de clic para seleccionar tipo de torreta
        standardOption.setOnMouseClicked(e -> {
            selectedTurretType = "standard";
            standardOption.setStyle("-fx-border-color: yellow; -fx-border-width: 2; -fx-padding: 5;");
            electricOption.setStyle("-fx-padding: 5;");
            fireOption.setStyle("-fx-padding: 5;");
        });
        
        electricOption.setOnMouseClicked(e -> {
            selectedTurretType = "electric";
            electricOption.setStyle("-fx-border-color: cyan; -fx-border-width: 2; -fx-padding: 5;");
            standardOption.setStyle("-fx-padding: 5;");
            fireOption.setStyle("-fx-padding: 5;");
        });
        
        fireOption.setOnMouseClicked(e -> {
            selectedTurretType = "fire";
            fireOption.setStyle("-fx-border-color: orange; -fx-border-width: 2; -fx-padding: 5;");
            standardOption.setStyle("-fx-padding: 5;");
            electricOption.setStyle("-fx-padding: 5;");
        });
        
        // Añadir opciones al HBox
        turretSelectionBox.getChildren().addAll(standardOption, electricOption, fireOption);
        
        // Posicionar el HBox en la parte inferior central de la pantalla
        turretSelectionBox.setLayoutX(500);
        turretSelectionBox.setLayoutY(610);
        
        // Añadir el HBox al pane principal
        pane1.getChildren().add(turretSelectionBox);
    }

    private void handleNextWaveAction() {
        if (gameState.isPlacingPhase()) {
            gameState.beginWave();
            spawnAliensFromWave(gameState.getWaveSystem().getCurrentWave());
            nextWaveButton.setDisable(true);
            nextWaveButton.setText("Siguiente oleada");
            rangeIndicator.setVisible(false); // Ocultar indicador de rango al iniciar la oleada
        } else if (gameState.isWaveCompletePhase()) {
            int numT = gameState.getTurrets().size();
            int refund = numT * COST_STANDARD;
            int bonus  = 40;
            System.out.println("[DEBUG] Torretas: " + numT + ", refund=" + refund + ", bonus=" + bonus);
            gameState.getBase().addScore(refund + bonus);
            
            // Limpiar elementos visuales de las torretas
            pane2.getChildren().clear();
            
            // Volver a añadir el indicador de rango después de limpiar pane2
            pane2.getChildren().add(rangeIndicator);
            
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
                
                // Crear la torreta según el tipo seleccionado
                switch (selectedTurretType) {
                    case "standard":
                        if (gameState.getBase().getScore() >= COST_STANDARD) {
                            gameState.addTurret(x, y, "standard");
                            Turret t = gameState.getTurrets().get(gameState.getTurrets().size()-1);
                            pane2.getChildren().addAll(t.getImageView(), t.getBombImageView());
                            t.startTargeting(gameState.getEnemyQueue());
                            updateStatusText();
                        }
                        break;
                    case "electric":
                        if (gameState.getBase().getScore() >= COST_ELECTRIC) {
                            gameState.addTurret(x, y, "electric");
                            Turret t = gameState.getTurrets().get(gameState.getTurrets().size()-1);
                            pane2.getChildren().addAll(t.getImageView(), t.getBombImageView());
                            t.startTargeting(gameState.getEnemyQueue());
                            updateStatusText();
                        }
                        break;
                    case "fire":
                        if (gameState.getBase().getScore() >= COST_FIRE) {
                            gameState.addTurret(x, y, "fire");
                            Turret t = gameState.getTurrets().get(gameState.getTurrets().size()-1);
                            pane2.getChildren().addAll(t.getImageView(), t.getBombImageView());
                            t.startTargeting(gameState.getEnemyQueue());
                            updateStatusText();
                        }
                        break;
                }
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
        statusText.setText("SALUD: " + gameState.getBase().getHealth() + "\nPUNTUACION: " + gameState.getBase().getScore());
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