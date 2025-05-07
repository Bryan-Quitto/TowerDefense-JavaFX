package characters;
import java.util.Random;
/**
 * This is a representation of the SlowAlien which is an Alien
 * @author Bilal Mawji
 * @version 1.0
 */
public class SlowAlien extends Alien {
    /**
     * Constructor for SlowAlien; Inherited attack of 50;
     * Calls Multi-argument constructor
     */
    public SlowAlien() {
        this(400, 1, "./provided/res/Pixel-Alien-1.png");
    }
    /**
     * Private constructor for SlowAlien; Inherited attack of 50;
     * @param health Health of SlowAlien
     * @param speed Speed of SlowAlien
     * @param fileName FileName of SlowAlien Picture
     */
    private SlowAlien(int health, int speed, String fileName) {
        super(400, 1, "./provided/res/Pixel-Alien-1.png");
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
            increaseHealth(200);
        }
    }

}
