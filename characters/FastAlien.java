package characters;
import java.util.Random;
/**
 * This is a representation of the FastAlien which is an Alien
 * @author Bilal Mawji
 * @version 1.0
 */
public class FastAlien extends Alien {
    private final Random rand = new Random();
    // Cambiado a una imagen que visualmente represente mejor a un alien rápido
    private static final String ALIEN_IMAGE_PATH = "./provided/res/Pixel-Alien-7.png";

    /**
     * Constructor for FastAlien; Inherited attack of 50;
     * Calls Multi-argument constructor
     */
    public FastAlien() {
        // Velocidad aumentada de 3 a 6
        this(200, 6, ALIEN_IMAGE_PATH);
    }

    /**
     * Private constructor for FastAlien; Inherited attack of 50;
     * @param health Health of FastAlien
     * @param speed Speed of FastAlien
     * @param fileName FileName of FastAlien Picture
     */
    private FastAlien(int health, int speed, String fileName) {
        super(health, speed, fileName);
    }

    /**
     * Randomly chooses number; if greater than 9, will increase health
     * Method inherited from Alien; Implemented from Health
     */
    @Override
    public void increaseHealth() {
        int temp = rand.nextInt(11);
        if (temp > 9) {
            super.increaseHealth(100);
        }
    }
}