package davydov.annamoney.util

import android.util.Xml
import davydov.annamoney.model.CurrencyWithRate
import org.xmlpull.v1.XmlPullParser
import java.io.Reader

class CurrencyWithRateParser {

    fun getCurrenciesWithRates(reader: Reader): ArrayList<CurrencyWithRate> {
        val parser = Xml.newPullParser()
        parser.setInput(reader)
        val result = ArrayList<CurrencyWithRate>()
        while (parser.eventType != XmlPullParser.END_DOCUMENT) {
            if (parser.eventType == XmlPullParser.START_TAG
                && parser.name == "Cube"
            ) {
                val name = parser.getAttributeValue(null, "currency")
                val rate = parser.getAttributeValue(null, "rate")?.toDoubleOrNull()
                if (name != null && rate != null) {
                    result.add(CurrencyWithRate(name, rate))
                }
            }
            parser.next()
        }
        return result
    }
}