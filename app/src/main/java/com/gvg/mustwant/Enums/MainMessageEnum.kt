package com.gvg.mustwant.Enums

enum class MainMessageEnum (val number: Int = 0,val message:String = ""){
    NO_SELECTED_GOALS (1,"Para iniciar el proceso de mejora individual hay que elegir uno o más objetivos."),
    NO_GOAL_PROGRESS (2,"Todos los días hay que registrar el avance de los objetivos."),
    NO_INSPIRATIONS (3,"Si no logra avanzar en los objetivos, cree inspiraciones para motivarse a seguir."),
    ALL_COMPLETED(4,"")
}