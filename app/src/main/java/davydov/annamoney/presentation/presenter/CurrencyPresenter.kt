package davydov.annamoney.presentation.presenter

import androidx.navigation.NavController
import davydov.annamoney.application.App
import davydov.annamoney.model.CurrencyWithRate
import davydov.annamoney.presentation.view.CurrencyView
import davydov.annamoney.repository.interfaces.CurrencyRatesRepository
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import davydov.annamoney.R
import davydov.annamoney.ui.lottie.LottieFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function4
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import davydov.annamoney.util.Optional
import davydov.annamoney.util.optional
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@InjectViewState
class CurrencyPresenter : MvpPresenter<CurrencyView>() {

    init {
        App.appComponent.inject(this)
    }

    @Inject
    lateinit var currencyRatesRepository: CurrencyRatesRepository

    private val sourceCurrencySubject =
        BehaviorSubject.createDefault<String>(currencyRatesRepository.defaultCurrencyName)
    private val destCurrencySubject =
        BehaviorSubject.createDefault<String>(currencyRatesRepository.defaultCurrencyName)
    private val amountSubject = BehaviorSubject.createDefault<Optional<BigDecimal>>(Optional.empty())
    private lateinit var disposable: CompositeDisposable


    fun screenShown() {
        disposable = CompositeDisposable()
        sourceCurrencySubject.distinctUntilChanged().subscribe { viewState.setSourceCurrency(it) }.beforeStop()
        destCurrencySubject.distinctUntilChanged().subscribe { viewState.setDestinationCurrency(it) }.beforeStop()
        amountSubject.distinctUntilChanged().subscribe {
            it.value?.let { viewState.setSourceAmount(it) }
        }.beforeStop()
        val refreshRatesObservable = Observable.interval(0, 30, TimeUnit.SECONDS, Schedulers.io())
            .flatMapMaybe {
                currencyRatesRepository.currenciesRatesSingle
                    .toMaybe()
                    .filter { it.isNotEmpty() }
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError { } //we can show error there
                    .doOnSuccess { } //we can hide error there
                    .onErrorComplete()
            }.distinctUntilChanged()
        Observable.combineLatest(
            refreshRatesObservable,
            sourceCurrencySubject.distinctUntilChanged(),
            destCurrencySubject.distinctUntilChanged(),
            amountSubject.distinctUntilChanged(),
            Function4<List<CurrencyWithRate>, String, String, Optional<BigDecimal>, Optional<BigDecimal>> { rates, source, dest, amount ->
                checkCurrencies(rates.map { it.currency }, source, dest)
                val ratesMap = rates.associate { it.currency to it.rate }
                val sourceRate = ratesMap[source]?.let { BigDecimal(it) }
                val destRate = ratesMap[dest]?.let { BigDecimal(it) }
                if (sourceRate == null || destRate == null) {
                    Optional.empty() //список валют изменился. checkCurrencies заэмитит другую выбранную валюту
                } else {
                    val current = amount.value ?: BigDecimal.ZERO
                    (current * sourceRate / destRate).setScale(2, RoundingMode.HALF_UP)
                        .optional()
                }

            })
            .subscribe { it.value?.let { viewState.setDestinationAmount(it) } }
            .beforeStop()
    }

    private fun checkCurrencies(currencies: List<String>, source: String, dest: String) {
        val defaultCurrency = currencies[0]
        viewState.setSourceCurrencies(currencies)
        viewState.setDestCurrencies(currencies)
        if (!currencies.contains(source)) {
            setSourceCurrency(defaultCurrency)
        }
        if (!currencies.contains(dest)) {
            setDestinationCurrency(defaultCurrency)
        }
    }

    fun setSourceCurrency(currency: String) {
        sourceCurrencySubject.onNext(currency)
    }

    fun setDestinationCurrency(currency: String) {
        destCurrencySubject.onNext(currency)
    }

    fun setAmount(amount: BigDecimal) {
        amountSubject.onNext(amount.optional())
    }

    fun screenHided() {
        disposable.dispose()
    }

    fun toLottieScreen(navController: NavController) {
        LottieFragment.navigateTo(navController, R.id.currencies_to_lotty)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

    private fun Disposable.beforeStop(): Disposable {
        disposable.add(this)
        return this
    }

}

