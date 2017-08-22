package chatting.faisal.pkp.chatting.Model;

import java.util.Date;

/**
 * Created by Faisal on 2/6/2017.
 */

public class ChatModel {
    public String SenderId;
    public String ReceiverId;
    public String SenderName;
    public String ReceiverName;
    public String Message;
    public int Status;
    public String PhotoUrl;
    public String Time;

    public ChatModel(
            String SenderId,
            String ReceiverId,
            String SenderName,
            String ReceiverName,
            String Message,
            int Status,
            String PhotoUrl,
            String Time
    ) {
        this.SenderId = SenderId;
        this.ReceiverId = ReceiverId;
        this.SenderName = SenderName;
        this.ReceiverName = ReceiverName;
        this.Message = Message;
        this.Status = Status;
        this.PhotoUrl = PhotoUrl;
        this.Time = Time;
    }

    public String getPhotoUrl() {
        return this.PhotoUrl;
    }

}