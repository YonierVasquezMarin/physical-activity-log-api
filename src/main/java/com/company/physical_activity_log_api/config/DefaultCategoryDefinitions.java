package com.company.physical_activity_log_api.config;

import java.util.List;

public final class DefaultCategoryDefinitions {

	private DefaultCategoryDefinitions() {
	}

	public record DefaultCategory(String name, String description) {
	}

	public static final List<DefaultCategory> CATEGORIES = List.of(
			new DefaultCategory(
					"Cardiovascular",
					"Ejercicio aeróbico de intensidad moderada a alta, sostenido o rítmico."),
			new DefaultCategory(
					"Fuerza",
					"Trabajo muscular con carga o resistencia externa o corporal."),
			new DefaultCategory(
					"Flexibilidad y movilidad",
					"Amplitud articular, elongación y trabajo de movilidad."),
			new DefaultCategory(
					"HIIT y circuitos",
					"Intervalos de alta intensidad o circuitos combinados."),
			new DefaultCategory(
					"Entrenamiento funcional",
					"Patrones multiarticulares orientados al movimiento cotidiano."),
			new DefaultCategory(
					"Deportes",
					"Actividades deportivas con reglas o contexto de juego."),
			new DefaultCategory(
					"Actividades cotidianas",
					"Movimiento no estructurado del día a día."),
			new DefaultCategory(
					"Recuperación activa",
					"Sesiones de baja intensidad orientadas a recuperar."),
			new DefaultCategory(
					"Otros",
					"Actividades que no encajan claramente en otra categoría."));

}
