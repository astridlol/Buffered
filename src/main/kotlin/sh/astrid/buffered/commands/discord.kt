@file:Command(
        "discord",
        permission = "",
)

package sh.astrid.buffered.commands

import me.honkling.commando.annotations.Command
import org.bukkit.entity.Player
import sh.astrid.buffered.lib.extensions.mm

fun discord(executor: Player) {
    executor.sendMessage("<p>Join our Discord server <u><click:open_url:'https://discord.gg/SzUKkgvyHS'>here</u> c:".mm())
}