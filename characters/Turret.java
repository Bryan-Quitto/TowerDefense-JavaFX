package characters;
import javafx.animation.PathTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;  

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
        this.upgradeLevel = 0; // Inicializa el nivel de mejora en 0
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
        Path path = createPathBombs(target);
        PathTransition pt2 = new PathTransition(Duration.millis(500), path, getBombImageView());  // Reducir la duración
        pt2.setCycleCount(1);  // Cambiar a 1 para que se actualice la trayectoria
        pt2.setAutoReverse(false);
        
        pt2.setOnFinished(_ -> {
            if (isInRange(target) && !target.isDead()) {
                attack(target);
                // Reiniciar posición de la bomba para el siguiente disparo
                getBombImageView().setX(getImageView().getX());
                getBombImageView().setY(getImageView().getY());
                // Iniciar inmediatamente la siguiente transición
                PathTransition nextShot = pathTransitionBomb(target);
                nextShot.play();
            } else {
                // Si el objetivo está muerto o fuera de rango, ocultar la bomba
                getBombImageView().setVisible(false);
            }
        });
        
        return pt2;
    }
}
