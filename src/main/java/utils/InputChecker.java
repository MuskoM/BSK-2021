package utils;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.util.IllegalFormatException;
import java.util.regex.Pattern;

public class InputChecker extends Label {

    private Pattern checkerPattern;
    private Color correctInputColor = Color.GREEN;
    private Color incorrectInputColor = Color.RED;

    public InputChecker(String s, Pattern ptrn) {
        super(s);
        checkerPattern = ptrn;
    }

    public InputChecker(String s) {
        super(s);
    }

    public boolean isInputCorrect(String input) throws NullPointerException {
        if(checkerPattern.matcher(input).matches()) {
            this.setTextFill(correctInputColor);
            return true;
        }
        else{
            this.setTextFill(incorrectInputColor);
            return false;
        }
    }


    public void setCheckerPattern(Pattern checkerPattern) {
        this.checkerPattern = checkerPattern;
    }
}
