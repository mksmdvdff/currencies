package davydov.annamoney.presentation.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import java.math.BigDecimal

interface CurrencyView : MvpView {
    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun setSourceAmount(bigDecimal: BigDecimal)

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun setDestinationAmount(bigDecimal: BigDecimal)

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun setSourceCurrencies(currencies: List<String>)

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun setDestCurrencies(currencies: List<String>)

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun setSourceCurrency(currency: String)

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun setDestinationCurrency(currency: String)
}