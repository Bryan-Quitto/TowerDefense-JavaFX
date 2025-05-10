package characters;

import javafx.scene.image.Image;
public class ElectricTurret extends Turret {
    public ElectricTurret() {
        super();
        this.attack = 40;
        this.cost = 50;
        this.range = 600.0;
        this.fileName = "./provided/res/electric_launcher.png";
        this.image = new Image(fileName);
        this.imageView.setImage(image);
        this.bombFileName = "./provided/res/bomb.gif";
        this.bombImage = new Image(bombFileName);
        this.bombImageView.setImage(bombImage);
    }
}
