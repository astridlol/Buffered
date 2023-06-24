package sh.astrid.buffered.lib

import org.bukkit.scheduler.BukkitTask
import sh.astrid.buffered.Buffered
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Consumer

fun delay(code: Consumer<BukkitTask>, ticks: Int) {
    Buffered.instance.server.scheduler.runTaskLater(Buffered.instance, code, ticks.toLong())
}

fun forever(code: Consumer<BukkitTask>, delay: Int) {
    Buffered.instance.server.scheduler.runTaskTimer(Buffered.instance, { it: BukkitTask ->
        code.accept(it)
    }, 0, delay.toLong())
}

fun timer(code: Consumer<BukkitTask>, times: Int, delay: Int) {
    val ran = AtomicInteger()
    Buffered.instance.server.scheduler.runTaskTimer(Buffered.instance, { it: BukkitTask ->
        code.accept(it)
        ran.getAndIncrement()
        if (ran.get() == times) it.cancel()
    }, 0, delay.toLong())
}