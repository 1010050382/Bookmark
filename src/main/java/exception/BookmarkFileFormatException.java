package exception;

public class BookmarkFileFormatException extends Exception{
    @Override
    public String toString() {
        return "please check file format!";
    }

    @Override
    public String getMessage() {
        return "please check file format!";
    }
}
