package com.gvg.mustwant.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gvg.mustwant.GeneralFunctions.animateLogo
import com.gvg.mustwant.Interfaces.OnAcceptCancelButtonClickListener
import com.gvg.mustwant.R
import com.gvg.mustwant.databinding.FragmentAcceptCancelBinding
class AcceptCancel : Fragment(R.layout.fragment_accept_cancel) {
    //Binding
    private var binding: FragmentAcceptCancelBinding? = null

    private var onAcceptCancelButtonClickListener: OnAcceptCancelButtonClickListener? = null

    fun setOnAcceptCancelButtonClickListener(listener: OnAcceptCancelButtonClickListener) {
        onAcceptCancelButtonClickListener = listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Animate Buttons
        animateLogo(binding?.btnAcceptFragment,300)
        animateLogo(binding?.btnCancelFragment,300)

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
