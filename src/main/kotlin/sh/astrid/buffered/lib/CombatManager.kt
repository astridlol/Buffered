package sh.astrid.buffered.lib

import org.bukkit.entity.Player
import sh.astrid.buffered.Buffered
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class CombatManager {
    private val encounters: MutableMap<UUID, Long> = ConcurrentHashMap()

    fun setTag(player: Player) {
        val expirationTime = System.currentTimeMillis() + (5 * 1000)
        encounters[player.uniqueId] = expirationTime
        Buffered.instance.logger.info("Added combat tag to ${player.uniqueId} (${expirationTime})")
    }

    fun hasTag(player: Player): Boolean {
        val expirationTime = encounters[player.uniqueId] ?: return false
        return System.currentTimeMillis() < expirationTime
    }

    fun getRemainingTime(player: Player): Long {
        val expirationTime = encounters[player.uniqueId] ?: return 0
        return (expirationTime - System.currentTimeMillis()).coerceAtLeast(0)
    }
}
