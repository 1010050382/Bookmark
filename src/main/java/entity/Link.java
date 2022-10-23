package entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Link implements Serializable {
    //link name
    private String name;
    //link url
    private String url;
    //read status
    private Boolean status;
    //visit times
    private Date visitTimes;

    public Link(String name, String url) {
        this.name = name;
        this.url = url;
        this.status = false;
        this.visitTimes = null;
    }

    @Override
    public String toString() {
        return "[" + getName() + "](" + getUrl() + ")";
    }
}
