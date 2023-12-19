package com.chatsapp.goosip.service;

import com.chatsapp.goosip.model.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public interface UserService {

    public ResponseEntity<User> addUser(User user) throws InterruptedException, ExecutionException;
    User getUser(String documentId);
    String updateUser(User user);
    List<User> getAllUser(String currentUser);
    ResponseEntity<String> uploadFile( MultipartFile file, String uid) throws IOException;

    boolean isloggedIn(String documentId);

    Boolean logout(String documentId) throws InterruptedException;

    ResponseEntity<String> UpdateField(String uid,String fieldName, String value);

    ResponseEntity<String> UpdateNamAndAbout(String uid,String name, String about);

}
