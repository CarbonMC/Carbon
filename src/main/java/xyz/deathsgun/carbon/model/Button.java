package xyz.deathsgun.carbon.model;

public class Button extends Element {

    public String id;
    public String text;
    public boolean active;
    public boolean visible;

    public Button() {
        super("button");
    }
}
