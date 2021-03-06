package pl.innowacja.model.mapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.stereotype.Component;
import pl.innowacja.model.dtos.SubjectDto;

@Component
public class GenericMapper {

  private final MapperFacade mapper;

  public GenericMapper() {
    var mapperFactory = new DefaultMapperFactory.Builder().build();
    mapperFactory.registerObjectFactory(new SubjectMapper(), SubjectDto.class);
    var converterFactory = mapperFactory.getConverterFactory();
    converterFactory.registerConverter(new UserRoleMapper());
    converterFactory.registerConverter(new IdeaStatusMapper());

    this.mapper = mapperFactory.getMapperFacade();
  }

  public <I, O> O map(I input, Class<O> outputClazz) {
    return mapper.map(input, outputClazz);
  }

}
