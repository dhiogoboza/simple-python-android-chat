package br.ufrn.chatclient.models;

/**
 * Created by dhiogoboza on 25/04/16.
 */
public class Message {

    private User sender;
    private String content;
    private boolean isPropertyMessage = false;

    public Message() {

    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isPropertyMessage() {
        return isPropertyMessage;
    }

    public void setPropertyMessage(boolean isPropertyMessage) {
        this.isPropertyMessage = isPropertyMessage;
    }
}
