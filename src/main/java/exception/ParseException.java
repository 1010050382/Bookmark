package exception;

public class ParseException extends Exception{
    @Override
    public String toString() {
        return "please enter correct format command!";
    }

    @Override
    public String getMessage() {
        return "please enter correct format command!";
    }


}
