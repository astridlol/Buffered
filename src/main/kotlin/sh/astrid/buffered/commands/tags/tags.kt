@file:Command(
        "tags",
        permission = ""
)

package sh.astrid.buffered.commands.tags

import me.honkling.commando.annotations.Command
import me.honkling.pocket.GUI
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import sh.astrid.buffered.Buffered
import sh.astrid.buffered.data.player.BufferedPlayer
import sh.astrid.buffered.lib.extensions.mm
import sh.astrid.buffered.lib.extensions.setDescription
import sh.astrid.buffered.lib.extensions.setName
import sh.astrid.buffered.lib.extensions.toLegacy

@Suppress("unused")
fun tags(executor: Player, tag: String) {
    val playerData = BufferedPlayer(executor.uniqueId).getPlayer()

    val tagData = Buffered.tagData.tags[tag]

    if(tagData === null) {
        executor.sendMessage("<error> That tag doesn't exist!".mm())
        return
    }

    val isOwned = playerData.ownedTags.contains(tag)
    val isEquip = playerData.currentTag == tag

    if(!isOwned) {
        executor.sendMessage("<error> You don't own the ${tagData.displayName} tag! You can get it from <u><click:open_url:'https://buffered.tebex.io/'>our store</u>.".mm())
        return
    }

    if(isEquip) BufferedPlayer(executor.uniqueId).setTag(null)
    else BufferedPlayer(executor.uniqueId).setTag(tag)

    executor.sendMessage("<success> Successfully ${if(isEquip) "unequip" else "equip"}ed the ${tagData.displayName} tag.".mm())
    return
}

@Suppress("unused")
fun tags(executor: Player) {
    val playerData = BufferedPlayer(executor.uniqueId).getPlayer()

    val template = """
      xxxxbxxxx
      xxxxxxxxx
      xxxxxxxxx
      xyyyyyyyx
      xxxxxxxxx
      xxxxxxxxx
    """.trimIndent()

    val gui = GUI(Buffered.instance, template, "<#c691ff><b>✦ Chat Tags".toLegacy())

    gui.put('x', ItemStack(Material.PINK_STAINED_GLASS_PANE).setName("<red>"))
    
    val about = ItemStack(Material.ENCHANTED_BOOK).setName("<p>Buffered <s><b>»</b></s> Tags")
    about.itemMeta = about.setDescription(listOf(
            "",
            "<#f7a8a8><bold>»<dark_gray><st>                        <#f7a8a8>«",
            "<p>Tags are prefixes next to",
            "      <p>your name in chat!",
            "<#f7a8a8><bold>»<dark_gray><st>                        <#f7a8a8>«",
            ""
    ))

    gui.put('b', about)

    var index = 28

    Buffered.tagData.tags.forEach {
        val item = ItemStack(Material.NAME_TAG)
                .setName("<p>${it.value.displayName}")

        val example =  "${it.value.tag} <white>${executor.name}</white><gray>: look at me!"

        var tagLore = listOf(
                "",
                example,
                "",
        )

        val isOwned = playerData.ownedTags.contains(it.key)
        val isEquip = playerData.currentTag == it.key

        tagLore = if(isOwned) {
            tagLore.plus("<#baa5d4> • <#f0c9ff>Click to ${if(isEquip) "unequip" else "equip"}")
        }
        else
            tagLore.plus(it.value.earnDescription)

        val meta = item.setDescription(tagLore)

        item.itemMeta = meta

        gui.put(index, item) { _ ->
           executor.performCommand("tags ${it.key}")
        }

        index++
    }

    gui.open(executor)
}