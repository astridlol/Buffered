@file:Command(
        "shop",
        permission = ""
)

package sh.astrid.buffered.commands

import me.honkling.commando.annotations.Command
import me.honkling.pocket.GUI
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import sh.astrid.buffered.Buffered
import sh.astrid.buffered.data.player.BufferedPlayer
import sh.astrid.buffered.lib.extensions.*

@Suppress("unused")
fun shop(executor: Player) {
    val template = """
      xxxxxxxxx
      yyyyyyyyy
      xxxxxxxxz
    """.trimIndent()
    var index = 10

    val gui = GUI(Buffered.instance, template, "Shop")

    Buffered.shopData.shop.forEach {
        val mat = it.value.material.parseMaterial() ?: throw Exception("Invalid material ${it.value.material}")

        val item = ItemStack(mat)
                .setName(it.value.name)
                .setDescription("<g>$${it.value.price}")

        gui.put('x', ItemStack(Material.PINK_STAINED_GLASS_PANE).setName("<red>"))
        gui.put('z', ItemStack(Material.BARRIER).setName("<s><i>← Exit")) {
            executor.inventory.close()
        }

        gui.put('y', ItemStack(Material.PINK_STAINED_GLASS_PANE).setName("<red>"))

        gui.put(index, item) { _ ->
            val player = BufferedPlayer(executor.uniqueId)
            val playerData = player.getPlayer()
            if(playerData.balance >= it.value.price) {
                player.removeBalance(it.value.price)
                executor.inventory.addItem(ItemStack(mat))
                executor.sendMessage("<success> Bought ${mat.key.toString().prettyNamespace()}".mm())
            } else {
                executor.sendMessage("<error> You don't have enough for this!".mm())
            }
        }

        index++
    }

    gui.open(executor)
}