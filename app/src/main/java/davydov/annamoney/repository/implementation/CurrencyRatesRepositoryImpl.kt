package davydov.annamoney.repository.implementation

import davydov.annamoney.model.CurrencyWithRate
import davydov.annamoney.repository.interfaces.CurrencyRatesRepository
import davydov.annamoney.util.CurrencyWithRateParser
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.Request

class CurrencyRatesRepositoryImpl(
        val client: OkHttpClient,
        val currencyWithRateParser: CurrencyWithRateParser,
        val url: String
) : CurrencyRatesRepository {
    override val defaultCurrencyName = "EUR"
    override val currenciesRatesSingle =
            Single.create<List<CurrencyWithRate>> {
                val call = client.newCall(Request.Builder().url(url).build())
                it.setCancellable { call.cancel() }
                try {
                    it.onSuccess(currencyWithRateParser.getCurrenciesWithRates(call.execute().body()!!.charStream()).apply {
                        add(0, CurrencyWithRate(defaultCurrencyName, 1.0))
                    })
                } catch (ex: Throwable) {
                    it.onError(ex)
                }
            }

}