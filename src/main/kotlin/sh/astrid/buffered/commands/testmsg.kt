@file:Command(
        "testmsg",
        permission = "buffered.admin",
)

package sh.astrid.buffered.commands

import me.honkling.commando.annotations.Command
import org.bukkit.entity.Player
import sh.astrid.buffered.lib.extensions.mm

fun testmsg(executor: Player, message: String) {
    executor.sendMessage(message.mm())
}