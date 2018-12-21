package davydov.annamoney.presentation.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import java.math.BigDecimal

interface CurrencyView : MvpView {
    @StateStrategyType(value = SingleStateStrategy::class)
    fun setSourceAmount(bigDecimal: BigDecimal)

    @StateStrategyType(value = SingleStateStrategy::class)
    fun setDestinationAmount(bigDecimal: BigDecimal)

    @StateStrategyType(value = SingleStateStrategy::class)
    fun setSourceCurrencies(currencies: List<String>)

    @StateStrategyType(value = SingleStateStrategy::class)
    fun setDestCurrencies(currencies: List<String>)

    @StateStrategyType(value = SingleStateStrategy::class)
    fun setSourceCurrency(currency: String)

    @StateStrategyType(value = SingleStateStrategy::class)
    fun setDestinationCurrency(currency: String)
}