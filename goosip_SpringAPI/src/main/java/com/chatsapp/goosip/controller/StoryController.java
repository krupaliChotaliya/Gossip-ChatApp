package com.chatsapp.goosip.controller;

import com.chatsapp.goosip.model.user.Story;
import com.chatsapp.goosip.model.user.UserStory;
import com.chatsapp.goosip.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@RestController
public class StoryController {

    @Autowired
    StoryService storyService;

    public StoryController(StoryService storyService) {
        this.storyService = storyService;
    }

    @RequestMapping(value = "/uploadStoryImg", method = { RequestMethod.GET, RequestMethod.POST }, produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> UploadImg(@RequestParam("file") MultipartFile file, @RequestParam("userId") String UserId) {
        try {
            return  storyService.uploadFile(file, UserId);
        } catch (Exception e) {
            String message = "failed "+e;
            return ResponseEntity.of(Optional.of(message));
        }
    }

    @PostMapping("/story")
    public ResponseEntity<String> addStory(@RequestBody Story story) throws InterruptedException, ExecutionException {
        return storyService.addStory(story,story.getUid());
    }

    @GetMapping("/story")
    public Story getStories(@RequestParam String currentUserId) {
        return storyService.getStories(currentUserId);
    }

    @GetMapping("/stories")
    public ResponseEntity<ArrayList<Story>> getAllStoryOfUser(@RequestParam String currentUser) {
        return storyService.getAllStoryOfUser(currentUser);
    }

    @GetMapping("/isCollectionExists")
    public ResponseEntity<String> isCollectionExists(@RequestParam String document) throws ExecutionException, InterruptedException {
        return storyService.isCollectionExists(document);
    }

    @GetMapping("/getOnlyStory")
    public ArrayList<UserStory> getOnlyStory() throws ExecutionException, InterruptedException, IOException, URISyntaxException {
       return storyService.getOnlyStory();
    }
}
