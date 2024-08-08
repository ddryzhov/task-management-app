package mate.academy.taskmanagement.service;

import java.io.IOException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface DropboxService {
    String uploadFile(MultipartFile file) throws IOException;

    Resource downloadFile(String dropboxFileId);

    void deleteFile(String dropboxFileId);
}
