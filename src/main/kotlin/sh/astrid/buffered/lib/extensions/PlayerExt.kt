package sh.astrid.buffered.lib.extensions

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldguard.WorldGuard
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import sh.astrid.buffered.Buffered

val spawnWorld = Bukkit.getWorld("world")

fun Player.tpToSpawn() {
    this.teleport(Location(spawnWorld, 0.577, 64.0, 2.496))
}

fun Player.isInRegion(region: String): Boolean {
    val container = WorldGuard.getInstance().platform.regionContainer
    val serverRegions = container.get(BukkitAdapter.adapt(spawnWorld)) ?: throw Exception("Error getting region manager.")

    val loc = BukkitAdapter.adapt(this.location)

    val query = container.createQuery()
    val set = query.getApplicableRegions(loc)

    return set.regions.contains(serverRegions.regions[region])
}

fun Player.isInCombat(): Boolean {
    return Buffered.combatLog.hasTag(this)
}

fun Player.getRemainingCombat(): Long {
    return Buffered.combatLog.getRemainingTime(this)
}

