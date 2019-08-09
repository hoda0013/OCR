package com.example.betterrecognize.processing

import com.google.firebase.ml.vision.text.FirebaseVisionText

class TextParser {

    private val GROSS_BUSHEL_ABBREVIATTED = "Grs Bu"
    private val NET_BUSHEL_ABBREVIATED = "Net Bu"
    private val matchEverythingUpToFirstDigitRegex: Regex = "(^\\D*)".toRegex()
    private val commaMatcherRegex: Regex = "(\\,)".toRegex()
    fun parse(input: FirebaseVisionText): ParsedOutput {
        val output = ParsedOutput(null, null, null, null, null)
        val blocks: MutableList<FirebaseVisionText.TextBlock> = input.textBlocks

        val lines: ArrayList<String> = arrayListOf()

        blocks.map { textBlock ->
            textBlock.lines.map { line ->
                lines.add(line.text)
            }
        }

        lines.forEach {
            if (it.contains(GROSS_BUSHEL_ABBREVIATTED)) {
                val temp = matchEverythingUpToFirstDigitRegex.replace(it, "")
                output.grossBushel = commaMatcherRegex.replace(temp, "")

            }
            if (it.contains(NET_BUSHEL_ABBREVIATED)) {
                val temp = matchEverythingUpToFirstDigitRegex.replace(it, "")
                output.netBushel = commaMatcherRegex.replace(temp, "")
            }
        }

        return output
    }
}

data class ParsedOutput(
    var tareWeight: String?,
    var netWeight: String?,
    var grossWeight: String?,
    var netBushel: String?,
    var grossBushel: String?
)