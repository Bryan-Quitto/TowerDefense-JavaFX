package characters;
import java.util.Random;

public class SlowAlien extends Alien {
    
    // Cambiado a una imagen que visualmente represente mejor a un alien lento
    private static final String ALIEN_IMAGE_PATH = "./provided/res/Pixel-Alien-2.png";
    private final Random rand = new Random();

    /**
     * Constructor público que establece salud, velocidad y ruta de la imagen
     */
    public SlowAlien() {
        super(600, 100, ALIEN_IMAGE_PATH);
    }

    /**
     * Método que aleatoriamente aumenta salud en 200 si el valor aleatorio es >9
     */
    @Override
    public void increaseHealth() {
        int temp = rand.nextInt(11);
        if (temp > 9) {
            super.increaseHealth(200);
        }
    }
}