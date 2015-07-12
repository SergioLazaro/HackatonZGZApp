package taxitouchzgz.example.com.taxitouchzgzv2;

/**
 * Created by sergiolazaromagdalena on 12/7/15.
 */
public class Comment {

    private String commentID;
    private String sender;
    private String receiver;
    private String date;
    private String text;

    public Comment(String id, String commentID, String sender, String receiver, String text){
        this.commentID = commentID;
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
    }

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String toString(){
        return sender + "has sent you " + text + ". " + date;
    }
}
