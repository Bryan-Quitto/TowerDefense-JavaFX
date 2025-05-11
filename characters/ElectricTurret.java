package characters;

import javafx.scene.image.Image;
public class ElectricTurret extends Turret {
    public ElectricTurret() {
        super();
        this.attack = 30;
        this.cost = 60;
        this.range = 700.0;
        this.fileName = "./provided/res/electric_launcher.png";
        this.image = new Image(fileName);
        this.imageView.setImage(image);
        this.bombFileName = "./provided/res/bomb.gif";
        this.bombImage = new Image(bombFileName);
        this.bombImageView.setImage(bombImage);
    }
}
