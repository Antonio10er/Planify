package com.espinoza.planify.models

data class Asignatura(
    val id: Int = 0,

    val nombre: String,
    val dia: String,
    val horaInicio: String,
    val horaFin: String,

    //Campos opcionales
    val aula: String?,
    val profesor: String?,
    val nota: String?
)

