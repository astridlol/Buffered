package sh.astrid.buffered

import cc.ekblad.toml.decode
import cc.ekblad.toml.tomlMapper
import me.honkling.commando.CommandManager
import org.bukkit.plugin.java.JavaPlugin
import sh.astrid.buffered.data.Database
import sh.astrid.buffered.data.Kits
import sh.astrid.buffered.data.Messages
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
        var cooldowns = CooldownManager()
    }

    fun reloadKits() {
        val mapper = tomlMapper {}

        dataFolder.mkdir()

        val kitConfig = dataFolder.resolve("kits.toml")
        saveResource("kits.toml", false)

        val msgConfig = dataFolder.resolve("messages.toml")
        saveResource("messages.toml", false)

        kitData = mapper.decode(kitConfig.toPath())
        messageData = mapper.decode(msgConfig.toPath())
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