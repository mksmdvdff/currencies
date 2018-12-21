package davydov.annamoney.util

import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.IdRes
import androidx.navigation.NavController

open class FragmentCompanion {
    fun navigateTo(navController: NavController, @IdRes id: Int) = navController.navigate(id)
}
//этот класс не используется в тестовом задании, он здесь просто для понимания того, почему я обычно использую компаньон
//так же с ним идет делегат для ленивого получения параметра, его тащить не стал
open class ParcelableFragmentCompanion<T : Parcelable> {
    fun navigateTo(navController: NavController, @IdRes id: Int, arg: T) =
        navController.navigate(id, Bundle().apply {
            putParcelable(KEY_ARG, arg)
        })

    companion object {
        const val KEY_ARG = "argument_key"
    }
}


