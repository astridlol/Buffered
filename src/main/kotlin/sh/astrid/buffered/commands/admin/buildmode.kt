@file:Command(
        "build",
        permission = "buffered.build",
        description = "Toggle build mode"
)

package sh.astrid.buffered.commands.admin

import me.honkling.commando.annotations.Command
import org.bukkit.entity.Player
import sh.astrid.buffered.data.player.BufferedPlayer
import sh.astrid.buffered.lib.extensions.mm

fun build(executor: Player) {
    val player = BufferedPlayer(executor.uniqueId)
    val buildMode = player.getPlayer().buildMode

    player.setBuildMode(!buildMode)

    executor.sendMessage("<success> Successfully ${if(!buildMode) "enabled" else "disabled" } build mode.".mm())
}