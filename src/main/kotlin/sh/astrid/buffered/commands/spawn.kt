@file:Command(
        "spawn",
        permission = ""
)

package sh.astrid.buffered.commands

import me.honkling.commando.annotations.Command
import org.bukkit.entity.Player
import sh.astrid.buffered.lib.extensions.isInRegion
import sh.astrid.buffered.lib.extensions.mm
import sh.astrid.buffered.lib.extensions.tpToSpawn

@Suppress("unused")
fun spawn(executor: Player) {
    if(executor.isInRegion("spawn")) {
        executor.sendMessage("<error>You're currently already at spawn.".mm())
        return
    }

    executor.tpToSpawn()
    executor.sendMessage("<p>Successfully teleported to spawn.".mm())
}