package pl.innowacja.model.mapper;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.ObjectFactory;
import pl.innowacja.model.dtos.SubjectDto;
import pl.innowacja.model.entities.SubjectEntity;
import pl.innowacja.model.enums.UserRole;

public class SubjectMapper implements ObjectFactory<SubjectDto> {
  @Override
  public SubjectDto create(Object source, MappingContext mappingContext) {
    if (source instanceof SubjectEntity) {
      var entity = (SubjectEntity) source;
      var dto = new SubjectDto();
      dto.setId(entity.getId());
      dto.setDone(entity.getDone());
      if (entity.getAudience() != null) {
        dto.setAudience(UserRole.valueOf(entity.getAudience()));
      }
      return dto;
    }
    throw new IllegalArgumentException(String.format("Can not convert %s to SubjectDto", source.getClass().getSimpleName()));
  }
}
