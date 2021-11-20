package pl.innowacja.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.innowacja.exception.IdeaServiceException;
import pl.innowacja.services.AttachmentService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin
@Api(tags = {"Innowacja API"})
@RequiredArgsConstructor
public class AttachmentController {

  private final AttachmentService attachmentService;

  @PostMapping("/ideas/{id}/attachments")
  @ApiOperation(value = "Save attachment for given ideaId")
  public Integer saveAttachment(@RequestParam("file") MultipartFile file, @PathVariable Integer id) {
    return attachmentService.saveAttachment(file, id);
  }

  @GetMapping("/ideas/{id}/attachments")
  @ApiOperation(value = "Get list of attachment id's belonging to given idea.")
  public List<Integer> getAttachmentIdsByIdeaId(@PathVariable Integer id) {
    return attachmentService.getAttachmentIds(id);
  }

  @GetMapping(path = "/attachments/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public void downloadAttachmentById(HttpServletResponse response,
                                     @PathVariable Integer id) {
    var attachmentEntity = attachmentService.getAttachment(id);
    response.addHeader("Content-Disposition", "attachment; filename=" + attachmentEntity.getFileName());
    try {
      response.getOutputStream().write(attachmentEntity.getFlieBytes());
    } catch (IOException e) {
      var errorMessage = "Could not write file bytes to Http response.";
      log.error(errorMessage);
      throw new IdeaServiceException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
