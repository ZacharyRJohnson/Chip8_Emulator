import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class Chip8 {

    private final int FOURKILOBYTES = 4096;

    public static final int FONTS[] = {
            0xF0, 0x90, 0x90, 0x90, 0xF0,   // 0
            0x20, 0x60, 0x20, 0x20, 0x70,   // 1
            0xF0, 0x10, 0xF0, 0x80, 0xF0,   // 2
            0xF0, 0x10, 0xF0, 0x10, 0xF0,   // 3
            0x90, 0x90, 0xF0, 0x10, 0x10,   // 4
            0xF0, 0x80, 0xF0, 0x10, 0xF0,   // 5
            0xF0, 0x80, 0xF0, 0x90, 0xF0,   // 6
            0xF0, 0x10, 0x20, 0x40, 0x40,   // 7
            0xF0, 0x90, 0xF0, 0x90, 0xF0,   // 8
            0xF0, 0x90, 0xF0, 0x10, 0xF0,   // 9
            0xF0, 0x90, 0xF0, 0x90, 0x90,   // A
            0xE0, 0x90, 0xE0, 0x90, 0xE0,   // B
            0xF0, 0x80, 0x80, 0x80, 0xF0,   // C
            0xE0, 0x90, 0x90, 0x90, 0xE0,   // D
            0xF0, 0x80, 0xF0, 0x80, 0xF0,   // E
            0xF0, 0x80, 0xF0, 0x80, 0x80    // F
    };

    private int memory[];

    private int V[];        // General purpose 8-bit registers
    private int I;          // 16 bit I register generally used for memory addresses
    private int DT,ST;      // Delay and sound timers

    private int PC;         // Program Counter
    private int SP = -1;    // Stack Pointer
    private int stack[] = new int[16];

    private Display display;
    private boolean dFlag;  // Display flag

    private boolean keyPad[] = new boolean[16];

    public Chip8() {
        memory = new int[FOURKILOBYTES];
        System.arraycopy(FONTS, 0, memory, 0, FONTS.length);    // Load Fonts into memory
        V = new int[16];
        PC = 0x200;     // Start of most Chip-8 program
        display = new Display();
    }

    public void executeOpcode() {
        int opcode = (memory[PC] << 8) | memory[PC + 1];    // Get full opcode from PC and PC + 1
        dFlag = false;

        int regX = opcode & 0x0F00 >>> 8;
        int regY = opcode & 0x00F0 >>> 4;

        switch(opcode){

            case(0x00E0):
                display.clearScreen();
                dFlag = true;
                PC += 2;
                return;

            case(0x00EE):
                PC = stack[SP--];
                PC += 2;
                return;

        }

        switch(opcode & 0xF000) {

            case (0x1000):
                PC = opcode & 0x0FFF;
                return;

            case (0x2000):
                SP++;
                stack[SP] = PC;
                PC = opcode & 0x0FFF;
                return;

            case (0x3000):
                if (V[regX] == (opcode & 0x00FF))
                    PC += 2;
                PC += 2;
                return;

            case (0x4000):
                if (V[regX] != (opcode & 0x00FF))
                    PC += 2;
                PC += 2;
                return;

            case (0x5000):
                if (V[regX] == V[regY])
                    PC += 2;
                PC += 2;
                return;

            case (0x6000):
                V[regX] = opcode & 0x00FF;
                PC += 2;
                return;

            case (0x7000):
                V[regX] += opcode & 0x00FF;
                PC += 2;
                return;

            case (0x8000):
                switch (opcode & 0x000F) {

                    case (0x0000):
                        V[regX] = V[regY];
                        PC += 2;
                        return;

                    case (0x0001):
                        V[regX] = V[regX] | V[regY];
                        PC += 2;
                        return;

                    case (0x0002):
                        V[regX] = V[regX] & V[regY];
                        PC += 2;
                        return;

                    case (0x0003):
                        V[regX] = V[regX] ^ V[regY];
                        PC += 2;
                        return;

                    case (0x0004):
                        V[regX] = V[regX] + V[regY];
                        V[15] = (V[regX] > 255) ? 1 : 0;
                        V[regX] = V[regX] & 0x00FF;
                        PC += 2;
                        return;

                    case (0x0005):
                        V[15] = (V[regX] > V[regY]) ? 1 : 0;
                        V[regX] = (V[regX] - V[regY]) & 0xFF;
                        PC += 2;
                        return;

                    case (0x0006):
                        V[15] = V[regX] & 0x1;
                        V[regX] = V[regX] >>> 1;
                        PC += 2;
                        return;

                    case (0x0007):
                        V[15] = (V[regY] > V[regX]) ? 1 : 0;
                        V[regX] = (V[regY] - V[regX]) & 0xFF;
                        PC += 2;
                        return;

                    case (0x000E):
                        V[15] = (V[regX] & 0x80) >>> 7;
                        V[regX] = (V[regX] << 1) & 0xFF;
                        PC += 2;
                        return;
                }

            case (0x9000):
                if (V[regX] != V[regY]) {
                    PC += 2;
                }
                PC += 2;
                return;

            case (0xA000):
                I = opcode & 0x0FFF;
                PC += 2;
                return;

            case (0xB000):
                PC = (opcode & 0x0FFF) + V[0];
                return;

            case (0xC000):
                Random rand = new Random();
                V[regX] = rand.nextInt(256) & (opcode & 0xFF);  // Vx = a random byte ANDed with the value kk
                PC += 2;
                return;

            case (0xD000):
                int n = opcode & 0xF;
                V[15] = 0;
                for(int i = 0; i < n; i++) {
                    int temp = memory[I + i];
                    for(int j = 0; j < 8; j++) {
                        int pixel = temp & (0b10000000 >> j);
                        if(pixel == 1){
                            // Check for collision
                            if(display.getPixel((regX + i) % 64, (regY + j) % 32) == 1)
                                V[15] = 1;      // Set VF = to 1 if there is a collision
                            display.setPixel((regX + i) % 64, (regY + j) % 32);
                        }
                    }
                }
                dFlag = true;
                PC += 2;
                return;

        }

        switch(opcode & 0xF0FF) {
            // Opcode: SKP Vx
            case(0xE09E):
                if (keyPad[V[regX]])     // Skips next instruction if key number Vx is down
                    PC += 2;
                PC += 2;
                return;

            // Opcode: SKNP Vx
            case(0xE0A1):
                if (!keyPad[V[regX]])    // Skips next instruction if key number Vx is not down
                    PC += 2;
                PC += 2;
                return;

            // Opcode: LD Vx, K
            case (0xF007):
                V[regX] = DT;
                PC += 2;
                return;

            case (0xF00A):
                for(int i = 0; i < 16; i++){
                    if(keyPad[i]){
                        V[regX] = i;
                        PC += 2;
                        return;
                    }
                }
                return;

            case(0xF015):
                DT = V[regX];
                PC += 2;
                return;

            case(0xF018):
                ST = V[regX];
                PC += 2;
                return;

            case(0xF01E):
                I = I + V[regX];
                PC += 2;
                return;

            case(0xF029):
                I = V[regX] * 5;
                PC += 2;
                return;

            case(0xF033):
                int temp = V[regX];
                memory[I] = temp / 100;
                temp %= 100;
                memory[I + 1] = temp / 10;
                temp %= 10;
                memory[I + 2] = temp;

                PC += 2;
                return;

            case(0xF055):
                for(int i = 0; i < regX; i++){
                    memory[I + i] = V[i];
                }

                PC += 2;
                return;

            case(0xF065):
                for(int i = 0; i < regX; i++){
                    V[i] = memory[I + i];
                }

                PC += 2;
                return;
        }
    }

    public boolean loadRom(String romFile) {
        try(
                InputStream inputStream = new FileInputStream(romFile);
        ) {
            long fileSize = new File(romFile).length();
            byte[] romBuffer = new byte[(int) fileSize];

            inputStream.read(romBuffer);

            System.arraycopy(romBuffer, 0, memory, 0x200, (int)fileSize);
            return true;
        } catch( IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public Display getDisplay(){ return display; }
}
