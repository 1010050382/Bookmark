import java.util.ArrayList;
import java.util.List;

public class Title implements Markdown {
    private int grade;
    private String name;

    private List<Title> NextLevelTitle;

    private List<Link> links;

    public Title(int grade, String name) {
        this.grade = grade;
        this.name = name;
        this.NextLevelTitle = new ArrayList<>();
        this.links = new ArrayList<>();
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < getGrade(); i++) {
            stringBuilder.append("#");
        }
        stringBuilder.append(" ").append(getName());
        return stringBuilder.toString();
    }
}
