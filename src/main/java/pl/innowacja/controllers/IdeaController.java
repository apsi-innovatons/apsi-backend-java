package pl.innowacja.controllers;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.innowacja.model.dtos.IdeaDto;
import pl.innowacja.model.entities.IdeaEntity;
import pl.innowacja.services.IdeaService;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/ideas")
@RequiredArgsConstructor
public class IdeaController {

  private final IdeaService ideaService;

  @GetMapping
  @ApiOperation(value = "Get simplified DTO's of all ideas in database (without costs, benefits or attachments)")
  public List<IdeaDto> getAllIdeas() {
    return ideaService.getAll();
  }

  @PostMapping
  @ApiOperation(value = "Save Idea in database, returns id of saved entity")
  public Integer saveIdea(@RequestBody IdeaDto ideaDto) {
    return ideaService.saveIdea(ideaDto);
  }

  @PutMapping
  @ApiOperation(value = "Update Idea in database, returns true if updated or false if saved new entity")
  public Boolean updateIdea(@RequestBody IdeaDto ideaDto) {
    return ideaService.update(ideaDto);
  }

  @DeleteMapping("/{id}")
  @ApiOperation(value = "Delete Idea with given id (also deletes all of its attachments, costs and benefits)")
  public void deleteIdea(@PathVariable Integer id) {
    ideaService.deleteIdea(id);
  }

  @GetMapping("/by-subject")
  @ApiOperation(value = "Get all ideas with given subject")
  public List<IdeaEntity> getIdeasBySubject(@RequestParam Integer subjectId) {
    return ideaService.getIdeasForSubject(subjectId);
  }

  @GetMapping("/{id}")
  @ApiOperation(value = "Get idea with given id")
  public IdeaDto getIdeaById(@PathVariable Integer id) {
    return ideaService.getById(id);
  }
}
