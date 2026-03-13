package pl.uksford.api.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import pl.uksford.api.entity.UserRole;

@Converter
public class UserRoleConverter implements AttributeConverter<UserRole, String> {

    @Override
    public String convertToDatabaseColumn(UserRole userRole) {
        if (userRole == null) throw new IllegalArgumentException("UserRole cannot be null.");
        return userRole.toString();
    }

    @Override
    public UserRole convertToEntityAttribute(String dbData) {
        if (dbData == null) throw new IllegalArgumentException("String to convert to UserRole cannot be null.");
        for (UserRole role : UserRole.values()) {
            if (role.toString().equals(dbData)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown database value: " + dbData);
    }
}
