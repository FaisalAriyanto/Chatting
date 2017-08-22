package chatting.faisal.pkp.chatting.Model;

/**
 * Created by Faisal on 2/6/2017.
 */

public class ChatListModel {
    public String id;
    public String name;
    public String message;
    public int status;
    public String photoUrl;


    public ChatListModel(
            String name,
            String message,
            int status,
            String photoUrl){
        this.name = name;
        this.message = message;
        this.status = status;
        this.photoUrl = photoUrl;
    }
    public String getPhotoUrl(){
        return this.photoUrl;
    }

}