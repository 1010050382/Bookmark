package service;

import Utils.CloneUtils;
import Utils.SaveUtils;
import entity.BookmarkNode;
import entity.Link;
import entity.Manager;
import entity.Option;
import exception.BookmarkFileFormatException;
import exception.ParseException;
import exception.SaveException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static controller.ManagerController.*;

@Service
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


    public void ShowTree2(List<BookmarkNode> nodes){
        int size = nodes.size();
        for(int i=0;i<size;i++){
            if(i==size-1){
                System.out.print("   ".repeat(nodes.get(i).getLevel()));
                System.out.println("└──"+nodes.get(i).getName());
            }else {
                System.out.print("   ".repeat(nodes.get(i).getLevel()));
                System.out.println("├──"+nodes.get(i).getName());
            }
            for(int j=0;j<nodes.get(i).getLinks().size();j++){
                if(j==nodes.get(i).getLinks().size()-1){
                    System.out.print("   ".repeat(nodes.get(i).getLevel()+1));
                    System.out.println("└──"+(nodes.get(i).getLinks().get(j).getStatus()==true?"*":"") +nodes.get(i).getLinks().get(j).toString());
                }else {
                    System.out.print("   ".repeat(nodes.get(i).getLevel()+1));
                    System.out.println("├──"+(nodes.get(i).getLinks().get(j).getStatus()==true?"*":"") +nodes.get(i).getLinks().get(j).toString());
                }
            }
            ShowTree2(nodes.get(i).getNextLevelBookmarkNode());
        }
    }

    public void LsTree(){
        if(manager.getFilePath().isEmpty()==true)
            System.out.println("当前不是打开的文件");
        else{

            File file =new File(manager.getFilePath());
            String absolutePath = file.getAbsolutePath();
            String filePath = absolutePath.
                    substring(0,absolutePath.lastIndexOf(File.separator));
            printFile(new File(filePath),1);
        }
            //ShowTree2(manager.getHistoryMd().getFirst().getNextLevelBookmarkNode());
    }

    public void printFile(File file,int tab) {
        if (file.isFile()) {
            System.out.println("您给定的是一个文件"); // 判断给定目录是否是一个合法的目录，如果不是，输出提示
        } else {
            File[] fileLists = file.listFiles(); // 如果是目录，获取该目录下的内容集合

            for (int i = 0; i < fileLists.length; i++) { // 循环遍历这个集合内容
                for (int j = 0; j < tab; j++) {        //输出缩进
                    System.out.print("|---");
                }

                System.out.println(fileLists[i].getName());    //输出元素名称

                if (fileLists[i].isDirectory()) {    //判断元素是不是一个目录
                    printFile(fileLists[i], tab + 1);    //如果是目录，继续调用本方法来输出其子目录，因为是其子目录，所以缩进次数 + 1
                }
            }
        }
    }


    public void ReadBookmark(String name){
        manager.setMatchLink(new LinkedList<>());
        FindBookmark(name,manager.getBookmarkNodes());
        if(manager.getMatchLink().size()>0){
            manager.getMatchLink().getFirst().setStatus(true);
        }
    }


    public void SaveBookmark(String path){
        try {
            GenrateSaveStr(manager.getBookmarkNodes().getNextLevelBookmarkNode());
            if(!path.isEmpty()){
                SaveUtils.TextToFile(path,manager.getSaveStr().toString());
                //System.out.println(manager.getSaveStr());
            }else {
                if(manager.getFilePath().isEmpty())
                    throw  new SaveException();
                SaveUtils.TextToFile(manager.getFilePath(),manager.getSaveStr().toString());
                //System.out.println(manager.getSaveStr());
            }
        } catch (SaveException e) {
            System.out.println(e.getMessage());
        }
    }

    public void GenrateSaveStr(List<BookmarkNode> nodes){
       for(BookmarkNode item : nodes){
           manager.getSaveStr().append("#".repeat(item.getLevel()-1)+" "+item.getName()+"\r\n");  //Save Title
           for (Link link:item.getLinks())
               manager.getSaveStr().append("[" + link.getName() + "]" + "(" + link.getUrl() + ")"+"\r\n");  //Save Bookmark
           GenrateSaveStr(item.getNextLevelBookmarkNode());
       }
    }

    public void AddBookmark(String arg1,String arg2,String arg3){
        if(arg1.isEmpty())
        {
            manager.getBookmarkNodes().getNextLevelBookmarkNode().add(new BookmarkNode(1,arg1));
        }else {
           FindTitle(arg1,manager.getBookmarkNodes().getNextLevelBookmarkNode());
           if(manager.getMatchNode().size()!=0)
               manager.getMatchNode().getFirst().addLink(new Link(arg2,arg3));
        }
    }

    /**
     * Description: arg1 = name arg2 = url arg3 = target
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



    public void AddTittle(String arg1,String arg2){
        if(arg2.isEmpty())
        {
            manager.getBookmarkNodes().getNextLevelBookmarkNode().add(new BookmarkNode(1,arg1));
        }else {
            FindAndAddTitle(arg2,manager.getBookmarkNodes().getNextLevelBookmarkNode(),arg1);
        }

    }


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



    public void FindAndDeleteBookmark(String name ,BookmarkNode allBookmarkNode){
        allBookmarkNode.getLinks().removeIf(x -> x.getName().equals(name));

        for (BookmarkNode item : allBookmarkNode.getNextLevelBookmarkNode()){
            FindAndDeleteBookmark(name,item);
        }
    }



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

    public void LoadTitle(Integer level,String name){
        manager.setMatchNode(new LinkedList<>());
        FindAllTitleByLevel(level-1,manager.getBookmarkNodes());
        if(manager.getMatchNode().size()>0){
            BookmarkNode node = new BookmarkNode(level,name);
             manager.setLastLoadedNode(node);
            manager.getMatchNode().getLast().addNode(node);
        }

    }


    public void LoadBookmark(Link link){
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


    public void BackUpAddDelete(){
        try {
            String a = manager.getCurCommand();
            manager.setLastCommand(a);
            BookmarkNode bookmarkNode = CloneUtils.clone(manager.getBookmarkNodes());
            manager.getHistoryMd().add(bookmarkNode);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


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


    public void Undo(){
        if(manager.getHistoryMd()!=null&&manager.getHistoryMd().size()!=0){
            if(manager.getHistoryMd().size()>1){
                manager.getHistoryMd().removeLast();
                BookmarkNode lastNode = manager.getHistoryMd().getLast();
                manager.setBookmarkNodes(lastNode);
            }

        }else {

        }
    }

    public void FindAllTitleByLevel(Integer level,BookmarkNode allNodes){
        if(allNodes.getLevel()==level)
            manager.getMatchNode().add(allNodes);
        if(allNodes.getNextLevelBookmarkNode().size()!=0&&allNodes.getLevel()<level)
            for (BookmarkNode node : allNodes.getNextLevelBookmarkNode())
                FindAllTitleByLevel(level,node);
    }



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
            manager.getHistoryMd().add(manager.getBookmarkNodes());
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
