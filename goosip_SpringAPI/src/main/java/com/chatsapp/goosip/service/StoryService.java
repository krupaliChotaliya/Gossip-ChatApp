package com.chatsapp.goosip.service;

import com.chatsapp.goosip.model.user.Message;
import com.chatsapp.goosip.model.user.Story;
import com.chatsapp.goosip.model.user.User;
import com.chatsapp.goosip.model.user.UserStory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public interface StoryService {

    ResponseEntity<String> uploadFile(MultipartFile file, String userId);

    public ResponseEntity<String> addStory(Story story,String documentID) throws InterruptedException, ExecutionException;

    Story getStories(String documentId);

    ResponseEntity<ArrayList<Story>> getAllStoryOfUser(String currentUser);

    ResponseEntity<String> isCollectionExists(String document) throws ExecutionException, InterruptedException;

    ArrayList<UserStory> getOnlyStory() throws ExecutionException, InterruptedException, IOException, URISyntaxException;
}
