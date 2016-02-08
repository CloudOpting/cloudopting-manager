package eu.cloudopting.jsonserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import eu.cloudopting.domain.Applications;

public class ApplicationSummarySerializer extends JsonSerializer<Applications>{

	@Override
	public void serialize(Applications value, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {
    	jgen.writeStartObject();
    	jgen.writeNumberField("id", value.getId());
    	jgen.writeStringField("applicationName", value.getApplicationName());
    	jgen.writeEndObject();
	}
}
