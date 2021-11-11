package pl.innowacja.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.innowacja.exception.NoResourceFoundException;
import pl.innowacja.model.dtos.BenefitDto;
import pl.innowacja.model.dtos.CostDto;
import pl.innowacja.model.dtos.IdeaDto;
import pl.innowacja.model.entities.BenefitEntity;
import pl.innowacja.model.entities.CostEntity;
import pl.innowacja.model.entities.IdeaEntity;
import pl.innowacja.model.mapper.GenericMapper;
import pl.innowacja.model.mapper.IdeaMapper;
import pl.innowacja.repositories.BenefitRepository;
import pl.innowacja.repositories.CostRepository;
import pl.innowacja.repositories.IdeaRepository;
import pl.innowacja.repositories.SubjectRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class IdeaService {

  private final IdeaRepository ideaRepository;
  private final BenefitRepository benefitRepository;
  private final CostRepository costRepository;
  private final SubjectRepository subjectRepository;
  private final GenericMapper genericMapper;

  public List<IdeaDto> getAll() {
    return ideaRepository.findAll().stream()
        .map(IdeaMapper::map)
        .collect(Collectors.toList());
  }

  public void saveIdea(IdeaDto ideaDto) {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    var userId = (int) authentication.getCredentials();
    ideaDto.setAuthorId(userId);
    ideaDto.setDate(LocalDate.now());
    var ideaEntity = IdeaMapper.map(ideaDto);

    log.info("Saving idea in database.");
    var savedIdea = ideaRepository.save(ideaEntity);

    var costs = ideaDto.getCosts().stream()
        .map(cost -> genericMapper.map(cost, CostEntity.class))
        .peek(costEntity -> costEntity.setIdeaId(savedIdea.getId()))
        .collect(Collectors.toList());

    log.info("Saving costs for idea {} in database.", savedIdea.getId());
    costRepository.saveAll(costs);

    var benefits = ideaDto.getBenefits().stream()
        .map(benefit -> genericMapper.map(benefit, BenefitEntity.class))
        .peek(benefit -> benefit.setIdeaId(savedIdea.getId()))
        .collect(Collectors.toList());

    log.info("Saving benefits for idea {} in database.", savedIdea.getId());
    benefitRepository.saveAll(benefits);
  }

  public List<IdeaEntity> getIdeasForSubject(Integer subjectId) {
    return ideaRepository.findAll().stream()
        .filter(idea -> idea.getSubjectId().equals(subjectId))
        .collect(Collectors.toList());
  }

  public IdeaDto getById(Integer id) {
    var ideaEntity = ideaRepository.findById(id).orElseThrow(NoResourceFoundException::new);
    var ideaDto = IdeaMapper.map(ideaEntity);
    var costs = costRepository.findAll().stream()
        .filter(cost -> cost.getIdeaId().equals(id))
        .map(costEntity -> genericMapper.map(costEntity, CostDto.class))
        .collect(Collectors.toList());

    var benefits = benefitRepository.findAll().stream()
        .filter(benefit -> benefit.getIdeaId().equals(id))
        .map(benefitEntity -> genericMapper.map(benefitEntity, BenefitDto.class))
        .collect(Collectors.toList());

    ideaDto.setCosts(costs);
    ideaDto.setBenefits(benefits);
    return ideaDto;
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
