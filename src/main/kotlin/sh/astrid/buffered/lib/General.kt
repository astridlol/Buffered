package sh.astrid.buffered.lib

import kotlin.random.Random

fun randomChance(probability: Double): Boolean {
    require(probability in 0.0..1.0) { "Probability must be between 0 and 1 (inclusive)" }
    return Random.nextDouble() < probability
}
