package pl.innowacja.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.innowacja.model.dtos.SubjectDto;
import pl.innowacja.services.SubjectService;

import java.util.List;

@RestController
@RequestMapping("/subjects")
@CrossOrigin
@Api(tags = {"backendApi"})
@RequiredArgsConstructor
public class SubjectController {

  private final SubjectService subjectService;

  @GetMapping
  @ApiOperation(value = "Get all subjects")
  public List<SubjectDto> getAllSubjects() {
    return subjectService.getAll();
  }

  @GetMapping("/{id}")
  @ApiOperation(value = "Get subject with given id")
  public SubjectDto getSubjectById(@PathVariable Integer id) {
    return subjectService.getById(id);
  }

  @PostMapping
  @ApiOperation(value = "Save subject in database, returns id of saved entity")
  public Integer saveSubject(@RequestBody SubjectDto subjectDto) {
    return subjectService.saveSubject(subjectDto);
  }

  @GetMapping("/{id}/committee")
  @ApiOperation(value = "Get committee members for given subject")
  public List<Integer> getCommitteeIdsBySubjectId(@PathVariable Integer id) {
    return subjectService.getCommitteeMembersForSubject(id);
  }

  @GetMapping("/current-user")
  @ApiOperation(value = "Get ids of subjects that current user belongs to")
  public List<Integer> getSubjectIdsForCurrentUser() {
    return subjectService.getSubjectIdsOfCurrentUser();
  }
}
