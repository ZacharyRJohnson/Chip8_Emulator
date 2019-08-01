import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Arrays;

public class Display extends Canvas {

    private final int WIDTH = 64, HEIGHT = 32, SCALE = 10;
    private int screen[][];
    private GraphicsContext gc;
    //private Canvas canvas;

    public Display(){
        super(800, 400);
        screen = new int[HEIGHT][WIDTH];
        for(int i = 0; i < HEIGHT; i++)
            Arrays.fill(screen[i], 0);

        //canvas = new Canvas(WIDTH * SCALE, HEIGHT * SCALE);
        setFocusTraversable(true);
        gc = this.getGraphicsContext2D();
        clearScreen();
    }

    public void drawScreen() {
        for(int x = 0; x < HEIGHT; x++){
            for(int y = 0; y < WIDTH; y++){
                if(screen[x][y] == 0)
                    gc.setFill(Color.WHITE);
                else if(screen[x][y] == 1)
                    gc.setFill(Color.BLACK);
                gc.fillRect(x * SCALE, y * SCALE, SCALE, SCALE);
            }
        }
    }

    public void clearScreen() {
        for(int x = 0; x < HEIGHT; x++)
            for(int y = 0; y < WIDTH; y++)
                screen[x][y] = 0;
    }

    public int getPixel(int x, int y) {
        return screen[x][y];
    }

    public void setPixel(int x, int y) {
        screen[x][y] ^= 1;
    }

}
