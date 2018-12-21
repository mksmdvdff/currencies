package davydov.annamoney.di

import davydov.annamoney.repository.implementation.CurrencyRatesRepositoryImpl
import davydov.annamoney.repository.interfaces.CurrencyRatesRepository
import davydov.annamoney.util.CurrencyWithRateParser
import dagger.Module
import javax.inject.Singleton
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Named


@Module(includes = [NetworkModule::class])
class CurrencyModule {

    @Provides
    @Singleton
    fun provideRepository(okHttpClient: OkHttpClient,
                          parser: CurrencyWithRateParser,
                          @Named(NetworkModule.CURENCIES_URL) currenciesUrl: String): CurrencyRatesRepository =
            CurrencyRatesRepositoryImpl(okHttpClient, parser, currenciesUrl)

    @Provides
    @Singleton
    fun provideXmlParser() = CurrencyWithRateParser()
}