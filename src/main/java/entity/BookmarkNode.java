package entity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
public class BookmarkNode implements Serializable {
    //title level
    private int level;
    //title name
    private String name;
    //next level titles list
    private List<BookmarkNode> nextLevelBookmarkNode;
    //links contained in current title
    private List<Link> links;

    public BookmarkNode(){
        this.level = 0;
        this.name = "";
        this.nextLevelBookmarkNode = new ArrayList<>();
        this.links = new ArrayList<>();
    }
    public BookmarkNode(int level, String name) {
        this.level = level;
        this.name = name;
        this.nextLevelBookmarkNode = new ArrayList<>();
        this.links = new ArrayList<>();
    }

    public void addNode(BookmarkNode node){
        nextLevelBookmarkNode.add(node);
    }

    public void addLink(Link link){
        links.add(link);
    }

    @Override
    public String toString() {
        return "#".repeat(Math.max(0, getLevel())) + " " + getName();
    }

    public BookmarkNode myClone() {

        BookmarkNode bookmarkNode = null;

        try {

            //创建一块内存来存放对象内容
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream ops = new ObjectOutputStream(bos);
            //将对象转换成二进制内容存入到开辟的内存中(序列化)
            ops.writeObject(this);

            //读取内存块中的二进制内容
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            //将二进制内容转换回对象 反序列化
            ObjectInputStream ois = new ObjectInputStream(bis);
            try {
                bookmarkNode = (BookmarkNode) ois.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookmarkNode;
    }
}
