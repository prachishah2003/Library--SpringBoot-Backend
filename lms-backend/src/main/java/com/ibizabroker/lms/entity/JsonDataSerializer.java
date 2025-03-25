package com.ibizabroker.lms.entity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Custom JSON serializer for Date objects.
 * This class provides a consistent date format (dd-MM-yyyy) when serializing
 * Date objects to JSON in the API responses.
 *
 * @author codematrix
 * @version 1.0
 */
@Component
public class JsonDataSerializer extends JsonSerializer<Date> {

    /**
     * Serializes a Date object to a JSON string in the format "dd-MM-yyyy".
     *
     * @param date The Date object to serialize
     * @param gen The JSON generator to write the serialized value
     * @param serializers The serializer provider
     * @throws IOException if there is an error writing to the JSON generator
     */
    @Override
    public void serialize(Date date, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        gen.writeString(simpleDateFormat.format(date));
    }

}
