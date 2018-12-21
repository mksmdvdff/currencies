package davydov.annamoney.util

import android.text.InputFilter
import android.text.Spanned

class DecimalDigitsFilter(
    private val digitsBeforeDot: Int = 8,
    private val digitsAfterDot: Int = 2,
    private val correctDelimeter: Char = '.',
    private val incorrectDelimeter: Char = ','
) :
    InputFilter {


    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        val newString = source.substring(start, end)
            .replace(incorrectDelimeter, correctDelimeter)
        val beforePart = dest.substring(0, dstart)
        val afterPart = dest.substring(dend, dest.length)

        val fullNewString = beforePart + newString + afterPart
        if (fullNewString.count { it == correctDelimeter } > 1) {
            return ""
        }
        val split = fullNewString.split(correctDelimeter)
        return when (split.size) {
            0 -> newString
            1 -> {
                if (isBeforeDotStringCorrect(split[0])) {
                    newString
                } else {
                    ""
                }
            }
            2 -> {
                if (isBeforeDotStringCorrect(split[0]) && isAfterDotStringCorrect(split[1])) {
                    newString
                } else {
                    ""
                }
            }
            else -> ""
        }
    }

    private fun isBeforeDotStringCorrect(beforeDotString: String) =
        (beforeDotString.length == 1 || !beforeDotString.startsWith('0')) && beforeDotString.length <= digitsBeforeDot

    private fun isAfterDotStringCorrect(afterDotString: String) =
        afterDotString.length <= digitsAfterDot
}