# Cloud data format

`cities.template.json` is the template of the `cities` node of the Firebase **Realtime Database**
that `:foodbanks` reads (`CityCloudDataSource`). Replace the example city and its points with real
data, keep the structure.

## Importing

Realtime Database console → open the `cities` node → ⋮ → **Import JSON** and choose the file.
The file is the *content* of `cities`, so it must be imported into that node, not into the root:
importing into the root replaces the whole database, including the old `points` node that the
released app still reads.

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
            └── workingHours    list or keyed object, optional
                └── (hour)      dayName, dayValue, hoursFrom, hoursTo, all required
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
- Fields that are not listed (`distance`, `isPointAvailable` from `sample1.json`) are ignored.
