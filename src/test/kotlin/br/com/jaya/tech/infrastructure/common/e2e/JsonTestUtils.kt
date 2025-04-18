package br.com.jaya.tech.infrastructure.common.e2e

import net.minidev.json.JSONValue
import java.nio.charset.StandardCharsets

object JsonTestUtils {

    fun readJsonAsString(path: String): String {
        val resourcePath = "/json/$path"
        val content = JsonTestUtils::class.java
            .getResourceAsStream(resourcePath)
            ?.bufferedReader(StandardCharsets.UTF_8)
            ?.use { it.readText() }
            ?: throw IllegalArgumentException("Failed to load resource file")
        val parsedContent = JSONValue.parse(content)
            ?: throw IllegalArgumentException("Falha ao parsear JSON em '$resourcePath'")
        return parsedContent.toString()
    }

}