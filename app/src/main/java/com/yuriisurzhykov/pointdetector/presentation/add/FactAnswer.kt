package com.yuriisurzhykov.pointdetector.presentation.add

/** The answer to "how to get help" for one kind of fact. Only [Yes] and [No] are saved. */
enum class FactAnswer { Unknown, Yes, No }

/** Turns the answers of the form into the data: what the person did not answer is not written. */
object FactAnswers {

    fun toHelp(answers: Map<String, FactAnswer>): Map<String, Boolean> = answers.mapNotNull { (type, answer) ->
        when (answer) {
            FactAnswer.Yes -> type to true
            FactAnswer.No -> type to false
            FactAnswer.Unknown -> null
        }
    }.toMap()
}
