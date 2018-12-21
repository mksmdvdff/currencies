package davydov.annamoney.di

import davydov.annamoney.presentation.presenter.CurrencyPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    CurrencyModule::class,
    NetworkModule::class
])
interface AppComponent {
    fun inject(currencyPresenter: CurrencyPresenter)

}