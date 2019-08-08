package com.example.betterrecognize.processing

import com.google.firebase.ml.vision.text.FirebaseVisionText

class TextParser {

    private val GROSS_BUSHEL_ABBREVIATTED = "Grs Bu"
    private val NET_BUSHEL_ABBREVIATED = "Net Bu"

    fun parse(input: FirebaseVisionText): ParsedOutput {
        var output = ParsedOutput(null, null, null, null, null)
        val blocks: MutableList<FirebaseVisionText.TextBlock> = input.textBlocks

        val lines: ArrayList<String> = arrayListOf()

        blocks.map { textBlock ->
            textBlock.lines.map { line ->
                lines.add(line.text)
            }
        }

        lines.forEach {
            if (it.contains(GROSS_BUSHEL_ABBREVIATTED)) {
                output.grossBushel = it
            }
            if (it.contains(NET_BUSHEL_ABBREVIATED)) {
                output.netBushel = it
            }
        }

//        for (i in blocks.indices) {
//            val lines = blocks[i].lines
//            for (j in lines.indices) {
//                val elements = lines[j].elements
//                for (k in elements.indices) {
//
//
////                    val textGraphic = TextGraphic(mGraphicOverlay, elements[k])
//                    //                    String keyword = "Grs Bu.";
//                    //                    FirebaseVisionText.TextBlock block = blocks.get(i);
//                    //                    String text = block.getText();
//                    //                    String lineText = lines.get(j).getText();
//                    //                    if (lineText.contains(keyword)) {
//                    //                        Rect boundingBox = block.getBoundingBox();
//                    //                        Point[] cornerPoints = block.getCornerPoints();
//
////                    mGraphicOverlay.add(textGraphic)
//                    //                        Log.i("TAG", String.format("Keyword: %s exists at \n boundingBox L: %d, R: %d, T: %d, B: %d \n cornerPoints: 1: %d", keyword));
//                    //                    }
//
//                }
//            }
//        }
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