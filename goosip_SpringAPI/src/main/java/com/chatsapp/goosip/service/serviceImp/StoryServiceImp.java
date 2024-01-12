package com.chatsapp.goosip.service.serviceImp;

import com.chatsapp.goosip.model.user.Story;
import com.chatsapp.goosip.model.user.UserStory;
import com.chatsapp.goosip.service.StoryService;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.chatsapp.goosip.service.serviceImp.MessageServiceImp.getRandomString;

@Service
public class StoryServiceImp implements StoryService {

    @Override
    public ResponseEntity<String> uploadFile(MultipartFile multipartFile, String uid) {
        try {
            System.out.println("[uploadFile] started uploading file to server");
            String fileName = multipartFile.getOriginalFilename();                        // to get original file name
            File file = UploadingImg.convertToFile(multipartFile, fileName);                      // to convert multipartFile to File
            String randomString = getRandomString(10);
            String URL = UploadingImg.uploadFile(file, randomString, "StoryImg/");                                   // to get uploaded file link
            return ResponseEntity.ok(URL);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image" + e);
        }
    }
    @Override
    public ResponseEntity<String> addStory(Story story, String documentId) {
        try {
            Firestore firestore = FirestoreClient.getFirestore();
            System.out.println(story.getStory());
            String randomString = getRandomString(10);
            //TODO
            CollectionReference storyCollection = firestore.collection("users");
            DocumentReference storyDocument = storyCollection.document(documentId);
            storyDocument.collection("storyList").document(randomString).set(story);
            return ResponseEntity.status(HttpStatus.OK).body("Story saved successfully!!");
        } catch (Exception e) {
            System.out.println("Exception[addStory] " + e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    @Override
    public Story getStories(String documentId) {
        ArrayList<Story> stories = new ArrayList<>();
        try {
            System.out.println("enter>>>");
            //changes required
            Firestore firestore = FirestoreClient.getFirestore();
            CollectionReference storyCollection = firestore.collection("stories");
            DocumentReference storyDocument = storyCollection.document(documentId);
            CollectionReference storyListCollection = storyDocument.collection("storyList");
            List<QueryDocumentSnapshot> storyDocuments = storyListCollection.get().get().getDocuments();

            for (QueryDocumentSnapshot storyDt : storyDocuments) {
                Story story = storyDt.toObject(Story.class);
                stories.add(story);
            }
        } catch (Exception e) {
            System.out.printf("Exception[getStory]: " + e);
        }
        return null;
    }
    @Override
    public ResponseEntity<ArrayList<Story>> getAllStoryOfUser(String currentUser) {
        ArrayList<Story> stories = new ArrayList<>();
        try {
            Firestore firestore = FirestoreClient.getFirestore();
            CollectionReference storyCollection = firestore.collection("users");
            DocumentReference storyDocument = storyCollection.document(currentUser);
            CollectionReference storyListCollection = storyDocument.collection("storyList");
            List<QueryDocumentSnapshot> storyDocuments = storyListCollection.get().get().getDocuments();
            // Use caching to avoid repeated calls
            for (QueryDocumentSnapshot storyDt : storyDocuments) {
                Story story = storyDt.toObject(Story.class);
                stories.add(story);
            }
            System.out.println(stories);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(stories);
    }
    @Override
    public ResponseEntity<String> isCollectionExists(String document) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        String parentCollectionPath = "users";
        String collection2Name = "storyList";
        DocumentReference documentReference = firestore.collection(parentCollectionPath).document(document);
        CollectionReference storyListCollection = documentReference.collection(collection2Name);
        List<QueryDocumentSnapshot> storyDocuments = storyListCollection.get().get().getDocuments();
        try {
            if (!storyDocuments.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body("storyList exist within users/storyList!");
            } else {
                System.out.println("Collection2 does not exist within the specified document!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("storyList does not exist within users/storyList!");
    }
    public ArrayList<UserStory> getOnlyStory() throws IOException, URISyntaxException {

        //storage
//        String fileName= extractFileName("https://firebasestorage.googleapis.com/v0/b/chatsapp-6c61d.appspot.com/o/StoryImg%2FIKcWBgucy5?alt=media&token=a4bfcb53-b546-4511-97bd-955c9cc2436a");
//        deleteStoryFromStorage(fileName);


        ArrayList<UserStory> userStoryArrayList = new ArrayList<>();
        final Firestore db = FirestoreClient.getFirestore();
        try {
            CollectionReference usersCollection = db.collection("users");
            ApiFuture<QuerySnapshot> query = usersCollection.get();
            QuerySnapshot querySnapshot = query.get();

            for (QueryDocumentSnapshot document : querySnapshot) {
                String uid = document.getId();
                CollectionReference storyListCollection = document.getReference().collection("storyList");

                CompletableFuture<Void> storyListFuture = CompletableFuture.runAsync(() -> {
                    try {
                        QuerySnapshot storyListQuerySnapshot = storyListCollection.get().get();

                        if (!storyListQuerySnapshot.isEmpty()) {
                            UserStory userStory = new UserStory();
                            userStory.setUid(uid);
                            userStory.setName(document.getString("name"));
                            userStory.setProfileImg(document.getString("profileImg"));

                            ArrayList<Story> stories = new ArrayList<>();
                            for (QueryDocumentSnapshot storyDocument : storyListQuerySnapshot) {
                                Story story = storyDocument.toObject(Story.class);

                                boolean isOlderThan24Hours = isTimestampOlderThan24Hours(story.getTimestamp());
                                if (isOlderThan24Hours) {
                                    System.out.println("The timestamp is more than 24 hours old. " + story.getTimestamp());

                                    //delete file from storage and Firestore
                                    String fileName= extractFileName(story.getStory());
                                    deleteStoryFromStorage(fileName);
                                    deleteStoryFromFirestore(story.getTimestamp(), uid, storyDocument.getId());

                                } else {
                                    System.out.println("The timestamp is not more than 24 hours old.");
                                }

                                if (Objects.equals(story.getUid(), userStory.getUid())) {
                                    stories.add(story);
                                }
                            }
                            stories.sort(Comparator.comparingLong(Story::getTimestamp));
                            userStory.setStories(stories);
                            userStoryArrayList.add(userStory);
                        }
                    } catch (InterruptedException | ExecutionException | IOException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                });
                storyListFuture.get();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userStoryArrayList;
    }
    public void deleteStoryFromFirestore(Long timestamp, String uid, String documentId) throws IOException {
        final Firestore db = FirestoreClient.getFirestore();

        CollectionReference subcollection = db.collection("users").document(uid).collection("storyList");
        Query query = subcollection.whereEqualTo("timestamp", timestamp);

        try {
            ApiFuture<QuerySnapshot> querySnapshotApiFuture = query.get();
            ApiFutures.addCallback(querySnapshotApiFuture, new ApiFutureCallback<QuerySnapshot>() {
                @Override
                public void onFailure(Throwable throwable) {
                    System.out.println("Exception: " + throwable.getMessage());
                }

                @Override
                public void onSuccess(QuerySnapshot querySnapshot) {
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        WriteBatch batch = db.batch();
                        for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                            batch.delete(documentSnapshot.getReference());
                        }
                        ApiFuture<List<WriteResult>> batchResult = batch.commit();
                        ApiFutures.addCallback(batchResult, new ApiFutureCallback<List<WriteResult>>() {
                            @Override
                            public void onFailure(Throwable throwable) {
                                System.out.println("Batch delete failed: " + throwable.getMessage());
                            }
                            @Override
                            public void onSuccess(List<WriteResult> writeResults) {
                                System.out.println("Batch delete successful.");
                            }
                        }, MoreExecutors.directExecutor());
                    } else {
                        System.out.println("No documents to delete.");
                    }
                }
            }, MoreExecutors.directExecutor());
        } catch (Exception e) {
            System.err.println("Error during subcollection deletion: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static boolean isTimestampOlderThan24Hours(long timestamp) {
        Date currentTimestamp = new Date();
        long timeDifferenceMillis = currentTimestamp.getTime() - timestamp;
        long timeDifferenceHours = TimeUnit.MILLISECONDS.toHours(timeDifferenceMillis);
        return timeDifferenceHours > 24;
    }
    private static void deleteStoryFromStorage(String filePath) throws IOException {
        filePath="StoryImg/"+filePath;
        System.out.println(filePath);
        InputStream inputStream = UserServiceImp.class.getClassLoader().getResourceAsStream("chatsapp-6c61d-firebase-adminsdk-zzi8i-03563e0e1e.json");
        Storage storage = StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(inputStream))
                .build()
                .getService();
        String bucketName = "chatsapp-6c61d.appspot.com";
        BlobId blobId = BlobId.of(bucketName, filePath);
        boolean deleted = storage.delete(blobId);

        if (deleted) {
            System.out.println("Image deleted successfully.");
        } else {
            System.err.println("Failed to delete image. File not found or other error.");
        }
    }
    private static String extractFileName(String storageUrl) throws URISyntaxException, UnsupportedEncodingException {
        URI uri = new URI(storageUrl);
        String path = URLDecoder.decode(Paths.get(uri.getPath()).toString(), StandardCharsets.UTF_8.toString());
        path.replaceAll(".*/([^/?]+).*", "$1");
        String[] pathParts = path.split("\\\\");
        String fileName = pathParts[pathParts.length - 1];
        System.out.println("fileName "+fileName);
        return fileName;
    }

}

