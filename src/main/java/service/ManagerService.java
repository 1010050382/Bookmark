package service;

import Utils.CloneUtils;
import entity.BookmarkNode;
import entity.Link;
import entity.Manager;
import entity.Option;
import exception.BookmarkFileFormatException;
import exception.ParseException;
import exception.SaveException;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static controller.ManagerController.*;

public class ManagerService {

    private static  Boolean isFind=false;
    private Manager manager;

    private ManagerService() {
        manager = Manager.getInstance();
    }

    public static Pattern titlePattern = Pattern.compile("^#{1,3}\\s[A-Za-z1-9]+$");
    public static Pattern linkPattern = Pattern.compile("^\\[[A-Za-z1-9\\s]+]\\([a-zA-z]+://\\S*\\)$");

    private static class ManagerServiceInstance {
        private static final ManagerService INSTANCE = new ManagerService();
    }

    public static ManagerService getInstance() {
        return ManagerServiceInstance.INSTANCE;
    }

    /**
     * Description: 展示树结构
     * date: 2022/10/22 19:40
     * @author: Haodong Li
     * @since: JDK 1.8
    */
    public void ShowTree(BookmarkNode bookmarkNode){

        System.out.print("#".repeat(bookmarkNode.getLevel()));
        System.out.println(bookmarkNode.getName());
        for (Link item:bookmarkNode.getLinks()) {
            System.out.println(" ".repeat(bookmarkNode.getLevel())+ (item.getStatus()==true?"*":"") +item.toString());
        }
        for (BookmarkNode item: bookmarkNode.getNextLevelBookmarkNode()) {
            ShowTree(item);
        }

    }

    public void ReadBookmark(String name){
        manager.setMatchLink(new LinkedList<>());
        FindBookmark(name,manager.getBookmarkNodes());
        if(manager.getMatchLink().size()>0){
            manager.getMatchLink().getFirst().setStatus(true);
        }
    }
    /**
     * Description: 保存字符串
     * date: 2022/10/23 20:50
     * @author: Haodong Li
     * @since: JDK 1.8
    */

    public void SaveBookmark(String path){
        try {
            GenrateSaveStr(manager.getBookmarkNodes().getNextLevelBookmarkNode());
            if(!path.isEmpty()){
                System.out.println(manager.getSaveStr());
            }else {
                if(manager.getFilePath().isEmpty())
                    throw  new SaveException();
                System.out.println(manager.getSaveStr());
            }
        } catch (SaveException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Description: 生成保存字符串
     * date: 2022/10/23 20:50
     * @author: Haodong Li
     * @since: JDK 1.8
    */
    public void GenrateSaveStr(List<BookmarkNode> nodes){
       for(BookmarkNode item : nodes){
           manager.getSaveStr().append("#".repeat(item.getLevel())+" "+item.getName()+"\r\n");  //Save Title
           for (Link link:item.getLinks())
               manager.getSaveStr().append("[" + link.getName() + "]" + "(" + link.getUrl() + ")"+"\r\n");  //Save Bookmark
           GenrateSaveStr(item.getNextLevelBookmarkNode());
       }
    }

    /**
     * Description: Add 操作,arg1=名称 ,arg2=链接, arg3 = at后的位置
     * root节点不存储任何信息，只为了遍历方便
     * date: 2022/10/21 21:11
     * @author: Haodong Li
     * @since: JDK 1.8
    */
    public void AddBookmark(String arg1,String arg2,String arg3){
        if(arg1.isEmpty())
        {
            manager.getBookmarkNodes().getNextLevelBookmarkNode().add(new BookmarkNode(1,arg1));
        }else {
           FindTitle(arg3,manager.getBookmarkNodes().getNextLevelBookmarkNode());
           if(manager.getMatchNode().size()!=0)
               manager.getMatchNode().getFirst().addLink(new Link(arg1,arg2));
        }
    }

    /**
     * Description: arg1 = name arg2 = url arg3 = target
     * date: 2022/10/23 17:43
     * @author: Haodong Li
     * @since: JDK 1.8

    */
    public void AddLastBookmark(String arg1,String arg2,String arg3){
        if(arg3.isEmpty())
        {
            manager.getBookmarkNodes().getNextLevelBookmarkNode().add(new BookmarkNode(1,arg1));
        }else {
            FindTitle(arg3,manager.getBookmarkNodes().getNextLevelBookmarkNode());
            if(manager.getMatchNode().size()!=0)
                manager.getMatchNode().getLast().addLink(new Link(arg1,arg2));
        }
    }

    /**
     * Description: arg1 = name agr2 = 父节点名称
     * date: 2022/10/22 15:13
     *
     * @author: Haodong Li
     * @since: JDK 1.8
     */

    public void AddTittle(String arg1,String arg2){
        if(arg2.isEmpty())
        {
            manager.getBookmarkNodes().getNextLevelBookmarkNode().add(new BookmarkNode(1,arg1));
        }else {
            FindAndAddTitle(arg2,manager.getBookmarkNodes().getNextLevelBookmarkNode(),arg1);
        }

    }

    /**
     * Description: 递归遍历并添加链接
     * date: 2022/10/22 21:04
     * @author: Haodong Li
     * @since: JDK 1.8

    */
    public void FindAndAddBookmark(String name ,List<BookmarkNode> allBookmarkNode,String arg1,String arg2){
        for (BookmarkNode item : allBookmarkNode){
            if(item.getName().equals(name)){
                item.addLink(new Link(arg1,arg2));
                return;
            }else if(item.getNextLevelBookmarkNode()!=null){
                FindAndAddBookmark(name,item.getNextLevelBookmarkNode(),arg1,arg2);
            }

        }
    }
    /**
     * Description: 递归遍历并添加标题
     * date: 2022/10/22 21:04
     * @author: Haodong Li
     * @since: JDK 1.8

     */
    public void FindAndAddTitle(String name ,List<BookmarkNode> allBookmarkNode,String arg1){
        for (BookmarkNode item : allBookmarkNode){
            if(item.getName().equals(name)){
                item.getNextLevelBookmarkNode().add(new BookmarkNode(item.getLevel()+1,arg1));
                return;
            }else if(item.getNextLevelBookmarkNode()!=null){
                FindAndAddTitle(name,item.getNextLevelBookmarkNode(),arg1);
            }

        }
    }

    /**
     * Description: 递归遍历并删除所有匹配的书签
     * date: 2022/10/22 21:04
     * @author: Haodong Li
     * @since: JDK 1.8

     */
    public void FindAndDeleteBookmark(String name ,BookmarkNode allBookmarkNode){
        allBookmarkNode.getLinks().removeIf(x -> x.getName().equals(name));

        for (BookmarkNode item : allBookmarkNode.getNextLevelBookmarkNode()){
            FindAndDeleteBookmark(name,item);
        }
    }

    /**
     * Description: 递归遍历并删除所有匹配的标题
     * date: 2022/10/22 21:04
     * @author: Haodong Li
     * @since: JDK 1.8

     */
    public void FindAndDeleteTitle(String name ,BookmarkNode allBookmarkNode){
        allBookmarkNode.getNextLevelBookmarkNode().removeIf(x->x.getName().equals(name));

        for (BookmarkNode item : allBookmarkNode.getNextLevelBookmarkNode()){
            FindAndDeleteTitle(name,item);
        }
    }


    public List<BookmarkNode> GetNodeByName(String name,List<BookmarkNode> allBookmarkNode){
        //子菜单
        List<BookmarkNode> childList = new ArrayList<>();
        for (BookmarkNode nav : allBookmarkNode) {
            // 遍历所有节点，将所有菜单的父id与传过来的根节点的id比较
            //相等说明：为该根节点的子节点。
            if (nav.getName().equals(name)){
                childList.add(nav);
            }
        }
        //递归
        for (BookmarkNode nav : childList) {
            nav.setNextLevelBookmarkNode(GetNodeByName(nav.getName(), allBookmarkNode));
        }

        //如果节点下没有子节点，返回一个空List（递归退出）
        if (childList.size() == 0 ){
            return new ArrayList<BookmarkNode>();
        }
        return childList;
    }

    public void AddTitle(String name,String targetTitle){
        manager.setMatchNode(new LinkedList<>());
        if(targetTitle==""||targetTitle.isEmpty()){
            manager.getBookmarkNodes().getNextLevelBookmarkNode().add(new BookmarkNode(1,name));
        }else {
            FindTitle(targetTitle,manager.getBookmarkNodes().getNextLevelBookmarkNode());
            if(manager.getMatchNode().size()!=0)
                manager.getMatchNode().get(0).addNode(new BookmarkNode(manager.getMatchNode().get(0).getLevel()+1,name));
        }
    }

    /**
     * Description: 载入Title，并保存最新的节点
     * date: 2022/10/23 18:19
     * @author: Haodong Li
     * @since: JDK 1.8
    */
    public void LoadTitle(Integer level,String name){
        manager.setMatchNode(new LinkedList<>());
        FindAllTitleByLevel(level-1,manager.getBookmarkNodes());
        if(manager.getMatchNode().size()>0){
            BookmarkNode node = new BookmarkNode(level,name);
             manager.setLastLoadedNode(node);
            manager.getMatchNode().getLast().addNode(node);
        }

    }

    /**
     * Description: 载入书签链接，并保存到最近的Title
     * date: 2022/10/23 18:55
     * @author: Haodong Li
     * @since: JDK 1.8

    */
    public void LoadBookmark(Link link){
//        if(manager.getMatchNode().size()>0)
//            manager.getMatchNode().getLast().addLink(link);
        if(manager.getLastLoadedNode()!=null)
            manager.getLastLoadedNode().addLink(link);
    }


    public void AddLastTitle(String name,String targetTitle){
        manager.setMatchNode(new LinkedList<>());
        if(targetTitle==""||targetTitle.isEmpty()){
            manager.getBookmarkNodes().getNextLevelBookmarkNode().add(new BookmarkNode(1,name));
        }else {
            FindTitle(targetTitle,manager.getBookmarkNodes().getNextLevelBookmarkNode());
            if(manager.getMatchNode().size()!=0)
                manager.getMatchNode().get(0).addNode(new BookmarkNode(manager.getMatchNode().getLast().getLevel()+1,name));
        }
    }

    /**
     * Description: 查找所有匹配的标题并添加到manger.matchNode
     * date: 2022/10/23 14:38
     * @author: Haodong Li
     * @since: JDK 1.8
    */
    public void FindTitle(String name,List<BookmarkNode> allBookmarkNode){
        for (BookmarkNode item : allBookmarkNode){
            if(item.getName().equals(name)){
                manager.getMatchNode().add(item);
                return;
            }else if(item.getNextLevelBookmarkNode()!=null){
                FindTitle(name,item.getNextLevelBookmarkNode());
            }

        }
    }

    /**
     * Description:  查找所有匹配的Link并添加到manger.matchLink
     * date: 2022/10/23 15:30
     * @author: Haodong Li
     * @since: JDK 1.8
    */
    public void FindBookmark(String name,BookmarkNode allBookmarkNode){
        for(Link link:allBookmarkNode.getLinks()){
            if(link.getName().equals(name))
                manager.getMatchLink().add(link);
        }
        if(allBookmarkNode.getNextLevelBookmarkNode().size()!=0)
            for (BookmarkNode node:allBookmarkNode.getNextLevelBookmarkNode()){
                FindBookmark(name,node);
            }
    }

    /**
     * Description: 保存增删操作以支持redo/undo
     * date: 2022/10/23 13:16
     * @author: Haodong Li
     * @since: JDK 1.8

    */
    public void BackUpAddDelete(){
        try {
            BookmarkNode bookmarkNode = CloneUtils.clone(manager.getBookmarkNodes());
            manager.getHistoryMd().add(bookmarkNode);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Description: 重做
     * date: 2022/10/23 18:54
     * @author: Haodong Li
     * @since: JDK 1.8

    */
    public void Redo(){
        if(manager.getLastCommand()==null|| manager.getLastCommand().isEmpty()){
            return;
        }else {
            try {
                String[] inputArgs = parseInputCommand2StringArray(manager.getLastCommand());
                Option option = parseInputArgs2Option(inputArgs);
                executeOption(option);
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println(new ParseException().getMessage());
            }
        }


    }

    /**
     * Description: 撤销
     * date: 2022/10/23 18:54
     * @author: Haodong Li
     * @since: JDK 1.8

    */
    public void Undo(){
        if(manager.getHistoryMd().size()!=0){
            if(manager.getHistoryMd().size()>1){
                manager.getHistoryMd().removeLast();
                BookmarkNode lastNode = manager.getHistoryMd().getLast();
                manager.setBookmarkNodes(lastNode);
            }

        }else {

        }
    }

    /**
     * Description: 找到所有对应level的节点
     * date: 2022/10/23 18:20
     * @author: Haodong Li
     * @since: JDK 1.8

    */
    public void FindAllTitleByLevel(Integer level,BookmarkNode allNodes){
        if(allNodes.getLevel()==level)
            manager.getMatchNode().add(allNodes);
        if(allNodes.getNextLevelBookmarkNode().size()!=0&&allNodes.getLevel()<level)
            for (BookmarkNode node : allNodes.getNextLevelBookmarkNode())
                FindAllTitleByLevel(level,node);
    }


    /**
     * Description: 读取md文件
     * date: 2022/10/21 15:18
     * @author: Haodong Li
     * @since: JDK 1.8
     */
    public void parseMarkdownFile(String filePath) {
        manager.setFilePath(filePath);
        manager.setBookmarkNodes(new BookmarkNode(0, "root"));
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(filePath));
            String line = bufferedReader.readLine();

            while (line != null) {
                parseMarkdownFileLine(line);
                line = bufferedReader.readLine();
            }
        } catch (FileNotFoundException e) {
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException ioException) {
                System.out.println("Failed to create file!");
            }
        } catch (IOException e) {
            System.out.println("something wrong");
        } catch (BookmarkFileFormatException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                System.out.println("something wrong");
            }

        }
    }

    /**
     * Description: 解析行
     * date: 2022/10/21 15:18
     * @author: Haodong Li
     * @since: JDK 1.8
     */
    public String parseMarkdownFileLine(String line) throws BookmarkFileFormatException {
        Matcher titleMatcher = titlePattern.matcher(line);
        Matcher linkMatcher = linkPattern.matcher(line);
        if (titleMatcher.matches()) {
            String[] titleInfo = line.split(" ");
            BookmarkNode bookmarkNode = new BookmarkNode(titleInfo[0].length(), titleInfo[1]);
            managerService.LoadTitle(titleInfo[0].length(), titleInfo[1]);
        } else if (linkMatcher.matches()) {
            String name = line.substring(1, line.indexOf("]"));
            String url = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
            Link link = new Link(name, url);
            managerService.LoadBookmark(link);
            //managerService.AddLastBookmark(name,url,);
        } else {
            throw new BookmarkFileFormatException();
        }
        return null;
    }



}
