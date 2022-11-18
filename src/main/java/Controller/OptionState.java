package Controller;

public class OptionState {
    public boolean music;
    public boolean sound;
    public short difficulty;
    public short fieldSize;

    public OptionState(boolean music, boolean sound, short difficulty, short fieldSize) {
        this.music = music;
        this.sound = sound;
        this.difficulty = difficulty;
        this.fieldSize = fieldSize;
    }
}
