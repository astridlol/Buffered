package sh.astrid.buffered.data.player

import org.litote.kmongo.eq
import org.litote.kmongo.getCollection
import org.litote.kmongo.setValue
import sh.astrid.buffered.Buffered
import sh.astrid.buffered.data.Database
import sh.astrid.buffered.data.PlayerEntry
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

    fun addExp(increment: Int) {
        val newExp = player.exp + increment
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

    fun getPlayer(): PlayerEntry {
        return player
    }
}