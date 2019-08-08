package com.example.betterrecognize.processing

import com.google.firebase.ml.vision.text.FirebaseVisionText

class TextParser {

    private val GROSS_BUSHEL_ABBREVIATTED = "Grs Bu"
    private val NET_BUSHEL_ABBREVIATED = "Net Bu"
    private val matchEverythingUpToFirstDigitRegex: Regex = "(^\\D*)".toRegex()
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
                output.grossBushel = matchEverythingUpToFirstDigitRegex.replace(it, "")
            }
            if (it.contains(NET_BUSHEL_ABBREVIATED)) {
                output.netBushel = matchEverythingUpToFirstDigitRegex.replace(it, "")
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