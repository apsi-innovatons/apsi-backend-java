package pl.innowacja.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.innowacja.exception.IdeaServiceException;
import pl.innowacja.exception.NoResourceFoundException;
import pl.innowacja.model.entities.AttachmentEntity;
import pl.innowacja.repositories.AttachmentRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttachmentService {

  private final AttachmentRepository attachmentRepository;

  public Integer saveAttachment(MultipartFile file, Integer id) {
    byte[] fileBytes;
    try {
      fileBytes = file.getInputStream().readAllBytes();
    } catch (IOException e) {
      log.error("Failed to get file bytes.");
      throw new IdeaServiceException("Failed to get file bytes.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    var attachmentEntity = new AttachmentEntity();
    attachmentEntity.setFileName(file.getOriginalFilename());
    attachmentEntity.setIdeaId(id);
    attachmentEntity.setFlieBytes(fileBytes);
    return attachmentRepository.save(attachmentEntity).getId();
  }

  public AttachmentEntity getAttachment(Integer id) {
    return attachmentRepository.findAll().stream()
        .filter(attachment -> attachment.getId().equals(id))
        .findAny()
        .orElseThrow(NoResourceFoundException::new);
  }

  public List<Integer> getAttachmentIds(Integer id) {
    return attachmentRepository.findAll().stream()
        .filter(attachment -> attachment.getIdeaId().equals(id))
        .map(AttachmentEntity::getId)
        .collect(Collectors.toList());
  }

  private FileSystemResource getFile(AttachmentEntity attachment) {
    var outputFile = new File(attachment.getFileName());
    try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
      outputStream.write(attachment.getFlieBytes());
    } catch (IOException e) {
      log.error("Failed to get file from database.");
      throw new IdeaServiceException("Failed to get file from database.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new FileSystemResource(outputFile);
  }
}
