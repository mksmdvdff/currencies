package davydov.annamoney.di

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Named
import javax.inject.Singleton

@Module()
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient()

    @Provides
    @Named(CURENCIES_URL)
    fun provideCurrenciesUrl(): String = URL

    companion object {
        private const val URL = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml"
        const val CURENCIES_URL = "currencies_url"
    }
}