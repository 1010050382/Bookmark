import entity.Option;
import exception.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import service.ManagerService;

import java.util.Scanner;

import static controller.ManagerController.*;

public class Main {



    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input;
        while (scanner.hasNext()) {
            input = scanner.nextLine();
            try {
                String[] inputArgs = parseInputCommand2StringArray(input);
                Option option = parseInputArgs2Option(inputArgs);
                manager.setCurCommand(input);  //存储最后的操作
                executeOption(option);
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println(new ParseException().getMessage());
            }
        }
    }
}
