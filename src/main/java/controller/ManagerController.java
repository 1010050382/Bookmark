package controller;

import constants.OptionConstants;
import constants.OptionEnum;
import entity.BookmarkNode;
import entity.Link;
import entity.Manager;
import entity.Option;
import exception.BookmarkFileFormatException;
import exception.ParseException;
import service.ManagerService;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManagerController {

//    @Autowired
//    ManagerService managerService;

    public static ManagerService managerService = ManagerService.getInstance();
    public static   Manager manager = Manager.getInstance();
    public static Pattern titlePattern = Pattern.compile("^#{1,3}\\s[A-Za-z1-9]+$");
    public static Pattern linkPattern = Pattern.compile("^\\[[A-Za-z1-9\\s]+]\\([a-zA-z]+://\\S*\\)$");



    /**
     * Description: 解析输入的命令字符串为String数组
     * date: 2022/10/21 15:01
     * @author: Haodong Li
     * @since: JDK 1.8
    */
    public static String[] parseInputCommand2StringArray(String input) throws ParseException {
        String[] inputArgs = input.replaceAll("-", "_").split(" ");
        if (inputArgs.length != 1 && inputArgs.length != 2 && inputArgs.length != 4) {
            throw new ParseException();
        }
        return inputArgs;
    }

    /**
     * Description: 将String数组解析为命令
     * date: 2022/10/21 15:17
     * @author: Haodong Li
     * @since: JDK 1.8
    */
    public static Option parseInputArgs2Option(String[] inputArgs) throws IllegalArgumentException, ParseException {
        int typeValue = OptionEnum.valueOf(inputArgs[0]).ordinal();
        int atTypeValue = OptionEnum.valueOf("at").ordinal();
        Option option = new Option(typeValue);
        option.setHasArg(OptionConstants.HAS_ARG[option.getType()]);
        if (option.getType() == atTypeValue) {
            throw new ParseException();
        }
        if (!option.isHasArg() && inputArgs.length > 1) {
            throw new ParseException();
        }
        if (option.isHasArg() && inputArgs.length < 2) {
            throw new ParseException();
        }
        if (option.isHasArg()) {
            option.addArg(inputArgs[1]);
        }
        if (inputArgs.length == 4) {
            typeValue = OptionEnum.valueOf(inputArgs[2]).ordinal();
            if (typeValue != atTypeValue) {
                throw new ParseException();
            }
            option.addArg(inputArgs[3]);
        }
        return option;
    }

    /**
     * Description: 执行命令
     * date: 2022/10/21 15:17
     * @author: Haodong Li
     * @since: JDK 1.8
    */
    public static void executeOption(Option option) {
        switch (option.getType()) {
            case 0:
                System.exit(0);
                break;
            case 1:
                managerService.ShowTree(manager.getBookmarkNodes());
                break;

                //TODO 1:  show tree，显示书签栏的树形结构
            case 2:
                //TODO 2:  ls tree，显示当前的文件目录结构 ？ （不是很确定）
            case 3:
                managerService.BackUpAddDelete();
                //managerService.AddTittle(option.getArgs().get(0),option.getArgs().size()>1?option.getArgs().get(1):"");
                managerService.AddTitle(option.getArgs().get(0),option.getArgs().size()>1?option.getArgs().get(1):"");
                break;
                //add-title "title" 直接在当前工作目录下添加title
                //add-title "title" at "title0" 在指定title下添加title，例如在1级title下添加2级title，在2级title下添加3级title
            case 5:
                managerService.BackUpAddDelete();
                String[] bookmark = option.getArgs().get(0).split("@");
                managerService.AddBookmark(option.getArgs().get(1),bookmark[0],bookmark[1]);
                break;
                //add-bookmark "bookmark" at "title" 在指定title下添加bookmark
            case 6:
                managerService.BackUpAddDelete();
                managerService.FindAndDeleteTitle(option.getArgs().get(0),manager.getBookmarkNodes());
                break;
                //delete title，删除title
                //若标题重复，需要删除所有重复标题，删除父节点需要删除所有子节点
            case 7:
                managerService.BackUpAddDelete();
                managerService.FindAndDeleteBookmark(option.getArgs().get(0),manager.getBookmarkNodes());
                break;
                //delete bookmark，删除bookmark
            case 8:
                manager.setMatchNode(null);
                manager.setLastCommand(null);
                manager.setHistoryMd(null);
                manager.setBookmarkNodes(null);
                managerService.parseMarkdownFile("D:/test.bmk");
                break;
                //打开文件后，当前工作目录为指定文件的书签栏，原本的工作空间放弃
                //如果文件不存在，需要新建文件
                //相当于执行case9 case10
                //即bookmark filepath
                // edit filepath
            case 9:
            case 10:
            case 11:
                managerService.SaveBookmark(option.getArgs().size()>0?option.getArgs().get(0):"");
                break;
                //save，保存当前工作空间的书签，覆盖原有文件，或者生成新文件
            case 12:
               managerService.Undo();
                break;
                //undo，撤销add或者delete
            case 13:
                managerService.Redo();
                //redo，撤销撤销add或者delete
            case 14:
                managerService.ReadBookmark(option.getArgs().get(0));
                //read bookmark，访问书签

            default:
                break;

        }

    }




}
