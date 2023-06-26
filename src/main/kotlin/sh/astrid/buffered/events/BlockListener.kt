package sh.astrid.buffered.events

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import sh.astrid.buffered.Buffered
import sh.astrid.buffered.data.player.BufferedPlayer

class BlockListener : Listener {
    init {
        Buffered.instance.server.pluginManager.registerEvents(this, Buffered.instance)
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onPlace(event: BlockPlaceEvent) {
        if(event.isCancelled) return

        val playerData = BufferedPlayer(event.player.uniqueId).getPlayer()

        if(playerData.buildMode) return

        event.isCancelled = true
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onBreak(event: BlockBreakEvent) {
        if(event.isCancelled) return

        val playerData = BufferedPlayer(event.player.uniqueId).getPlayer()

        if(playerData.buildMode) return

        event.isCancelled = true
    }
}