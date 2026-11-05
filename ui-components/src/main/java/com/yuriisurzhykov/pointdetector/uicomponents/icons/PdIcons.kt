package com.yuriisurzhykov.pointdetector.uicomponents.icons

import com.yuriisurzhykov.pointdetector.uicomponents.resolver.drawable.PdDrawableResolver

/** Registry of the project's icons. A new icon is one line here; components never build vectors. */
object PdIcons {

    private const val HEART = "M12 20s-7-4.6-7-10a4 4 0 0 1 7-2.5A4 4 0 0 1 19 10c0 5.4-7 10-7 10z"
    private const val CIRCLE = "M12 3a9 9 0 1 1 0 18a9 9 0 1 1 0 -18z"
    private const val STATUS_RING = "M12 6.5a5.5 5.5 0 1 1 0 11a5.5 5.5 0 1 1 0 -11z"
    private const val STATUS_HALF = "M12 6.5a5.5 5.5 0 0 1 0 11z"

    val Heart: PdDrawableResolver = strokeIcon("Heart", HEART)
    val HeartFilled: PdDrawableResolver = strokeAndFillIcon("HeartFilled", HEART)
    val Route: PdDrawableResolver = strokeIcon("Route", "M4 12l16-8-8 16-2-6-6-2z")
    val Close: PdDrawableResolver = strokeIcon("Close", "M6 6l12 12M18 6L6 18")
    val Check: PdDrawableResolver = strokeIcon("Check", "M5 12.5l4.5 4.5L19 7.5")
    val Info: PdDrawableResolver = strokeIcon("Info", CIRCLE, "M12 11v5", "M12 7.5v.01")
    val Warning: PdDrawableResolver = strokeIcon("Warning", "M12 4l9 16H3z", "M12 10v5", "M12 17.5v.01")
    val Error: PdDrawableResolver = strokeIcon("Error", CIRCLE, "M9 9l6 6M15 9l-6 6")

    val Calendar: PdDrawableResolver = strokeIcon(
        "Calendar",
        "M6 5h12a2 2 0 0 1 2 2v11a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2V7a2 2 0 0 1 2-2z",
        "M4 10h16M9 3v4M15 3v4",
    )
    val Id: PdDrawableResolver = strokeIcon(
        "Id",
        "M5 6h14a2 2 0 0 1 2 2v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2z",
        "M14 11h4M14 14h3",
        "M9 10a2 2 0 1 1 0 4a2 2 0 1 1 0 -4z",
        "M6.5 16.5c.6-1.2 1.6-1.8 2.5-1.8s1.9.6 2.5 1.8",
    )
    val Truck: PdDrawableResolver = strokeIcon(
        "Truck",
        "M3 7h11v9H3z",
        "M14 10h4l3 3v3h-7",
        "M7 16.2a1.8 1.8 0 1 1 0 3.6a1.8 1.8 0 1 1 0 -3.6z",
        "M17 16.2a1.8 1.8 0 1 1 0 3.6a1.8 1.8 0 1 1 0 -3.6z",
    )

    // Status marks differ by shape as well as colour: filled, half filled, empty.
    val StatusOpen: PdDrawableResolver = fillIcon("StatusOpen", STATUS_RING)
    val StatusClosingSoon: PdDrawableResolver = ringAndFillIcon("StatusClosingSoon", STATUS_RING, STATUS_HALF)
    val StatusClosed: PdDrawableResolver = strokeIcon("StatusClosed", STATUS_RING, width = 1.6f)
}
