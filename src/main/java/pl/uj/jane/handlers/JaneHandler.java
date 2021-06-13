package pl.uj.jane.handlers;

import com.amazon.ask.Skill;
import com.amazon.ask.builder.StandardSkillBuilder;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.ResponseEnvelope;
import com.amazon.ask.model.services.util.JacksonSerializer;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class JaneHandler implements RequestStreamHandler {

    private final Skill skill;
    private final JacksonSerializer jacksonSerializer;

    public JaneHandler() {
        this.skill = new StandardSkillBuilder().addRequestHandler(new RandomNumberHandler()).addRequestHandler(new LaunchRequestHandlerImpl()).build();
        this.jacksonSerializer = new JacksonSerializer();
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        context.getLogger().log("Request received");
        String request = IOUtils.toString(inputStream);
        RequestEnvelope requestEnvelope = new JacksonSerializer().deserialize(request, RequestEnvelope.class);
        ResponseEnvelope responseEnvelope = this.skill.invoke(requestEnvelope);
        byte[] response = jacksonSerializer.serialize(responseEnvelope).getBytes(StandardCharsets.UTF_8);
        outputStream.write(response);
    }
}
