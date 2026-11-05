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

    val Search: PdDrawableResolver = strokeIcon("Search", "M11 4a7 7 0 1 1 0 14a7 7 0 1 1 0 -14z", "M20 20l-4-4")
    val Tune: PdDrawableResolver = strokeIcon(
        "Tune",
        "M4 7h9M17 7h3M4 17h3M11 17h9",
        "M15 5a2 2 0 1 1 0 4a2 2 0 1 1 0 -4z",
        "M9 15a2 2 0 1 1 0 4a2 2 0 1 1 0 -4z",
    )
    val Locate: PdDrawableResolver = strokeIcon(
        "Locate",
        "M12 8.5a3.5 3.5 0 1 1 0 7a3.5 3.5 0 1 1 0 -7z",
        "M12 3v3M12 18v3M3 12h3M18 12h3",
    )
    val Back: PdDrawableResolver = strokeIcon("Back", "M15 5l-7 7 7 7")
    val Share: PdDrawableResolver = strokeIcon(
        "Share",
        "M6 9.5a2.5 2.5 0 1 1 0 5a2.5 2.5 0 1 1 0 -5z",
        "M17 3.5a2.5 2.5 0 1 1 0 5a2.5 2.5 0 1 1 0 -5z",
        "M17 15.5a2.5 2.5 0 1 1 0 5a2.5 2.5 0 1 1 0 -5z",
        "M8.2 10.8l6.6-3.6M8.2 13.2l6.6 3.6",
    )
    val Pin: PdDrawableResolver = strokeIcon(
        "Pin",
        "M12 21s-6-5.2-6-10a6 6 0 1 1 12 0c0 4.8-6 10-6 10z",
        "M12 8.8a2.2 2.2 0 1 1 0 4.4a2.2 2.2 0 1 1 0 -4.4z",
    )
    val Plus: PdDrawableResolver = strokeIcon("Plus", "M12 5v14M5 12h14")
    val Chevron: PdDrawableResolver = strokeIcon("Chevron", "M9 5l7 7-7 7")
    val Clock: PdDrawableResolver = strokeIcon("Clock", CIRCLE, "M12 7v5l3 2")
    val WifiOff: PdDrawableResolver = strokeIcon(
        "WifiOff",
        "M3 3l18 18",
        "M5.5 12.5a10 10 0 0 1 4-2.4",
        "M14.5 10.1a10 10 0 0 1 4 2.4",
        "M8.5 16a5.5 5.5 0 0 1 4-1.6",
        "M12 19.5v.01",
    )

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
