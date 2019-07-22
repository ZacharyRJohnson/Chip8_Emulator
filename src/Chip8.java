import java.util.Random;

public class Chip8 {

    private final int FOURKILOBYTES = 4096;

    private int memory[];

    private int V[];   // General purpose 8-bit registers
    private int I;     // 16 bit I register generally used for memory addresses
    private int DT,ST; // Delay and sound timers

    private int PC;   // Program Counter
    private int SP = -1;    // Stack Pointer
    private int stack[] = new int[16];

    private boolean display[][], dFlag;     // Display array, and the display flag

    private boolean keyPad[] = new boolean[16];

    public Chip8() {
        memory = new int[FOURKILOBYTES];
        V = new int[16];
        PC = 0x200;     // Start of most Chip-8 program
        display = new boolean[32][64];
    }

    public void executeOpcode() {
        int topByte = memory[PC];
        int bottomByte = memory[PC + 1];
        int opcode = (topByte << 8) | bottomByte;
        dFlag = false;

        int regX = opcode & 0x0F00 >>> 8;
        int regY = opcode & 0x00F0 >>> 4;

        switch(opcode){

            case(0x00E0):
                this.clearDisplay();
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
                //TODO Implement display opcode
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

    private void clearDisplay() {
        for(int i = 0; i < display.length; i++){
            for (int j = 0; j < display[i].length; j++){
                display[i][j] = false;
            }
        }
        dFlag = true;
    }
}
