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
    
    private void generateEnemies() {
        int baseEnemies = 5 + waveNumber;
        for (int i = 0; i < baseEnemies; i++) {
            if (i % 2 == 0) {
                enemies.offer(new FastAlien());
            } else {
                enemies.offer(new SlowAlien());
            }
        }
    }
    
    public Alien getNextEnemy() {
        Alien nextEnemy = enemies.poll();
        if (nextEnemy != null && nextEnemy.isDead()) {
            nextEnemy = null; // Eliminar referencia si el alien está muerto
        }
        return nextEnemy;
    }
    
    public boolean hasMoreEnemies() {
        return !enemies.isEmpty();
    }
    
    public Alien[] getEnemies() {
        Alien[] enemyArray = new Alien[enemies.size()];
        return enemies.toArray(enemyArray);
    }
}