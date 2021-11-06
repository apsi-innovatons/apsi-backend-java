package pl.innowacja.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.innowacja.entities.BenefitEntity;
import pl.innowacja.entities.IdeaEntity;
import pl.innowacja.services.IdeaService;

import java.util.List;

@RestController
@RequestMapping("/ideas")
@RequiredArgsConstructor
public class IdeaController {

  private final IdeaService ideaService;

  @GetMapping
  public List<IdeaEntity> getAllIdeas() {
    return ideaService.getAll();
  }

  @GetMapping("/by-subject")
  public List<IdeaEntity> getIdeasBySubject(@RequestParam String subject) {
    return ideaService.getIdeasForSubject(subject);
  }

  @GetMapping("/{id}")
  public IdeaEntity getIdeaById(@PathVariable Integer id) {
    return ideaService.getById(id);
  }

  @GetMapping("/{id}/benefits")
  public List<BenefitEntity> getBenefitsForIdea(@PathVariable Integer id) {
    return ideaService.getBenefitsForIdea(id);
  }
}
