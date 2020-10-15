package com.giaynhap.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import sun.plugin2.message.Conversation;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.io.Serializable;

public class Pins implements Serializable {
    @Column(name = "conversation_uuid")
    private String converSationUUID;
    @JsonProperty("messsage_uuid")
    private String messageUUID;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "conversation_uuid", referencedColumnName = "uuid")
    private Conversation conversation;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "messsage_uuid", referencedColumnName = "uuid")
    private Message message;

    public String getConverSationUUID() {
        return converSationUUID;
    }

    public void setConverSationUUID(String converSationUUID) {
        this.converSationUUID = converSationUUID;
    }

    public String getMessageUUID() {
        return messageUUID;
    }

    public void setMessageUUID(String messageUUID) {
        this.messageUUID = messageUUID;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
