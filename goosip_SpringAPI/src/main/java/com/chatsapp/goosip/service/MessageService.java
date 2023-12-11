package com.chatsapp.goosip.service;

import com.chatsapp.goosip.model.user.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@Service
public interface MessageService {

    public ResponseEntity<String> saveMessage(Message message, String senderRoom, String receiverRoom) throws ExecutionException;

    public ResponseEntity<ArrayList<Message>> getMessages(String senderRoom);

}
