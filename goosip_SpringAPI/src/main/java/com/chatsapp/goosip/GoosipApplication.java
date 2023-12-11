package com.chatsapp.goosip;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.InputStream;

@SpringBootApplication
public class GoosipApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoosipApplication.class, args);


        try {
            InputStream serviceAccount=GoosipApplication.class.getResourceAsStream("/chatsapp-6c61d-firebase-adminsdk-zzi8i-03563e0e1e.json");

//            FileInputStream serviceAccount = new FileInputStream("path/to/your/serviceAccountKey.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://chatsapp-6c61d-default-rtdb.firebaseio.com/") // Replace with your database URL
                    .build();

            FirebaseApp.initializeApp(options);

            System.out.printf("sucesss: "+  FirebaseApp.getInstance().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }


