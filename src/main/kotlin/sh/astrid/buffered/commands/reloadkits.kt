@file:Command(
    "reloadkits",
    permission = "buffered.reload.kits"
)

package sh.astrid.buffered.commands

import me.honkling.commando.annotations.Command
import org.bukkit.entity.Player
import sh.astrid.buffered.Buffered
import sh.astrid.buffered.lib.extensions.mm

@Suppress("unused")
fun reloadkits(executor: Player) {
    Buffered.instance.reloadKits()

    executor.sendMessage("<green>Reloaded kit data.".mm())
}