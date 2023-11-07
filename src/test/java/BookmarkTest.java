import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.DEFAULT)
public class BookmarkTest {
    Bookmark bookmark = new Bookmark();

    @Before
    public void setUp() throws Exception {
        System.out.println("Start Testing");
    }
    @Test
    public void open() {
        bookmark.Open();
    }

    @Test
    public void lsTree() {
        bookmark.LsTree();
    }

    @Test
    public void showTree() {
        bookmark.ShowTree();
    }

    @Test
    public void addTitle() {
        bookmark.AddTitle();
    }

    @Test
    public void addBookmark() {
        bookmark.AddBookmark();
    }

    @Test
    public void reDo() {
        bookmark.ReDo();
    }z

    @Test
    public void deleteTitle() {
        bookmark.DeleteTitle();
    }

    @Test
    public void undo() {
        bookmark.Undo();
    }

    @Test
    public void readBookmark() {
        bookmark.ReadBookmark();
    }

    @Test
    public void save() {
        bookmark.Save();
    }
    @After
    public void tearDown() throws Exception {
        System.out.println("Ënd Test");
    }


}