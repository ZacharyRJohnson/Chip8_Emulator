import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Chip8 chip8 = new Chip8();

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(15, 12, 15, 12));
        chip8.getDisplay().drawScreen();
        chip8.loadRom("C:\\Users\\Zachary\\IdeaProjects\\untitled\\ROMs\\test_opcode.ch8");

        hBox.getChildren().add(chip8.getDisplay());
        Scene scene = new Scene(hBox);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Chip-8 Emulator");

        Timeline gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        KeyFrame gameFrame = new KeyFrame(Duration.seconds(0.016),
                actionEvent -> {
                    chip8.executeOpcode();
                    //System.out.println("test");
                    chip8.updateTimers();
                    if(chip8.getdFlag())
                        chip8.getDisplay().drawScreen();
                });

        gameLoop.getKeyFrames().add(gameFrame);
        gameLoop.play();

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

