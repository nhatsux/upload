package com.nhatsux.upload.utils;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.ReadChannel;
import com.google.cloud.WriteChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.FileInputStream;
import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class FilesUtils {

    private static final String PROJECT_ID = "nhatsux-bucket";
    private static final int CHUNK_SIZE = 1024 * 1024;
    private static final String BUCKET_NAME = "files-chunks";
    private static final String PATH_FILE_CREDENTIALS = "META-INF/credentials.json";

    public static void uploadChunks(FileUploadFormChunks form) throws Exception {

        // Create the BlobId and BlobInfo objects
        BlobId blobId = BlobId.of(BUCKET_NAME, form.getName());
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        Storage storage = StorageOptions.newBuilder().setCredentials(createCredentials()).setProjectId(PROJECT_ID).build().getService();

        // Get the Storage service
        //Storage storage = StorageOptions.getDefaultInstance().getService();
        // If this is the first chunk, create the file
        if (form.getIndex() == 0) {
            WriteChannel channel = storage.writer(blobInfo);
            channel.write(ByteBuffer.wrap(form.getFileData()));
            channel.close();
        } else { // If this is not the first chunk, append to the file
            // Get the current file object
            ReadableByteChannel channelStorage = storage.reader(blobId);
            InputStream inputStream = Channels.newInputStream(channelStorage);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int read;
            byte[] chunk = new byte[CHUNK_SIZE];
            while ((read = inputStream.read(chunk, 0, chunk.length)) != -1) {
                outputStream.write(chunk, 0, read);
            }
            inputStream.close();

            // Append the new chunk to the file
            outputStream.write(form.getFileData());

            // Write the updated file back to GCS
            WriteChannel channel = storage.writer(blobInfo);
            channel.write(ByteBuffer.wrap(outputStream.toByteArray()));
            channel.close();
        }

    }

    public static FileDownloadChunks buildInputStream(String fileName) throws Exception {
        Storage storage = StorageOptions.newBuilder().setCredentials(createCredentials()).setProjectId(PROJECT_ID).build().getService();
        // Fetch the requested file
        BlobId blobId = BlobId.of(BUCKET_NAME, fileName);
        Blob blob = storage.get(blobId);
        ReadChannel readChannel = blob.reader();

        FileDownloadChunks fileData = new FileDownloadChunks();
        fileData.setBlob(blob);
        fileData.setFile(Channels.newInputStream(readChannel));
        return fileData;
    }

    public static GoogleCredentials createCredentials() throws Exception {
        URL resource = FilesUtils.class.getClassLoader().getResource(PATH_FILE_CREDENTIALS);
        File file = new File(resource.toURI());
        FileInputStream credentialsStream = new FileInputStream(file);
        return GoogleCredentials.fromStream(credentialsStream);
    }

}
