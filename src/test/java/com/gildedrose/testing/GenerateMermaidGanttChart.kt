package com.gildedrose.testing

import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun generateMermaidGanttChart(testStatsList: List<TestStats>): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
        .withZone(ZoneId.systemDefault())

    val builder = StringBuilder()
    builder.appendLine("gantt")
    builder.appendLine("    title Test Execution Timeline")
    builder.appendLine("    dateFormat  YYYY-MM-DDTHH:mm:ss.SSS")
    builder.appendLine("    axisFormat  %S.%L")
    builder.appendLine("    tickInterval 250ms")

    fun addTaskToChart(testStats: TestStats, depth: Int = 0) {
        val indent = "    ".repeat(depth)
        val taskName = testStats.test.displayName.replace(":", " ")
        val startTime = formatter.format(testStats.start)
        val endTime = formatter.format(testStats.end)

        // Append the task in Mermaid syntax
        if (depth == 1)
            builder.appendLine("${indent}section $taskName")
        builder.appendLine("$indent$taskName ${testStats.duration.toMillis()} :, $startTime, $endTime")

        (testStats.events + testStats.children)
            .sortedBy { it.start }
            .forEach { thing ->
                when (thing) {
                    is TestStats -> addTaskToChart(thing, depth + 1)
                    is TestEvent -> builder.appendLine("${indent}${thing.name} : milestone, ${thing.start},")
                    else -> error("Unexpected type of thing")
                }
            }
    }

    // Add all the root-level tasks
    testStatsList.forEach { addTaskToChart(it) }

    return builder.toString()
}
