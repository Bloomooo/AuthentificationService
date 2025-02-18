package org.acme.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.smallrye.mutiny.Uni;

import java.io.IOException;

public class UniSerializer extends JsonSerializer<Uni<?>> {
    @Override
    public void serialize(Uni<?> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null) {
            gen.writeStartObject();
            gen.writeStringField("value", value.toString());
            gen.writeEndObject();
        }
    }
}

