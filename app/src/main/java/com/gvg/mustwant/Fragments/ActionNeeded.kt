package com.gvg.mustwant.Fragments

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.gvg.mustwant.R
import com.gvg.mustwant.databinding.FragmentActionNeededBinding

class ActionNeeded : Fragment(R.layout.fragment_action_needed) {
    //Binding
    private var binding: FragmentActionNeededBinding? = null

    interface FunctionProvider {
        fun provideFunction(messageNumber:Int)
    }
    fun setFunctionProvider(provider: FunctionProvider) {
        functionProvider = provider
    }
    private var functionProvider: FunctionProvider? = null

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

        binding?.tvRecommendedAction?.setOnClickListener {
            functionProvider?.provideFunction(messageNumber)
        }
        when (messageNumber) {
            1 -> {
                binding?.ivToDo?.setImageDrawable(getResources().getDrawable(R.drawable.bg_action_without_title))
                binding?.tvToDO?.text = getResources().getString(R.string.action_goals_message)
                binding?.ivToDo?.scaleType = ImageView.ScaleType.FIT_XY
                binding?.tvRecommendedAction?.text = getString(R.string.option_name_select_goals)
                binding?.tvRecommendedAction?.visibility = View.VISIBLE

            }
            2 -> {
                binding?.ivToDo?.setImageDrawable(getResources().getDrawable(R.drawable.bg_action_without_title))
                binding?.tvToDO?.text = getResources().getString(R.string.action_goals_progress_message)
                binding?.ivToDo?.scaleType = ImageView.ScaleType.FIT_XY
                binding?.tvRecommendedAction?.text = getString(R.string.option_name_register_advance)
                binding?.tvRecommendedAction?.visibility = View.VISIBLE
            }
            3 -> {
                binding?.ivToDo?.setImageDrawable(getResources().getDrawable(R.drawable.bg_action_without_title))
                binding?.tvToDO?.text = getResources().getString(R.string.action_create_inspiration_message)
                binding?.ivToDo?.scaleType = ImageView.ScaleType.FIT_XY
                binding?.tvRecommendedAction?.text = getString(R.string.option_name_create_inspiration)
                binding?.tvRecommendedAction?.visibility = View.VISIBLE
            }
            4 -> {
                binding?.tvRecommendedAction?.visibility = View.GONE
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