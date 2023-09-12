package com.example.musthave.Fragments

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.musthave.GeneralFunctions.animateLogo
import com.example.musthave.R
import com.example.musthave.databinding.FragmentAcceptCancelBinding
import com.example.musthave.databinding.FragmentActionNeededBinding

class ActionNeeded : Fragment(R.layout.fragment_action_needed) {
    //Binding
    private var binding: FragmentActionNeededBinding? = null

    @SuppressLint("ClickableViewAccessibility")
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
                binding?.ivToDo?.setImageDrawable(getResources().getDrawable(R.drawable.bg_action_without_title))
                binding?.tvToDO?.text = getResources().getString(R.string.action_goals_message)
                binding?.ivToDo?.scaleType = ImageView.ScaleType.FIT_XY
            }
            2 -> {
                binding?.ivToDo?.setImageDrawable(getResources().getDrawable(R.drawable.bg_action_without_title))
                binding?.tvToDO?.text = getResources().getString(R.string.action_goals_progress_message)
                binding?.ivToDo?.scaleType = ImageView.ScaleType.FIT_XY
            }
            3 -> {
                binding?.ivToDo?.setImageDrawable(getResources().getDrawable(R.drawable.bg_action_without_title))
                binding?.tvToDO?.text = getResources().getString(R.string.action_create_inspiration_message)
                binding?.ivToDo?.scaleType = ImageView.ScaleType.FIT_XY
            }
            4 -> {
                var bitmap: Bitmap = BitmapFactory.decodeFile(messageImage)
                binding?.ivToDo?.setImageBitmap(bitmap)
                binding?.tvToDO?.text = message
                binding?.ivToDo?.scaleType = ImageView.ScaleType.CENTER_CROP
                binding?.tvToDO?.setTextColor(resources.getColor(R.color.white))
                binding?.tvToDO?.setBackgroundColor(resources.getColor(R.color.black))
            }
        }
        return binding?.root
    }
}