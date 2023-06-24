package sh.astrid.buffered.data.player

import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection
import java.util.logging.Level
import sh.astrid.buffered.Buffered
import sh.astrid.buffered.data.Database
import sh.astrid.buffered.data.PlayerData
import sh.astrid.buffered.data.PlayerEntry
import java.util.*

class Players {
    private val db = Database.get()
    private val plugin = Buffered.instance

    fun createPlayer(data: PlayerData): PlayerEntry? {
        val uuid = data.uuid
        val existingPlayer = getPlayer(uuid)

        if(existingPlayer !== null) {
            plugin.logger.log(Level.WARNING, "Cannot create data for $uuid as they already have data.")
            return null
        }

        val col = db.getCollection<PlayerEntry>("players")
        val player = PlayerEntry(data.uuid.toString(), System.currentTimeMillis())
        col.insertOne(player)

        plugin.logger.log(Level.INFO, "Successfully created player data for $uuid")

        return player
    }

    fun getPlayer(uuid: UUID): PlayerEntry? {
        val col = db.getCollection<PlayerEntry>("players")
        return col.findOne(PlayerEntry::uuid eq uuid.toString())
    }

    fun getCount(): Long {
        val col = db.getCollection<PlayerEntry>("players")
        return col.countDocuments()
    }
}