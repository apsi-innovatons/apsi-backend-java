package pl.innowacja.model.mapper;

import pl.innowacja.model.dtos.IdeaDto;
import pl.innowacja.model.entities.IdeaEntity;
import pl.innowacja.model.enums.IdeaStatus;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class IdeaMapper {
  public static IdeaEntity map(IdeaDto ideaDto) {
    return new IdeaEntity(
        ideaDto.getId(),
        ideaDto.getTitle(),
        ideaDto.getDescription(),
        ideaDto.getStatus().getValue(),
        ideaDto.getStatusDescription(),
        ideaDto.getDate() == null ? null : Date.valueOf(ideaDto.getDate()),
        ideaDto.getBlocked(),
        ideaDto.getAnonymous(),
        ideaDto.getAuthorId(),
        ideaDto.getSubjectId(),
        mapKeywords(ideaDto.getKeywords())
    );
  }

  public static IdeaDto map(IdeaEntity ideaEntity) {
    return new IdeaDto(
        ideaEntity.getId(),
        ideaEntity.getTitle(),
        ideaEntity.getDescription(),
        IdeaStatus.valueOf(ideaEntity.getStatus()),
        ideaEntity.getStatusDescription(),
        ideaEntity.getDate() == null ? null : ideaEntity.getDate().toLocalDate(),
        ideaEntity.getSubjectId(),
        ideaEntity.getAuthorId(),
        ideaEntity.getAnonymous(),
        ideaEntity.getBlocked(),
        mapKeywords(ideaEntity.getKeywords()),
        null,
        null
    );
  }

  private static String mapKeywords(List<String> keywords) {
    var sb = new StringBuilder();
    keywords.forEach(keyword -> sb.append(keyword).append(','));
    return sb.toString();
  }

  private static List<String> mapKeywords(String keywords) {
    return Arrays.stream(keywords.split(",")).collect(Collectors.toList());
  }
}