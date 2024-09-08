package hexlet.code.app.mapper;

import hexlet.code.app.dto.UserCreateDTO;
import hexlet.code.app.dto.UserDTO;
import hexlet.code.app.dto.UserUpdateDTO;
import hexlet.code.app.model.User;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-04T15:46:53+0300",
    comments = "version: 1.6.0.Beta1, compiler: javac, environment: Java 21.0.3 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl extends UserMapper {

    @Autowired
    private JsonNullableMapper jsonNullableMapper;

    @Override
    public User map(UserCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();

        user.setPasswordDigest( dto.getPassword() );
        user.setFirstName( dto.getFirstName() );
        user.setLastName( dto.getLastName() );
        user.setEmail( dto.getEmail() );

        return user;
    }

    @Override
    public UserDTO map(User model) {
        if ( model == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId( model.getId() );
        userDTO.setEmail( model.getEmail() );
        userDTO.setFirstName( model.getFirstName() );
        userDTO.setLastName( model.getLastName() );
        userDTO.setCreatedAt( model.getCreatedAt() );

        return userDTO;
    }

    @Override
    public void updateEntity(UserUpdateDTO dto, User model) {
        if ( dto == null ) {
            return;
        }

        if ( jsonNullableMapper.isPresent( dto.getFirstName() ) ) {
            model.setFirstName( jsonNullableMapper.unwrap( dto.getFirstName() ) );
        }
        if ( jsonNullableMapper.isPresent( dto.getLastName() ) ) {
            model.setLastName( jsonNullableMapper.unwrap( dto.getLastName() ) );
        }
        if ( jsonNullableMapper.isPresent( dto.getEmail() ) ) {
            model.setEmail( jsonNullableMapper.unwrap( dto.getEmail() ) );
        }
    }
}
