package davydov.annamoney.ui.currency.adapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import davydov.annamoney.R
import kotlin.properties.Delegates

class CurrencyAdapter(val clickListener: (String) -> Unit) : RecyclerView.Adapter<CurrencyViewHolder>() {
    var currencies by Delegates.observable(emptyList<String>()) { property, oldValue, newValue ->
        DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(old: Int, new: Int) = oldValue[old] == newValue[new]

            override fun getOldListSize() = oldValue.size

            override fun getNewListSize() = newValue.size

            override fun areContentsTheSame(p0: Int, p1: Int) =
                true // если изменится выбор - presenter назначит другой выбор

        }).dispatchUpdatesTo(this)
    }
    var selection by Delegates.observable<String?>(null) { property, oldValue, newValue ->
        DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize() = currencies.size

            override fun getNewListSize() = currencies.size

            override fun areContentsTheSame(p0: Int, p1: Int) =
                currencies[p0].let { it != oldValue && it != newValue }

            override fun areItemsTheSame(p0: Int, p1: Int) = p0 == p1 //список элементов не меняется
        }, false).dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int) =
        CurrencyViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.currency_item, p0, false))

    override fun getItemCount() = currencies.size

    override fun onBindViewHolder(p0: CurrencyViewHolder, p1: Int) {
        val currency = currencies[p1]
        p0.bindViewHolder(currency, currency == selection, clickListener)
    }

}

class CurrencyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val textView = itemView as TextView
    fun bindViewHolder(currency: String, selected: Boolean, listener: (String) -> Unit) {
        textView.text = currency
        textView.setBackgroundResource(if (selected) R.drawable.selected_rectangle else R.drawable.unselected_rectangle)
        textView.setOnClickListener { listener(currency) }
    }
}