package com.yuriisurzhykov.foodbanks.domain.points

/**
 * One answer to "how to get help": a type such as `appointment` or `documents` and whether it
 * applies. The type is a key from the data, so a new kind of fact needs no change here.
 */
data class HelpFact(val type: String, val enabled: Boolean)
