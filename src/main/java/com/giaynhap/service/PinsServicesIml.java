package com.giaynhap.service;

import com.giaynhap.model.Message;
import com.giaynhap.model.Pins;
import com.giaynhap.repository.MessageRepository;
import com.giaynhap.repository.PinsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PinsServicesIml implements  PinsServices{
    @Autowired
    MessageRepository messageRepository;

@Autowired
PinsRepository pinsRepository;

    @Override
    public Message getMessagePins(String conversationId) {
        Message Message = messageRepository.getMessagePins(conversationId);
        return Message;
    }

    @Override
    public Pins setMessagePins(Pins pins) {
        return pinsRepository.save(pins);
    }


}
