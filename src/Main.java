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
        chip8.loadRom("C:\\Users\\Zachary\\IdeaProjects\\untitled\\ROMs\\PONG.ch8");

        hBox.getChildren().add(chip8.getDisplay());
        Scene scene = new Scene(hBox);

        scene.setOnKeyPressed(e -> {
            switch(e.getCode()){
                case DIGIT1:
                    chip8.setKeyPressed(1);
                    break;
                
                case DIGIT2:
                    chip8.setKeyPressed(2);
                    break;

                case DIGIT3:
                    chip8.setKeyPressed(3);
                    break;

                case DIGIT4:
                    chip8.setKeyPressed(12);
                    break;

                case Q:
                    chip8.setKeyPressed(4);
                    break;

                case W:
                    chip8.setKeyPressed(5);
                    break;

                case E:
                    chip8.setKeyPressed(6);
                    break;

                case R:
                    chip8.setKeyPressed(13);
                    break;

                case A:
                    chip8.setKeyPressed(7);
                    break;

                case S:
                    chip8.setKeyPressed(8);
                    break;

                case D:
                    chip8.setKeyPressed(9);
                    break;

                case F:
                    chip8.setKeyPressed(14);
                    break;

                case Z:
                    chip8.setKeyPressed(10);
                    break;

                case X:
                    chip8.setKeyPressed(0);
                    break;

                case C:
                    chip8.setKeyPressed(11);
                    break;

                case V:
                    chip8.setKeyPressed(15);
                    break;

            }
        });

        scene.setOnKeyReleased(e -> {
            switch(e.getCode()){
                case DIGIT1:
                    chip8.setKeyReleased(1);
                    break;

                case DIGIT2:
                    chip8.setKeyReleased(2);
                    break;

                case DIGIT3:
                    chip8.setKeyReleased(3);
                    break;

                case DIGIT4:
                    chip8.setKeyReleased(12);
                    break;

                case Q:
                    chip8.setKeyReleased(4);
                    break;

                case W:
                    chip8.setKeyReleased(5);
                    break;

                case E:
                    chip8.setKeyReleased(6);
                    break;

                case R:
                    chip8.setKeyReleased(13);
                    break;

                case A:
                    chip8.setKeyReleased(7);
                    break;

                case S:
                    chip8.setKeyReleased(8);
                    break;

                case D:
                    chip8.setKeyReleased(9);
                    break;

                case F:
                    chip8.setKeyReleased(14);
                    break;

                case Z:
                    chip8.setKeyReleased(10);
                    break;

                case X:
                    chip8.setKeyReleased(0);
                    break;

                case C:
                    chip8.setKeyReleased(11);
                    break;

                case V:
                    chip8.setKeyReleased(15);
                    break;

            }
        });

        primaryStage.setScene(scene);
        primaryStage.setTitle("Chip-8 Emulator");

        Timeline gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        KeyFrame gameFrame = new KeyFrame(Duration.seconds(0.003),
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

