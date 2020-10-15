package com.giaynhap.service;

import com.giaynhap.model.Message;
import com.giaynhap.model.Pins;
import org.springframework.stereotype.Service;

@Service
public interface PinsServices {
public Message getMessagePins(String conversationId);
public Pins setMessagePins(Pins pins);
}
