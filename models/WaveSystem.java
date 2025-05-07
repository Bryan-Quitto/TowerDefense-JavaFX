package models;

import java.util.Queue;
import java.util.LinkedList;

public class WaveSystem {
    private Queue<Wave> waveQueue;
    private int currentWaveNumber;
    
    public WaveSystem() {
        waveQueue = new LinkedList<>();
        currentWaveNumber = 0;
    }
    
    public void generateNextWave() {
        currentWaveNumber++;
        Wave newWave = new Wave(currentWaveNumber);
        waveQueue.offer(newWave);
    }
    
    public Wave getCurrentWave() {
        return waveQueue.peek();
    }
    
    public void waveCompleted() {
        waveQueue.poll();
    }
    
    public int getCurrentWaveNumber() {
        return currentWaveNumber;
    }
    
    public int getWaveNumber() {
        return currentWaveNumber;
    }
}