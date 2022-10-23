package exception;

/**
 * @author Haodong Li
 * @date 2022年10月23日 20:43
 */
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
