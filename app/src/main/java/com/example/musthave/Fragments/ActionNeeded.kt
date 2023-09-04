package com.example.musthave.Fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.musthave.R
import com.example.musthave.databinding.FragmentAcceptCancelBinding
import com.example.musthave.databinding.FragmentActionNeededBinding

class ActionNeeded : Fragment(R.layout.fragment_action_needed) {

    //Binding
    private var binding: FragmentActionNeededBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentActionNeededBinding.inflate(layoutInflater)

        val bundle = arguments
        val messageNumber = bundle!!.getInt("messageNumber")
        val messageImage = bundle!!.getString("messageImage")
        val message = bundle!!.getString("message")

        when (messageNumber) {
            1 -> {
                binding?.ivToDo?.setImageDrawable(getResources().getDrawable(R.drawable.fondo_accion))
                binding?.tvToDO?.text = getResources().getString(R.string.action_goals_message)
                binding?.ivToDo?.scaleType = ImageView.ScaleType.FIT_START
            }
            2 -> {
                binding?.ivToDo?.setImageDrawable(getResources().getDrawable(R.drawable.fondo_accion))
                binding?.tvToDO?.text = getResources().getString(R.string.action_goals_progress_message)
                binding?.ivToDo?.scaleType = ImageView.ScaleType.FIT_START
            }
            3 -> {
                binding?.ivToDo?.setImageDrawable(getResources().getDrawable(R.drawable.fondo_recomendada))
                binding?.tvToDO?.text = getResources().getString(R.string.action_create_inspiration_message)
                binding?.ivToDo?.scaleType = ImageView.ScaleType.FIT_START
            }
            4 -> {
                var bitmap: Bitmap = BitmapFactory.decodeFile(messageImage)
                binding?.ivToDo?.setImageBitmap(bitmap)
                binding?.tvToDO?.text = message
                binding?.ivToDo?.scaleType = ImageView.ScaleType.FIT_END
                binding?.tvToDO?.setTextColor(resources.getColor(R.color.white))
            }
        }
        return binding?.root
    }
}