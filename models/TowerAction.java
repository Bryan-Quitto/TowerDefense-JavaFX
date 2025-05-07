package models;

import characters.Turret;

public class TowerAction {
    private Turret turret;
    private int x;
    private int y;
    
    public TowerAction(Turret turret, int x, int y) {
        this.turret = turret;
        this.x = x;
        this.y = y;
    }
    
    public Turret getTurret() { return turret; }
    public int getX() { return x; }
    public int getY() { return y; }
}