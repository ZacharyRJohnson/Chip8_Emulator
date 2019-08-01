import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Chip8 chip8 = new Chip8();

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(15, 12, 15, 12));
        chip8.getDisplay().drawScreen();
        //Display screen = new Display();
        //screen.drawScreen();
        Button btn = new Button();
        btn.setText("Test");

        hBox.getChildren().add(chip8.getDisplay());
        hBox.getChildren().add(btn);


        Scene scene = new Scene(hBox);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Chip-8 Emulator");
        primaryStage.show();
        //chip8.executeOpcode();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

