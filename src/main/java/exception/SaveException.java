package exception;


public class SaveException extends Exception{
    @Override
    public String toString() {
        return "please enter path!";
    }

    @Override
    public String getMessage() {
        return "please enter path!";
    }


}
