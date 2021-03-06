package pl.innowacja.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.innowacja.model.dtos.DecisionDto;
import pl.innowacja.model.dtos.IdeaDto;
import pl.innowacja.model.dtos.RatingSettingDto;
import pl.innowacja.model.requests.AddRatingSettingsDto;
import pl.innowacja.services.IdeaService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/ideas")
@CrossOrigin
@Api(tags = {"backendApi"})
@RequiredArgsConstructor
@Validated
public class IdeaController {

  private final IdeaService ideaService;

  @GetMapping
  @ApiOperation(value = "Get simplified DTO's of all ideas in database (without costs, benefits or attachments)")
  public List<IdeaDto> getAllIdeas() {
    return ideaService.getAll();
  }

  @PostMapping
  @ApiOperation(value = "Save Idea in database, returns id of saved entity")
  public Integer saveIdea(@Valid @RequestBody IdeaDto ideaDto) {
    return ideaService.saveIdea(ideaDto);
  }

  @PutMapping
  @ApiOperation(value = "Update existing Idea in database.")
  public void updateIdea(@RequestBody IdeaDto ideaDto) {
    ideaService.update(ideaDto);
  }

  @DeleteMapping("/{id}")
  @ApiOperation(value = "Delete Idea with given id (also deletes all of its attachments, costs and benefits)")
  public void deleteIdeaById(@PathVariable Integer id) {
    ideaService.deleteIdea(id);
  }

  @GetMapping("/by-subject")
  @ApiOperation(value = "Get all ideas with given subject")
  public List<IdeaDto> getIdeasBySubjectId(@RequestParam Integer subjectId) {
    return ideaService.getIdeasForSubject(subjectId);
  }

  @GetMapping("/uncategorized")
  @ApiOperation(value = "Get all ideas with no category")
  public List<IdeaDto> getUncategorizedIdeas() {
    return ideaService.getUncategorizedIdeas();
  }

  @GetMapping("/{id}")
  @ApiOperation(value = "Get idea with given id")
  public IdeaDto getIdeaById(@PathVariable Integer id) {
    return ideaService.getById(id);
  }

  @PostMapping("/{ideaId}/rating-settings")
  public void saveRatingSatingsByIdeaId(@PathVariable Integer ideaId,
                                        @RequestBody @Valid AddRatingSettingsDto addRatingSettingsDto) {
    ideaService.saveRatingSettingsByIdeaId(ideaId, addRatingSettingsDto);
  }

  @GetMapping("/{ideaId}/rating-settings")
  public List<RatingSettingDto> getRatingSettingsByIdeaId(@PathVariable Integer ideaId) {
    return ideaService.getRatingSettingsByIdeaId(ideaId);
  }

  @PutMapping("/{ideaId}/rating-settings")
  public void updateExistingRatingSettingsByIdeaId(@PathVariable Integer ideaId,
                                                   @RequestBody List<RatingSettingDto> newRatingSettings) {
    ideaService.updateExistingRatingSettingsByIdeaId(ideaId, newRatingSettings);
  }

  @DeleteMapping("/{ideaId}/rating-settings")
  public void deleteRatingSettingsByIdeaId(@PathVariable Integer ideaId) {
    ideaService.deleteRatingSettingsByIdeaId(ideaId);
  }

  @PutMapping("/{ideaId}/decision")
  public void addDecisionForIdea(@PathVariable Integer ideaId, @RequestBody DecisionDto decisionDto) {
    ideaService.addDecisionForIdea(decisionDto, ideaId);
  }
}
