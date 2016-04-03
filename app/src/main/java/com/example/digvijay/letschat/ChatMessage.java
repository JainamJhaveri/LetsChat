package com.example.digvijay.letschat;

/**
 * Created by Digvijay on 02-04-2016.
 */
public class ChatMessage {
    String content;
    Boolean isMine , isImage;

    public ChatMessage(String content, boolean isMine){
        this.content = content;
        this.isMine = isMine;
    }

    public boolean isMine() {
        return isMine;
    }

   public String getContent() {
        return content;
    }
}
