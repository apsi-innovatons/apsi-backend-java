package pl.innowacja.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pl.innowacja.services.IdeaService;
import pl.innowacja.entities.BenefitEntity;
import pl.innowacja.entities.IdeaEntity;

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
