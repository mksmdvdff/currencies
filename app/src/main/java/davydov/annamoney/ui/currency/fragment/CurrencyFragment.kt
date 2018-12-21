package davydov.annamoney.ui.currency.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import davydov.annamoney.R
import davydov.annamoney.presentation.presenter.CurrencyPresenter
import davydov.annamoney.presentation.view.CurrencyView
import davydov.annamoney.ui.MainActivity
import davydov.annamoney.ui.currency.adapter.CurrencyAdapter
import davydov.annamoney.util.DecimalDigitsFilter
import java.math.BigDecimal
import java.math.RoundingMode


class CurrencyFragment : MvpAppCompatFragment(), CurrencyView {

    private val sourceAdapter = CurrencyAdapter {
        currencyPresenter.setSourceCurrency(it)
    }
    private val destAdapter = CurrencyAdapter {
        currencyPresenter.setDestinationCurrency(it)
    }
    private lateinit var sourceAmount: EditText
    private lateinit var destAmount: TextView


    @InjectPresenter(type = PresenterType.GLOBAL)
    lateinit var currencyPresenter: CurrencyPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.currency_screen, container, false)
        with(view.findViewById<RecyclerView>(R.id.source_currencies)) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = sourceAdapter
        }
        with(view.findViewById<RecyclerView>(R.id.dest_currencies)) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = destAdapter
        }
        sourceAmount = view.findViewById(R.id.source_amount)
        with(sourceAmount) {
            filters = arrayOf(DecimalDigitsFilter(9, 2))
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {
                    val value = s.toString().toDoubleOrNull() ?: 0.0
                    currencyPresenter.setAmount(BigDecimal(value).setScale(2, RoundingMode.HALF_UP))
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

            })
        }
        destAmount = view.findViewById(R.id.dest_amount)
        view.findViewById<Button>(R.id.next_stage).setOnClickListener {
            currencyPresenter.toLottieScreen((activity as MainActivity).navController)
        }
        return view
    }

    override fun setSourceCurrency(currency: String) {
        sourceAdapter.selection = currency
    }

    override fun setDestinationCurrency(currency: String) {
        destAdapter.selection = currency
    }

    override fun setSourceCurrencies(currencies: List<String>) {
        sourceAdapter.currencies = currencies
    }

    override fun setDestCurrencies(currencies: List<String>) {
        destAdapter.currencies = currencies
    }

    override fun setSourceAmount(bigDecimal: BigDecimal) {
        if (sourceAmount.text.isEmpty()) {
            sourceAmount.setText(bigDecimal.toPlainString())
        }
    }

    override fun setDestinationAmount(bigDecimal: BigDecimal) {
        destAmount.text = bigDecimal.toPlainString()
    }

    override fun onStart() {
        super.onStart()
        currencyPresenter.screenShown()
    }

    override fun onStop() {
        super.onStop()
        currencyPresenter.screenHided()
    }

}