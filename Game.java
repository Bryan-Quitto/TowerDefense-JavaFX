// import characters.interfaces.Health;
import characters.Turret;
import characters.Alien;
import characters.Base;
import characters.FastAlien;
import characters.SlowAlien;

// import java.util.ArrayList;
// import java.util.Collections;
// import java.util.Comparator;
// import javafx.animation.SequentialTransition;
// import javafx.beans.binding.Bindings;
// import javafx.beans.property.ObjectProperty;
// import javafx.collections.FXCollections;
// import javafx.collections.ObservableList;
// import javafx.collections.ObservableList;
// import javafx.geometry.Orientation;
// import javafx.geometry.Pos;
// import javafx.geometry.VPos;
// import javafx.scene.control.Label;
// import javafx.scene.control.ListView;
// import javafx.scene.control.Separator;
// import javafx.scene.control.TextField;
// import javafx.scene.control.ToolBar;
// import javafx.scene.Cursor;
// import javafx.scene.layout.Background;
// import javafx.scene.layout.BackgroundImage;
// import javafx.scene.layout.BackgroundRepeat;
// import javafx.scene.layout.BackgroundSize;
// import javafx.scene.layout.BorderPane;
// import javafx.scene.layout.HBox;
// import javafx.scene.layout.StackPane;
// import javafx.scene.layout.VBox;
// import javafx.scene.Node;
// import javafx.scene.shape.Rectangle;
import javafx.animation.Animation.Status;
import javafx.animation.PathTransition;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.ImageCursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Stage;
import javafx.util.Duration;
/**
 * This is the driver class for the TowerDefense Game
 * @author Bilal Mawji
 * @version 1.0
 */
public class Game extends Application {
    /**
     * This is the main class which runs the Game
     * @param stage Stage on which Game is run
     */
    @Override
    public void start(Stage stage) {
        Pane root = new Pane();
        Pane pane1 = new Pane();
        Pane pane2 = new Pane();
        // Base, FastAlien, SlowAlien, Turret
        Base base = new Base();
        Alien a1 = new FastAlien();
        Alien a2 = new SlowAlien();
        a1.increaseHealth();
        a2.increaseHealth();
        // Turret t = new Turret();
        // Background
        Image image = new Image("./provided/res/MapCropped.png");
        ImageView imageBG = new ImageView(image);
        // Covered Areas
        Image covered = new Image("./provided/res/MapCroppedForeground.png");
        ImageView covered2 = new ImageView(covered);
        // Turret Image
        Image turretImage = new Image("./provided/res/launcher.png");
        ImageView turretImageView = new ImageView(turretImage);
        // Turret Areas
        Image areas =
            new Image("./provided/res/placeMapCroppedMinified.png", 1376, 736,
                true, true);
        ImageView areas2 = new ImageView(areas);
        // ImageCursor
        Image green = new Image("./provided/res/green.png");
        Image red = new Image("./provided/res/red.png");
        ImageCursor greenCursor = new ImageCursor(green);
        ImageCursor redCursor = new ImageCursor(red);
        // Aliens
        Image image2 = new Image(a1.getFileName());
        ImageView alien2 = new ImageView(image2);
        Image image3 = new Image(a2.getFileName());
        ImageView alien3 = new ImageView(image3);
        // Text
        Text text = new Text("Health: " + base.getHealth() + "\nScore: "
            + base.getScore());
        text.setBoundsType(TextBoundsType.VISUAL);
        text.setFont(Font.loadFont("file:./provided/res/RetroComputer.ttf",
            50));
        // Path
        Path p1 = createPath();
        // Root
        root.setMinSize(1376, 736);
        root.getChildren().addAll(imageBG, pane1);
        // Pane1
        pane1.setMinSize(1376, 736);
        pane1.getChildren().addAll(text, alien2, alien3, covered2, pane2);
        text.relocate(325, 20);
        // Pane2
        areas2.setFitWidth(1376);
        areas2.setFitHeight(736);
        pane2.setMinSize(1376, 736);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("TowerDefense!!");
        stage.setResizable(false);
        stage.show();
        pane2.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                int mouseX = ((int) event.getX());
                int mouseY = ((int) event.getY());
                PixelReader placePixels = areas.getPixelReader();
                try {
                    if ((1376 - mouseX) < 1376 && (736 - mouseY) < 736) {
                        if (!placePixels.getColor(mouseX, mouseY)
                            .equals(Color.BLACK)) {
                            if (!placePixels.getColor(mouseX + 5, mouseY + 5)
                                .equals(Color.BLACK)) {
                                if (!placePixels.getColor(mouseX, mouseY + 5)
                                    .equals(Color.BLACK)) {
                                    if (!placePixels
                                        .getColor(mouseX + 5, mouseY)
                                        .equals(Color.BLACK)) {
                                        scene
                                        .setCursor(greenCursor);
                                    }
                                }
                            }
                            } else {
                                scene.setCursor(redCursor);
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Out of bounds");
                }
            }
        });
        pane2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                int mouseX = ((int) event.getX());
                int mouseY = ((int) event.getY());
                if (scene.getCursor().equals(greenCursor) && base.haveScore()) {
                    Turret t = new Turret();
                    t.getImageView().setX(mouseX);
                    t.getImageView().setY(mouseY);
                    t.getBombImageView().setX(mouseX);
                    t.getBombImageView().setY(mouseY);
                    pane2.getChildren().addAll(t.getImageView(),
                        t.getBombImageView());
                    t.pathTransitionBomb().play();
                    base.buyTurret();
                    text.setText("Health: " + base.getHealth() + "\nScore: "
                        + base.getScore());
                }
            }
        });
        // PathTransition
        PauseTransition pause = new PauseTransition(Duration.millis(5000));
        PathTransition pt =
            new PathTransition(Duration.millis(20000), p1, alien2);
        PathTransition pt2 =
            new PathTransition(Duration.millis(35000), p1, alien3);
        pt.statusProperty()
            .addListener(new ChangeListener<Status>() {
                @Override
                public void changed(ObservableValue<? extends Status>
                    observableValue, Status oldValue, Status newValue) {
                        if (newValue == Status.STOPPED) {
                            base.subHealth(a1.getAttack());
                            text.setText("Health: " + base.getHealth()
                                + "\nScore: " + base.getScore());
                        }
                    }
                });
        pt2.statusProperty()
            .addListener(new ChangeListener<Status>() {
                @Override
                public void changed(ObservableValue<? extends Status>
                    observableValue, Status oldValue, Status newValue) {
                        if (newValue == Status.STOPPED) {
                            base.subHealth(a2.getAttack());
                            text.setText("Health: " + base.getHealth()
                                + "\nScore: " + base.getScore());
                        }
                    }
                });
        pt.play();
        pause.play();
        pt2.play();
    }

    /**
     * Makes a path for Aliens to travel on
     * @return Path Path for Aliens
     */
    public Path createPath() {
        Path path = new Path();
        MoveTo spawn = new MoveTo(6.0, 430.0);
        LineTo line1 = new LineTo(85.0, 430.0);
        LineTo line2 = new LineTo(85.0, 80.0);
        LineTo line3 = new LineTo(265.0, 80.0);
        LineTo line4 = new LineTo(265.0, 682.5);
        LineTo line5 = new LineTo(350.0, 682.5);
        LineTo line6 = new LineTo(350.0, 600.0);
        LineTo line7 = new LineTo(800.0, 600.0);
        LineTo line8 = new LineTo(800.0, 425.5);
        LineTo line9 = new LineTo(1121.0, 425.5);
        LineTo line10 = new LineTo(1121.0, 300.0);
        path.getElements().addAll(spawn, line1, line2, line3, line4, line5,
            line6, line7, line8, line9, line10);
        return path;
    }
}
