package dev.ceccon.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LLMSanitizerTest {

    @Test
    void sanitizingSpecialTokensReturnsStringWithNoSpecialTokens() {
        String withSpecialTokens = "</s><|eot_id|>";
        String noSpecialTokens = "";

        String sanitized = LLMSanitizer.sanitizeLLMSpecialTokens(withSpecialTokens);

        assertEquals(noSpecialTokens, sanitized);
    }

    @Test
    void sanitizeForChatAlsoSanitizesSpecialTokens() {
        String withSpecialTokens = "</s><|eot_id|>";
        String noSpecialTokens = "";

        String sanitized = LLMSanitizer.sanitizeForChat(withSpecialTokens);

        assertEquals(noSpecialTokens, sanitized);
    }

    @Test
    void sanitizeForChatSanitizesVariationsOfA() {
        String problemVersionLowerCase = "áàâäã";
        String safeVersionLowerCase = "aaaaa";

        String sanitizedLowerCase = LLMSanitizer.sanitizeForChat(problemVersionLowerCase);

        assertEquals(safeVersionLowerCase, sanitizedLowerCase);

        String problemVersionUpperCase = "ÁÀÂÄÃ";
        String safeVersionUpperCase = "AAAAA";

        String sanitizedUpperCase = LLMSanitizer.sanitizeForChat(problemVersionUpperCase);

        assertEquals(sanitizedUpperCase, safeVersionUpperCase);
    }

    @Test
    void sanitizeForChatSanitizesVariationsOfE() {
        String problemVersionLowerCase = "éèêëẽ";
        String safeVersionLowerCase = "eeeee";

        String sanitizedLowerCase = LLMSanitizer.sanitizeForChat(problemVersionLowerCase);

        assertEquals(safeVersionLowerCase, sanitizedLowerCase);

        String problemVersionUpperCase = "ÉÈÊËẼ";
        String safeVersionUpperCase = "EEEEE";

        String sanitizedUpperCase = LLMSanitizer.sanitizeForChat(problemVersionUpperCase);

        assertEquals(sanitizedUpperCase, safeVersionUpperCase);
    }

    @Test
    void sanitizeForChatSanitizesVariationsOfI() {
        String problemVersionLowerCase = "íìîïĩ";
        String safeVersionLowerCase = "iiiii";

        String sanitizedLowerCase = LLMSanitizer.sanitizeForChat(problemVersionLowerCase);

        assertEquals(safeVersionLowerCase, sanitizedLowerCase);

        String problemVersionUpperCase = "ÍÌÎÏĨ";
        String safeVersionUpperCase = "IIIII";

        String sanitizedUpperCase = LLMSanitizer.sanitizeForChat(problemVersionUpperCase);

        assertEquals(sanitizedUpperCase, safeVersionUpperCase);
    }

    @Test
    void sanitizeForChatSanitizesVariationsOfO() {
        String problemVersionLowerCase = "óòôöõ";
        String safeVersionLowerCase = "ooooo";

        String sanitizedLowerCase = LLMSanitizer.sanitizeForChat(problemVersionLowerCase);

        assertEquals(safeVersionLowerCase, sanitizedLowerCase);

        String problemVersionUpperCase = "ÓÒÔÖÕ";
        String safeVersionUpperCase = "OOOOO";

        String sanitizedUpperCase = LLMSanitizer.sanitizeForChat(problemVersionUpperCase);

        assertEquals(sanitizedUpperCase, safeVersionUpperCase);
    }

    @Test
    void sanitizeForChatSanitizesVariationsOfU() {
        String problemVersionLowerCase = "úùûüũ";
        String safeVersionLowerCase = "uuuuu";

        String sanitizedLowerCase = LLMSanitizer.sanitizeForChat(problemVersionLowerCase);

        assertEquals(safeVersionLowerCase, sanitizedLowerCase);

        String problemVersionUpperCase = "ÙÙÛÜŨ";
        String safeVersionUpperCase = "UUUUU";

        String sanitizedUpperCase = LLMSanitizer.sanitizeForChat(problemVersionUpperCase);

        assertEquals(sanitizedUpperCase, safeVersionUpperCase);
    }

    @Test
    void sanitizeForChatSanitizesVariationsOfN() {
        String problemVersionLowerCase = "ñ";
        String safeVersionLowerCase = "n";

        String sanitizedLowerCase = LLMSanitizer.sanitizeForChat(problemVersionLowerCase);

        assertEquals(safeVersionLowerCase, sanitizedLowerCase);

        String problemVersionUpperCase = "Ñ";
        String safeVersionUpperCase = "N";

        String sanitizedUpperCase = LLMSanitizer.sanitizeForChat(problemVersionUpperCase);

        assertEquals(sanitizedUpperCase, safeVersionUpperCase);
    }

    @Test
    void sanitizesForChatSanitizesVariationsOfC() {
        String problemVersionLowerCase = "ç";
        String safeVersionLowerCase = "c";

        String sanitizedLowerCase = LLMSanitizer.sanitizeForChat(problemVersionLowerCase);

        assertEquals(safeVersionLowerCase, sanitizedLowerCase);

        String problemVersionUpperCase = "Ç";
        String safeVersionUpperCase = "C";

        String sanitizedUpperCase = LLMSanitizer.sanitizeForChat(problemVersionUpperCase);

        assertEquals(sanitizedUpperCase, safeVersionUpperCase);
    }

    @Test
    void sanitizesForChatSanitizesVariationsOfSS() {
        String problemVersion = "ß";
        String safeVersion = "ss";

        String sanitized = LLMSanitizer.sanitizeForChat(problemVersion);

        assertEquals(safeVersion, sanitized);
    }

    @Test
    void sanitizesForChatSanitizesPunctuation() {
        String problemVersionSpanishStarters = "¿¡";
        String safeVersionSpanishStarters = "";

        String sanitizedSpanishStarters = LLMSanitizer.sanitizeForChat(problemVersionSpanishStarters);

        assertEquals(safeVersionSpanishStarters, sanitizedSpanishStarters);

        String problemVersionDashes = "–—";
        String safeVersionDashes = "--";

        String sanitizedDashes = LLMSanitizer.sanitizeForChat(problemVersionDashes);

        assertEquals(sanitizedDashes, safeVersionDashes);
    }

    @Test
    void sanitizesForChatSanitizesQuotes() {
        String problemVersionSingleQuotes = "’‘";
        String safeVersionSingleQuotes = "''";

        String sanitizedSingleQuotes = LLMSanitizer.sanitizeForChat(problemVersionSingleQuotes);

        assertEquals(safeVersionSingleQuotes, sanitizedSingleQuotes);

        String problemVersionDoubleQuotes = "“”";
        String safeVersionDoubleQuotes = "\"\"";

        String sanitizedDoubleQuotes = LLMSanitizer.sanitizeForChat(problemVersionDoubleQuotes);

        assertEquals(sanitizedDoubleQuotes, safeVersionDoubleQuotes);
    }

}