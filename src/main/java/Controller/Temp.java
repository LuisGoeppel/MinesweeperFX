package Controller;

public class Temp {

    public static void main(String[] args) {
        OptionsSaver optionsSaver = new OptionsSaver();
        OptionState optionState = new OptionState(true, true, (short) 0, (short) 1);

        optionsSaver.saveOptions(optionState);
    }
}
