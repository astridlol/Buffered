package sh.astrid.buffered.lib

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

class CooldownManager {
    private val cooldowns: MutableMap<String, Long> = ConcurrentHashMap()

    fun setCooldown(id: String, duration: Long, unit: TimeUnit = TimeUnit.MICROSECONDS) {
        val cooldownDuration = unit.toMillis(duration)
        val expirationTime = System.currentTimeMillis() + cooldownDuration
        cooldowns[id] = expirationTime
    }

    fun hasCooldown(id: String): Boolean {
        val expirationTime = cooldowns[id] ?: return false
        return System.currentTimeMillis() < expirationTime
    }

    fun getRemainingTime(id: String): Long {
        val expirationTime = cooldowns[id] ?: return 0
        return (expirationTime - System.currentTimeMillis()).coerceAtLeast(0)
    }

    fun removeCooldown(id: String) {
        cooldowns.remove(id)
    }
}
