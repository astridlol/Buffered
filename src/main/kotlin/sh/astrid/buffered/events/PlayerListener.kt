package sh.astrid.buffered.events

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import sh.astrid.buffered.Buffered
import sh.astrid.buffered.data.PlayerData
import sh.astrid.buffered.data.player.Players
import me.clip.placeholderapi.PlaceholderAPI
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import sh.astrid.buffered.data.player.BufferedPlayer
import sh.astrid.buffered.lib.extensions.getMMResolvers
import sh.astrid.buffered.lib.extensions.mm
import sh.astrid.buffered.lib.forever
import sh.astrid.buffered.scoreboard.FastBoard



fun Player.updateScoreboard() {
    fun String.parseMM(player: Player): Component {
        // would be better if we had some sort of internal caching system to reduce stress
        val playerData = BufferedPlayer(player.uniqueId).getPlayer()

        val defaultResolvers = listOf<TagResolver>(
                Placeholder.parsed("kills", playerData.kills.toString()),
                Placeholder.parsed("killstreak", playerData.killstreak.toString()),
                Placeholder.parsed("deaths", playerData.deaths.toString()),
        ).toTypedArray()

        val mm = MiniMessage.miniMessage()

        return mm.deserialize(this, *getMMResolvers(), *defaultResolvers)
                .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)
    }
    fun String.parse(p: Player): String {
        return PlaceholderAPI.setPlaceholders(p, this)
    }

    Buffered.instance.logger.info("Scoreboard updated for ${this.uniqueId}")
    val scoreboard = FastBoard(this)

    val srv = Buffered.instance.server

    val online = srv.onlinePlayers.size
    val total = srv.maxPlayers
    val bullet = "<s> •<p>"

    val ping = "%player_ping%".parse(this)
    val tps = "%server_tps_1%".parse(this)

    scoreboard.updateTitle("<p><b><u>Buffered</u></b> <dark_gray>(<p>$online/$total</p>)".mm());
    // Change the lines
    scoreboard.updateLines(
            listOf(
                    "",
                    "<s>Player",
                    "$bullet Rank: <t>%vault_rank%".parse(this),
                    "",
                    "<s>Stats",
                    "$bullet Kills: <t><kills>",
                    "$bullet Killstreak: <t><killstreak>",
                    "$bullet Deaths: <t><deaths>",
                    "",
                    "<s>Server",
                    "$bullet TPS: <t>$tps</t>, Ping: <t>$ping</t>",
            ).map {
                it.parseMM(this)
            }
    );
}

class JoinListener : Listener {
    init {
        Buffered.instance.server.pluginManager.registerEvents(this, Buffered.instance)
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val players = Players()

        val p = event.player
        val playerData = players.getPlayer(p.uniqueId)

        forever({
            p.updateScoreboard()
        }, 20 * 30)

        if(playerData != null) {
            Bukkit.broadcast("<s>→<p> ${p.name}".mm())
            return
        }

        // If data doesn't exist for them, create it
        players.createPlayer(PlayerData(p.uniqueId))

        val playerCount = players.getCount()
        Bukkit.broadcast("<s>→ <p>Welcome, ${p.name} <l><i>(#$playerCount)".mm())
    }
}