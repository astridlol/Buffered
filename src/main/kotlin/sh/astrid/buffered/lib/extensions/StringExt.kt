package sh.astrid.buffered.lib.extensions

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment

/**
 * A String extension function to convert a string into a deserialized MiniMessage component.
 *
 * @return A MiniMessage component
 */
fun String.mm(): Component {
    val mm = MiniMessage.miniMessage()
    val errorResolver = Placeholder.parsed("error", "<#ff6e6e>⚠ <#ff7f6e>")

    return mm.deserialize(this, *getMMResolvers(), errorResolver)
            .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)
}

fun String.parseEnchant(): Enchantment? {
    return Enchantment.getByKey(NamespacedKey.minecraft(this))
}

fun String.parseMaterial(): Material? {
    return Material.matchMaterial(this)
}

fun String.prettyNamespace(): String {
    return this.replace("minecraft:", "").prettify().proper()
}

fun String.proper(): String {
    return this.split(" ").joinToString(" ") { word -> word[0].uppercaseChar() + word.substring(1) }
}

fun String.prettify(): String {
    return this.replace(Regex("[-_]"), " ")
            .split(" ")
            .joinToString(" ") {
                ((it.getOrNull(0) ?: "").toString()).uppercase() +
                        it.substring(it.length.coerceAtMost(1)).lowercase()
            }
}

fun getMMResolvers(): Array<TagResolver> {
    return listOf<TagResolver>(
            TagResolver.resolver("p", Tag.styling(TextColor.color(255, 212, 227))),
            TagResolver.resolver("s", Tag.styling(TextColor.color(255, 181, 207))),
            TagResolver.resolver("t", Tag.styling(TextColor.color(235, 155, 183))),
            TagResolver.resolver("l", Tag.styling(TextColor.color(244, 212, 255))),
    ).toTypedArray()
}