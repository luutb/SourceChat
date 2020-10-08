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




}
