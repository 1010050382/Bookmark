import entity.Option;
import exception.ParseException;

import static controller.ManagerController.*;
import static controller.ManagerController.manager;


public class Bookmark {
    public void Open(){
        Execute("open-file D:/test/test.bmk");
    }

    public void LsTree(){
        Execute("ls-tree");
    }

    public void ShowTree(){
        Execute("show-tree");
    }

    public void AddTitle(){
        Execute("add-title lhd");
    }

    public void AddBookmark(){
        Execute("add-bookmark lhd@lhd at test1");
    }

    public void ReDo(){
        Execute("Redo");
    }


    public void DeleteTitle(){
        Execute("delete-title test411");
    }

    public void DeleteBookmark(){
        Execute("delete-bookmark lhd");
    }

    public void Undo(){
       Execute("undo");
    }

    public void ReadBookmark(){
        Execute("read-bookmark elearning");
    }


    public void Save(){
        Execute("save");
    }

    public void Execute(String command){
        try {
            String[] inputArgs = parseInputCommand2StringArray(command);
            Option option = parseInputArgs2Option(inputArgs);
            executeOption(option);
            manager.setLastCommand(command);  //存储最后的操作
            managerService.ShowTree2(manager.getBookmarkNodes().getNextLevelBookmarkNode());
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(new ParseException().getMessage());
        }
    }
}
