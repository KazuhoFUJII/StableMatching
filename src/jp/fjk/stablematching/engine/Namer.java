package jp.fjk.stablematching.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Namer {
	private final static String[] MALE_NAMES = {
			"Barco", "Zapato", "Sol", "Libro", "Mundo", "Abril", "Ano", "Amino",
			"Lapiz", "Vidrio", "Metal", "Brazo", "Derecho", "Fuego",
			"Castillo", "Arbol", "Hielo", "Pajaro", "Viento", "Edificio",
			"Campo", "Recado", "Traje", "Bolsillo", "Avion", "Alumno", "Agosto",
			"Matres", "Marzo", "Espejo", "Esfuerzo", "Jueves", "Juego",
			};
	private final static String[] FEMALE_NAMES = {
			"Luna", "Letra", "Mano", "Mesa", "Poesia", "Noche", "Manana",
			"Botella", "Fuerza", "Fruta", "Fuente", "Flor", "Verdura",
			"Bicicleta", "Agua", "Camisa", "Casa", "Estrella", "Ventana",
			"Cuerda", "Guerra", "Aguja", "Carne", "Gota", "Moda", "Primavera",
			"Foto", "Fortuna", "Literatura", "Historia", "Esperanza", "Salida",
			"Joya", "Cultura", "Curva",
			};
	private List<String> mlist = new ArrayList<String>();
	private List<String> flist = new ArrayList<String>();

	private Random random;

	public Namer() {
		random = new Random(System.currentTimeMillis());
		for (String name : MALE_NAMES) {
			mlist.add(name);
		}
		for (String name : FEMALE_NAMES) {
			flist.add(name);
		}
	}

	public String getMaleName() {
		return getName(mlist);
	}

	public String getFemaleName() {
		return getName(flist);
	}

	private String getName(List<String> list) {
		int s = list.size();
		if (s <= 0) {
			return "";
		}
		int i = random.nextInt(s);
		return list.remove(i);
	}
}
