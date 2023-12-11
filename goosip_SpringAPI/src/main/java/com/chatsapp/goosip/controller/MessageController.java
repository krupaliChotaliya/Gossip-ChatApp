package com.chatsapp.goosip.controller;

import com.chatsapp.goosip.model.user.Message;
import com.chatsapp.goosip.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/message")
    public ResponseEntity<String> saveMessage(@RequestBody Message message, @RequestParam String senderRoom, @RequestParam String receiverRoom) {
        System.out.println("enter//////////////////");
        try {
            return messageService.saveMessage(message, senderRoom, receiverRoom);
        } catch (Exception e) {
            System.out.println("[saveMessage] " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/message")
    public ResponseEntity<ArrayList<Message>> getMessages(@RequestParam String senderRoom) {
        System.out.println("[getMessages] enter>>>>>>>>>");
        try {
            return messageService.getMessages(senderRoom);
        } catch (Exception e) {
            System.out.println("[getMessages] " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
