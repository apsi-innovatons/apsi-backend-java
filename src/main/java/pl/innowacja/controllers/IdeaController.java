package pl.innowacja.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.innowacja.model.dtos.IdeaDto;
import pl.innowacja.model.entities.IdeaEntity;
import pl.innowacja.services.IdeaService;

import java.util.List;

@RestController
@RequestMapping("/ideas")
@RequiredArgsConstructor
public class IdeaController {

  private final IdeaService ideaService;

  @GetMapping
  public List<IdeaDto> getAllIdeas() {
    return ideaService.getAll();
  }

  @PostMapping
  public void saveIdea(@RequestBody IdeaDto ideaDto) {
    ideaService.saveIdea(ideaDto);
  }

  @GetMapping("/by-subject")
  public List<IdeaEntity> getIdeasBySubject(@RequestParam Integer subjectId) {
    return ideaService.getIdeasForSubject(subjectId);
  }

  @GetMapping("/{id}")
  public IdeaDto getIdeaById(@PathVariable Integer id) {
    return ideaService.getById(id);
  }

}
