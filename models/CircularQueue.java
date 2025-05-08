package models;

import characters.Alien;

import java.util.ArrayList;
import java.util.List;

public class CircularQueue {
    private Alien[] queue;
    private int front;
    private int rear;
    private int size;
    private final int capacity;

    public CircularQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new Alien[capacity];
        this.front = 0;
        this.rear = -1;
        this.size = 0;
    }

    public void enqueue(Alien alien) {
        if (isFull()) return;
        rear = (rear + 1) % capacity;
        queue[rear] = alien;
        size++;
    }

    public Alien dequeue() {
        if (isEmpty()) return null;
        Alien alien = queue[front];
        queue[front] = null;
        front = (front + 1) % capacity;
        size--;
        return alien;
    }

    public void updateStatus() {
        int newSize = 0;
        for (int i = 0; i < capacity; i++) {
            if (queue[i] != null && queue[i].isDead()) {
                System.out.println("Eliminando enemigo muerto de la cola: " + queue[i]);
                queue[i] = null;
            }
            if (queue[i] != null) {
                newSize++;
            }
        }
        size = newSize;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == capacity;
    }

    public int getSize() {
        return size;
    }

    public List<Alien> getAliveAliens() {
        List<Alien> aliveAliens = new ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            if (queue[i] != null && !queue[i].isDead()) {
                aliveAliens.add(queue[i]);
            }
        }
        return aliveAliens;
    }
}