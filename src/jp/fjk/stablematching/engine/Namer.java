package jp.fjk.stablematching.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Namer {
	private final static String[] MALE_NAMES = {
		"Abril"  , "Agosto"  , "Alumno"  , "Amino"   , "Ano"     ,
		"Arbol"  , "Avion"   , "Barco"   , "Bolsillo", "Brazo"   ,
		"Campo"  , "Canamo"  , "Canon"   , "Cantaro" , "Castillo", 
		"Derecho", "Edificio", "Esfuerzo", "Espejo"  , "Fuego"   ,
		"Hielo"  , "Juego"   , "Jueves"  , "Lapiz"   , "Libro"   ,
		"Marzo"  , "Matres"  , "Metal"   , "Mundo"   , "Oro"     ,
		"Pajaro" , "Paseo"   , "Porvenir", "Rayo"    , "Recado"  ,
		"Reposo" , "Sol"     , "Traje"   , "Verano"  , "Viaje"   ,
		"Vidrio" , "Viento"  , "Zapato"  ,		
		};
	private final static String[] FEMALE_NAMES = {
		"Agua"   , "Aguja"  , "Bicicleta", "Boda"     , "Botella", 
		"Camisa" , "Cantata", "Carne"    , "Casa"     , "Cuerda" , 
		"Cultura", "Curva"  , "Esperanza", "Estrella" , "Fuente" , 
		"Fuerza" , "Flor"   , "Fortuna"  , "Foto"     , "Fruta"  , 
		"Luna"   , "Letra"  , "Mano"     , "Mesa"     , "Manana" , 
		"Moda"   , "Gota"   , "Guerra"   , "Historia" , "Joya"   , 
		"Ventana", "Verdad" , "Verdura"  , "Orilla"   , "Partida",
		"Pasion" , "Patria" , "Poesia"   , "Primavera", "Noche"  ,		 
		"Revista", "Ruta"   , "Salida"   , "Semana"   , "Torre"  , 
		};
	private List<String> mlist, flist;

	private Random random;

	public Namer() {
		random = new Random(System.currentTimeMillis());
		mlist = newMaleNameList();
		flist = newFemaleNameList();
	}
	
	private List<String> newMaleNameList() {
		List<String> list = new ArrayList<String>();
		for (String name: MALE_NAMES) {
			list.add(name);
		}
		return list;
	}
	
	private List<String> newFemaleNameList() {
		List<String> list = new ArrayList<String>();
		for (String name: FEMALE_NAMES) {
			list.add(name);
		}
		return list;
	}

	public String getMaleName() {
		int s = mlist.size();
		if (s <= 0) {
			mlist = newMaleNameList();
		}
		int i = random.nextInt(s);
		return mlist.remove(i);
	}

	public String getFemaleName() {
		int s = flist.size();
		if (s <= 0) {
			flist = newMaleNameList();
		}
		int i = random.nextInt(s);
		return flist.remove(i);
	}
}
