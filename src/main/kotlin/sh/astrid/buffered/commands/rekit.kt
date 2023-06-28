@file:Command(
        "rekit",
        permission = "buffered.rekit",
        description = "Toggle re-kit."
)

package sh.astrid.buffered.commands

import me.honkling.commando.annotations.Command
import org.bukkit.entity.Player
import sh.astrid.buffered.data.player.BufferedPlayer
import sh.astrid.buffered.lib.extensions.mm

@Suppress("unused")
fun rekit(executor: Player) {
  val player = BufferedPlayer(executor.uniqueId)
  val rekit = player.getPlayer().rekitEnabled

  player.setRekit(!rekit)

  executor.sendMessage("<success> Successfully ${if(!rekit) "enabled" else "disabled" } rekit.".mm())
}