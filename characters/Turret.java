package characters;
import javafx.animation.PathTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
/**
 * This is a representation of the Turret
 * @author Bilal Mawji
 * @version 1.0
 */
public class Turret {

    private int attack;
    private int cost;
    private String fileName;
    private Image image;
    private ImageView imageView;
    private String bombFileName;
    private Image bombImage;
    private ImageView bombImageView;
    /**
     * Constructor for Turret; sets attack to 50
     */
    public Turret() {
        this.attack = 50;
        this.cost = 40;
        this.fileName = "./provided/res/launcher.png";
        this.image = new Image(fileName);
        this.imageView = new ImageView(image);
        this.bombFileName = "./provided/res/bomb.gif";
        this.bombImage = new Image(bombFileName);
        this.bombImageView = new ImageView(bombImage);
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
    public void attack(Alien a) {
        // Takes 4 attacks for Turret t to destroy Alien a
        a.subHealth(attack);
    }

    /**
     * Makes a Path for Bomb to travel
     * @return Path Path for Bombs
     * @param x x coordinate
     * @param y y coordinate
     */
    public Path createPathBombs(double x, double y) {
        Path path = new Path();
        MoveTo spawn = new MoveTo(x, y);
        LineTo line1 = new LineTo(0, 0);
        path.getElements().addAll(spawn, line1);
        return path;
    }

    public PathTransition pathTransitionBomb() {
        Path path = createPathBombs(getImageView().getX(),
            getImageView().getY());
        PathTransition pt2 =
            new PathTransition(Duration.millis(5000), path, getBombImageView());
        return pt2;
    }
}
