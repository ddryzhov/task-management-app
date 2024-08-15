package mate.academy.taskmanagement.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagement.dto.attachment.AttachmentResponseDto;
import mate.academy.taskmanagement.exception.TaskNotFoundException;
import mate.academy.taskmanagement.mapper.AttachmentMapper;
import mate.academy.taskmanagement.model.Attachment;
import mate.academy.taskmanagement.model.Task;
import mate.academy.taskmanagement.repository.AttachmentRepository;
import mate.academy.taskmanagement.repository.TaskRepository;
import mate.academy.taskmanagement.service.AttachmentService;
import mate.academy.taskmanagement.service.DropboxService;
import mate.academy.taskmanagement.service.EmailSenderService;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {
    private final AttachmentRepository attachmentRepository;
    private final TaskRepository taskRepository;
    private final DropboxService dropboxService;
    private final AttachmentMapper attachmentMapper;
    private final EmailSenderService emailSenderService;

    @Override
    @Transactional
    public AttachmentResponseDto uploadAttachment(
            MultipartFile file,
            Long taskId,
            String email,
            String role
    ) throws IOException {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID '"
                        + taskId + "' not found."));

        checkTaskAccess(task, email, role);

        String dropboxFileId = dropboxService.uploadFile(file);

        Attachment attachment = new Attachment();
        attachment.setTask(task);
        attachment.setDropboxFileId(dropboxFileId);
        attachment.setFilename(file.getOriginalFilename());
        attachment.setUploadDate(LocalDateTime.now());

        attachment = attachmentRepository.save(attachment);

        String subject = "File Uploaded Successfully";
        String text = "Your file '" + file.getOriginalFilename()
                + "' has been successfully uploaded to Dropbox.";
        emailSenderService.sendEmail(email, subject, text);

        return attachmentMapper.toDto(attachment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttachmentResponseDto> getAttachmentsByTaskId(Long taskId,
                                                              String email,
                                                              String role,
                                                              Pageable pageable) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID '"
                        + taskId + "' not found."));

        checkTaskAccess(task, email, role);

        List<Attachment> attachments = attachmentRepository.findByTaskId(taskId, pageable);
        return attachmentMapper.toDtoList(attachments);
    }

    @Override
    @Transactional(readOnly = true)
    public Resource downloadAttachment(String fileId, String email, String role) {
        Attachment attachment = attachmentRepository.findByDropboxFileId(fileId)
                .orElseThrow(() -> new TaskNotFoundException("Attachment with file ID '"
                        + fileId + "' not found."));

        Task task = attachment.getTask();
        checkTaskAccess(task, email, role);
        return dropboxService.downloadFile(fileId);
    }

    @Override
    @Transactional
    public void deleteAttachment(Long attachmentId, String email, String role) {
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new TaskNotFoundException("Attachment with ID '"
                        + attachmentId + "' not found."));

        Task task = attachment.getTask();
        checkTaskAccess(task, email, role);

        dropboxService.deleteFile(attachment.getDropboxFileId());

        attachmentRepository.deleteById(attachmentId);
    }

    private void checkTaskAccess(Task task, String email, String role) {
        if (!task.getProject().getUser().getEmail().equals(email) && !role.equals("ROLE_ADMIN")) {
            throw new AccessDeniedException("You do not have permission for this action.");
        }
    }
}
