package characters;
import characters.interfaces.Health;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
/**
 * This is a representation of the Alien
 * @author Bilal Mawji
 * @version 1.0
 */
public abstract class Alien implements Health {

    private int health;
    private int attack;
    private int speed;
    private String fileName;
    private Image image;
    private ImageView imageView;

    /**
     * Constructor for Alien; Sets attack to 50
     * @param health Health of Alien
     * @param speed Speed of Alien
     * @param fileName FileName of Alien Picture
     */
    public Alien(int health, int speed, String fileName) {
        this.attack = 50;
        this.health = health;
        this.speed = speed;
        this.fileName = fileName;
        Image image = new Image(fileName);
        ImageView imageView = new ImageView(image);
    }

    /**
     * @return Health of Alien
     */
    public int getHealth() {
        return health;
    }

    /**
     * @return Attack of Alien
     */
    public int getAttack() {
        return attack;
    }

    /**
     * @return FileName of Alien
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param amount Amount to increase health by
     */
    public void increaseHealth(int amount) {
        health = health + amount;
    }

    /**
     * Subtracts amount from health; will be 0 if amount is greater than zero
     * @param amount Amount of health that is substracted
     */
    public void subHealth(int amount) {
        health = (health - amount < 0) ? 0 : (health - amount);
    }

    /**
     * Attacks a Base b; Base b loses health equal to attack of Alien
     * @param b Base that is attacked by Alien
     */
    public void attack(Base b) {
        // Takes 20 hits for Alien a to destroy Base b
        b.subHealth(attack);
    }

    /**
     * @return true if Alien is dead; false if Alien is Alive
     */
    public boolean isDead() {
        return health == 0;
    }

    /**
     * @return image of FastAlien
     */
    public Image getImage() {
        return image;
    }

    /**
     * @return ImageView of FastAlien
     */
    public ImageView getImageView() {
        return imageView;
    }
}
