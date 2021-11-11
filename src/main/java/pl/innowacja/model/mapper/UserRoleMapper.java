package pl.innowacja.model.mapper;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;
import pl.innowacja.model.enums.UserRole;

public class UserRoleMapper extends BidirectionalConverter<UserRole, Integer> {
  @Override
  public Integer convertTo(UserRole source, Type<Integer> destinationType, MappingContext mappingContext) {
    return source.getValue();
  }

  @Override
  public UserRole convertFrom(Integer source, Type<UserRole> destinationType, MappingContext mappingContext) {
    return UserRole.valueOf(source);
  }
}
