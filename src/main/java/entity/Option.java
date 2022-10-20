package entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Option {
    private int type;
    private boolean hasArg;
    private String description;
    private List<String> args;

    public Option(int type) {
        this.type = type;
        this.args = new ArrayList<>();
    }

    public void addArg(String arg) {
        args.add(arg);
    }
}
