package br.ufrn.chatclient.models;

/**
 * Created by dhiogoboza on 26/04/16.
 */
public class User {

    private String name;
    private String color;
    private String userId;

    public User() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "{userid: " + userId +
                ", username: " + name +
                ", color: " + color + "}";
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }

    @Override
    public boolean equals(Object o) {

        if (o == null || !(o instanceof User)) {
            return false;
        }

        User another = (User) o;

        return userId != null && another.getUserId() != null && userId.equals(another.getUserId());
    }
}
