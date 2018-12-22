package davydov.annamoney.presentation.presenter

import androidx.navigation.NavController
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import davydov.annamoney.R
import davydov.annamoney.application.App
import davydov.annamoney.presentation.view.CurrencyView
import davydov.annamoney.repository.interfaces.CurrencyRatesRepository
import davydov.annamoney.ui.lottie.LottieFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.properties.Delegates

@InjectViewState
class CurrencyPresenter : MvpPresenter<CurrencyView>() {

    @Inject
    lateinit var currencyRatesRepository: CurrencyRatesRepository

    var sourceCurrency : String by Delegates.observable("") {_, old, new ->
        if (new != old) {
            viewState.setSourceCurrency(new)
            recalcSum(rates, sourceCurrency, destCurrency, amount)
        }
    }

    var destCurrency : String by Delegates.observable("") {_, old, new ->
        if (new != old) {
            viewState.setDestinationCurrency(new)
            recalcSum(rates, sourceCurrency, destCurrency, amount)
        }
    }
    var amount : BigDecimal? by Delegates.observable<BigDecimal?>(null) { _, old, new ->
        if (new != old) {
            new?.let {viewState.setSourceAmount(it)}
            recalcSum(rates, sourceCurrency, destCurrency, amount)
        }
    }
    private var rates: Map<String, Double> = emptyMap()
    private lateinit var disposable: Disposable

    init {
        App.appComponent.inject(this)
        //инициализирую переменные здесь, чтобы сработал делегат
        sourceCurrency = currencyRatesRepository.defaultCurrencyName
        destCurrency = currencyRatesRepository.defaultCurrencyName
    }




    fun screenShown() {
        disposable = Observable.interval(0, 30, TimeUnit.SECONDS, Schedulers.io())
            .flatMapMaybe {
                currencyRatesRepository.currenciesRatesSingle
                    .toMaybe()
                    .filter {rates -> rates.isNotEmpty() }
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError { } //we can show error there
                    .doOnSuccess { } //we can hide error there
                    .onErrorComplete()
            }
            .distinctUntilChanged()
            .subscribe{ratesList ->
                rates = ratesList.associateTo(HashMap()) { it.currency to it.rate }
                checkCurrencies(ratesList.map { it.currency }, sourceCurrency, destCurrency)
                recalcSum(rates, sourceCurrency, destCurrency, amount)
            }
    }

    private fun checkCurrencies(currencies: List<String>, source: String, dest: String) {
        val defaultCurrency = currencies[0]
        viewState.setSourceCurrencies(currencies)
        viewState.setDestCurrencies(currencies)
        if (!currencies.contains(source)) {
            sourceCurrency = defaultCurrency
        }
        if (!currencies.contains(dest)) {
            destCurrency = defaultCurrency
        }
    }

    private fun recalcSum(rates: Map<String, Double>,
                          sourceCurrency : String,
                          destCurrency: String,
                          amount: BigDecimal?){
        val sourceRate = rates[sourceCurrency]?.let { BigDecimal(it) }
        val destRate = rates[destCurrency]?.let { BigDecimal(it) }
        if (sourceRate != null && destRate != null) {
            val current = amount ?: BigDecimal.ZERO
            val newSum = (current * destRate / sourceRate).setScale(2, RoundingMode.HALF_UP)
            viewState.setDestinationAmount(newSum)
        }
    }

    fun screenHided() {
        disposable.dispose()
    }

    fun toLottieScreen(navController: NavController) {
        LottieFragment.navigateTo(navController, R.id.currencies_to_lottie)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

}

