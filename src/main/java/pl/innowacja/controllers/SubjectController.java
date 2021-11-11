package pl.innowacja.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.innowacja.model.dtos.SubjectDto;
import pl.innowacja.services.SubjectService;

import java.util.List;

@RestController
@RequestMapping("/subjects")
@RequiredArgsConstructor
public class SubjectController {

  private final SubjectService subjectService;

  @GetMapping
  public List<SubjectDto> getAll() {
    return subjectService.getAll();
  }
}
