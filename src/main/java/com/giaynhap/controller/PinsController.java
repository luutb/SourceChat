package com.giaynhap.controller;

import com.giaynhap.config.AppConstant;
import com.giaynhap.model.ApiResponse;
import com.giaynhap.model.DTO.MessageDTO;
import com.giaynhap.model.Message;
import com.giaynhap.model.Pins;
import com.giaynhap.service.PinsServicesIml;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PinsController {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private PinsServicesIml pinsServices;

    @RequestMapping(value = "/getMessages/{conversationId}", method = RequestMethod.GET)
    public ResponseEntity<?> getMessagesPins(@PathVariable("conversationId") String conversationId){

        Message message = pinsServices.getMessagePins(conversationId);
        return ResponseEntity.ok(new ApiResponse<MessageDTO>(0, AppConstant.SUCCESS_MESSAGE, MessageDTO.fromEntity(modelMapper,message)));
    }

    @RequestMapping(value = "/setMessagePins/{conversationId}", method = RequestMethod.POST)
    public  ResponseEntity<?> setMessagePins(@PathVariable("conversationId") String conversationId, @Param("messageId") String messageId){
        Pins pins = new Pins();
        pins.setConverSationUUID(conversationId);
        pins.setMessageUUID(messageId);
        pinsServices.setMessagePins(pins);
        return ResponseEntity.ok(new ApiResponse<Boolean>(0,AppConstant.SUCCESS_MESSAGE,true));
    }

}
