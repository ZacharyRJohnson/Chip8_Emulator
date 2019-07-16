public class Chip8 {

    private byte memory[];

    private byte V[];   // General purpose 8-bit registers
    private byte I;     // 16 bit I register generally used for memory addresses
    private byte DT,ST; // Delay and sound timers

    private short PC;   // Program Counter
    private byte SP;    // Stack Pointer
}
