package com.example.musthave.Enums

import android.content.Context
import com.example.musthave.R

enum class GoalTypeEnum (val label:String = "", val number: Int = 0) {
    ME ("Yo",1),
    HOME ("Hogar",2),
    WORK ("Trabajo",3),
    RELATIONS ("Relaciones",4);

    fun getText(context: Context): String {
        return when (this) {
            ME ->  context.getString(R.string.GoalMe)
            HOME ->  context.getString(R.string.GoalHome)
            WORK ->  context.getString(R.string.GoalWork)
            RELATIONS -> context.getString(R.string.GoalRelations)
        }
    }
}