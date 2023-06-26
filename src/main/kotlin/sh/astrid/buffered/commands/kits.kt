@file:Command(
        "kits",
        permission = "",
        description = "Shows the kit GUI"
)

package sh.astrid.buffered.commands

import me.honkling.commando.annotations.Command
import me.honkling.pocket.GUI
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import sh.astrid.buffered.Buffered
import sh.astrid.buffered.data.player.Players
import sh.astrid.buffered.lib.*
import sh.astrid.buffered.lib.extensions.*

private const val questionSkull = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWFiNmVhZWMyMzdkOTRjMzY5OWVhZWQ1NjZhNTkyMzg2ZmI2Nzk1MDNhMzA4NWNhNDliM2ExMjk4NDFkY2MxMyJ9fX0="
private const val kitSkull = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmRjYTNmMzcwYmZjMGNhZGVlN2ZkMDE0NTc5YjY0MjcxNjQ5YjZlN2MwMjkwZDhlMWFjZGQ2ZjI3YjQ3NmJmMiJ9fX0="

@Suppress("unused")
fun kits(executor: Player) {
    if(!executor.isInRegion("spawn")) {
        executor.sendMessage("<error>You can only change your kit at spawn!".mm())
        return
    }

    val template = """
      xxxxxxxxx
      xyyyyyyyx
      xxxxxxxxz
    """.trimIndent()
    var index = 10

    val playerData = Players().getPlayer(executor.uniqueId)

    if(playerData === null) {
        executor.sendMessage("<error>Player data was not found! Please rejoin.".mm())
        return
    }

    var gui = GUI(Buffered.instance, template, "Kits")
    gui = gui.put('x', ItemStack(Material.PINK_STAINED_GLASS_PANE).setName("<red>"))
    gui = gui.put('y', createSkull(questionSkull).setName("<i><p>Coming soon..."))

    Buffered.kitData.kits.forEach {
        val material = it.value.guiItem.parseMaterial() ?: return@forEach
        val isCurrent = playerData.currentKit == it.key

        var item = ItemStack(material)
        var name = it.value.name

        if(isCurrent) name = "<p>$name <i>(Current)"
        item = item.setName(name).setDescription(it.value.description)

        gui = gui.put(index, item) { _ ->
            if(!isCurrent){
                executor.performCommand("kit ${it.key}")
                executor.inventory.close()
            }
        }
        index++
    }

    gui = gui.put('z', createSkull(kitSkull).setName("<p>Current Kit: ${playerData.currentKit ?: "none"}"))

    gui.open(executor)
}