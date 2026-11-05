# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

PointDetector is an Android app (Kotlin, Jetpack Compose on its own design system, Hilt, Room, Firebase Realtime Database, MapLibre over OpenFreeMap tiles) that finds the closest food banks / places to the user's location. Application id `com.yuriisurzhykov.pointdetector` (debug builds get the `.dev` suffix).

## Commands

Use the Gradle wrapper (Gradle 9.4, JDK 21 is required locally — `ProjectProperties` targets Java 21; note that CI workflows still set up JDK 17).

```
./gradlew assembleDebug                 # build debug APK
./gradlew bundleDebug                   # build debug AAB
./gradlew assembleRelease bundleRelease # release (needs app/signing.properties + keystore, see below)
./gradlew testDebugUnitTest             # all JVM unit tests
./gradlew :app:testDebugUnitTest --tests "com.yuriisurzhykov.pointdetector.AbstractConfigServiceTest"
./gradlew :foodbanks:testDebugUnitTest --tests "*CityCacheDataSourceTest.test searching city by code"
./gradlew connectedDebugAndroidTest     # instrumented tests (needs device/emulator)
```

No linter/formatter is configured (Android lint runs with `abortOnError = false`). Kotlin style is `official`.

### Local files required by the build (all git-ignored except where noted)

- `app/keys.properties` — `API_KEY=...` Google Maps key, used for **debug** builds. Release builds read `GOOGLE_MAPS_API_KEY` from the environment.
- `app/signing.properties` + `app/keystore.jks` — `app/build.gradle.kts` reads `signing.properties` **at configuration time** (the `release` signing config is created unconditionally), so Gradle configuration fails without this file even for debug builds. Create a dummy one locally if you only need debug builds.
- `app/version.properties` — fallback `versionCode`/`versionName`; the `VERSION_CODE` / `VERSION_NAME` env vars override it (CI sets them from `github.run_number` and the git tag).
- `app/google-services.json` is tracked.

## Architecture

Gradle modules (`settings.gradle.kts`): `:app`, `:ui-components`, `:foodbanks`, `:geo`, plus the new Compose modules `:designsystem` (the theme), `:toast`, `:map-maplibre` and `:konsist` described below (the Compose components live in `:ui-components`), shared build constants (SDK levels, Java version, `RoomSchemaArgProvider`) live in `buildSrc` (`ProjectProperties`). Dependency versions are in `gradle/libs.versions.toml`.

- **`:app`** — the shipping application. Package `com.yuriisurzhykov.pointdetector`, layered Clean + MVVM:
  - `core/` — generic capability interfaces (`Fetch`, `Save`, `Delete`, `Clear`, `Mapper`, `SuspendMapper`, …) that repositories/use cases compose. The project style is "one small interface per capability" with a `Base` nested implementation class (e.g. `interface FetchAllPointsUseCase { class Base @Inject constructor(...) }`).
  - `data/` — Room (`CacheDatabase`, `PointsDao`, `ConfigsDao`, entities, converters, migrations), repositories, distance services.
  - `domain/` — use cases, mappers between layers (cache entity ↔ domain `Point` ↔ `PointUi`), location/file/date services, sorting executors.
  - `presentation/` — the Compose screens, grouped by feature: `main` (the activity, `AppRoot`), `places` (the main screen, its routes and sheets), `points/details` (the place screen), `add` (adding a place), `city`, `filter`, `permissions`, `map` (`LocationUpdates`). There are **no XML layouts, no Fragments, no ViewBinding / DataBinding**: a screen is a `*Screen` composable over plain state, a `*Route` composable that connects it to the view models, and a mapper that turns state into `Pd*` components.
  - `di/` — Hilt modules installed in `SingletonComponent`; bindings are mostly hand-written `@Provides` (use cases, mappers, repositories) rather than `@Binds`, so new use cases/mappers must be registered in the matching module (`UseCaseModule`, `MappersModule`, …).
  - Remote points come from Firebase Realtime Database (`PointsFirebaseListFetchListener`, `SaveImportsUseCase`) and are cached in Room; there is also a bundled `assets/sample1.json` import path (`ImportAssetsPointsUseCase`).
- **`:ui-components`** — the Compose components `Pd*` (packages `atoms`, `molecules`, `organisms`, `component`, `resolver`, `icons`, `model`; see below). There is no View code left in it. `:app` depends on it; it exposes `:designsystem` as `api`.
- **`:foodbanks`** — the data layer of cities and points, with its own `core/`, `data/`, `domain/`, `di/` layers and its own Room database (`FoodbankDatabase`, schemas in `foodbanks/room-schemas`): cities, points, favorites, user points and the cloud↔cache sync. `:app` uses it. The format of the cloud data is in `foodbanks/firebase/README.md`.

### Compose design system

The whole UI is Jetpack Compose. The Compose side is spread over several modules; read the project skill `compose-design-system` (`.claude/skills/compose-design-system/SKILL.md`) before touching any of them.

- **`:designsystem`** — the theme only, Compose without Material, a leaf that depends on nothing of the app. Package `com.yuriisurzhykov.pointdetector.designsystem.theme`: `PdTheme.colors | shapes | typography | brushes | dimensions | motion | opacity | haptics`, semantic sub-packages, light/dark schemes built from the project blue (`#1B69FD`, night `#75A4FE`).
- **`:ui-components`** (Compose part) — every component, built on `:designsystem`. Package `com.yuriisurzhykov.pointdetector.uicomponents`:
  - `resolver/`, `icons/`, `component/`, `model/` — base layer: `PdTextResolver`, `PdDrawableResolver`, `PdIcons`, `PdComponent` / `PdBaseComponent` / `PdListItem`, either-state models such as `PlaceStatus`.
  - `atoms/`, `molecules/`, `organisms/` — atomic design, each split into group packages (`buttons`, `texts`, `status`, `lists`, …). A level may import only itself and the levels below it.
- **`:toast`** — priority toast queue as a state machine (`ToastCommand.reduce` → `ToastSideEffect`), `ToastHostState`, `PdSwipeToastHost`. Depends on `:ui-components` (and through it on `:designsystem`). The app shows its toasts through **`AppMessages`** (`domain/messages`): any layer posts a plain `AppMessage` (no text, no UI type: `NoConnection`, `SyncFailed`, `PlaceAdded`, `PlaceNotSaved`, `FavoriteAdded` / `FavoriteRemoved`, `CityNotDetected`), the same message is not repeated while it is "quiet", and `AppRoot` keeps one `PdSwipeToastHost` above everything and turns each message into a toast with `ToastMessages` (the type decides the colour and the priority). A new message is a new `AppMessage`, a branch in `ToastMessages` and a string.
- **`:map-maplibre`** — the free map: MapLibre over the OpenFreeMap vector tiles (OpenStreetMap data, no key, no limits). It is one implementation of the contract `PdMap` / `PdMapFactory` that lives in `:ui-components` (`organisms/map`, with the UI-only types in `model/map`). Screens know only the contract; the single place that picks the implementation is `di/MapModule.kt` in `:app`. To use another map (for example Google), add another module with a `PdMapFactory` and rebind it there. `:konsist` fails the build if a map SDK (`org.maplibre.`, `org.osmdroid.`, Google Maps views) is imported outside of a `map-*` module. The style is built from `PdTheme.colors.map`, so the map follows day and night; the OpenStreetMap attribution is drawn by the map and must stay.
- **`:konsist`** — JVM tests that enforce the rules below. Violations go through a ratchet (`KnownViolations` may only shrink).

**Screens and navigation.** `PermissionsCheckActivity` is the launcher (it asks for the location, or leads to the settings, and can be skipped); `MainActivity` is the only other activity: edge-to-edge Compose, `AppRoot`, and the phone's position (`LocationUpdates`). `AppRoot` is two screens: the main screen (`PlacesRoute` → `PlacesScreen`: the map, the search, the three-stop sheet with the cards) and a place (`PlaceRoute` → `PlaceScreen`) that slides over it. What lies over the main screen (the city sheet, the filters sheet, the flow of adding a place) is `PlacesViewModel`'s `PlacesOverlay`. View models are taken with `hiltViewModel()`; the mappers (`UiDependencies`) are injected once in the activity. A screen turns its state into components with `remember`, using mappers such as `PlaceCardMapper`, `PlaceStatusMapper`, `PlaceDetailsMapper` and the hours logic in `domain/hours` (`WorkingHoursParser`, `StandingResolver`: open / closing soon / opens today / closed until / unknown). Kinds of "how to get help" facts are `HelpFactFactory` classes bound into a Hilt set in `di/HelpFactsModule.kt`; each brings its icon, its texts and its question for the form of a new place.

**Cities and the sync.** The list of cities is read from the cloud without the points (`CityListReader`); the points of a city only when it is selected. The cache is shown first, then the cloud answer. An entry of the cloud that cannot be read is skipped, never an error of the whole sync. Detecting the city (`SelectedCityUseCase.detect`) refreshes the list of cities first. The cities are synced every time the app comes to the front.

Rules for any Compose UI code (they are enforced by `:konsist`):

1. Every component is an **interface** (`PdComponent`) with an optional abstract base (`PdBase*`) and concrete classes. No public free `@Composable fun` outside `theme/`; consumers never take a `PdBase*` type.
2. No **nullable** structured types or lambdas in a public API; nullable primitives are fine. Use Null Object / Strategy / a class per state. Nullable data lives only at the data boundary and in mappers.
3. No `java.time` / `kotlin.time` / `Date` or other business types in UI models or composables. The UI gets `Float` fractions and ready texts from a mapper.
4. **No hardcode:** no `dp`/`sp` literals, colours, alpha, durations, shapes or font weights in components. Only `PdTheme.…`. New numbers become named tokens in the theme (or `toast/tokens`).
5. Visible text is a `PdTextResolver`, graphics a `PdDrawableResolver`; `stringResource` / `painterResource` appear only in `resolver/`.
6. Either-state values (status, toast type) are `sealed interface` models that return their own colour role and icon (see `PlaceStatus`).
7. Lazy-list items implement `PdListItem` with both `key` and `contentType`.
8. One type per file, files ≤ 250 lines, `is` in `when` over sealed objects.

Commands:

```
./gradlew :ui-components:compileDebugKotlin :designsystem:compileDebugKotlin
./gradlew :toast:testDebugUnitTest
./gradlew :konsist:test            # architecture rules + metrics report (hardcodedTokens, nullableStructures, interfaces, …)
```

**Previews.** Every component has a `@PdPreviews` preview (light, dark, large font) in the `src/debug` source set of its module, never in `src/main`: `ui-components/src/debug/.../uicomponents/preview/{atoms,molecules,organisms,resolver}` and `toast/src/debug/.../toast/preview`. Open a file in Android Studio and switch a card to "Interactive mode" to click, toggle and swipe the component. Preview-only dependencies (`ui-tooling`) are `debugImplementation`, and sample strings live in `ui-components/src/debug/res`, so release builds contain none of it. A new component is not done until it has a preview.

### Room schemas

`CacheDatabase` (version 3, `autoMigrations` 2→3 plus a `MigrationV1ToV2Spec`) exports schemas to `app/schemas/`; `FoodbankDatabase` to `foodbanks/room-schemas/`. Schema JSONs are tracked in git — when changing an entity, bump the version, add a migration, and commit the regenerated JSON.

## Commits and changelog

All commit messages **must** follow [Conventional Commits](https://www.conventionalcommits.org): `<type>(<optional scope>)<!>: <Description>`, with type one of `feat, fix, docs, style, refactor, perf, test, build, ci, chore, revert` (scopes used here: `app`, `foodbanks`, `ui-components`, or a feature name; add `!` for breaking changes). Commits are authored only by the user — no `Co-Authored-By` trailers.

- Enforced locally by `.githooks/commit-msg`; enable it once per clone with `git config core.hooksPath .githooks`. The same script is run in CI by `.github/workflows/commit-lint.yml` (on PRs and pushes to `master`).
- `CHANGELOG.md` is **not committed**: the release workflow generates it with [git-cliff](https://git-cliff.org) (`cliff.toml`) and attaches it to the GitHub release; the release body is the notes for the tag being released. Commits that are not Conventional Commits (most of the history before this rule) are silently omitted from it, and `style`/`chore` commits are skipped. To preview locally: `git cliff --unreleased` or `git cliff --latest --strip header`.

## CI

GitHub Actions in `.github/workflows/`: `debuggable-workflow.yaml` builds debug APK/AAB on push to `master`; `release_build_github.yaml` builds signed release artifacts and creates a GitHub release on version tags (`v1.2.3`, `1.2.3`, optionally with `-suffix`); `commit-lint.yml` validates commit messages; `release_build_new.yml` is deprecated/disabled.
