package com.yuriisurzhykov.pointdetector.presentation.add

import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.designsystem.theme.color.semantic.PdContentTone
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.presentation.entities.CityUi
import com.yuriisurzhykov.pointdetector.presentation.places.TimeLabels
import com.yuriisurzhykov.pointdetector.presentation.places.help.HelpFactFactory
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdDecorativeIcon
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.icons.PdIconSize
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.illustration.PdSuccessIllustration
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdPlainText
import com.yuriisurzhykov.pointdetector.uicomponents.atoms.texts.PdTextRole
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdComponent
import com.yuriisurzhykov.pointdetector.uicomponents.component.PdNoComponent
import com.yuriisurzhykov.pointdetector.uicomponents.icons.PdIcons
import com.yuriisurzhykov.pointdetector.uicomponents.model.PdChoice
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.bars.PdBackTopBar
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.bars.PdSegmentedSteps
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdDisabledButton
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdFlatIconButton
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdLoadingButton
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdOutlineButton
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.buttons.PdPrimaryButton
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.choice.PdChipRow
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.choice.PdDayChip
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.fields.PdErrorFieldMessage
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.fields.PdHintFieldMessage
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.fields.PdSearchField
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.fields.PdTextField
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.fields.PdTimeField
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.help.PdFactQuestion
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.layout.PdPairRow
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.layout.PdStack
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.rows.PdIconListRow
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.rows.PdListRow
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.rows.PdPlainListRow
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.rows.PdSkeletonListRow
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.search.PdSearchPrompt
import com.yuriisurzhykov.pointdetector.uicomponents.molecules.summary.PdSummaryCard
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.bars.PdStepTopBar
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.form.PdCenteredFormPage
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.form.PdFormPage
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.message.PdResultPanel
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.place.PdPage
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.rows.PdBorderedRows
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.section.PdTitledSection
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.sheet.PdModalSheet
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.sheet.PdSheetBody
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.sheet.PdSheetFooter
import com.yuriisurzhykov.pointdetector.uicomponents.organisms.states.PdNoMatches
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdLiteralText
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdStringText
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver
import java.time.DayOfWeek
import java.util.Locale
import javax.inject.Inject

private const val TOTAL_STEPS = 3
private const val SKELETON_ROWS = 3

/** Turns the state of the add-a-place flow into the components of its three steps and of the hours sheet. */
class AddPlaceMapper @Inject constructor(
    factories: Set<@JvmSuppressWildcards HelpFactFactory>,
) {

    private val questions = factories.sortedBy { it.question.order }

    fun page(state: AddPlaceState, city: CityUi?, actions: AddPlaceActions): PdPage = when (state.step) {
        AddStep.Search -> search(state, actions)
        AddStep.Details -> details(state, city, actions)
        AddStep.Done -> done(state, actions)
    }

    /** The sheet for choosing the hours; the screen shows it over the form while [AddPlaceState.draft] is set. */
    fun hoursSheet(state: AddPlaceState, draft: HoursDraft, actions: AddPlaceActions): PdComponent {
        val problem = draft.problem
        val days = DayOfWeek.entries.map { day ->
            PdDayChip(PdLiteralText(HoursGroups.name(day)), day in draft.days) { actions.onDay(day) }
        }
        val blocks = mutableListOf<PdComponent>(
            PdTitledSection(PdStringText(R.string.add_days), PdChipRow(days)),
            PdPairRow(
                PdTimeField(PdStringText(R.string.add_time_from), draft.fromDigits, actions::onFrom),
                PdTimeField(PdStringText(R.string.add_time_to), draft.toDigits, actions::onTo),
            ),
        )
        when (problem) {
            DraftProblem.BadTime -> blocks += PdErrorFieldMessage(PdStringText(R.string.error_incorrect_selected_days))
            DraftProblem.NoDays -> blocks += PdHintFieldMessage(PdStringText(R.string.add_choose_days))
            DraftProblem.None -> Unit
        }
        if (state.groups.isNotEmpty()) {
            blocks += PdTitledSection(PdStringText(R.string.add_hours_added), PdBorderedRows(addedRows(state.groups, actions)))
        }
        val add: PdComponent = if (problem == DraftProblem.None) {
            PdPrimaryButton(PdStringText(R.string.add_hours_add), onClick = actions::onAddHours)
        } else {
            PdDisabledButton(PdStringText(R.string.add_hours_add))
        }
        return PdModalSheet(
            title = PdStringText(R.string.title_working_hours_input),
            closeDescription = PdStringText(R.string.sheet_close),
            onClose = actions::onCloseHours,
            content = PdSheetBody(blocks),
            footer = PdSheetFooter(add),
        )
    }

    // ---- step 1 ----

    private fun search(state: AddPlaceState, actions: AddPlaceActions): PdPage = PdFormPage(
        topBar = topBar(1, R.string.title_points_add_screen, actions),
        body = listOf(
            PdSearchField(
                value = state.query,
                onValueChange = actions::onQuery,
                hint = PdStringText(R.string.hint_input_your_address),
                clearDescription = PdStringText(R.string.places_clear),
                onSearch = {},
                autoFocus = true,
            ),
            suggestions(state, actions),
        ),
        footer = emptyList(),
    )

    private fun suggestions(state: AddPlaceState, actions: AddPlaceActions): PdComponent = when (val found = state.suggestions) {
        is Suggestions.Idle -> PdSearchPrompt(PdStringText(R.string.label_description_search_address))
        is Suggestions.Loading -> PdBorderedRows(List(SKELETON_ROWS) { PdSkeletonListRow })
        is Suggestions.NotFound -> PdNoMatches(PdLiteralText(state.query), actions::onManual)
        is Suggestions.Found -> PdBorderedRows(found.points.map { suggestion(it, actions) })
    }

    private fun suggestion(point: Point, actions: AddPlaceActions): PdListRow = PdIconListRow(
        icon = PdIcons.Pin,
        title = PdLiteralText(point.placeName.ifBlank { point.address }),
        subtitle = quiet(PdLiteralText(point.address)),
        trailing = chevron(),
        onClick = { actions.onPick(point) },
    )

    // ---- step 2 ----

    private fun details(state: AddPlaceState, city: CityUi?, actions: AddPlaceActions): PdPage {
        val save: PdComponent = if (state.saving) {
            PdLoadingButton(PdStringText(R.string.add_saving))
        } else {
            PdPrimaryButton(PdStringText(R.string.button_save), onClick = actions::onSave)
        }
        val footer = listOf(save)
        return PdFormPage(
            topBar = topBar(2, R.string.title_points_add_info_screen, actions),
            body = listOf(
                PdTextField(PdStringText(R.string.hint_place_name), state.name, actions::onName, PdStringText(R.string.hint_input_place_name)),
                PdTextField(PdStringText(R.string.hint_place_address), state.address, actions::onAddress, PdStringText(R.string.hint_input_your_address)),
                PdBorderedRows(listOf(cityRow(city, actions))),
                PdTitledSection(PdStringText(R.string.hint_place_working_hours), hours(state, actions)),
                PdTitledSection(PdStringText(R.string.help_title), PdStack(questions.map { question(it, state, actions) })),
            ),
            footer = footer,
        )
    }

    private fun cityRow(city: CityUi?, actions: AddPlaceActions): PdListRow = PdIconListRow(
        icon = PdIcons.Pin,
        title = city?.let { PdLiteralText(it.name) } ?: PdStringText(R.string.city_not_selected),
        subtitle = quiet(PdStringText(R.string.add_city_label)),
        trailing = chevron(),
        onClick = actions::onCity,
    )

    private fun hours(state: AddPlaceState, actions: AddPlaceActions): PdComponent {
        val groupRows = state.groups.map { group ->
            PdPlainListRow(
                title = PdLiteralText(HoursGroups.daysLabel(group.days)),
                subtitle = quiet(PdLiteralText(range(group))),
                trailing = chevron(),
                onClick = actions::onOpenHours,
            )
        }
        val action = PdPlainListRow(
            title = PdStringText(if (state.groups.isEmpty()) R.string.title_working_hours_input else R.string.add_hours_change),
            subtitle = if (state.groups.isEmpty()) quiet(PdStringText(R.string.label_nothing_selected)) else PdNoComponent,
            trailing = chevron(),
            onClick = actions::onOpenHours,
        )
        val rows = PdBorderedRows(groupRows + action)
        return if (state.hoursError) PdStack(listOf(rows, PdErrorFieldMessage(PdStringText(R.string.error_days_not_selected)))) else rows
    }

    private fun question(factory: HelpFactFactory, state: AddPlaceState, actions: AddPlaceActions): PdFactQuestion {
        val type = factory.type
        val current = state.answer(type)
        val ask = factory.question
        return PdFactQuestion(
            icon = ask.icon,
            label = ask.label,
            answers = listOf(
                PdChoice(PdStringText(R.string.add_fact_unknown), current == FactAnswer.Unknown) { actions.onAnswer(type, FactAnswer.Unknown) },
                PdChoice(ask.yes, current == FactAnswer.Yes) { actions.onAnswer(type, FactAnswer.Yes) },
                PdChoice(ask.no, current == FactAnswer.No) { actions.onAnswer(type, FactAnswer.No) },
            ),
        )
    }

    // ---- step 3 ----

    private fun done(state: AddPlaceState, actions: AddPlaceActions): PdPage {
        val saved = state.saved
        val lines = buildList {
            add(PdLiteralText(saved?.address.orEmpty()))
            add(if (saved == null || saved.groups.isEmpty()) PdStringText(R.string.add_no_hours) else PdLiteralText(saved.groups.joinToString(" · ") { summary(it) }))
        }
        return PdCenteredFormPage(
            topBar = topBar(TOTAL_STEPS, R.string.title_points_add_info_screen, actions),
            body = listOf(
                PdResultPanel(
                    illustration = PdSuccessIllustration(PdIcons.Check),
                    title = PdStringText(R.string.label_point_successfully_created),
                    summary = PdSummaryCard(PdLiteralText(saved?.name.orEmpty()), lines),
                ),
            ),
            footer = listOf(
                PdPrimaryButton(PdStringText(R.string.add_show_on_map), PdDecorativeIcon(PdIcons.Pin, PdIconSize.Regular, PdContentTone.Inherit), actions::onShowOnMap),
                PdOutlineButton(PdStringText(R.string.add_another), onClick = actions::onAnother),
            ),
        )
    }

    // ---- shared pieces ----

    private fun addedRows(groups: List<HoursGroup>, actions: AddPlaceActions): List<PdListRow> = groups.mapIndexed { index, group ->
        val days = HoursGroups.daysLabel(group.days)
        PdPlainListRow(
            title = PdLiteralText(days),
            subtitle = quiet(PdLiteralText(range(group))),
            trailing = PdFlatIconButton(PdIcons.Close, PdStringText(R.string.add_hours_remove, listOf(PdLiteralText(days)))) { actions.onRemoveHours(index) },
            onClick = {},
        )
    }

    private fun topBar(step: Int, title: Int, actions: AddPlaceActions) = PdStepTopBar(
        topBar = PdBackTopBar(PdStringText(title), PdStringText(R.string.place_back), actions::onBack),
        steps = PdSegmentedSteps(
            step = step,
            total = TOTAL_STEPS,
            label = PdStringText(R.string.add_step_of, listOf(PdLiteralText(step.toString()), PdLiteralText(TOTAL_STEPS.toString()))),
        ),
    )

    private fun range(group: HoursGroup): String = "${TimeLabels.text(group.fromMinute)} – ${TimeLabels.text(group.toMinute)}"

    private fun summary(group: HoursGroup): String = "${HoursGroups.daysLabel(group.days, Locale.getDefault())} · ${range(group)}"

    private fun quiet(text: PdTextResolver) = PdPlainText(text, PdTextRole.BodySmall, PdContentTone.Secondary, maxLines = 1)

    private fun chevron() = PdDecorativeIcon(PdIcons.Chevron, PdIconSize.Regular, PdContentTone.Secondary)
}
