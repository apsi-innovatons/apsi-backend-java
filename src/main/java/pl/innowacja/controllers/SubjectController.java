package pl.innowacja.controllers;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.innowacja.model.dtos.SubjectDto;
import pl.innowacja.services.SubjectService;

import java.util.List;

@RestController
@RequestMapping("/subjects")
@CrossOrigin
@RequiredArgsConstructor
public class SubjectController {

  private final SubjectService subjectService;

  @GetMapping
  @ApiOperation(value = "Get all subjects")
  public List<SubjectDto> getAll() {
    return subjectService.getAll();
  }

  @GetMapping("/{id}")
  @ApiOperation(value = "Get subject with given id")
  public SubjectDto getById(@PathVariable Integer id) {
    return subjectService.getById(id);
  }

  @PostMapping
  @ApiOperation(value = "Save subject in database, returns id of saved entity")
  public Integer save(SubjectDto subjectDto) {
    return subjectService.saveSubject(subjectDto);
  }
}
