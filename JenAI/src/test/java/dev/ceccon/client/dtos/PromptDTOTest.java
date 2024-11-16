package dev.ceccon.client.dtos;

import dev.ceccon.config.APIConfig;
import dev.ceccon.conversation.Chat;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PromptDTOTest {

    @Test
    void keepsMessageOnChatWhenBuilding() {
        Chat chat = new Chat();
        chat.addMessage("system", "System message");
        APIConfig config = new APIConfig();

        PromptDTO dto = PromptDTO.build(chat, config);

        List<PromptDTO.MessageDTO> messages = dto.getMessages();

        assertEquals(chat.getMessages().get(0).role(), messages.get(0).role());
        assertEquals(chat.getMessages().get(0).content(), messages.get(0).content());
    }

    @Test
    void keepsMultipleMessagesOnChatWhenBuilding() {
        Chat chat = new Chat();
        chat.addMessage("system", "System message");
        chat.addMessage("user", "First user message");
        APIConfig config = new APIConfig();

        PromptDTO dto = PromptDTO.build(chat, config);

        List<PromptDTO.MessageDTO> messages = dto.getMessages();

        assertEquals(chat.getMessages().get(0).role(), messages.get(0).role());
        assertEquals(chat.getMessages().get(0).content(), messages.get(0).content());
        assertEquals(chat.getMessages().get(1).role(), messages.get(1).role());
        assertEquals(chat.getMessages().get(1).content(), messages.get(1).content());
    }

    @Test
    void usesTemperateFromConfigWhenBuilding() {
        double temperature = 0.5;

        Chat chat = new Chat();
        APIConfig config = new APIConfig();
        config.setTemperature(temperature);

        PromptDTO dto = PromptDTO.build(chat, config);
        double temperatureOnDto = dto.getTemperature();

        assertEquals(temperature, temperatureOnDto);
    }

    @Test
    void usesModelFromConfigWhenBuilding() {
        String model = "a_model";

        Chat chat = new Chat();
        APIConfig config = new APIConfig();
        config.setModel(model);

        PromptDTO dto = PromptDTO.build(chat, config);
        String modelOnDto = dto.getModel();

        assertEquals(model, modelOnDto);
    }

    @Test
    void temperatureIsZeroByDefault() {
        PromptDTO dto = PromptDTO.build(new Chat(), new APIConfig());

        assertEquals(0.0, dto.getTemperature());
    }

    @Test
    void getAndSetModel() {
        String model = "model_a";

        PromptDTO dto = PromptDTO.build(new Chat(), new APIConfig());
        dto.setModel(model);

        String modelOnDto = dto.getModel();

        assertEquals(model, modelOnDto);
    }

    @Test
    void getAndSetStream() {
        boolean stream = true;

        PromptDTO dto = PromptDTO.build(new Chat(), new APIConfig());
        dto.setStream(stream);

        boolean streamOnDto = dto.isStream();

        assertEquals(stream, streamOnDto);
    }

    @Test
    void getAndSetTemperature() {
        double temperature = 0.5;
        double temperature2 = 0.75;

        PromptDTO dto = PromptDTO.build(new Chat(), new APIConfig());

        dto.setTemperature(temperature);
        assertEquals(temperature, dto.getTemperature());

        dto.setTemperature(temperature2);
        assertEquals(temperature2, dto.getTemperature());
    }

}