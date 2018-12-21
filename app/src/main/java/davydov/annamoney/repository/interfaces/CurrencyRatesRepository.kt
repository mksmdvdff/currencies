package davydov.annamoney.repository.interfaces

import davydov.annamoney.model.CurrencyWithRate
import io.reactivex.Single

interface CurrencyRatesRepository {
    val currenciesRatesSingle: Single<out List<CurrencyWithRate>>
    val defaultCurrencyName: String
}