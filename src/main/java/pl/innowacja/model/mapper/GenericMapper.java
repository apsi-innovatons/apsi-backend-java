package pl.innowacja.model.mapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.stereotype.Component;

@Component
public class GenericMapper {
  private final MapperFacade mapper;

  public GenericMapper() {
    var mapperFactory = new DefaultMapperFactory.Builder().build();
    var converterFactory = mapperFactory.getConverterFactory();
    converterFactory.registerConverter(new UserRoleMapper());
    //TODO add status as well
    this.mapper = mapperFactory.getMapperFacade();
  }

  public <I, O> O map(I input, Class<O> outputClazz) {
    return mapper.map(input, outputClazz);
  }

}
