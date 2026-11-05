---
name: compose-design-system
description: Создание и расширение дизайн-системы на Jetpack Compose по правилам Юрия. Тема как объект `<Name>Theme` (colors, shapes, typography, brushes, dimensions, motion), компоненты по atomic design (atoms, molecules, organisms) с группами в пакетах, каждый компонент как интерфейс + абстрактный каркас + реализации, ни одного hardcode и nullable-структуры, тексты и графика через резолверы, состояния «either» как sealed-модели (как BuildStatus), тосты как очередь на конечном автомате, правила проверяются Konsist с «храповиком». Использовать при просьбах создать/переписать дизайн-систему, UI-kit, тему, компонент, тост, перевести UI на Compose, а также при ревью Compose-компонентов.
---

# Дизайн-система на Compose: правила, примеры, показатели

Эталонные реализации (читать, а не придумывать заново):

| Что | Где |
|---|---|
| Полная реализация по этому skill | `PointDetector`: модули `:designsystem`, `:toast`, `:konsist` |
| Тема, `BuildStatus`, тосты, правила Konsist (первоисточник) | `E:\Projects\build-notify-plugin\mobile-app` (`core/ui/.../theme`, `core/ui/.../components/model/BuildStatus.kt`, `core/toast`, `konsist/`, `config/konsist`) |

Если пользователь прислал свой путь-образец, прочитай его до того, как писать код: принципы ниже получены из таких образцов.

## 0. Порядок работы

1. Прочитай существующие ресурсы проекта (`colors.xml`, иконки, шрифты): цвета не выдумываются, берутся из проекта. Проверь контраст (WCAG) до того, как показывать палитру.
2. Модули: `:designsystem` (тема и все компоненты, ни от чего не зависит), `:toast` (зависит только от `:designsystem`), `:konsist` (JVM-тесты правил).
3. Тема → база (интерфейсы, резолверы, модели) → атомы → молекулы → организмы → тосты → правила → документация. После каждого блока компилируй.
4. Правила и тесты пишутся вместе с кодом, не после. Мутационная проверка: временно внеси `16.dp` в компонент, тест обязан упасть, потом откати.
5. Все изменения в worktree, коммиты Conventional Commits без `Co-Authored-By` (так в CLAUDE.md проекта).

## 1. Тема: объект `<Name>Theme`

Все токены читаются только так: `PdTheme.colors`, `PdTheme.dimensions`, `PdTheme.shapes`, `PdTheme.typography`, `PdTheme.brushes`, `PdTheme.motion`, `PdTheme.opacity`, `PdTheme.haptics`. Прямое обращение к `LocalPd*`, конструкторам токенов или `Color(0x…)` в компонентах запрещено.

```
theme/
  PdTheme.kt                 object: аксессоры @Composable @ReadOnlyComposable
  PdThemeProvider.kt         fun PdTheme(darkTheme, dimensions, content) + staticCompositionLocalOf
  color/   Palette.kt (internal, сырые значения)  PdColorScheme.kt  PdLightColors.kt  PdDarkColors.kt
           semantic/ ColorRole  ContentColors  SurfaceColors  StatusColors  PdContentTone
  dimens/  PdDimensions.kt (Compact/Medium/Expanded)  AdaptiveSpacings.kt  FixedSpacings.kt
           semantic/ Spacing  Layout  Icon  Stroke  Elevation  Component
  shapes/  PdShapes.kt
  typography/ PdTypography.kt
  brush/   PdBrushScheme.kt  PdBrushSchemes.kt  semantic/GradientSpec.kt
  motion/  PdMotion.kt (Default и Reduced)  ReduceMotion.kt
  opacity/ PdOpacity.kt
  haptics/ PdHaptics.kt  PdHapticEffect.kt  PlatformHaptics.kt  NoHaptics.kt
```

Принципы:

- **Сырое отделено от смыслового.** `Palette` internal; компонент видит роль (`ColorRole(main, onMain, container, onContainer)`), а не оттенок.
- **Каждая семантическая группа это `@Immutable data class` в `semantic/`.** Схема собирается из групп: `PdColorScheme(isDark, surface, content, primary, secondary, status, outline, divider)`.
- **Размеры по смыслу.** `spacing.regular`, `icon.small`, `stroke.thin`, `component.buttonHeight`. Нужен новый размер компонента: добавь поле в `ComponentDimensions`, а не литерал в компоненте.
- **Адаптивность через экземпляры:** `CompactDimensions`, `MediumDimensions`, `ExpandedDimensions` меняют spacing и layout, фиксированные размеры общие.
- **Движение целиком в токенах.** `PdMotion` содержит длительности, пружину (`spatial()`), твин (`effects()`), `pressedScale`, `popPeakScale`. Под «убрать анимации» тема подставляет `ReducedMotion` (`isReduced = true`, нули), и все анимации обязаны проверять `PdTheme.motion.isReduced`.
- **Haptics только через интерфейс** `PdHaptics.perform(PdHapticEffect.Confirm)`. В тестах подменяется.
- **Тёмная тема проверяется не на глаз:** если проектный акцент плохо виден (контраст < 3:1), подбери ночной вариант и запиши причину в комментарии рядом (в PointDetector: `#053898` → `#75A4FE`, было 1.4:1, стало 5.8:1).

## 2. Atomic design и группы в пакетах

Уровни импортируют только вниз: **частицы** (`theme`, `resolver`, `icons`, `component`, `model`) < **atoms** < **molecules** < **organisms**.

| Уровень | Что это | Критерий | Примеры |
|---|---|---|---|
| atoms | один примитив, ничего не композирует из компонентов проекта | не делится на части | `texts/PdPlainText`, `icons/PdDecorativeIcon`, `status/PdPlaceStatusMark`, `layout/PdHairlineDivider`, `feedback/PdSkeletonBlock` |
| molecules | 2–4 атома с одной задачей | имеет смысл как «кнопка», «метка» | `buttons/PdPrimaryButton`, `status/PdHintedStatusLine`, `tags/PdTextTag`, `toggles/PdHeartToggle`, `tabs/PdTextTab` |
| organisms | самостоятельный блок экрана из молекул | можно положить на экран | `lists/PdPlaceCardData`, `help/PdHelpList`, `tabs/PdTextTabs` |

Правила размещения:

- Внутри уровня **всегда пакет-группа по смыслу**: `buttons`, `surfaces`, `texts`, `icons`, `status`, `tags`, `tabs`, `toggles`, `lists`, `help`, `feedback`, `layout`. Файл прямо в `atoms/` запрещён.
- Новая группа заводится, когда у неё минимум два компонента или она описывает отдельный смысл. Иначе положи в ближайшую.
- Один тип на файл. Файл не длиннее 250 строк.
- Общие внутренние помощники (`PdBasicText`, `PdTintedImage`, `pdPressable`) `internal` и лежат рядом с группой или в `component/`.

## 3. Контракт компонента: интерфейс + каркас + реализации

Каждый компонент, включая самые простые, это **интерфейс**. Свободных публичных `@Composable fun` нет (кроме `PdTheme(...)` в теме). Потребитель знает только интерфейс.

```kotlin
@Stable
interface PdComponent { @Composable fun Content(modifier: Modifier) }

@Immutable
abstract class PdBaseComponent : PdComponent {           // шаблонный метод
    @Composable final override fun Content(modifier: Modifier) { PdComponentFrame { Render(modifier) } }
    @Composable protected abstract fun Render(modifier: Modifier)
}
```

Реализации бывают трёх видов: **без данных** (скелетон, Null Object), **с данными**, **с экстра-данными**. Новый класс заводится, когда меняется то, что рисуется или как блок ведёт себя, а не когда «нет одного значения»: у списка нет строки, значит строка не создаётся.

Паттерны вместо `null`:

| Было | Стало | Паттерн |
|---|---|---|
| `icon: ImageVector?` | `PdIcon`: `PdNoIcon` / `PdDecorativeIcon` / `PdLabeledIcon` | Null Object |
| `help: Info?` с 4 `String?` | `PdHelpSection`: `PdNoHelpSection` / `PdHelpList(items)` | Null Object + список |
| `loading: Boolean` при обязательном `onClick` | `PdLoadingButton` без `onClick` вообще | состояние как тип |
| `onScrub: ((Float) -> Unit)?` | `PdScrub`: `PdScrubOff` / `PdScrubTo(consumer)` | Strategy |

Каркас с крючками (пример кнопки): `PdBaseButton` задаёт раскладку и шрифт, подкласс отвечает на `colors()` и `Modifier.activate()`; у `PdLoadingButton`/`PdDisabledButton` обработчика нажатия нет по построению.

В публичных сигнатурах **никогда** не появляются типы `PdBase*`. Обязательные лямбды допустимы, необязательное поведение это стратегия.

### Элементы списков

```kotlin
@Stable interface PdListItem : PdComponent { val key: String; val contentType: PdContentType }
@JvmInline value class PdContentType(val id: String) { companion object { val PlaceCard = PdContentType("place-card") } }
// экран: items(cards, key = { it.key }, contentType = { it.contentType }) { it.Content(Modifier) }
```
Ключ это `String`, потому что ключи ленивых списков должны сохраняться в Bundle. Каждый класс-реализация сам называет свой `contentType`.

## 4. Модели состояния «either» (как `BuildStatus`)

Когда компонент зависит от состояния, из которого берутся цвет и иконка, состояние оборачивается в `sealed interface`, который сам отдаёт роль и иконку. Компонент не знает, какой цвет у «открыто».

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
Состояние не должно держаться на одном цвете: иконки различаются формой (заполненный, половина, пустой круг). В ветках `when` по sealed-объектам всегда `is`.

Такой же приём для тостов (`ToastType`: `Info`, `Success`, `Warning`, `Error` с `statusRole`, `icon`, приоритетом) и для слота хоста (`ToastSlot`: `Empty` / `Showing(data)`, вместо nullable).

## 5. Резолверы текста и графики

Компонент не принимает `String` для видимого текста, `@StringRes`, `ImageVector?`, `@DrawableRes`.

```kotlin
@Stable interface PdTextResolver { @Composable @ReadOnlyComposable fun resolve(): CharSequence }
// реализации: PdLiteralText(value), PdStringText(@StringRes id, args: List<PdTextResolver>),
//             PdPluralText(@PluralsRes id, count), PdEmphasisText(base, emphasized)  // возвращает AnnotatedString со спанами

@Stable interface PdDrawableResolver { @Composable fun resolve(): Painter }
@Immutable class PdVectorDrawable(private val vector: ImageVector) : PdDrawableResolver {
    @Composable override fun resolve(): Painter = rememberVectorPainter(vector)
}
```
- `CharSequence`, чтобы возвращать `AnnotatedString`; единственная точка вывода текста `PdBasicText` разбирает оба случая.
- `ImageVector` неизменяем, поэтому реализация остаётся `@Immutable`, а painter кешируется. `painterResource` такой гарантии не даёт, XML-ресурсы переводятся в `ImageVector` один раз.
- `stringResource`, `painterResource`, `vectorResource` разрешены только в пакете `resolver`.
- Иконки живут в реестре `PdIcons` (одна строка на иконку), файлы иконок освобождены от правила hardcode.
- Цвет текста и иконок это `PdContentTone` (`Inherit`, `Primary`, `Secondary`, `Tertiary`, `Brand`, `Inverse`), не `Color`. `Inherit` читает `LocalPdContentColor`, поэтому кнопка красит метку и иконку одним цветом.

## 6. Ноль hardcode

В параметрах функций и в теле компонента нет литералов `dp`/`sp`, цветов, прозрачности, длительностей, форм, весов шрифта. Только токены темы.

Допустимые исключения (файлы токенов): `theme/**`, `icons/**` (координаты путей), `toast/tokens/**` (настройки эффектов). `0.dp` допустим. Нужно новое число: добавь его в нужную группу токенов с именем, затем используй.

Запрещённые паттерны (регулярные выражения правил): `\d+\.dp|sp`, `Color(0x`, `alpha = <цифра>`, `tween(<цифра>`, `durationMillis = <цифра>`, `delayMillis = <цифра>`, `fillMax*(0.<цифра>`, `RoundedCornerShape(`, `TextStyle(`, `FontWeight.`. Вместо `alpha = 1f - x` пиши именованную константу (`OPAQUE`), правило ловит цифру сразу после `=`.

## 7. Тосты

Модуль `:toast`, зависит только от `:designsystem`. Архитектура:

```
ToastHostState.show(data) ──► Channel<ToastCommand> ──► scan(reduce) ──► onEach(execute side effects, publish slot)
ToastCommand:  Enqueue | Dismiss(id?) | TimerExpired(id) | Evict(ticket)     (чистые функции: reduce(snapshot) → transition)
ToastSideEffect: Complete | CancelTicket | ScheduleTimer | CancelTimer
```
- Приоритет: более важный вытесняет текущий (`Replaced`), равный ждёт в FIFO. `show` приостанавливается до `Dismissed`/`TimedOut`/`Replaced`.
- Один коллектор на всё: мьютекс не нужен. Таймеры в `EffectExecutor`.
- UI: `PdToastHost` (интерфейс) → `PdSwipeToastHost`; `PdToastBanner` → `PdStatusToastBanner` (северное сияние фоном, цвета из `type.statusRole`); смахивание вверх для нелипких типов, `Error` липкий.
- Анимации (`PullRevealState`, `AuroraBackgroundNode`) принимают `animated`, под «убрать анимации» применяются мгновенно. Настройки эффектов лежат в `ToastTokens`.
- Тесты очереди обязательны (приоритеты, FIFO, таймеры, вытеснение, отмена по id).

## 8. Правила проверяются (Konsist + храповик)

Правила это чистые функции над `SourceFile(path, text)`, поэтому их тестируют на строковых фикстурах (`RulesSelfTest`). Нарушения сравниваются со списком известных: список может только уменьшаться, исправил нарушение, удали строку.

| Правило | Что ловит |
|---|---|
| `layerViolations` | импорт вышестоящего уровня (atoms → molecules и т.п.) |
| `filesOutsideGroupPackages` | файл прямо в `atoms/`, `molecules/`, `organisms/` |
| `hardcodedTokens` | литералы dp/sp, цветов, alpha, длительностей, форм, шрифтов |
| `resourceAccess` | `stringResource`/`painterResource` вне `resolver` |
| `nullableStructures` | nullable-структуры и nullable-лямбды в публичном API (примитивы можно) |
| `freePublicComposables` | публичная `@Composable fun` вне темы |
| `componentsWithoutInterface` | класс компонента без `PdBase*`/`Pd*` в супертипах |
| `baseTypesInSignatures` | `PdBase*` в публичной сигнатуре |
| `textAsString` | `text: String`, `label: String` и т.п. в публичном API |
| `businessTimeImports` | `java.time`, `kotlin.time`, `kotlinx.datetime`, `Date` в UI (в `:toast` разрешён только `kotlin.time.Duration` как таймер) |
| `manyTypesPerFile`, `longFiles`, `whenBranchesWithoutIs`, Cyrillic-литералы | стиль и ресурсы |

Время и локаль в UI не попадают: UI получает доли оси (`Float`) и готовые тексты, а `LocalTime`, форматирование и бизнес-правила остаются в маппере слоя `:app`.

## 9. Показатели (Definition of Done)

Считаются автоматически (`./gradlew :konsist:test`, отчёт метрик печатается в `DesignSystemTest`).

| Показатель | Цель | Как измеряется |
|---|---|---|
| hardcode-литералы в компонентах | 0 | `metrics.hardcodedTokens` |
| nullable-структуры в публичном API | 0 | `metrics.nullableStructures` |
| свободные публичные composable вне темы | 0 | правило `freePublicComposables` |
| доля компонентов с интерфейсом | 100 % | `componentsWithoutInterface` пуст |
| файлы длиннее 250 строк / файлы с 2+ типами | 0 / 0 | `longFiles`, `manyTypesPerFile` |
| нарушения слоёв atoms → molecules → organisms | 0 | `layerViolations` |
| контраст текста к фону | ≥ 4.5:1 | расчёт WCAG для всех пар (текст, контейнер) в обеих темах |
| контраст значков состояния к фону | ≥ 3:1 и форма отличается | расчёт + разные иконки |
| цель касания | ≥ 48 dp | `component.touchTarget` |
| реакция на «убрать анимации» | во всех анимациях | `PdTheme.motion.isReduced` проверяется в каждом бесконечном/пружинном эффекте |
| тесты очереди тостов | все сценарии зелёные | `:toast:testDebugUnitTest` |
| мутационная проверка | правило падает на вставленном литерале | ручная вставка `16.dp` → красный тест → откат |

Растущие показатели: `interfaces`, `implementations`, `abstractBases` (из `metrics`).

## 10. Подводные камни (получены на практике)

- `internal abstract fun` в публичном базовом классе делает его неразширяемым вне модуля. Если каркас должен расширяться из других модулей, делай крючки `protected`.
- Для `@get:[Composable ReadOnlyComposable]` нужны одинаковые аннотации на геттере в `data object`.
- `Float` с `alpha =` и цифрой сразу после `=` ловится правилом; клади значение в именованную константу или токен.
- `coroutines-test` 1.11: один `advanceUntilIdle()` может вернуться раньше, чем конвейер на `Channel` обработал последнюю команду. В тестах используй `settle()` (`advanceUntilIdle(); runCurrent(); advanceUntilIdle()`).
- Тип `HapticFeedbackType.Confirm/Reject/ToggleOn/ToggleOff/SegmentTick` зависит от версии Compose BOM, проверяй компиляцией.
- `buildAnnotatedString` в `PdEmphasisText` теряет спаны, если передать результат аргументом в `stringResource(formatArgs)`: аргументы приводятся к строке.
- Настоящие шрифты проекта подключаются отдельным шагом; до него используется `FontFamily.Default`, токен `headings` позволяет сменить заголовочный шрифт, не трогая текст.
- Визуальные проверки (Preview/скриншот-тесты на каждую реализацию, день и ночь, fontScale 2.0) добавляются отдельным слоем: Roborazzi или Paparazzi.

## 11. Как создать новую дизайн-систему (чек-лист)

1. Собери цвета проекта, проверь контраст, реши, что `primary`, что `status.*`.
2. Заведи `:designsystem` (Compose, без Material), `:konsist`, при необходимости `:toast`.
3. Напиши тему (раздел 1) и компилируй.
4. Базовый слой: `PdComponent`, `PdBaseComponent`, `PdListItem`/`PdBaseListItem`/`PdContentType`, резолверы, `PdIcons`, модели состояния.
5. Атомы → молекулы → организмы, каждый как интерфейс + каркас + реализации, по группам-пакетам.
6. Тосты (раздел 7), если нужны.
7. Правила Konsist и самотесты (раздел 8), мутационная проверка.
8. Раздел в `CLAUDE.md` проекта с командами и ссылкой на этот skill.
9. Отчёт с показателями (раздел 9).
