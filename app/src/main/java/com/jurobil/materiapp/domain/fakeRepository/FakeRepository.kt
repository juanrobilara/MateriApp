package com.jurobil.materiapp.domain.fakeRepository

import com.jurobil.materiapp.domain.model.Asignatura
import com.jurobil.materiapp.domain.model.Carrera
import com.jurobil.materiapp.domain.model.Tutor
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
        ),

        Carrera(
            id = "16",
            nombre = "Ingeniería en Telecomunicaciones",
            descripcion = "Diseño, implementación y gestión de sistemas de comunicación y redes.",
            cantidadAsignaturas = 10
        ),
        Carrera(
            id = "17",
            nombre = "Psicología Clínica",
            descripcion = "Formación para el diagnóstico y tratamiento de trastornos mentales y emocionales.",
            cantidadAsignaturas = 9
        ),
        Carrera(
            id = "18",
            nombre = "Ciencias de la Computación",
            descripcion = "Estudios avanzados en algoritmos, programación y teoría computacional.",
            cantidadAsignaturas = 12
        ),
        Carrera(
            id = "19",
            nombre = "Ingeniería Civil",
            descripcion = "Diseño, construcción y mantenimiento de infraestructuras y obras públicas.",
            cantidadAsignaturas = 11
        ),
        Carrera(
            id = "20",
            nombre = "Marketing Digital",
            descripcion = "Estrategias de promoción y publicidad en medios digitales y redes sociales.",
            cantidadAsignaturas = 7
        ),
        Carrera(
            id = "21",
            nombre = "Medicina Veterinaria",
            descripcion = "Atención médica y sanitaria para animales domésticos y de producción.",
            cantidadAsignaturas = 10
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


    val tutoresEjemplo = listOf(
        Tutor(
            id = "t1",
            nombre = "Ana Gómez",
            areaExpertise = "Matemáticas y Física",
            materias = listOf("Matemática", "Física"),
            fotoUrl = "https://images.unsplash.com/photo-1680127499432-d93494c09eb0?q=80&w=903&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
        ),
        Tutor(
            id = "t2",
            nombre = "Carlos Pérez",
            areaExpertise = "Programación y Software",
            materias = listOf("Programación I", "Estructuras de Datos", "Ingeniería de Software"),
            fotoUrl = "https://images.unsplash.com/photo-1607990281513-2c110a25bd8c?q=80&w=934&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
        ),
        Tutor(
            id = "t3",
            nombre = "Lucía Fernández",
            areaExpertise = "Ciencias Sociales",
            materias = listOf("Comunicación Oral", "Historia del Arte"),
            fotoUrl = null
        ),
        Tutor(
            id = "t4",
            nombre = "Jorge Martínez",
            areaExpertise = "Química y Biología",
            materias = listOf("Química"),
            fotoUrl = "https://images.unsplash.com/photo-1726722886957-2ed42b15aaa3?q=80&w=896&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
        ),
        Tutor(
            id = "t5",
            nombre = "María López",
            areaExpertise = "Derecho",
            materias = listOf("Derecho Civil"),
            fotoUrl = "https://plus.unsplash.com/premium_photo-1664392159809-0834c57c6637?q=80&w=880&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
        ),

        Tutor(
            id = "t6",
            nombre = "Diego Ramírez",
            areaExpertise = "Estadística",
            materias = listOf("Estadística"),
            fotoUrl = "https://randomuser.me/api/portraits/men/32.jpg"
        ),
        Tutor(
            id = "t7",
            nombre = "Sofía Torres",
            areaExpertise = "Diseño Gráfico",
            materias = listOf("Diseño Gráfico"),
            fotoUrl = "https://randomuser.me/api/portraits/women/45.jpg"
        ),
        Tutor(
            id = "t8",
            nombre = "Martín Silva",
            areaExpertise = "Comunicación",
            materias = listOf("Comunicación Oral"),
            fotoUrl = null
        ),
        Tutor(
            id = "t9",
            nombre = "Paula Méndez",
            areaExpertise = "Marketing",
            materias = listOf("Marketing"),
            fotoUrl = "https://randomuser.me/api/portraits/women/68.jpg"
        ),
        Tutor(
            id = "t10",
            nombre = "Lucas Fernández",
            areaExpertise = "Arquitectura",
            materias = listOf("Arquitectura Digital"),
            fotoUrl = null
        ),
        Tutor(
            id = "t11",
            nombre = "Elena Ríos",
            areaExpertise = "Psicología",
            materias = listOf("Psicología General"),
            fotoUrl = "https://randomuser.me/api/portraits/women/22.jpg"
        )
    )
    // Método para filtrar tutores por materia y nombre:
    fun filtrarTutores(query: String, materiaFiltro: String?): List<Tutor> {
        return tutoresEjemplo.filter { tutor ->
            val matchesQuery = tutor.nombre.contains(query, ignoreCase = true) ||
                    tutor.areaExpertise.contains(query, ignoreCase = true)
            val matchesMateria = materiaFiltro.isNullOrEmpty() || tutor.materias.contains(materiaFiltro)
            matchesQuery && matchesMateria
        }
    }




}