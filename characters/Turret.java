package characters;
import javafx.animation.PathTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;  
import models.CircularQueue;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame; 

public class Turret {

    protected int attack;
    protected int cost;
    protected double range;
    protected String fileName;
    protected Image image;
    protected ImageView imageView;
    protected String bombFileName;
    protected Image bombImage;
    protected ImageView bombImageView;
    protected int upgradeLevel; // Nuevo atributo para el nivel de mejora
    protected static final int UPGRADE_COST = 50; // Costo fijo de mejora
    protected static final int UPGRADE_ATTACK_BONUS = 10; // Bonus de ataque por mejora
    private Timeline targetingTimeline;
    private Alien currentTarget;

    public Turret() {
        this.attack = 50;
        this.cost = 40;
        this.range = 500.0;
        this.fileName = "./provided/res/launcher.png";
        this.image = new Image(fileName);
        this.imageView = new ImageView(image);
        this.bombFileName = "./provided/res/bomb.gif";
        this.bombImage = new Image(bombFileName);
        this.bombImageView = new ImageView(bombImage);
        this.upgradeLevel = 0;
    }

    /**
     * Mejora la torreta aumentando su daño
     * @return true si la mejora fue exitosa, false si no hay suficientes recursos
     */
    public boolean upgrade() {
        this.attack += UPGRADE_ATTACK_BONUS;
        this.upgradeLevel++;
        return true;
    }

    /**
     * Obtiene el costo de la siguiente mejora
     * @return el costo fijo de mejora
     */
    public int getUpgradeCost() {
        return UPGRADE_COST;
    }

    /**
     * Obtiene el nivel actual de mejora de la torreta
     * @return el nivel de mejora actual
     */
    public int getUpgradeLevel() {
        return upgradeLevel;
    }

    /**
     * @return attack power of Turret
     */
    public int getAttack() {
        return attack;
    }

    /**
     * @return cost of Turret
     */
    public int getCost() {
        return cost;
    }

    /**
     * @return fileName of Turret
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @return image of Turret
     */
    public Image getImage() {
        return image;
    }

    /**
     * @return ImageView of Turret
     */
    public ImageView getImageView() {
        return imageView;
    }

    /**
     * @return bombFileName of Turret
     */
    public String getBombFileName() {
        return bombFileName;
    }

    /**
     * @return bombImage of Turret
     */
    public Image getBombImage() {
        return bombImage;
    }

    /**
     * @return BombImageView of Turret
     */
    public ImageView getBombImageView() {
        return bombImageView;
    }

    /**
     * Attacks a Alien a; Alien a loses health equal to attack of Turret
     * @param a Alien that is attacked by Turret
     */
    /**
     * Calcula la distancia entre la torre y un alien
     * @param a Alien objetivo
     * @return distancia en píxeles
     */
    public double calculateDistance(Alien a) {
        double turretX = this.imageView.getX();
        double turretY = this.imageView.getY();
        double alienX = a.getImageView().getX();
        double alienY = a.getImageView().getY();
        
        return Math.sqrt(Math.pow(turretX - alienX, 2) + Math.pow(turretY - alienY, 2));
    }

    /**
     * Verifica si un alien está dentro del rango de ataque
     * @param a Alien a verificar
     * @return true si está en rango, false si no
     */
    public boolean isInRange(Alien a) {
        return calculateDistance(a) <= this.range;
    }

    /**
     * Ataca a un Alien si está dentro del rango
     * @param a Alien que será atacado por la Torre
     * @return true si el ataque fue exitoso, false si está fuera de rango
     */
    public boolean attack(Alien a) {
        if (isInRange(a)) {
            a.subHealth(attack);
            System.out.println("Daño realizado: " + attack + " - Vida restante del enemigo: " + a.getHealth());
            return true;
        }
        return false;
    }

    /**
     * Crea un camino para que la bomba viaje hacia el objetivo
     * @param target el Alien objetivo
     * @return Path camino para la bomba
     */
    public Path createPathBombs(Alien target) {
        Path path = new Path();
        double startX = getBombImageView().getX();  // Usar la posición actual de la bomba
        double startY = getBombImageView().getY();
        double targetX = target.getImageView().getX();
        double targetY = target.getImageView().getY();
        
        MoveTo spawn = new MoveTo(startX, startY);
        LineTo line1 = new LineTo(targetX, targetY);
        path.getElements().addAll(spawn, line1);
        return path;
    }

    /**
     * Crea la transición de la bomba hacia el alien objetivo
     * @param target el Alien objetivo
     * @return PathTransition configurada para el ataque
     */
    public PathTransition pathTransitionBomb(Alien target) {
        // Posicionar la bomba directamente sobre el objetivo
        getBombImageView().setX(target.getImageView().getX());
        getBombImageView().setY(target.getImageView().getY());
        
        // Crear una transición sin movimiento solo para manejar el tiempo
        Path path = new Path();
        MoveTo spawn = new MoveTo(target.getImageView().getX(), target.getImageView().getY());
        path.getElements().add(spawn);
        
        PathTransition pt2 = new PathTransition(Duration.millis(500), path, getBombImageView());
        pt2.setCycleCount(1);
        pt2.setAutoReverse(false);
        
        pt2.setOnFinished(_ -> {
            if (isInRange(target) && !target.isDead()) {
                // Realizar el ataque y verificar si fue exitoso
                boolean attackSuccess = attack(target);
                if (attackSuccess) {
                    System.out.println("Daño realizado: " + attack + " - Vida restante del enemigo: " + target.getHealth());
                    // Si el enemigo muere después del ataque, ocultar la bomba y detener los ataques
                    if (target.isDead()) {
                        getBombImageView().setVisible(false);
                        return;
                    }
                }
                // Continuar el ataque solo si el enemigo sigue vivo
                PathTransition nextShot = pathTransitionBomb(target);
                nextShot.play();
            } else {
                getBombImageView().setVisible(false);
            }
        });
        
        return pt2;
    }
    
    public void startTargeting(CircularQueue enemyQueue) {
        targetingTimeline = new Timeline(
            new KeyFrame(Duration.millis(500), e -> {
                updateTarget(enemyQueue);
            })
        );
        targetingTimeline.setCycleCount(Timeline.INDEFINITE);
        targetingTimeline.play();
    }
    
    private void updateTarget(CircularQueue enemyQueue) {
        // Si el objetivo actual está muerto o fuera de rango, buscar uno nuevo
        if (currentTarget == null || currentTarget.isDead() || !isInRange(currentTarget)) {
            currentTarget = findClosestAlien(enemyQueue);
        }
        // Si hay un objetivo válido, atacar en cada ciclo
        if (currentTarget != null && !currentTarget.isDead() && isInRange(currentTarget)) {
            attack(currentTarget);
        }
    }
    
    private Alien findClosestAlien(CircularQueue enemyQueue) {
        double minDistance = Double.MAX_VALUE;
        Alien closest = null;
        
        // Iterar sobre la cola circular de enemigos
        for (Alien alien : enemyQueue.getAliveAliens()) {
            if (!alien.isDead() && isInRange(alien)) {
                double distance = calculateDistance(alien);
                if (distance < minDistance) {
                    minDistance = distance;
                    closest = alien;
                }
            }
        }
        return closest;
    }

    
    public void stopTargeting() {
        if (targetingTimeline != null) {
            targetingTimeline.stop();
        }
    }
}
