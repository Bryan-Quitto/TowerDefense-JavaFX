package models;

import java.util.Queue;
import java.util.LinkedList;
import characters.Alien;
import characters.FastAlien;
import characters.SlowAlien;

public class Wave {
    private Queue<Alien> enemies;
    private int waveNumber;

    public Wave(int waveNumber) {
        this.waveNumber = waveNumber;
        this.enemies = new LinkedList<>();
        generateEnemies();
    }

    /**
     * Genera un número de enemigos incrementales: 10 + 5*(waveNumber-1)
     */
    private void generateEnemies() {
        int numEnemies = 10 + 5 * (waveNumber - 1);
        for (int i = 0; i < numEnemies; i++) {
            if (Math.random() < 0.5) {
                enemies.offer(new FastAlien());
            } else {
                enemies.offer(new SlowAlien());
            }
        }
    }

    /**
     * Obtiene y elimina el siguiente enemigo de la cola, o null si la cola está vacía
     */
    public Alien getNextEnemy() {
        Alien next = enemies.poll();
        if (next != null && next.isDead()) {
            return getNextEnemy(); // omitir muertos
        }
        return next;
    }

    /**
     * @return true si aún quedan enemigos por spawnear
     */
    public boolean hasMoreEnemies() {
        return !enemies.isEmpty();
    }

    /**
     * @return arreglo de los enemigos actualmente en la cola
     */
    public Alien[] getEnemies() {
        Alien[] enemyArray = new Alien[enemies.size()];
        return enemies.toArray(enemyArray);
    }
}
