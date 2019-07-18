public class Chip8 {

    private final int FOURKILOBYTES = 4096;

    private short memory[];

    private int V[];   // General purpose 8-bit registers
    private short I;     // 16 bit I register generally used for memory addresses
    private short DT,ST; // Delay and sound timers

    private int PC;   // Program Counter
    private short SP = -1;    // Stack Pointer
    private int stack[] = new int[16];

    private boolean display[][], dFlag;     // Display array, and the display flag

    private boolean keyPad[] = new boolean[16];

    public Chip8() {
        memory = new short[FOURKILOBYTES];
        V = new int[16];
        PC = 0x200;     // Start of most Chip-8 program
        display = new boolean[32][64];
    }

    public void executeOpcode() {
        short topByte = memory[PC];
        short bottomByte = memory[PC + 1];
        int opcode = (topByte << 8) | bottomByte;
        dFlag = false;

        int regX, regY;

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

        switch(opcode & 0xF000){

            case(0x1000):
                PC = opcode & 0x0FFF;
                return;
                
            case(0x2000):
                SP++;
                stack[SP] = PC;
                PC = opcode & 0x0FFF;
                return;
                
            case(0x3000):
                regX = opcode & 0x0F00 >>> 8;
                if(V[regX] == (opcode & 0x00FF))
                    PC += 2;
                PC += 2;
                return;
                
            case(0x4000):
                regX = opcode & 0x0F00 >>> 8;
                if(V[regX] != (opcode & 0x00FF))
                    PC += 2;
                PC += 2;
                return;

            case(0x5000):
                regX = opcode & 0x0F00 >>> 8;
                regY = opcode & 0x00F0 >>> 4;
                if(V[regX] == V[regY])
                    PC += 2;
                PC += 2;
                return;

            case(0x6000):
                regX = opcode & 0x0F00 >>> 8;
                V[regX] = opcode & 0x00FF;
                PC += 2;
                return;

            case(0x7000):
                regX = opcode & 0x0F00 >>> 8;
                V[regX] += opcode & 0x00FF;
                PC += 2;
                return;

            case(0x8000):
                regX = opcode & 0x0F00 >>> 8;
                regY = opcode & 0x00F0 >>> 4;
                switch(opcode & 0x000F){

                    case(0x0000):
                        V[regX] = V[regY];
                        PC += 2;
                        return;

                    case(0x0001):
                        V[regX] = V[regX] | V[regY];
                        PC += 2;
                        return;

                    case(0x0002):
                        V[regX] = V[regX] & V[regY];
                        PC += 2;
                        return;

                    case(0x0003):
                        V[regX] = V[regX] ^ V[regY];
                        PC += 2;
                        return;

                    case(0x0004):
                        V[regX] = V[regX] + V[regY];
                        V[15] = (V[regX] > 255) ? 1: 0;
                        V[regX] = V[regX] & 0x00FF;
                        PC += 2;
                        return;

                    case(0x0005):
                        V[15] = (V[regX] > V[regY]) ? 1: 0;
                        V[regX] = (V[regX] - V[regY]) & 0xFF;
                        PC += 2;
                        return;

                    case(0x0006):
                        V[15] = V[regX] & 0x1;
                        V[regX] = V[regX] >>> 1;
                        PC += 2;
                        return;

                    case(0x0007):
                        V[15] = (V[regY] > V[regX]) ? 1: 0;
                        V[regX] = (V[regY] - V[regX]) & 0xFF;
                        PC += 2;
                        return;

                    case(0x000E):
                        V[15] = (V[regX] & 0x80) >>> 7;
                        V[regX] = (V[regX] << 1) & 0xFF;
                        PC += 2;
                        return;
                }
        }



    }

    private void clearDisplay() {
        for(int i = 0; i < display.length; i++){
            for (int j = 0; j < display[i].length; j++){
                display[i][j] = false;
                dFlag = true;
            }
        }
    }
}
