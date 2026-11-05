---
name: compose-design-system
description: Create and extend a Jetpack Compose design system following the project's rules. The theme is a `<Name>Theme` object (colors, shapes, typography, brushes, dimensions, motion), components follow atomic design (atoms, molecules, organisms) split into group packages, every component is an interface + abstract scaffold + implementations, no hardcoded values and no nullable structures, texts and graphics go through resolvers, "either" states are sealed models (like BuildStatus), toasts are a queue on a state machine, and the rules are enforced by Konsist with a ratchet. Use when asked to create or rewrite a design system, UI kit, theme, component or toast, to move UI to Compose, and when reviewing Compose components.
---

# Compose design system: rules, examples, metrics

Reference implementations (read them, do not reinvent):

| What | Where |
|---|---|
| Full implementation of this skill | `PointDetector`: modules `:designsystem`, `:toast`, `:konsist` |
| Theme, `BuildStatus`, toasts, Konsist rules (the origin) | `E:\Projects\build-notify-plugin\mobile-app` (`core/ui/.../theme`, `core/ui/.../components/model/BuildStatus.kt`, `core/toast`, `konsist/`, `config/konsist`) |

If the user sends their own reference path, read it before writing code: the principles below come from such references.

## 0. Workflow

1. Read the project's existing resources (`colors.xml`, icons, fonts): colors are not invented, they come from the project. Check contrast (WCAG) before showing a palette.
2. Modules: `:designsystem` (the theme and all components, depends on nothing), `:toast` (depends only on `:designsystem`), `:konsist` (JVM tests of the rules).
3. Theme → base layer (interfaces, resolvers, models) → atoms → molecules → organisms → toasts → rules → documentation. Compile after each block.
4. Rules and tests are written together with the code, not after. Mutation check: temporarily put `16.dp` into a component, the test must fail, then revert.
5. All changes in a worktree, commits in Conventional Commits without `Co-Authored-By` (as in the project's CLAUDE.md).

## 1. Theme: the `<Name>Theme` object

Tokens are read only like this: `PdTheme.colors`, `PdTheme.dimensions`, `PdTheme.shapes`, `PdTheme.typography`, `PdTheme.brushes`, `PdTheme.motion`, `PdTheme.opacity`, `PdTheme.haptics`. Components must not touch `LocalPd*`, token constructors or `Color(0x…)` directly.

```
theme/
  PdTheme.kt                 object: @Composable @ReadOnlyComposable accessors
  PdThemeProvider.kt         fun PdTheme(darkTheme, dimensions, content) + staticCompositionLocalOf
  color/   Palette.kt (internal, raw values)  PdColorScheme.kt  PdLightColors.kt  PdDarkColors.kt
           semantic/ ColorRole  ContentColors  SurfaceColors  StatusColors  PdContentTone
  dimens/  PdDimensions.kt (Compact/Medium/Expanded)  AdaptiveSpacings.kt  FixedSpacings.kt
           semantic/ Spacing  Layout  Icon  Stroke  Elevation  Component
  shapes/  PdShapes.kt
  typography/ PdTypography.kt
  brush/   PdBrushScheme.kt  PdBrushSchemes.kt  semantic/GradientSpec.kt
  motion/  PdMotion.kt (Default and Reduced)  ReduceMotion.kt
  opacity/ PdOpacity.kt
  haptics/ PdHaptics.kt  PdHapticEffect.kt  PlatformHaptics.kt  NoHaptics.kt
```

Principles:

- **Raw is separate from semantic.** `Palette` is internal; a component sees a role (`ColorRole(main, onMain, container, onContainer)`), not a shade.
- **Every semantic group is an `@Immutable data class` in `semantic/`.** The scheme is assembled from groups: `PdColorScheme(isDark, surface, content, primary, secondary, status, outline, divider)`.
- **Sizes by meaning.** `spacing.regular`, `icon.small`, `stroke.thin`, `component.buttonHeight`. A component needs a new size: add a field to `ComponentDimensions`, not a literal in the component.
- **Adaptivity through instances:** `CompactDimensions`, `MediumDimensions`, `ExpandedDimensions` change spacing and layout, fixed sizes are shared.
- **Motion lives entirely in tokens.** `PdMotion` holds durations, the spring (`spatial()`), the tween (`effects()`), `pressedScale`, `popPeakScale`. For "remove animations" the theme supplies `ReducedMotion` (`isReduced = true`, zeros), and every animation must check `PdTheme.motion.isReduced`.
- **Haptics only through the interface** `PdHaptics.perform(PdHapticEffect.Confirm)`. It is replaced in tests.
- **Dark theme is not judged by eye:** if the project accent is hard to see (contrast < 3:1), pick a night variant and write the reason in a comment next to it (in PointDetector: `#053898` → `#75A4FE`, was 1.4:1, now 5.8:1).

## 2. Atomic design and group packages

Levels import only downwards: **particles** (`theme`, `resolver`, `icons`, `component`, `model`) < **atoms** < **molecules** < **organisms**.

| Level | What it is | Criterion | Examples |
|---|---|---|---|
| atoms | a single primitive, composes no project components | cannot be split into parts | `texts/PdPlainText`, `icons/PdDecorativeIcon`, `status/PdPlaceStatusMark`, `layout/PdHairlineDivider`, `feedback/PdSkeletonBlock` |
| molecules | 2–4 atoms with one job | makes sense as a "button", a "label" | `buttons/PdPrimaryButton`, `status/PdHintedStatusLine`, `tags/PdTextTag`, `toggles/PdHeartToggle`, `tabs/PdTextTab` |
| organisms | a standalone screen block made of molecules | can be put on a screen | `lists/PdPlaceCardData`, `help/PdHelpList`, `tabs/PdTextTabs` |

Placement rules:

- Inside a level there is **always a group package by meaning**: `buttons`, `surfaces`, `texts`, `icons`, `status`, `tags`, `tabs`, `toggles`, `lists`, `help`, `feedback`, `layout`. A file directly in `atoms/` is forbidden.
- A new group is created when it has at least two components or describes a separate meaning. Otherwise put the component into the nearest group.
- One type per file. A file is at most 250 lines.
- Shared internal helpers (`PdBasicText`, `PdTintedImage`, `pdPressable`) are `internal` and live next to the group or in `component/`.

## 3. Component contract: interface + scaffold + implementations

Every component, including the simplest, is an **interface**. There are no free public `@Composable fun` (except `PdTheme(...)` in the theme). The consumer knows only the interface.

```kotlin
@Stable
interface PdComponent { @Composable fun Content(modifier: Modifier) }

@Immutable
abstract class PdBaseComponent : PdComponent {           // template method
    @Composable final override fun Content(modifier: Modifier) { PdComponentFrame { Render(modifier) } }
    @Composable protected abstract fun Render(modifier: Modifier)
}
```

Implementations come in three kinds: **without data** (skeleton, Null Object), **with data**, **with extra data**. A new class is created when what is drawn or how the block behaves changes, not when "one value is missing": a list without a row does not get a row created.

Patterns instead of `null`:

| Before | After | Pattern |
|---|---|---|
| `icon: ImageVector?` | `PdIcon`: `PdNoIcon` / `PdDecorativeIcon` / `PdLabeledIcon` | Null Object |
| `help: Info?` with 4 `String?` | `PdHelpSection`: `PdNoHelpSection` / `PdHelpList(items)` | Null Object + list |
| `loading: Boolean` with a required `onClick` | `PdLoadingButton` with no `onClick` at all | state as a type |
| `onScrub: ((Float) -> Unit)?` | `PdScrub`: `PdScrubOff` / `PdScrubTo(consumer)` | Strategy |

A scaffold with hooks (the button example): `PdBaseButton` defines the layout and font, a subclass answers `colors()` and `Modifier.activate()`; `PdLoadingButton` / `PdDisabledButton` have no click handler by construction.

`PdBase*` types **never** appear in public signatures. Required lambdas are fine, optional behavior is a strategy.

### List items

```kotlin
@Stable interface PdListItem : PdComponent { val key: String; val contentType: PdContentType }
@JvmInline value class PdContentType(val id: String) { companion object { val PlaceCard = PdContentType("place-card") } }
// screen: items(cards, key = { it.key }, contentType = { it.contentType }) { it.Content(Modifier) }
```
The key is a `String` because lazy list keys must be savable to a Bundle. Each implementing class names its own `contentType`.

## 4. "Either" state models (like `BuildStatus`)

When a component depends on a state that supplies color and icon, the state is wrapped in a `sealed interface` that returns its own role and icon. The component does not know which color "open" has.

```kotlin
@Immutable
sealed interface PlaceStatus {
    @get:[Composable ReadOnlyComposable] val statusRole: ColorRole
    val icon: PdDrawableResolver

    data object Open : PlaceStatus {
        override val statusRole: ColorRole @[Composable ReadOnlyComposable] get() = PdTheme.colors.primary
        override val icon: PdDrawableResolver = PdIcons.StatusOpen
    }
    data object ClosingSoon : PlaceStatus { /* status.warning, PdIcons.StatusClosingSoon */ }
    data object Closed : PlaceStatus { /* status.neutral, PdIcons.StatusClosed */ }
}
```
A state must not rely on color alone: the icons differ in shape (filled, half, empty circle). Branches of `when` over sealed objects always use `is`.

The same approach is used for toasts (`ToastType`: `Info`, `Success`, `Warning`, `Error` with `statusRole`, `icon`, priority) and for the host slot (`ToastSlot`: `Empty` / `Showing(data)`, instead of a nullable).

## 5. Text and graphics resolvers

A component does not accept `String` for visible text, `@StringRes`, `ImageVector?`, `@DrawableRes`.

```kotlin
@Stable interface PdTextResolver { @Composable @ReadOnlyComposable fun resolve(): CharSequence }
// implementations: PdLiteralText(value), PdStringText(@StringRes id, args: List<PdTextResolver>),
//                  PdPluralText(@PluralsRes id, count), PdEmphasisText(base, emphasized)  // returns an AnnotatedString with spans

@Stable interface PdDrawableResolver { @Composable fun resolve(): Painter }
@Immutable class PdVectorDrawable(private val vector: ImageVector) : PdDrawableResolver {
    @Composable override fun resolve(): Painter = rememberVectorPainter(vector)
}
```
- `CharSequence`, so that an `AnnotatedString` can be returned; the single text output point `PdBasicText` handles both cases.
- `ImageVector` is immutable, so the implementation stays `@Immutable` and the painter is cached. `painterResource` gives no such guarantee, XML resources are converted to `ImageVector` once.
- `stringResource`, `painterResource`, `vectorResource` are allowed only in the `resolver` package.
- Icons live in the `PdIcons` registry (one line per icon), icon files are exempt from the hardcode rule.
- Text and icon color is a `PdContentTone` (`Inherit`, `Primary`, `Secondary`, `Tertiary`, `Brand`, `Inverse`), not a `Color`. `Inherit` reads `LocalPdContentColor`, so a button paints its label and icon with one color.

## 6. Zero hardcode

Function parameters and component bodies contain no `dp`/`sp` literals, colors, opacity, durations, shapes or font weights. Only theme tokens.

Allowed exceptions (token files): `theme/**`, `icons/**` (path coordinates), `toast/tokens/**` (effect settings). `0.dp` is allowed. Need a new number: add it to the right token group with a name, then use it.

Forbidden patterns (regular expressions of the rules): `\d+\.dp|sp`, `Color(0x`, `alpha = <digit>`, `tween(<digit>`, `durationMillis = <digit>`, `delayMillis = <digit>`, `fillMax*(0.<digit>`, `RoundedCornerShape(`, `TextStyle(`, `FontWeight.`. Instead of `alpha = 1f - x` write a named constant (`OPAQUE`), the rule catches a digit right after `=`.

## 7. Toasts

Module `:toast`, depends only on `:designsystem`. Architecture:

```
ToastHostState.show(data) ──► Channel<ToastCommand> ──► scan(reduce) ──► onEach(execute side effects, publish slot)
ToastCommand:  Enqueue | Dismiss(id?) | TimerExpired(id) | Evict(ticket)     (pure functions: reduce(snapshot) → transition)
ToastSideEffect: Complete | CancelTicket | ScheduleTimer | CancelTimer
```
- Priority: a more important toast displaces the current one (`Replaced`), an equal one waits in a FIFO. `show` suspends until `Dismissed` / `TimedOut` / `Replaced`.
- One collector for everything: no mutex needed. Timers live in `EffectExecutor`.
- UI: `PdToastHost` (interface) → `PdSwipeToastHost`; `PdToastBanner` → `PdStatusToastBanner` (northern-lights background, colors from `type.statusRole`); swipe up to dismiss for non-sticky types, `Error` is sticky.
- Animations (`PullRevealState`, `AuroraBackgroundNode`) take `animated` and apply instantly under "remove animations". Effect settings live in `ToastTokens`.
- Queue tests are mandatory (priorities, FIFO, timers, displacement, cancel by id).

## 8. The rules are enforced (Konsist + ratchet)

The rules are pure functions over `SourceFile(path, text)`, so they are tested on string fixtures (`RulesSelfTest`). Violations are compared with the list of known ones: the list can only shrink, fix a violation and delete its line.

| Rule | What it catches |
|---|---|
| `layerViolations` | an import of a higher level (atoms → molecules and so on) |
| `filesOutsideGroupPackages` | a file directly in `atoms/`, `molecules/`, `organisms/` |
| `hardcodedTokens` | literals of dp/sp, colors, alpha, durations, shapes, fonts |
| `resourceAccess` | `stringResource` / `painterResource` outside `resolver` |
| `nullableStructures` | nullable structures and nullable lambdas in the public API (primitives are fine) |
| `freePublicComposables` | a public `@Composable fun` outside the theme |
| `componentsWithoutInterface` | a component class without `PdBase*` / `Pd*` among its supertypes |
| `baseTypesInSignatures` | `PdBase*` in a public signature |
| `textAsString` | `text: String`, `label: String` and the like in the public API |
| `businessTimeImports` | `java.time`, `kotlin.time`, `kotlinx.datetime`, `Date` in UI (in `:toast` only `kotlin.time.Duration` is allowed, as a timer) |
| `manyTypesPerFile`, `longFiles`, `whenBranchesWithoutIs`, Cyrillic literals | style and resources |

Time and locale do not reach the UI: the UI receives axis fractions (`Float`) and ready texts, while `LocalTime`, formatting and business rules stay in a mapper of the `:app` layer.

## 9. Metrics (Definition of Done)

Counted automatically (`./gradlew :konsist:test`, the metrics report is printed by `DesignSystemTest`).

| Metric | Target | How it is measured |
|---|---|---|
| hardcoded literals in components | 0 | `metrics.hardcodedTokens` |
| nullable structures in the public API | 0 | `metrics.nullableStructures` |
| free public composables outside the theme | 0 | rule `freePublicComposables` |
| share of components with an interface | 100 % | `componentsWithoutInterface` is empty |
| files over 250 lines / files with 2+ types | 0 / 0 | `longFiles`, `manyTypesPerFile` |
| layer violations atoms → molecules → organisms | 0 | `layerViolations` |
| text to background contrast | ≥ 4.5:1 | WCAG calculation for all (text, container) pairs in both themes |
| state icon to background contrast | ≥ 3:1 and the shape differs | calculation + different icons |
| touch target | ≥ 48 dp | `component.touchTarget` |
| response to "remove animations" | in every animation | `PdTheme.motion.isReduced` is checked in every infinite/spring effect |
| toast queue tests | all scenarios green | `:toast:testDebugUnitTest` |
| mutation check | the rule fails on an inserted literal | manually insert `16.dp` → red test → revert |

Growing metrics: `interfaces`, `implementations`, `abstractBases` (from `metrics`).

## 10. Pitfalls (learned in practice)

- `internal abstract fun` in a public base class makes it non-extendable outside the module. If the scaffold must be extended from other modules, make the hooks `protected`.
- `@get:[Composable ReadOnlyComposable]` needs the same annotations on the getter in a `data object`.
- A `Float` with `alpha =` and a digit right after `=` is caught by the rule; put the value into a named constant or a token.
- `coroutines-test` 1.11: a single `advanceUntilIdle()` can return before the `Channel` pipeline has processed the last command. In tests use `settle()` (`advanceUntilIdle(); runCurrent(); advanceUntilIdle()`).
- The `HapticFeedbackType.Confirm/Reject/ToggleOn/ToggleOff/SegmentTick` types depend on the Compose BOM version, check by compiling.
- `buildAnnotatedString` in `PdEmphasisText` loses spans if the result is passed as an argument to `stringResource(formatArgs)`: arguments are converted to a string.
- The project's real fonts are wired in as a separate step; until then `FontFamily.Default` is used, and the `headings` token lets you change the heading font without touching the text.
- Visual checks (Preview / screenshot tests for every implementation, day and night, fontScale 2.0) are added as a separate layer: Roborazzi or Paparazzi.

## 11. How to create a new design system (checklist)

1. Collect the project's colors, check contrast, decide what is `primary` and what is `status.*`.
2. Create `:designsystem` (Compose, no Material), `:konsist`, and `:toast` if needed.
3. Write the theme (section 1) and compile.
4. Base layer: `PdComponent`, `PdBaseComponent`, `PdListItem` / `PdBaseListItem` / `PdContentType`, resolvers, `PdIcons`, state models.
5. Atoms → molecules → organisms, each as an interface + scaffold + implementations, in group packages.
6. Toasts (section 7), if needed.
7. Konsist rules and self-tests (section 8), mutation check.
8. A section in the project's `CLAUDE.md` with the commands and a link to this skill.
9. A report with the metrics (section 9).
