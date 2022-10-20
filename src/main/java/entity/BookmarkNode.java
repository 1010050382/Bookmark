package entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class BookmarkNode implements Markdown {
    //title level
    private int level;
    //title name
    private String name;
    //next level titles list
    private List<BookmarkNode> nextLevelBookmarkNode;
    //links contained in current title
    private List<Link> links;

    public BookmarkNode(int level, String name) {
        this.level = level;
        this.name = name;
        this.nextLevelBookmarkNode = new ArrayList<>();
        this.links = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "#".repeat(Math.max(0, getLevel())) + " " + getName();
    }
}
