package sh.astrid.buffered.data.player

import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.litote.kmongo.addToSet
import org.litote.kmongo.eq
import org.litote.kmongo.getCollection
import org.litote.kmongo.setValue
import sh.astrid.buffered.Buffered
import sh.astrid.buffered.data.Database
import sh.astrid.buffered.data.PlayerEntry
import sh.astrid.buffered.lib.extensions.getMMResolvers
import java.util.UUID

class BufferedPlayer(uuid: UUID) {
    private val db = Database.get()
    private val player = Players()
            .getPlayer(uuid) ?: throw Exception("Could not find player")

    private val col = db.getCollection<PlayerEntry>("players")
    private val query = PlayerEntry::uuid eq player.uuid

    // Balance

    fun addBalance(increment: Int) {
        val newBalance = player.balance + increment
        col.updateOne(query, setValue(PlayerEntry::balance, newBalance))
    }

    fun removeBalance(decrement: Int) {
        val newBalance = player.balance - decrement
        col.updateOne(query, setValue(PlayerEntry::balance, newBalance))
    }

    // Levels

    private fun Player.announceLevelUp(level: Int) {
        val mm = MiniMessage.miniMessage()

        Bukkit.broadcast(mm.deserialize(
                Buffered.messageData.levelUp.joinToString("\n"),
                Placeholder.parsed("player", this.name),
                Placeholder.parsed("level", level.toString()),
                *getMMResolvers()
        ))
    }

    fun addExp(increment: Int) {
        val p = Bukkit.getPlayer(UUID.fromString(this.player.uuid)) ?: throw Exception("Could not fetch player.")

        var newExp = player.exp + increment

        if(newExp >= player.level * 30)  {
            newExp = 0;
            col.updateOne(query, setValue(PlayerEntry::level, player.level + 1))
            p.announceLevelUp(player.level + 1)
        }

        col.updateOne(query, setValue(PlayerEntry::exp, newExp))
    }

    // Kit

    fun setKit(kit: String?) {
        col.updateOne(query, setValue(PlayerEntry::currentKit, kit))
    }

    // Kills / Deaths

    fun addKill() {
        col.updateOne(query, setValue(PlayerEntry::kills, player.kills + 1))
        col.updateOne(query, setValue(PlayerEntry::killstreak, player.killstreak + 1))
        Buffered.instance.logger.info("Added kill to ${player.uuid}, added onto killstreak.")
    }

    fun addDeath() {
        col.updateOne(query, setValue(PlayerEntry::deaths, player.deaths + 1))
        col.updateOne(query, setValue(PlayerEntry::killstreak, 0))
        Buffered.instance.logger.info("Added death to ${player.uuid}, wiped killstreak.")
    }

    // Tags

    fun setTag(tag: String?) {
        col.updateOne(query, setValue(PlayerEntry::currentTag, tag))
    }

    fun addTag(tag: String?) {
        col.updateOne(query, addToSet(PlayerEntry::ownedTags, tag))
    }

    // Misc
    fun setBuildMode(enabled: Boolean) {
        col.updateOne(query, setValue(PlayerEntry::buildMode, enabled))
    }

    fun getPlayer(): PlayerEntry {
        return player
    }
}