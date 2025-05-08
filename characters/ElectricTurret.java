package characters;

import javafx.scene.image.Image;

public class ElectricTurret extends Turret {
    private static final String ELECTRIC_TURRET_IMAGE = "./provided/res/electric_launcher.png";
    private static final String ELECTRIC_BOMB_IMAGE = "./provided/res/electric_bomb.gif";
    
    public ElectricTurret() {
        super();
        this.attack = 40;   // Menos daño que la torreta básica
        this.cost = 50;     // Precio intermedio
        this.range = 600.0; // Mayor rango que la básica
        this.fileName = ELECTRIC_TURRET_IMAGE;
        this.image = new Image(fileName);
        this.imageView.setImage(image);
        this.bombFileName = ELECTRIC_BOMB_IMAGE;
        this.bombImage = new Image(bombFileName);
        this.bombImageView.setImage(bombImage);
    }
}