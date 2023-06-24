package sh.astrid.buffered.data

import java.util.*

// Stuff used for passing into createPlayer, typically pre-determined data that can change based on the player who joined. (Ex: UUIDs, not a level.)
data class PlayerData(val uuid: UUID)
// Player data
data class PlayerEntry(
        val uuid: String,
        val firstJoin: Long,
        val balance: Int = 0,
        val level: Int = 1,
        val exp: Int = 0,
        val deaths: Int = 0,
        val kills: Int = 0,
        val killstreak: Int = 0,
        val currentKit: String? = null
)