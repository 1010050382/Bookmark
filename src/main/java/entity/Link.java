package entity;

import lombok.Data;

@Data
public class Link implements Markdown {
    //link name
    private String name;
    //link url
    private String url;
    //read status
    private boolean status;
    //visit times
    private int visitTimes;

    public Link(String name, String url) {
        this.name = name;
        this.url = url;
    }

    @Override
    public String toString() {
        return "[" + getName() + "](" + getUrl() + ")";
    }
}
