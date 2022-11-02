package controller;

import constants.OptionConstants;
import constants.OptionEnum;
import entity.BookmarkNode;
import entity.Link;
import entity.Option;
import exception.BookmarkFileFormatException;
import exception.ParseException;
import service.ManagerService;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManagerController {

    public static ManagerService managerService = ManagerService.getInstance();

    public static Pattern titlePattern = Pattern.compile("^#{1,3}\\s[A-Za-z1-9]+$");
    public static Pattern linkPattern = Pattern.compile("^\\[[A-Za-z1-9\\s]+]\\([a-zA-z]+://\\S*\\)$");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input;
        while (scanner.hasNext()) {
            input = scanner.nextLine();
            try {
                String[] inputArgs = parseInputCommand2StringArray(input);
                Option option = parseInputArgs2Option(inputArgs);
                executeOption(option);
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println(new ParseException().getMessage());
            }
        }
    }

    public static String[] parseInputCommand2StringArray(String input) throws ParseException {
        String[] inputArgs = input.replaceAll("-", "_").split(" ");
        if (inputArgs.length != 1 && inputArgs.length != 2 && inputArgs.length != 4) {
            throw new ParseException();
        }
        return inputArgs;
    }

    public static Option parseInputArgs2Option(String[] inputArgs) throws IllegalArgumentException, ParseException {
        int typeValue = OptionEnum.valueOf(inputArgs[0]).ordinal();
        int atTypeValue = OptionEnum.valueOf("at").ordinal();
        int saveTypeValue = OptionEnum.valueOf("save").ordinal();
        Option option = new Option(typeValue);
        option.setHasArg(OptionConstants.HAS_ARG[option.getType()]);
        if (option.getType() == atTypeValue) {
            throw new ParseException();
        }
        if (!option.isHasArg() && inputArgs.length > 1) {
            throw new ParseException();
        }
        if (option.isHasArg() && inputArgs.length < 2 && option.getType() != saveTypeValue) {
            throw new ParseException();
        }
        if (option.isHasArg()) {
            option.addArg(inputArgs[inputArgs.length - 1]);
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

    public static void executeOption(Option option) {
        switch (option.getType()) {
            case 0:
                System.exit(0);
                break;
            case 1:
                //TODO 1:  show tree，显示书签栏的树形结构
            case 2:
                //TODO 2:  ls tree，显示当前的文件目录结构 ？ （不是很确定）
            case 3:
                //TODO 3:  add title，添加标题
                //add-title "title" 直接在当前工作目录下添加title
                //add-title "title" at "title0" 在指定title下添加title，例如在1级title下添加2级title，在2级title下添加3级title
            case 5:
                //TODO 4:  add bookmark，添加书签链接
                //add-bookmark "bookmark" at "title" 在指定title下添加bookmark
            case 6:
                //TODO 5:  delete title，删除title
                //若标题重复，需要删除所有重复标题，删除父节点需要删除所有子节点
            case 7:
                //TODO 6:  delete bookmark，删除bookmark
            case 8:
                //TODO 7:  open，打开指定路径书签文件
                //打开文件后，当前工作目录为指定文件的书签栏，原本的工作空间放弃
                //如果文件不存在，需要新建文件
                //相当于执行case9 case10
                //即bookmark filepath
                // edit filepath
            case 9:
            case 10:
            case 11:
                //TODO 8:  save，保存当前工作空间的书签，覆盖原有文件，或者生成新文件
            case 12:
                //TODO 9:  undo，撤销add或者delete
            case 13:
                //TODO 10:  redo，撤销撤销add或者delete
            case 14:
                //TODO 11:  read bookmark，访问书签

            default:
                break;
        }
    }


    public void parseMarkdownFile(String filePath) {
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

    public String parseMarkdownFileLine(String line) throws BookmarkFileFormatException {
        Matcher titleMatcher = titlePattern.matcher(line);
        Matcher linkMatcher = linkPattern.matcher(line);
        if (titleMatcher.matches()) {
            String[] titleInfo = line.split(" ");
            BookmarkNode bookmarkNode = new BookmarkNode(titleInfo[0].length(), titleInfo[1]);
            //TODO 解析到文件中的标题，需要添加到工作目录中
        } else if (linkMatcher.matches()) {
            String name = line.substring(1, line.indexOf("]"));
            String url = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
            Link link = new Link(name, url);
            //TODO 解析到文件中的链接，需要添加到工作目录中的标题下
        } else {
            throw new BookmarkFileFormatException();
        }
        return null;
    }
}
