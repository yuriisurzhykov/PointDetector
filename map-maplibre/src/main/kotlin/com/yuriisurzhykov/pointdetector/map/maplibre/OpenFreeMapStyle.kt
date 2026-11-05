package com.yuriisurzhykov.pointdetector.map.maplibre

/** The colours a style needs, as ARGB integers, so building a style does not depend on Compose. */
internal class MapStyleColors(
    val land: Int,
    val water: Int,
    val park: Int,
    val road: Int,
    val edge: Int,
    val label: Int,
    val labelHalo: Int,
)

/**
 * A MapLibre style over the free vector tiles of OpenFreeMap (OpenStreetMap data, no key, no
 * limits). Only the colours of the app's theme are used, so the map follows day and night.
 */
internal object OpenFreeMapStyle {

    const val ATTRIBUTION = "© OpenFreeMap © OpenMapTiles Data from OpenStreetMap"

    private const val TILES = "https://tiles.openfreemap.org/planet"
    private const val GLYPHS = "https://tiles.openfreemap.org/fonts/{fontstack}/{range}.pbf"
    private const val RGB_MASK = 0xFFFFFF

    fun json(colors: MapStyleColors): String {
        val land = hex(colors.land)
        val water = hex(colors.water)
        val park = hex(colors.park)
        val road = hex(colors.road)
        val edge = hex(colors.edge)
        val label = hex(colors.label)
        val halo = hex(colors.labelHalo)
        return """
{
  "version": 8,
  "name": "PointDetector",
  "sources": { "openmaptiles": { "type": "vector", "url": "$TILES" } },
  "glyphs": "$GLYPHS",
  "layers": [
    { "id": "background", "type": "background", "paint": { "background-color": "$land" } },
    { "id": "park", "type": "fill", "source": "openmaptiles", "source-layer": "park",
      "paint": { "fill-color": "$park" } },
    { "id": "landcover", "type": "fill", "source": "openmaptiles", "source-layer": "landcover",
      "filter": ["match", ["get", "class"], ["grass", "wood"], true, false],
      "paint": { "fill-color": "$park" } },
    { "id": "water", "type": "fill", "source": "openmaptiles", "source-layer": "water",
      "paint": { "fill-color": "$water" } },
    { "id": "waterway", "type": "line", "source": "openmaptiles", "source-layer": "waterway",
      "paint": { "line-color": "$water", "line-width": ["interpolate", ["linear"], ["zoom"], 10, 0.5, 16, 3] } },
    { "id": "building", "type": "fill", "source": "openmaptiles", "source-layer": "building", "minzoom": 14,
      "paint": { "fill-color": "$edge", "fill-opacity": 0.6 } },
    { "id": "road-casing", "type": "line", "source": "openmaptiles", "source-layer": "transportation",
      "filter": ["match", ["get", "class"], ["motorway", "trunk", "primary", "secondary", "tertiary", "minor", "service"], true, false],
      "layout": { "line-cap": "round", "line-join": "round" },
      "paint": { "line-color": "$edge", "line-width": ["interpolate", ["exponential", 1.5], ["zoom"], 10, 1, 14, 3.5, 18, 15] } },
    { "id": "road", "type": "line", "source": "openmaptiles", "source-layer": "transportation",
      "filter": ["match", ["get", "class"], ["motorway", "trunk", "primary", "secondary", "tertiary", "minor", "service"], true, false],
      "layout": { "line-cap": "round", "line-join": "round" },
      "paint": { "line-color": "$road", "line-width": ["interpolate", ["exponential", 1.5], ["zoom"], 10, 0.5, 14, 2, 18, 12] } },
    { "id": "road-name", "type": "symbol", "source": "openmaptiles", "source-layer": "transportation_name", "minzoom": 14,
      "layout": { "symbol-placement": "line", "text-field": ["get", "name"], "text-font": ["Noto Sans Regular"], "text-size": 11 },
      "paint": { "text-color": "$label", "text-halo-color": "$halo", "text-halo-width": 1.5 } },
    { "id": "place", "type": "symbol", "source": "openmaptiles", "source-layer": "place", "maxzoom": 16,
      "layout": { "text-field": ["get", "name"], "text-font": ["Noto Sans Bold"],
                  "text-size": ["interpolate", ["linear"], ["zoom"], 4, 11, 12, 15] },
      "paint": { "text-color": "$label", "text-halo-color": "$halo", "text-halo-width": 1.5 } }
  ]
}
""".trimIndent()
    }

    private fun hex(argb: Int): String = "#%06X".format(argb and RGB_MASK)
}
