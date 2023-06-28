package sh.astrid.buffered

import cc.ekblad.toml.decode
import cc.ekblad.toml.tomlMapper
import me.honkling.commando.CommandManager
import org.bukkit.plugin.java.JavaPlugin
import sh.astrid.buffered.data.*
import sh.astrid.buffered.events.*
import sh.astrid.buffered.events.abiliites.KnightAbility
import sh.astrid.buffered.events.abiliites.WitchAbility
import sh.astrid.buffered.lib.CombatManager
import sh.astrid.buffered.lib.CooldownManager
import sh.astrid.buffered.lib.forever
import sh.astrid.buffered.scoreboard.fastboard.FastBoard
import sh.astrid.buffered.scoreboard.updateScoreboard
import java.util.*


class Buffered : JavaPlugin() {
    companion object {
        lateinit var instance: Buffered
        lateinit var kitData: Kits
        lateinit var messageData: Messages
        lateinit var tagData: Tags
        lateinit var shopData: Shop
        var boards: MutableMap<UUID, FastBoard> = HashMap()
        var cooldowns = CooldownManager()
        var combatLog = CombatManager()
    }

    fun reloadConfigs() {
        val mapper = tomlMapper {}
        dataFolder.mkdir()

        val fileConfigs = listOf(
                "kits.toml",
                "messages.toml",
                "tags.toml",
                "shop.toml"
        )

        fileConfigs.forEach { configFile ->
            val configPath = dataFolder.resolve(configFile)
            saveResource(configFile, false)
            when (configFile) {
                "kits.toml" -> kitData = mapper.decode(configPath.toPath())
                "messages.toml" -> messageData = mapper.decode(configPath.toPath())
                "tags.toml" -> tagData = mapper.decode(configPath.toPath())
                "shop.toml" -> shopData = mapper.decode(configPath.toPath())
            }
        }
    }

    init {
        reloadConfigs()
    }

    override fun onEnable() {
        instance = this

        saveDefaultConfig()
        Database.load()

        // Sets up the command handler
        val commandManager = CommandManager(instance)
        commandManager.registerCommands("sh.astrid.buffered.commands")

        forever({ _ ->
            boards.forEach {
                it.value.updateScoreboard()
            }
       }, 20 * 3)


        // Setup events
        JoinListener()
        ChatListener()
        EntityDamageListener()
        BlockListener()

        // Abilities
        KnightAbility()
        WitchAbility()
    }

    override fun onDisable() {
        Database.close()
    }
}