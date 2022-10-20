package entity;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;


@Data
public class Manager {
    private LinkedList<BookmarkNode> bookmarkNodes;

    private Manager() {
        bookmarkNodes = new LinkedList<>();
    }

    private static class ManagerInstance {
        private static final Manager INSTANCE = new Manager();
    }

    public void clearAllBookmark() {
        this.bookmarkNodes = new LinkedList<>();
    }

    public static Manager getInstance() {
        return ManagerInstance.INSTANCE;
    }


}
