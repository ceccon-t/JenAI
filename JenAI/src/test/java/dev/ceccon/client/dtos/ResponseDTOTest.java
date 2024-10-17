package dev.ceccon.client.dtos;

import dev.ceccon.client.response.BlockResponse;
import dev.ceccon.client.response.UsageMetrics;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResponseDTOTest {

    @Test
    void buildsBlockResponseCorrectly() {
        String role = "user";
        String content = "aaa";
        Integer completionTokens = 5;
        Integer promptTokens = 6;
        Integer totalTokens = 11;
        UsageMetrics metrics = new UsageMetrics(completionTokens, promptTokens, totalTokens);
        ResponseDTO.MessageDTO messageDTO = new ResponseDTO.MessageDTO(role, content);
        ResponseDTO.ChoiceDTO choiceDTO = new ResponseDTO.ChoiceDTO("", 0, messageDTO);
        ResponseDTO.UsageDTO usageDTO = new ResponseDTO.UsageDTO(completionTokens, promptTokens, totalTokens);
        ResponseDTO dto = new ResponseDTO();
        dto.setChoices(List.of(choiceDTO));
        dto.setUsage(usageDTO);

        BlockResponse blockResponse = dto.toBlockResponse();

        assertEquals(role, blockResponse.getRole());
        assertEquals(content, blockResponse.getContent());
        assertEquals(metrics, blockResponse.getMetrics());
    }

}