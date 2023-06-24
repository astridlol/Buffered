package sh.astrid.buffered

import cc.ekblad.toml.decode
import cc.ekblad.toml.tomlMapper
import me.honkling.commando.CommandManager
import org.bukkit.plugin.java.JavaPlugin
import sh.astrid.buffered.data.Database
import sh.astrid.buffered.data.Kits
import sh.astrid.buffered.data.Messages
import sh.astrid.buffered.data.Tags
import sh.astrid.buffered.events.EntityDamageListener
import sh.astrid.buffered.events.JoinListener
import sh.astrid.buffered.events.abiliites.KnightAbility
import sh.astrid.buffered.events.abiliites.WitchAbility
import sh.astrid.buffered.lib.CooldownManager

class Buffered : JavaPlugin() {
    companion object {
        lateinit var instance: Buffered
        lateinit var kitData: Kits
        lateinit var messageData: Messages
        lateinit var tagData: Tags
        var cooldowns = CooldownManager()
    }

    fun reloadKits() {
        val mapper = tomlMapper {}
        dataFolder.mkdir()

        val fileConfigs = listOf(
                "kits.toml" to "kits.toml",
                "messages.toml" to "messages.toml",
                "tags.toml" to "tags.toml"
        )

        fileConfigs.forEach { (configFile, resourceName) ->
            val configPath = dataFolder.resolve(configFile)
            saveResource(resourceName, false)
            when (resourceName) {
                "kits.toml" -> kitData = mapper.decode(configPath.toPath())
                "messages.toml" -> messageData = mapper.decode(configPath.toPath())
                "tags.toml" -> tagData = mapper.decode(configPath.toPath())
            }
        }
    }

    init {
        reloadKits()
    }

    override fun onEnable() {
        instance = this

        saveDefaultConfig()
        Database.load()

        // Sets up the command handler
        val commandManager = CommandManager(instance)
        commandManager.registerCommands("sh.astrid.buffered.commands")


        // Setup events
        JoinListener()
        EntityDamageListener()

        // Abilities
        KnightAbility()
        WitchAbility()
    }

    override fun onDisable() {
        Database.close()
    }
}