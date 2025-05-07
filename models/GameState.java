package models;

import javafx.scene.image.Image;
import characters.Base;
import characters.Alien;
import characters.FastAlien;
import characters.SlowAlien;
import characters.Turret;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase mantiene el estado del juego y sus estructuras de datos principales
 */
public class GameState {
    private Base base;
    private Alien[] aliens;
    
    // Imágenes y recursos
    private Image backgroundImage;
    private Image coveredAreaImage;
    private Image turretImage;
    private Image placementAreaImage;
    private Image greenCursorImage;
    private Image redCursorImage;
    private List<Turret> turrets = new ArrayList<>();
    private WaveSystem waveSystem;
    private TowerHistory towerHistory;
    
    public GameState() {
        initializeGameElements();
        loadResources();
        waveSystem = new WaveSystem();
        towerHistory = new TowerHistory();
    }
    
    private void initializeGameElements() {
        base = new Base();
        aliens = new Alien[]{
            new FastAlien(),
            new SlowAlien()
        };
        
        // Inicializar la salud de los aliens
        for (Alien alien : aliens) {
            alien.increaseHealth();
        }
    }
    
    private void loadResources() {
        backgroundImage = new Image("./provided/res/MapCropped.png");
        coveredAreaImage = new Image("./provided/res/MapCroppedForeground.png");
        turretImage = new Image("./provided/res/launcher.png");
        placementAreaImage = new Image("./provided/res/placeMapCroppedMinified.png", 1376, 736, true, true);
        greenCursorImage = new Image("./provided/res/green.png");
        redCursorImage = new Image("./provided/res/red.png");
    }
    
    // Getters y setters
    public Base getBase() { return base; }
    public Alien[] getAliens() { return aliens; }
    public Image getBackgroundImage() { return backgroundImage; }
    public Image getCoveredAreaImage() { return coveredAreaImage; }
    public Image getTurretImage() { return turretImage; }
    public Image getPlacementAreaImage() { return placementAreaImage; }
    public Image getGreenCursorImage() { return greenCursorImage; }
    public Image getRedCursorImage() { return redCursorImage; }
    
    public void addTurret(int x, int y) {
        if (base.haveScore()) {
            Turret newTurret = new Turret();
            newTurret.getImageView().setX(x);
            newTurret.getImageView().setY(y);
            turrets.add(newTurret);
            towerHistory.addAction(newTurret, x, y);
            base.buyTurret();
        }
    }
    
    public void undoTurretPlacement() {
        TowerAction action = towerHistory.undo();
        if (action != null) {
            turrets.remove(action.getTurret());
        }
    }
    
    public void redoTurretPlacement() {
        TowerAction action = towerHistory.redo();
        if (action != null) {
            turrets.add(action.getTurret());
        }
    }
    
    public List<Turret> getTurrets() {
        return turrets;
    }
    
    public void startNextWave() {
        waveSystem.generateNextWave();
        Wave currentWave = waveSystem.getCurrentWave();
        // Actualizar los aliens basados en la nueva oleada
        aliens = currentWave.getEnemies();
    }
    
    public boolean isWaveComplete() {
        return waveSystem.getCurrentWave() != null && 
               !waveSystem.getCurrentWave().hasMoreEnemies();
    }
    
    public int getCurrentWaveNumber() {
        return waveSystem.getWaveNumber();
    }
}