package hanieum.conik.adapter.member.persistence;

import hanieum.conik.domain.member.shared.Email;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EmailAttributeConverter
        implements AttributeConverter<Email,String> {

    @Override
    public String convertToDatabaseColumn(Email attribute) {
        return attribute == null ? null : attribute.address();
    }

    @Override
    public Email convertToEntityAttribute(String dbData) {
        return dbData == null || dbData.isBlank()
                ? null
                : new Email(dbData);
    }
}
