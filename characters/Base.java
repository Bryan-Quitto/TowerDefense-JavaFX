package characters;
/**
 * This is a representation of the Base
 * @author Bilal Mawji
 * @version 1.0
 */
public class Base {

    private int health;
    private int score;


    /**
     * Constructor for Base; sets health to 1000
     */
    public Base() {
        this.health = 1000;
        this.score = 160;
    }

    /**
     * @return Health of Base
     */
    public int getHealth() {
        return health;
    }

    /**
     * @return Score of Base / Game
     */
    public int getScore() {
        return score;
    }

    /**
     * If player has Score (money), then will be able to buy a turret
     */
    public void buyTurret() {
        if (haveScore()) {
            score = score - 40;
        }
    }

    /**
     * Subtracts amount from health; will be 0 if amount is greater than zero
     * @param amount Amount of health that is substracted
     */
    public void subHealth(int amount) {
        health = (health - amount < 0) ? 0 : (health - amount);
    }

    /**
     * @return true if Base is dead; false if Base is alive
     */
    public boolean isDead() {
        return health <= 0;
    }

    /**
     * @return true if you have Score (money); false if you don't
     */
    public boolean haveScore() {
        return score != 0;
    }
}
