import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Chip8 chip8 = new Chip8();
        chip8.executeOpcode();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

