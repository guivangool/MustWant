package com.example.musthave.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.musthave.Interfaces.OnAcceptCancelButtonClickListener
import com.example.musthave.R
import com.example.musthave.databinding.FragmentAcceptCancelBinding
class AcceptCancel : Fragment(R.layout.fragment_accept_cancel) {
    //Binding
    private var binding: FragmentAcceptCancelBinding? = null

    private var onAcceptCancelButtonClickListener: OnAcceptCancelButtonClickListener? = null

    fun setOnAcceptCancelButtonClickListener(listener: OnAcceptCancelButtonClickListener) {
        onAcceptCancelButtonClickListener = listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.btnAcceptFragment?.setOnClickListener {
            onAcceptCancelButtonClickListener?.onAcceptButtonCLicked()

        }
        binding?.btnCancelFragment?.setOnClickListener {
            onAcceptCancelButtonClickListener?.onCancelButtonCLicked()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAcceptCancelBinding.inflate(layoutInflater)
        return binding?.root
    }
}
