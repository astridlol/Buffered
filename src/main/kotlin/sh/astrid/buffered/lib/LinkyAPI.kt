package sh.astrid.buffered.lib

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginManager
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

object LinkyAPI {
    @Serializable
    data class UserInfoResponse(
            val success: Boolean,
            val isInGuild: Boolean,
            val id: String,
            val createdAt: String,
            val discordId: String,
            val username: String,
            val uuid: String,
            val status: String
    )

    private fun getLinkyConfig(): YamlConfiguration {
        val pluginManager: PluginManager = Bukkit.getPluginManager()
        val linky: Plugin = pluginManager.getPlugin("Linky") ?: throw Exception("Linky is not enabled")
        val configFile = File(linky.dataFolder, "config.yml")
        return YamlConfiguration.loadConfiguration(configFile)
    }

    // Note: I used native methods for this because I only needed to do one web request.
    // Might switch over to OkHttp if time ever needs it.

    fun getUser(uuid: UUID): UserInfoResponse? {
        val apiUrl = "https://linky.astrid.sh/api/users/$uuid"

        val url = URL(apiUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        val apiKey = getLinkyConfig().getString("token")

        connection.setRequestProperty("Authorization", "Bearer $apiKey")

        val responseCode = connection.responseCode
        val response = StringBuilder()

        if (responseCode == HttpURLConnection.HTTP_OK) {
            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
            reader.close()
        } else return null

        connection.disconnect()

        val json = response.toString()

        return try {
            Json.decodeFromString<UserInfoResponse>(json)
        } catch (e: Exception) {
            null
        }
    }
}
