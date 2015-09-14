package characters;
import characters.interfaces.Health;
import java.util.Random;
/**
 * This is a representation of the FastAlien which is an Alien
 * @author Bilal Mawji
 * @version 1.0
 */
public class FastAlien extends Alien {
    /**
     * Constructor for FastAlien; Inherited attack of 50;
     * Calls Multi-argument constructor
     */
    public FastAlien() {
        this(200, 3, "./provided/res/Pixel-Alien-7.png");
    }
    /**
     * Private constructor for FastAlien; Inherited attack of 50;
     * @param health Health of FastAlien
     * @param speed Speed of FastAlien
     * @param fileName FileName of FastAlien Picture
     */
    private FastAlien(int health, int speed, String fileName) {
        super(200, 3, "./provided/res/Pixel-Alien-7.png");
    }
    /**
     * Randomly chooses number; if greater than 9, will increase health
     * Method inherited from Alien; Implemented from Health
     * @param amount Amount to increase health by
     */
    public void increaseHealth() {
        Random rand = new Random();
        int temp = rand.nextInt(11);
        if (temp > 9) {
            increaseHealth(100);
        }
    }
}
