package pl.uksford.api.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import pl.uksford.api.entity.AcademicDegree;

@Converter
public class AcademicDegreeConverter implements AttributeConverter<AcademicDegree, String> {

    @Override
    public String convertToDatabaseColumn(AcademicDegree academicDegree) {
        if (academicDegree == null) throw new IllegalArgumentException("AcademicDegree cannot be null.");
        return academicDegree.getLabel();
    }

    @Override
    public AcademicDegree convertToEntityAttribute(String dbData) {
        if (dbData == null) throw new IllegalArgumentException("String to convert to AcademicDegree cannot be null.");
        for (AcademicDegree degree : AcademicDegree.values()) {
            if (degree.getLabel().equals(dbData)) {
                return degree;
            }
        }
        throw new IllegalArgumentException("Unknown database value: " + dbData);
    }
}
