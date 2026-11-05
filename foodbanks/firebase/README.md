# Cloud data format

`cities.template.json` is the template of the `cities` node of the Firebase **Realtime Database**
that `:foodbanks` reads (`CityCloudDataSource`). Replace the example city and its points with real
data, keep the structure.

## Files

- `root.sacramento.json` is the real data of Sacramento (20 points, converted from `points-grouped`
  of `app/src/main/assets/sample1.json`) already wrapped into `cities`. **Imported into the root
  of the database.**
- `cities.sacramento.json` is the same data without the wrapper. It is the content of `cities`,
  imported into the existing `cities` node.
- `cities.template.json` is the template of a new city (two points as an example), imported the
  same way as `cities.sacramento.json`.

Every file here is checked by `CitiesTemplateTest`.

## Importing

Realtime Database console → ⋮ of a node → **Import JSON**. The import **replaces everything** at
that location. The console can't create an empty node, so there are two cases:

**The database is empty, or holds nothing you need** (there is no `points` node of the released app,
no other data): import `root.sacramento.json` into the root. One step, the result is `cities → …`.

**The database holds data you must keep** (for example the old `points` node that the released
app reads): don't touch the root. Add a node named `cities` with any value through **+**, then
⋮ of `cities` → **Import JSON** → `cities.sacramento.json`. The value is replaced by the content
of the file.

To check the result open
`https://<database>.firebaseio.com/cities.json?shallow=true`, it must list the city codes.

(The JSON is not importable into Firestore as is. The module reads Realtime Database only.)

## Structure

```
cities
└── <name_code>                 key must be equal to name_code
    ├── name                    required, city name in English, as the geocoder returns it
    ├── name_code               required, unique and stable, e.g. "sacramento-ca-usa"
    ├── region                  optional, e.g. "CA"
    ├── country                 optional
    ├── coordinates             optional, city center {lat, lng}
    └── points                  list or keyed object, optional
        └── (point)
            ├── placeName       required
            ├── address         required, unique inside of the city
            ├── coordinates     required {lat, lng}
            ├── workingHours    list or keyed object, optional
            │   └── (hour)      dayName, dayValue, hoursFrom, hoursTo, all required
            └── help            object, optional: fact type -> true / false
                                e.g. { "appointment": true, "documents": false }
```

## Rules the app relies on

- `name` is matched with the locality name from the geocoder (case insensitive) to select the
  city automatically. If it doesn't match, the nearest city within 100 km is used, so give every
  city `coordinates` or at least some points; without both a city can't be found by distance.
- `coordinates` of a city is its center. When it is omitted, the center is the average of its points.
- `address` identifies a point inside of its city: the same address is an update of the same
  point (favorites stay), a new address is a new point. Points removed from the cloud are not
  removed from the devices that already cached them.
- A point has no city code, it takes it from the city it is in.
- `help` answers "how to get help". Today the types are `appointment` (an appointment is
  required) and `documents` (documents are required). A type that is missing means "not
  known" and the app shows nothing for it; do not write `false` when you do not know. A value
  that is not `true` / `false` is ignored, and so is a type the app has no text for yet, so a
  new type can be added to the data before the app learns it.
- Fields that are not listed (`distance`, `isPointAvailable` from `sample1.json`) are ignored.
