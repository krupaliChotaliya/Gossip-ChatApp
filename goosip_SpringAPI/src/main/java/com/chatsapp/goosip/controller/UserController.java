package com.chatsapp.goosip.controller;

import com.chatsapp.goosip.model.user.User;
import com.chatsapp.goosip.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/upload", method = { RequestMethod.GET, RequestMethod.POST }, produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> UploadImg(@RequestParam("file") MultipartFile file, @RequestParam("userId") String UserId) {
        try {
            return  userService.uploadFile(file, UserId);
        } catch (Exception e) {
            String message = "failed "+e;
            return ResponseEntity.of(Optional.of(message));
        }
    }
    
    @PostMapping("/user")
    public ResponseEntity<User> addUser(@RequestBody User user) throws InterruptedException, ExecutionException {
        return userService.addUser(user);
    }

    @GetMapping("/user")
    public User getUser(@RequestParam String currentUserId) {
        return userService.getUser(currentUserId);
    }

    @GetMapping("/users")
    public List<User> getAllUserExceptCurrentUser(@RequestParam String currentUser) {
        return userService.getAllUserExceptCurrentUser(currentUser);
    }

    @GetMapping("/getusers")
    public List<User> getAllUser() {
        return userService.getAllUser();
    }

    @PutMapping("/user")
    public String updateUser(@RequestBody User user, String id) throws InterruptedException, ExecutionException {
        return userService.updateUser(user);
    }
    @GetMapping("/IsLoggedIn")
    public Boolean IsLoggedIn(@RequestParam String documentId){
       return userService.isloggedIn(documentId);
    }

    @GetMapping("logout")
    public Boolean logout(@RequestParam String documentId) throws InterruptedException {
        return userService.logout(documentId);
    }
    @PutMapping("/userField")
    public ResponseEntity<String> UpdateField(@RequestParam String uid,@RequestParam String fieldName, @RequestParam String value) {
        System.out.println("enter[UpdateStatus]");
        try {
            return userService.UpdateField(uid,fieldName, value);
        } catch (Exception e) {
            System.out.println("[UpdateStatus] " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/updateUser")
    public ResponseEntity<String> UpdateNamAndAbout(@RequestParam String uid,@RequestParam String name, @RequestParam String about){
        System.out.println("enter[UpdateNamAndAbout]");
        try {
            return userService.UpdateNamAndAbout(uid,name, about);
        } catch (Exception e) {
            System.out.println("[UpdateNamAndAbout] " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}
