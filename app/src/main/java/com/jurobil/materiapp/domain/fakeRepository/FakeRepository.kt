package com.jurobil.materiapp.domain.fakeRepository

import com.jurobil.materiapp.domain.model.Asignatura
import com.jurobil.materiapp.domain.model.Carrera
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeRepository @Inject constructor() {

    var currentAsignatura : Asignatura? = null

    val carrerasIntraconsulta = listOf(
        Carrera(
            id = "11",
            nombre = "Biotecnología",
            descripcion = "Aplicación de la biología y la tecnología en la producción industrial y científica.",
            cantidadAsignaturas = 9
        ),
        Carrera(
            id = "12",
            nombre = "Nutrición",
            descripcion = "Formación en alimentación saludable, dietética y promoción de la salud.",
            cantidadAsignaturas = 7
        ),
        Carrera(
            id = "13",
            nombre = "Ingeniería Ambiental",
            descripcion = "Estudios sobre impacto ambiental, sostenibilidad y gestión de recursos naturales.",
            cantidadAsignaturas = 11
        ),
        Carrera(
            id = "14",
            nombre = "Educación Inicial",
            descripcion = "Carrera enfocada en el desarrollo infantil y la enseñanza temprana.",
            cantidadAsignaturas = 8
        ),
        Carrera(
            id = "15",
            nombre = "Turismo",
            descripcion = "Gestión de servicios turísticos, hotelería y planificación de viajes.",
            cantidadAsignaturas = 6
        )
    )


    var carrerasEjemplo = listOf(
        Carrera(
            id = "1",
            nombre = "Ingeniería en Sistemas",
            descripcion = "Carrera orientada al desarrollo de software y sistemas informáticos.",
            cantidadAsignaturas = 10
        ),
        Carrera(
            id = "2",
            nombre = "Administración de Empresas",
            descripcion = "Enfocada en la gestión organizacional, finanzas y recursos humanos.",
            cantidadAsignaturas = 12
        ),
        Carrera(
            id = "3",
            nombre = "Diseño Gráfico",
            descripcion = "Carrera que forma profesionales en comunicación visual y diseño digital.",
            cantidadAsignaturas = 8
        ),
        Carrera(
            id = "4",
            nombre = "Medicina",
            descripcion = "Formación integral en ciencias de la salud y atención médica.",
            cantidadAsignaturas = 15
        ),
        Carrera(
            id = "5",
            nombre = "Contador Público",
            descripcion = "Carrera especializada en contabilidad, impuestos y auditoría.",
            cantidadAsignaturas = 11
        ),
        Carrera(
            id = "6",
            nombre = "Psicología",
            descripcion = "Estudia la mente humana y el comportamiento.",
            cantidadAsignaturas = 5
        ),
        Carrera(
            id = "7",
            nombre = "Arquitectura",
            descripcion = "Diseño y planificación de espacios habitables y estructuras.",
            cantidadAsignaturas = 4
        ),
        Carrera(
            id = "8",
            nombre = "Ingeniería Industrial",
            descripcion = "Optimización de procesos productivos y gestión de operaciones.",
            cantidadAsignaturas = 12
        ),
        Carrera(
            id = "9",
            nombre = "Derecho",
            descripcion = "Estudios jurídicos y del sistema legal.",
            cantidadAsignaturas = 22
        ),
        Carrera(
            id = "10",
            nombre = "Comunicación Social",
            descripcion = "Formación en medios, periodismo y estrategias comunicacionales.",
            cantidadAsignaturas = 10
        )
    )


    fun generarAsignaturasPorCarrera(idCarrera: String): List<Asignatura> {
        val carrera = carrerasEjemplo.find { it.id == idCarrera } ?: return emptyList()

        val nombresPosibles = listOf(
            "Matemática", "Física", "Química", "Programación I", "Programación II",
            "Estructuras de Datos", "Álgebra", "Estadística", "Base de Datos",
            "Ingeniería de Software", "Contabilidad", "Economía", "Marketing",
            "Psicología General", "Derecho Civil", "Arquitectura Digital",
            "Redes de Computadoras", "Diseño Gráfico", "Comunicación Oral",
            "Historia del Arte"
        ).shuffled()

        return List(carrera.cantidadAsignaturas.coerceAtMost(nombresPosibles.size)) { index ->
            Asignatura(
                nombre = nombresPosibles[index],
                nota = (4..10).random().toDouble(),
                completada = (0..1).random() == 1,
                observaciones = if ((0..1).random() == 1) "Observación ${index + 1}" else "",
                numero = index + 1
            )
        }
    }
}