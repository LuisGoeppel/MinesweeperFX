package Controller;

import java.io.*;

public class OptionsSaver {

    private String filePath;
    private FileWriter fileWriter;
    private FileReader fileReader;
    private BufferedReader reader;
    private PrintWriter writer;

    public OptionsSaver() {

        filePath = "src/main/resources/Files/SavedOptions";
        File file = new File(filePath);
        try {
            fileWriter = new FileWriter(file, true);
            fileReader = new FileReader(file);
            reader = new BufferedReader(fileReader);
            writer = new PrintWriter(fileWriter);
        } catch (IOException e) {
            System.out.println("OptionsSaver Error!");
        }
    }

    public OptionState readOptions() {
        boolean music, sound;
        short difficulty, fieldSize;
        try {
            String musicString = reader.readLine();
            music = toBoolean(musicString);
            String soundString = reader.readLine();
            sound = toBoolean(soundString);
            String difficultyString = reader.readLine();
            difficulty = Short.parseShort(difficultyString);
            String fieldSizeString = reader.readLine();
            fieldSize = Short.parseShort(fieldSizeString);

            return new OptionState(music, sound, difficulty, fieldSize);

        } catch (IOException | NullPointerException e) {
            System.out.println("Reading error");
        }
        return null;
    }

    public void saveOptions(OptionState state) {

        //delete old contents
        try {
            new FileWriter(filePath, false).close();
        } catch (IOException e) {
            System.out.println("Error while deleting Option content");
        }

        //write new contents
        writer.write(state.music + "\n");
        writer.write(state.sound + "\n");
        writer.write(state.difficulty + "\n");
        writer.write(state.fieldSize + "\n");
        writer.close();
    }

    private Boolean toBoolean(String input) {
        if (input.equalsIgnoreCase("true")) {
            return true;
        }
        return false;
    }
}
