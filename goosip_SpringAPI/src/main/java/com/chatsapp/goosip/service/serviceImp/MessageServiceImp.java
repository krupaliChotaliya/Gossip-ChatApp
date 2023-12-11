package com.chatsapp.goosip.service.serviceImp;

import com.chatsapp.goosip.model.user.Message;
import com.chatsapp.goosip.service.MessageService;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@Service
public class MessageServiceImp implements MessageService {
    @Override
    public ResponseEntity<String> saveMessage(Message message, String senderRoom, String receiverRoom) throws ExecutionException {
        try {
            System.out.println("save msg----------------------");
            Firestore firestore = FirestoreClient.getFirestore();
            String randomString = getRandomString(10);
            firestore.collection("chats").document(senderRoom).collection("messageList").document(randomString).set(message);
            firestore.collection("chats").document(receiverRoom).collection("messageList").document(randomString).set(message);
            return ResponseEntity.status(HttpStatus.OK).body("Message saved successfully!!");
        } catch (Exception e) {
            System.out.printf("Exception[getUser]: " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    static String getRandomString(int n) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index = (int) (AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }

  public ResponseEntity<ArrayList<Message>> getMessages(String senderRoom) {
        ArrayList<Message> messages = new ArrayList<>();
        try {
            Firestore firestore = FirestoreClient.getFirestore();
            CollectionReference chatsCollection = firestore.collection("chats");
            DocumentReference chatDocument = chatsCollection.document(senderRoom);
            CollectionReference messageListCollection = chatDocument.collection("messageList");

            messageListCollection.orderBy("timestamp").get().get().forEach(messageDocument -> {
                DocumentSnapshot messageSnapshot = messageDocument;
                Message message = messageSnapshot.toObject(Message.class);
                messages.add(message);
                System.out.println(messages);
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
      return ResponseEntity.status(HttpStatus.OK).body(messages);
    }
}
