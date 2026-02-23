package models;

public class Staff {
    private int id;
    private String name;
    private String role;

    public Staff(int id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getRole() { return role; }
}
