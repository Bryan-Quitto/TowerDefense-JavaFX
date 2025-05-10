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
    if (alien == null) {
        System.out.println("[ERROR] Intento de añadir alien nulo a la cola");
        return;
    }
    if (isFull()) return;
    rear = (rear + 1) % capacity;
    queue[rear] = alien;
    size++;
}

    public Alien dequeue() {
    if (isEmpty()) {
        System.out.println("[DEBUG] Cola vacía. No se puede desencolar.");
        return null;
    }
    Alien alien = queue[front];
    if (alien == null) {
        System.out.println("[DEBUG] Elemento nulo encontrado en la cola.");
        front = (front + 1) % capacity;
        size--;
        return null;
    }
    System.out.println("[DEBUG] Dequeuing: " + alien);
    queue[front] = null;
    front = (front + 1) % capacity;
    size--;
    return alien;
}

    public void updateStatus() {
        int newSize = 0;
        for (int i = 0; i < capacity; i++) {
            if (queue[i] != null) {
                if (queue[i].isDead()) {
                    System.out.println("[DEBUG] Eliminando enemigo muerto: " + queue[i]);
                    queue[i] = null;
                } else {
                    newSize++;
                }
            }
        }
        size = newSize; // Actualizar tamaño real
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

    public void clear() {
    for (int i = 0; i < capacity; i++) {
        queue[i] = null;
    }
    front = 0;
    rear  = -1;
    size  = 0;
}
}