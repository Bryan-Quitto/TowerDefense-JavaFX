package models;

import java.util.Stack;
import characters.Turret;

public class TowerHistory {
    private Stack<TowerAction> undoStack;
    private Stack<TowerAction> redoStack;
    
    public TowerHistory() {
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }
    
    public void addAction(Turret turret, int x, int y) {
        TowerAction action = new TowerAction(turret, x, y);
        undoStack.push(action);
        redoStack.clear(); // Clear redo stack when new action is added
    }
    
    public TowerAction undo() {
        if (!undoStack.isEmpty()) {
            TowerAction action = undoStack.pop();
            redoStack.push(action);
            return action;
        }
        return null;
    }
    
    public TowerAction redo() {
        if (!redoStack.isEmpty()) {
            TowerAction action = redoStack.pop();
            undoStack.push(action);
            return action;
        }
        return null;
    }
}