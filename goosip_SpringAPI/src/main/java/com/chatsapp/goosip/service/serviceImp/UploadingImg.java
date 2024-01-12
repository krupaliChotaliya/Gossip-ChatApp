package com.chatsapp.goosip.service.serviceImp;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

public class UploadingImg {
    public static String uploadFile(File file, String fileName,String folder) throws IOException {
        System.out.println("[uploadFile] uploading file to server");
        BlobId blobId = BlobId.of("chatsapp-6c61d.appspot.com", folder + fileName); // Replace with your bucker name
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/png").build();
        InputStream inputStream = UserServiceImp.class.getClassLoader().getResourceAsStream("chatsapp-6c61d-firebase-adminsdk-zzi8i-03563e0e1e.json"); // change the file name with your one
        Credentials credentials = GoogleCredentials.fromStream(inputStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));

        Blob blob = storage.get(blobId);
        if (blob != null) {
            return ((Blob) blob).signUrl(365, TimeUnit.DAYS).toString();
        }
        return null;
    }

    public static File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        System.out.println("[convertToFile] Converting Byte stream to File");
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
        }
        return tempFile;
    }
}
