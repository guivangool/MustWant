package com.example.musthave.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import com.example.musthave.Interfaces.OnAcceptCancelButtonClickListener
import com.example.musthave.Interfaces.OnSpinnerButtonCLickListener
import com.example.musthave.R
import com.example.musthave.databinding.FragmentAcceptCancelBinding
import com.example.musthave.databinding.FragmentSpinnerBinding

class Spinner : Fragment(R.layout.fragment_spinner) {

    companion object {
        private const val ARG_FRAGMENT_ID = "fragment_id"
        private const val ARG_MIN_VALUE = "min_value"
        private const val ARG_MAX_VALUE = "max_value"

        fun newInstance(fragmentId: String,miValue: Int = 15, maValue : Int = 365): Spinner {
            val fragment = Spinner()
            val args = Bundle()
            args.putString(ARG_FRAGMENT_ID, fragmentId)
            args.putInt(ARG_MIN_VALUE, miValue)
            args.putInt(ARG_MAX_VALUE, maValue)
            fragment.arguments = args
            return fragment
        }
    }



    //Binding
    private var binding: FragmentSpinnerBinding? = null

    private var fragmentId: String? = null
    private var maxValue : Int? = null
    private var minValue : Int? = null

    private var onSpinnerButtonClickListener: OnSpinnerButtonCLickListener? = null

    fun setOnSpinnerButtonCLickListener(listener: OnSpinnerButtonCLickListener) {
        onSpinnerButtonClickListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            fragmentId = it.getString(ARG_FRAGMENT_ID)
            maxValue= it.getInt(ARG_MAX_VALUE)
            minValue= it.getInt(ARG_MIN_VALUE)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.btnDecrement?.setOnClickListener {
            binding?.etDays!!.setText(((binding?.etDays!!.text.toString().toInt()) - 5).toString())
            onSpinnerButtonClickListener?.onDecrementButtonCLicked(this.fragmentId)
        }
        binding?.btnIncrement?.setOnClickListener {
            binding?.etDays!!.setText(((binding?.etDays!!.text.toString().toInt()) + 5).toString())
            onSpinnerButtonClickListener?.onIncrementButtonCLicked(this.fragmentId)
        }
        binding?.etDays?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.toString()?.toIntOrNull()?.let { number ->
                    if (number > maxValue!!) {
                        binding?.etDays?.setText(maxValue.toString())
                        binding?.etDays?.setSelection(binding?.etDays?.text!!.length)
                    }
                    if (number < minValue!! )
                    {
                        binding?.etDays?.setText(minValue.toString())
                        binding?.etDays?.setSelection(binding?.etDays?.text!!.length)
                    }
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSpinnerBinding.inflate(layoutInflater)

        return binding?.root
    }
}