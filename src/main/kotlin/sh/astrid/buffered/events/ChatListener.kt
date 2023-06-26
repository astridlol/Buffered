package sh.astrid.buffered.events

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import sh.astrid.buffered.Buffered
import sh.astrid.buffered.data.player.BufferedPlayer
import sh.astrid.buffered.lib.extensions.mm
import sh.astrid.buffered.lib.extensions.parse
import sh.astrid.buffered.lib.extensions.proper

class ChatListener : Listener {
    init {
        Buffered.instance.server.pluginManager.registerEvents(this, Buffered.instance)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onChat(event: AsyncPlayerChatEvent) {
        val p = event.player

        val playerData = BufferedPlayer(p.uniqueId).getPlayer()
        val playerRank = "%vault_rank%".parse(p)

        val level = "<gray>[</gray><p>✪${playerData.level}</p><gray>]</gray>"
        val chatColor = if (p.hasPermission("buffered.chat.white")) "<white>" else "<gray>"
        val playerName = p.name
        val message = event.message

        var format = "$level $playerName: $chatColor$message"
        var heart: String? = null

        if (p.hasPermission("buffered.staff")) {
            val heartColor = when (playerRank.lowercase()) {
                "owner" -> "<l>"
                "admin" -> "<p>"
                else ->  "<y>"
            }
            heart = "$heartColor❤"
        }

        val rankMsg = "<hover:show_text:'<p><u>Staff Member</u>\n<s>$playerName is a <b>${playerRank.proper()}</b>.'>$heart</hover>"

        if (playerData.currentTag !== null) {
            val tagData = Buffered.tagData.tags[playerData.currentTag] ?: throw Exception("Tag ${playerData.currentTag} no longer exists.")
            val tag = tagData.tag
            format = "$level $tag $playerName: $chatColor$message"

            if (heart !== null) {
                format = "$rankMsg $level $tag $playerName: $chatColor$message"
            }
        } else {
            if (heart !== null) {
                format = " $rankMsg $level $playerName: $chatColor$message"
            }
        }

//        event.format = format.toLegacy()
        event.isCancelled = true
        Bukkit.broadcast(format.mm())
    }
}