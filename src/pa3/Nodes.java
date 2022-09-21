package pa3;

public class Nodes {
    public int number;
    public Action actions;
    public Boolean terminal;

    public Nodes(int number, Action actions) {
        this.number = number;
        this.actions = actions;
        this.terminal = false;
    }

    public Nodes(int number, Action actions, Boolean terminal) {
        this.number = number;
        this.actions = actions;
        this.terminal = terminal;
    }

}
