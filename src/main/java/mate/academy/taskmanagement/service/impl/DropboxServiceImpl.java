package mate.academy.taskmanagement.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Map;
import mate.academy.taskmanagement.exception.DropboxDeleteException;
import mate.academy.taskmanagement.exception.DropboxDownloadException;
import mate.academy.taskmanagement.exception.DropboxUploadException;
import mate.academy.taskmanagement.service.DropboxService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DropboxServiceImpl implements DropboxService {
    @Value("${dropbox.upload.url}")
    private String dropboxUploadUrl;

    @Value("${dropbox.download.url}")
    private String dropboxDownloadUrl;

    @Value("${dropbox.delete.url}")
    private String dropboxDeleteUrl;

    @Value("${dropbox.access.token}")
    private String dropboxAccessToken;

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();

        String filePath = "/" + file.getOriginalFilename();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(dropboxUploadUrl))
                .header("Authorization", "Bearer " + dropboxAccessToken)
                .header("Dropbox-API-Arg", "{\"path\": \"" + filePath + "\", "
                        + "\"mode\": \"add\", \"autorename\": true, \"mute\": false}")
                .header("Content-Type", "application/octet-stream")
                .POST(HttpRequest.BodyPublishers.ofByteArray(file.getBytes()))
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException | IOException e) {
            Thread.currentThread().interrupt();
            throw new DropboxUploadException("Request to Dropbox was interrupted");
        }

        if (response.statusCode() != 200) {
            throw new DropboxUploadException("Failed to upload file to Dropbox: "
                    + response.statusCode() + " " + response.body());
        }

        try {
            Map<String, Object> responseMap = objectMapper.readValue(response.body(),
                    new TypeReference<>() {});
            return (String) responseMap.get("id");
        } catch (IOException e) {
            throw new DropboxUploadException("Failed to parse Dropbox response");
        }
    }

    @Override
    public Resource downloadFile(String dropboxFileId) {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(dropboxDownloadUrl))
                .header("Authorization", "Bearer " + dropboxAccessToken)
                .header("Dropbox-API-Arg", "{\"path\": \"" + dropboxFileId + "\"}")
                .GET()
                .build();

        HttpResponse<byte[]> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
        } catch (InterruptedException | IOException e) {
            Thread.currentThread().interrupt();
            throw new DropboxDownloadException("Request to Dropbox was interrupted");
        }

        if (response.statusCode() != 200) {
            throw new DropboxDownloadException("Failed to download file from Dropbox: "
                    + response.statusCode() + " " + Arrays.toString(response.body()));
        }

        return new ByteArrayResource(response.body());
    }

    @Override
    public void deleteFile(String dropboxFileId) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(dropboxDeleteUrl))
                .header("Authorization", "Bearer " + dropboxAccessToken)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"path\": \""
                        + dropboxFileId + "\"}"))
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException | IOException e) {
            Thread.currentThread().interrupt();
            throw new DropboxDeleteException("Request to Dropbox was interrupted");
        }

        if (response.statusCode() != 200) {
            throw new DropboxDeleteException("Failed to delete file from Dropbox: "
                    + response.statusCode() + " " + response.body());
        }
    }
}
