package constants;

public class OptionConstants {
    private static final boolean EXIT_HAS_ARG = false;
    private static final boolean SHOW_TREE_HAS_ARG = false;
    private static final boolean LS_TREE_HAS_ARG = false;
    private static final boolean ADD_TITLE_HAS_ARG = true;
    private static final boolean AT_HAS_ARG = true;
    private static final boolean ADD_BOOKMARK_HAS_ARG = true;
    private static final boolean DELETE_TITLE_HAS_ARG = true;
    private static final boolean DELETE_BOOKMARK_HAS_ARG = true;
    private static final boolean OPEN_FILE_HAS_ARG = true;
    private static final boolean CREATE_FILE_HAS_ARG = true;
    private static final boolean EDIT_FILE_HAS_ARG = true;
    private static final boolean SAVE_HAS_ARG = true;
    private static final boolean UNDO_HAS_ARG = false;
    private static final boolean REDO_HAS_ARG = false;
    private static final boolean READ_BOOKMARK_HAS_ARG = true;

    public static boolean[] HAS_ARG = new boolean[]{EXIT_HAS_ARG, SHOW_TREE_HAS_ARG, LS_TREE_HAS_ARG, ADD_TITLE_HAS_ARG,
            AT_HAS_ARG, ADD_BOOKMARK_HAS_ARG, DELETE_TITLE_HAS_ARG, DELETE_BOOKMARK_HAS_ARG, OPEN_FILE_HAS_ARG,
            CREATE_FILE_HAS_ARG, EDIT_FILE_HAS_ARG, SAVE_HAS_ARG, UNDO_HAS_ARG, REDO_HAS_ARG, READ_BOOKMARK_HAS_ARG};


    private static final String EXIT_DESCRIPTION = "Exit";
    private static final String SHOW_TREE_DESCRIPTION = "Display the tree structure of the bookmark bar";
    private static final String LS_TREE_DESCRIPTION = "Display the current file directory structure";
    private static final String ADD_TITLE_DESCRIPTION = "Add title in bookmark";
    private static final String AT_DESCRIPTION = "Add to where";
    private static final String ADD_BOOKMARK_DESCRIPTION = "Add link in bookmark";
    private static final String DELETE_TITLE_DESCRIPTION = "Delete title in bookmark";
    private static final String DELETE_BOOKMARK_DESCRIPTION = "Delete link in bookmark";
    private static final String OPEN_FILE_DESCRIPTION = "Open bookmark file";
    private static final String CREATE_FILE_DESCRIPTION = "New bookmark file";
    private static final String EDIT_FILE_DESCRIPTION = "Edit bookmark file";
    private static final String SAVE_DESCRIPTION = "Save bookmark file";
    private static final String UNDO_DESCRIPTION = "Undo edit";
    private static final String REDO_DESCRIPTION = "Redo edit";
    private static final String READ_BOOKMARK_DESCRIPTION = "Visit link";

    public static String[] DESCRIPTION = new String[]{EXIT_DESCRIPTION, SHOW_TREE_DESCRIPTION, LS_TREE_DESCRIPTION,
            ADD_TITLE_DESCRIPTION, AT_DESCRIPTION, ADD_BOOKMARK_DESCRIPTION, DELETE_TITLE_DESCRIPTION,
            DELETE_BOOKMARK_DESCRIPTION, OPEN_FILE_DESCRIPTION, CREATE_FILE_DESCRIPTION, EDIT_FILE_DESCRIPTION,
            SAVE_DESCRIPTION, UNDO_DESCRIPTION, REDO_DESCRIPTION, READ_BOOKMARK_DESCRIPTION};
}
