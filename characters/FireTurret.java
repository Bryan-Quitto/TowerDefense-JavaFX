package characters;

import javafx.scene.image.Image;

public class FireTurret extends Turret {
    private static final String FIRE_TURRET_IMAGE = "./provided/res/fire_launcher.png";
    private static final String FIRE_BOMB_IMAGE = "./provided/res/fire_bomb.gif";
    
    public FireTurret() {
        super();
        this.attack = 75; // Más daño que la torreta básica
        this.cost = 60;   // Más costosa
        this.range = 400.0; // Menor rango que la básica
        this.fileName = FIRE_TURRET_IMAGE;
        this.image = new Image(fileName);
        this.imageView.setImage(image);
        this.bombFileName = FIRE_BOMB_IMAGE;
        this.bombImage = new Image(bombFileName);
        this.bombImageView.setImage(bombImage);
    }
}