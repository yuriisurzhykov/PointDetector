# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

PointDetector is an Android app (Kotlin, XML layouts with ViewBinding/DataBinding, Hilt, Room, Firebase Realtime Database, Google Maps/Places) that finds the closest food banks / places to the user's location. Application id `com.yuriisurzhykov.pointdetector` (debug builds get the `.dev` suffix).

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

Three Gradle modules (`settings.gradle.kts`), shared build constants (SDK levels, Java version, `RoomSchemaArgProvider`) live in `buildSrc` (`ProjectProperties`). Dependency versions are in `gradle/libs.versions.toml`.

- **`:app`** — the shipping application. Package `com.yuriisurzhykov.pointdetector`, layered Clean + MVVM:
  - `core/` — generic capability interfaces (`Fetch`, `Save`, `Delete`, `Clear`, `Mapper`, `SuspendMapper`, …) that repositories/use cases compose. The project style is "one small interface per capability" with a `Base` nested implementation class (e.g. `interface FetchAllPointsUseCase { class Base @Inject constructor(...) }`).
  - `data/` — Room (`CacheDatabase`, `PointsDao`, `ConfigsDao`, entities, converters, migrations), repositories, distance services.
  - `domain/` — use cases, mappers between layers (cache entity ↔ domain `Point` ↔ `PointUi`), location/file/date services, sorting executors.
  - `presentation/` — Activities/Fragments/ViewModels grouped by feature (`points/list|create|details`, `favorites`, `filter`, `permissions`, `main`), plus `delegate/` property delegates for view binding/arguments.
  - `di/` — Hilt modules installed in `SingletonComponent`; bindings are mostly hand-written `@Provides` (use cases, mappers, repositories) rather than `@Binds`, so new use cases/mappers must be registered in the matching module (`UseCaseModule`, `MappersModule`, …).
  - Remote points come from Firebase Realtime Database (`PointsFirebaseListFetchListener`, `SaveImportsUseCase`) and are cached in Room; there is also a bundled `assets/sample1.json` import path (`ImportAssetsPointsUseCase`).
- **`:ui-components`** — shared Android library of reusable views and RecyclerView infrastructure (`list/` base adapters, view holders, diff and swipe callbacks, `CenteredTextToolbar`). `:app` and `:foodbanks` both depend on it.
- **`:foodbanks`** — a separate, newer library module with its own `core/`, `data/`, `domain/`, `di/` layers and its own Room database (`FoodbankDatabase`, schemas in `foodbanks/room-schemas`): cities, points, favorites, and a cloud↔cache sync (`data/sync`). **`:app` does not currently depend on it** — it is a work-in-progress replacement of the data layer, so changes there do not affect the running app.

### Room schemas

`CacheDatabase` (version 3, `autoMigrations` 2→3 plus a `MigrationV1ToV2Spec`) exports schemas to `app/schemas/`; `FoodbankDatabase` to `foodbanks/room-schemas/`. Schema JSONs are tracked in git — when changing an entity, bump the version, add a migration, and commit the regenerated JSON.

## CI

GitHub Actions in `.github/workflows/`: `debuggable-workflow.yaml` builds debug APK/AAB on push to `master`; `release_build_github.yaml` builds signed release artifacts and creates a GitHub release on version tags (`v1.2.3`, `1.2.3`, optionally with `-suffix`); `release_build_new.yml` is deprecated/disabled.
