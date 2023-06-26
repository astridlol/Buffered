@file:Command(
    "reloadconfig",
    permission = "buffered.reload"
)

package sh.astrid.buffered.commands.admin

import me.honkling.commando.annotations.Command
import org.bukkit.entity.Player
import sh.astrid.buffered.Buffered
import sh.astrid.buffered.lib.extensions.mm

@Suppress("unused")
fun reloadconfig(executor: Player) {
    Buffered.instance.reloadConfigs()

    executor.sendMessage("<green>Reloaded kit data.".mm())
}