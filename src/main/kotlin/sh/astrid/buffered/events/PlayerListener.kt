package sh.astrid.buffered.events

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import sh.astrid.buffered.Buffered
import sh.astrid.buffered.data.PlayerData
import sh.astrid.buffered.data.player.Players
import org.bukkit.event.player.PlayerQuitEvent
import sh.astrid.buffered.data.player.BufferedPlayer
import sh.astrid.buffered.lib.extensions.mm
import sh.astrid.buffered.scoreboard.createScoreboard
import sh.astrid.buffered.scoreboard.getFastboard
import sh.astrid.buffered.scoreboard.updateScoreboard

class JoinListener : Listener {
    init {
        Buffered.instance.server.pluginManager.registerEvents(this, Buffered.instance)
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val players = Players()

        val p = event.player
        var playerData = players.getPlayer(p.uniqueId)

        val hasJoinedBefore = playerData !== null

        if(!hasJoinedBefore) {
            players.createPlayer(PlayerData(p.uniqueId))
            playerData = players.getPlayer(p.uniqueId)
        }

        if(!hasJoinedBefore) {
            val playerCount = players.getCount()
            event.joinMessage("<s>→ <p>Welcome, ${p.name} <l><i>(#$playerCount)".mm())
        } else {
            event.joinMessage("<s>→<p> ${p.name}".mm())
        }

        // Create a scoreboard for the player
        p.createScoreboard()

        // Update all other scoreboards
        Buffered.boards.forEach {
            it.value.updateScoreboard()
        }

        if(playerData !== null && !playerData.ownedTags.contains("release")) {
            val player = BufferedPlayer(p.uniqueId)
            player.addTag("release")
            player.addBalance(10)
            p.sendMessage("<p>Thanks so much for joining! You've received a <s><bold>tag</s> and <s><bold>10 coins</s> <3".mm())
        }
    }

    @EventHandler
    fun onLeave(event: PlayerQuitEvent) {
        val p = event.player

        // Delete their scoreboard
        val sb = p.getFastboard()
        sb.delete()
        Buffered.boards.remove(p.uniqueId)

        // Update all others
        Buffered.boards.forEach {
            it.value.updateScoreboard()
        }
    }
}