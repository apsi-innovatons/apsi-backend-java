package pl.innowacja.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.innowacja.entities.BenefitEntity;
import pl.innowacja.entities.CostEntity;
import pl.innowacja.entities.IdeaEntity;
import pl.innowacja.exception.NoResourceFoundException;
import pl.innowacja.repositories.BenefitRepository;
import pl.innowacja.repositories.CostRepository;
import pl.innowacja.repositories.IdeaRepository;
import pl.innowacja.repositories.SubjectRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IdeaService {

  private final IdeaRepository ideaRepository;
  private final BenefitRepository benefitRepository;
  private final CostRepository costRepository;
  private final SubjectRepository subjectRepository;

  public List<IdeaEntity> getAll() {
    return ideaRepository.findAll();
  }

  public List<IdeaEntity> getIdeasForSubject(String subject) {
    return ideaRepository.findAll().stream()
        .filter(idea -> idea.getSubject().getName().equals(subject))
        .collect(Collectors.toList());
  }

  public IdeaEntity getById(Integer id) {
    return ideaRepository.findById(id).orElseThrow(NoResourceFoundException::new);
  }

  public List<BenefitEntity> getBenefitsForIdea(Integer ideaId) {
    return benefitRepository.findAll().stream()
        .filter(benefit -> benefit.getIdeaId().equals(ideaId))
        .collect(Collectors.toList());
  }

  public List<CostEntity> getCostsForIdea(Integer ideaId) {
    return costRepository.findAll().stream()
        .filter(cost -> cost.getIdeaId().equals(ideaId))
        .collect(Collectors.toList());
  }
}
