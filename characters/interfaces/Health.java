package characters.interfaces;
/**
 * This is an interface Health which will be implemented in the
 * Alien, FastAlien, and SlowAlien classes. It will randomly increase
 * the health.
 * @author Bilal Mawji
 * @version 1.0
 */
public interface Health {

    /**
     * Will randomly increase health of alien
     */
    void increaseHealth();
}
