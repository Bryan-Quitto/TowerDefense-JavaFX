package models;

import java.util.Queue;
import java.util.LinkedList;

public class WaveSystem {
    private Queue<Wave> waveQueue;
    private int currentWaveNumber;
    private static final int MAX_WAVES = 10;

    public WaveSystem() {
        waveQueue = new LinkedList<>();
        currentWaveNumber = 1;
        // ¡AÑADE ESTO para empezar en la ola 1!
        waveQueue.offer(new Wave(currentWaveNumber));
        System.out.println("[DEBUG] Oleada " + currentWaveNumber + " generada (inicial)");
    }

    public void generateNextWave() {
        if (currentWaveNumber >= MAX_WAVES) return;
        currentWaveNumber++;
        Wave newWave = new Wave(currentWaveNumber);
        waveQueue.offer(newWave);
        System.out.println("[DEBUG] Oleada " + currentWaveNumber + " generada");
    }
    
    public Wave getCurrentWave() {
        return waveQueue.peek();
    }

    public void waveCompleted() {
        waveQueue.poll();
    }

    public int getWaveNumber() {
        return currentWaveNumber;
    }
}