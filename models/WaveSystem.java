package models;

import java.util.Queue;
import java.util.LinkedList;

public class WaveSystem {
    private Queue<Wave> waveQueue;
    private int currentWaveNumber;
    private static final int MAX_WAVES = 10;

    public WaveSystem() {
        waveQueue = new LinkedList<>();
        currentWaveNumber = 0;
    }

    /**
     * Genera la siguiente ola si no se ha alcanzado el límite de oleadas.
     */
    public void generateNextWave() {
        if (currentWaveNumber >= MAX_WAVES) return;
        currentWaveNumber++;
        Wave newWave = new Wave(currentWaveNumber);
        waveQueue.offer(newWave);
        System.out.println("[DEBUG] Oleada " + currentWaveNumber + " generada");
    }
    
    /**
     * @return la ola actual (o null si no existe)
     */
    public Wave getCurrentWave() {
        return waveQueue.peek();
    }

    /**
     * Marca la ola actual como completada y la elimina de la cola.
     */
    public void waveCompleted() {
        waveQueue.poll();
    }

    /**
     * @return número de la ola más reciente generada
     */
    public int getWaveNumber() {
        return currentWaveNumber;
    }
}
