package entity;

import java.util.LinkedList;
import java.util.List;
import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class Manager {
    private BookmarkNode bookmarkNodes;

    private BookmarkNode lastLoadedNode;
    private LinkedList<BookmarkNode> historyMd;
    private LinkedList<BookmarkNode> matchNode;

    private LinkedList<Link> matchLink;
    private String lastCommand;
    private String filePath;



    private StringBuilder saveStr;
    private Manager() {
        bookmarkNodes = new BookmarkNode(0,"root");
        historyMd = new LinkedList<>();
        matchNode = new LinkedList<>();
        saveStr = new StringBuilder();
        lastCommand = "";
        filePath ="";
    }

    private static class ManagerInstance {
        private static final Manager INSTANCE = new Manager();
    }

    public void clearAllBookmark() {
        this.bookmarkNodes = new BookmarkNode(0,"root");;
    }

    public void setBookmarkNodes(BookmarkNode node){
        this.bookmarkNodes = node;
    }

    //历史操作记录

    public static Manager getInstance() {
        return ManagerInstance.INSTANCE;
    }




}
