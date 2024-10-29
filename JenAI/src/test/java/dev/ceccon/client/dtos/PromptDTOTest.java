package dev.ceccon.client.dtos;

import dev.ceccon.conversation.Chat;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PromptDTOTest {

    @Test
    void temperatureIsZeroByDefault() {
        PromptDTO dto = PromptDTO.forChat(new Chat());

        assertEquals(0.0, dto.getTemperature());
    }

    @Test
    void getAndSetTemperature() {
        double temperature = 0.5;
        double temperature2 = 0.75;

        PromptDTO dto = PromptDTO.forChat(new Chat());

        dto.setTemperature(temperature);
        assertEquals(temperature, dto.getTemperature());

        dto.setTemperature(temperature2);
        assertEquals(temperature2, dto.getTemperature());
    }

}