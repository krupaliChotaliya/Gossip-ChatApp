package com.chatsapp.goosip.service.serviceImp;

import com.chatsapp.goosip.model.user.User;
import com.chatsapp.goosip.service.UserService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class UserServiceImp implements UserService {
    private List<User> cachedUsers;
    private long lastFetchTime;
    private static final long CACHE_EXPIRATION_TIME_MS = 60000; // Cache expiration time in milliseconds (1 minute)

    @Override
    public ResponseEntity<User> addUser(User user) throws InterruptedException, ExecutionException {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApifuture = firestore.collection("users").document(user.getUid()).set(user);
        //get profilePicture url
        DocumentReference docRef = firestore.collection("users").document(user.getUid());
        DocumentSnapshot document = docRef.get().get();
        if (document.exists()) {
            User user2 = new User();
            user2.setUid(document.getString("uid"));
            user2.setAbout(document.getString("about"));
            user2.setName(document.getString("name"));
            user2.setProfileImg(document.getString("profileImg"));
            user2.setActive(document.getString("active"));
            return ResponseEntity.status(HttpStatus.OK).body(user2);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Override
    public User getUser(String documentId) {
        try {
            Firestore firestore = FirestoreClient.getFirestore();
            DocumentReference documentReference = firestore.collection("users").document(documentId);
            ApiFuture<DocumentSnapshot> future = documentReference.get();
            DocumentSnapshot documentSnapshot = future.get();
            User user;
            if (documentSnapshot.exists()) {
                user = documentSnapshot.toObject(User.class);
                return user;
            }
        } catch (Exception e) {
            System.out.printf("Exception[getUser]: " + e);
        }
        return null;
    }

    @Override
    public String updateUser(User user) {
        try {
            Firestore firestore = FirestoreClient.getFirestore();
            ApiFuture<WriteResult> collectionsApifuture = firestore.collection("users").document(user.getName()).set(user);
            WriteResult writeResult = collectionsApifuture.get();
            Class<?> responseClass = writeResult.getClass();
            return responseClass.getName();

        } catch (Exception e) {
            System.out.printf("Exception[updateUser]: " + e);
        }
        return null;
    }

    @Override
    public List<User> getAllUserExceptCurrentUser(String currentUser) {
        try {
            List<User> users;
            Firestore firestore = FirestoreClient.getFirestore();
            users = firestore.collection("users").get().get().toObjects(User.class);
            Iterator<User> iterator = users.iterator();
            while (iterator.hasNext()) {
                User item = iterator.next();
                if (item.getUid().equals(currentUser)) {
                    iterator.remove();
                    System.out.println("Item removed...");
                }
            }
            System.out.println(users);
            return users;
        } catch (Exception e) {
            System.out.printf("Exception[getUsers]: " + e);
        }
        return null;
    }

    @Override
    public ResponseEntity<String> uploadFile(MultipartFile multipartFile, String uid) throws IOException {
        try {
            System.out.println("[uploadFile] started uploading file to server");
            String fileName = multipartFile.getOriginalFilename();                        // to get original file name
            File file = UploadingImg.convertToFile(multipartFile, fileName);                      // to convert multipartFile to File
            String URL = UploadingImg.uploadFile(file, uid,"images/");                                   // to get uploaded file link
            return ResponseEntity.ok(URL);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image" + e);
        }
    }

    @Override
    public boolean isloggedIn(String documentId) {
        try {
            Firestore firestore = FirestoreClient.getFirestore();
            DocumentReference docRef = firestore.collection("users").document(documentId);
            DocumentSnapshot document = docRef.get().get();

            if (document.exists()) {
                String result = document.getString("active");
                assert result != null;
                if (result.equals("1")) {
                    return true;
                }
            } else {
                return false; // Handle the case where the document doesn't exist
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
        return false;
    }

    @Override
    public Boolean logout(String documentId)throws InterruptedException {
        try {
            Firestore firestore = FirestoreClient.getFirestore();
            DocumentReference docRef = firestore.collection("users").document(documentId);

            // Get the current document
            DocumentSnapshot document = docRef.get().get();

            if (document.exists()) {
                // Create a map with the field to be updated
                Map<String, Object> updates = new HashMap<>();
                updates.put("active", "0");

                // Commit the update
                ApiFuture<WriteResult> writeResult = docRef.update(updates);
                System.out.println("Update time : " + writeResult.get().getUpdateTime());
                return true;
            }
            } catch(ExecutionException e){
                throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public ResponseEntity<String> UpdateField(String uid,String fieldName,String value) {
        try{
            Firestore firestore = FirestoreClient.getFirestore();
            CollectionReference userCollection = firestore.collection("users");
            DocumentReference docRef = userCollection.document(uid);
            docRef.update(fieldName, value);
            return ResponseEntity.status(HttpStatus.OK).body(value);
        }catch (Exception e){
            System.out.println("[updateStatus]Exception:"+e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Override
    public ResponseEntity<String> UpdateNamAndAbout(String uid, String name, String about) {
        try{
            Firestore firestore = FirestoreClient.getFirestore();
            CollectionReference userCollection = firestore.collection("users");
            DocumentReference docRef = userCollection.document(uid);
            docRef.update("name", name);
            docRef.update("about", about);
            return ResponseEntity.status(HttpStatus.OK).body(name+" "+about);
        }catch (Exception e){
            System.out.println("[updateStatus]Exception:"+e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Override
    public List<User> getAllUser() {
        if (cachedUsers != null && System.currentTimeMillis() - lastFetchTime < CACHE_EXPIRATION_TIME_MS) {
            return cachedUsers;
        }
        try {
            Firestore firestore = FirestoreClient.getFirestore();
            QuerySnapshot querySnapshot = firestore.collection("users").get().get();
            List<User> users = new ArrayList<>();
            for (QueryDocumentSnapshot document : querySnapshot) {
                users.add(document.toObject(User.class));
            }
            cachedUsers = users;
            lastFetchTime = System.currentTimeMillis();
            return users;
        } catch (Exception e) {
            System.out.printf("Exception[getAllUser]: " + e);
        }
        return null;
    }
}

