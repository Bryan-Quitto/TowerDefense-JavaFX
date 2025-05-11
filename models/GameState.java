package models;

import characters.Turret;
import javafx.scene.image.Image;
import characters.Base;
import characters.Alien;
import characters.ElectricTurret;
import characters.FireTurret;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase mantiene el estado del juego y sus estructuras de datos principales
 */
public class GameState {
    private Base base;
    private Alien[] aliens;
    private Image backgroundImage;
    private Image coveredAreaImage;
    private Image turretImage;
    private Image placementAreaImage;
    private Image greenCursorImage;
    private Image redCursorImage;
    private List<Turret> turrets = new ArrayList<>();
    private WaveSystem waveSystem;
    private TowerHistory towerHistory;
    private CircularQueue enemyQueue;
    private static final int COST_STANDARD = 40;
    private static final int COST_ELECTRIC = 60;
    private static final int COST_FIRE     = 100;

    // Fases de la oleada
    private boolean placingPhase = true;
    private boolean waveActive = false;
    private boolean waveComplete = false;

    public GameState() {
        enemyQueue = new CircularQueue(50);
        waveSystem = new WaveSystem();    // inicializo sistema de oleadas
        initializeGameElements();        // genera la primera oleada (no activa hasta beginWave())
        loadResources();
        towerHistory = new TowerHistory();
    }

    private void initializeGameElements() {
        base = new Base();
        aliens = new Alien[0]; // sin enemigos hasta empezar oleada
    }

    public void beginWave() {
        // Generar ola 1 si es necesario
        if (waveSystem.getWaveNumber() == 0) {
            waveSystem.generateNextWave();
        }

        Wave currentWave = waveSystem.getCurrentWave();
        if (currentWave == null) {
            System.out.println("[ERROR] No hay oleada disponible");
            return;
        }

        // Cambiar fases
        placingPhase = false;
        waveActive = true;
        waveComplete = false;

        // Inicializamos el arreglo de aliens pero NO los añadimos a la cola aquí
        // La cola se maneja desde Game.spawnAliensFromWave()
        aliens = currentWave.getEnemies();
        System.out.println("[DEBUG] beginWave() — aliens.length = " + aliens.length);
        System.out.println("[DEBUG] Oleada " + waveSystem.getWaveNumber() + " iniciada");
    }

    // Genera y avanza a la siguiente oleada
    public void startNextWave() {
        waveSystem.waveCompleted(); // Elimina la oleada completada
        waveSystem.generateNextWave(); // Genera la siguiente
        
        Wave currentWave = waveSystem.getCurrentWave();
        if (currentWave == null) return;
        
        // Reiniciar cola de enemigos
        aliens = currentWave.getEnemies();
        enemyQueue = new CircularQueue(50);
        // No añadir los aliens a la cola todavía - esto se hará en beginWave()
        
        // Restablecer fases
        placingPhase = true; // Permitir colocar torres nuevamente
        waveActive = false;
        waveComplete = false;
        System.out.println("[DEBUG] Preparada nueva oleada: " + waveSystem.getWaveNumber());
    }

    // Actualiza estado de enemigos y detecta fin de oleada
    public void updateGame() {
        enemyQueue.updateStatus();
        updateEnemyStatus();
        if (waveActive && isWaveComplete()) {
            waveActive = false;
            waveComplete = true;
        }
    }

    public void updateEnemyStatus() {
        for (int i = 0; i < aliens.length; i++) {
            Alien a = aliens[i];
            if (a != null && a.isDead()) {
                handleAlienDeath(a);
                a.getImageView().setVisible(false);
                aliens[i] = null;
            }
        }
    }

    // Getters y setters de fase
    public boolean isPlacingPhase() { return placingPhase; }
    public boolean isWaveActive()    { return waveActive; }
    public boolean isWaveCompletePhase() { return waveComplete; }

    // Resto de getters para recursos, base, torres y cola
    public Base getBase() { return base; }
    public Alien[] getAliens() { return aliens; }
    public Image getBackgroundImage() { return backgroundImage; }
    public Image getCoveredAreaImage() { return coveredAreaImage; }
    public Image getTurretImage() { return turretImage; }
    public Image getPlacementAreaImage() { return placementAreaImage; }
    public Image getGreenCursorImage() { return greenCursorImage; }
    public Image getRedCursorImage() { return redCursorImage; }
    public List<Turret> getTurrets() { return turrets; }
    public CircularQueue getEnemyQueue() { return enemyQueue; }
    public int getCurrentWaveNumber()  { return waveSystem.getWaveNumber(); }

    private void loadResources() {
        backgroundImage = new Image("./provided/res/MapCropped.png");
        coveredAreaImage = new Image("./provided/res/MapCroppedForeground.png");
        turretImage = new Image("./provided/res/launcher.png");
        placementAreaImage = new Image("./provided/res/placeMapCroppedMinified.png", 1376, 736, true, true);
        greenCursorImage = new Image("./provided/res/green.png");
        redCursorImage = new Image("./provided/res/red.png");
    }

    // Método original para añadir una torre estándar
    public void addTurret(int x, int y) {
        addTurret(x, y, "standard");
    }
    
    // Método sobrecargado que acepta un tipo de torre
    public void addTurret(int x, int y, String type) {
        if (!placingPhase) return;
        
        Turret t = null;
        boolean canAfford = false;
        
        // Crear la torre según el tipo especificado
        switch (type) {
            case "standard":
                if (base.getScore() >= COST_STANDARD) {
                    t = new Turret();
                    canAfford = true;
                }
                break;
            case "electric":
                if (base.getScore() >= COST_ELECTRIC) {
                    t = new ElectricTurret();
                    canAfford = true;
                }
                break;
            case "fire":
                if (base.getScore() >= COST_FIRE) {
                    t = new FireTurret();
                    canAfford = true;
                }
                break;
            default:
                // Por defecto, crear una torre estándar
                if (base.getScore() >= 100) {
                    t = new Turret();
                    canAfford = true;
                }
        }
        
        if (t != null && canAfford) {
            t.getImageView().setX(x - t.getImageView().getImage().getWidth()/2);
            t.getImageView().setY(y - t.getImageView().getImage().getHeight()/2);
            t.getBombImageView().setX(x);
            t.getBombImageView().setY(y);
            turrets.add(t);
            towerHistory.addAction(t, x, y);
            
            // Restar el costo de la torre correspondiente
            base.subScore(t.getCost());
        }
    }

    public void undoTurretPlacement() {
    // Solo se permite deshacer durante la fase de colocación
    if (!placingPhase) return;

    TowerAction action = towerHistory.undo();
    if (action != null) {
        Turret t = action.getTurret();
        base.addScore(t.getCost());
        turrets.remove(t);
        t.stopTargeting();
    }
}

public void redoTurretPlacement() {
    // Solo se permite rehacer durante la fase de colocación
    if (!placingPhase) return;

    TowerAction action = towerHistory.redo();
    if (action != null) {
        Turret t = action.getTurret();
        if (base.getScore() >= t.getCost()) {
            base.subScore(t.getCost());
            turrets.add(t);
        }
    }
}

public TowerHistory getTowerHistory() {
    return towerHistory;
}

    public void handleAlienDeath(Alien alien) {
        // Recompensar al jugador con puntos por matar un alien
        base.addScore(10);
    }

    public boolean isWaveComplete() {
        Wave currentWave = waveSystem.getCurrentWave();
        return currentWave != null && !currentWave.hasMoreEnemies() && enemyQueue.isEmpty();
    }

    public WaveSystem getWaveSystem() {
        return waveSystem;
    }
 
    public void reset() {
    this.base = new Base(); // restablece salud
    this.enemyQueue.clear();
    // cualquier otra variable que quieras resetear
    }

}