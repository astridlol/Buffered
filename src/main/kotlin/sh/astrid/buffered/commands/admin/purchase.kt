@file:Command(
        "purchase",
        permission = "buffered.admin",
        description = "Used by tebex to broadcast purchases"
)

package sh.astrid.buffered.commands.admin

import me.honkling.commando.annotations.Command
import org.bukkit.Bukkit
import org.bukkit.command.ConsoleCommandSender
import sh.astrid.buffered.lib.extensions.mm

fun purchase(executor: ConsoleCommandSender, player: String, price: String, item: String) {
    val line = "<#f7a8a8>»<gray><st>                                                                       </gray><st/>«".mm()
    val emptyLine = "".mm()

    val purchaseMessage = "<gray> (( <p><u>$player</u></p><l> made a purchase <gray>@<l> <click:open_url:'https://buffered.tebex.io/'>buffered.tebex.io <gray>))".mm()
    val itemMessage = "<gray> • <s>Purchased Item <bold>|</bold> <p>$item".mm()
    val priceMessage = "<gray> • <s>Purchase Price <bold>|</bold> <p>$$price".mm()
    val supportMessage = "<#a8ffa8><em>These purchases allow us to fund and maintain the server!".mm()
    val thankYouMessage = "               <#a8ffa8><em>Thank you for your support! <#ffc2ff>❤".mm()

    Bukkit.broadcast(line)
    Bukkit.broadcast(emptyLine)
    Bukkit.broadcast(purchaseMessage)
    Bukkit.broadcast(emptyLine)
    Bukkit.broadcast(itemMessage)
    Bukkit.broadcast(priceMessage)
    Bukkit.broadcast(emptyLine)
    Bukkit.broadcast(supportMessage)
    Bukkit.broadcast(thankYouMessage)
    Bukkit.broadcast(emptyLine)
    Bukkit.broadcast(line)
}