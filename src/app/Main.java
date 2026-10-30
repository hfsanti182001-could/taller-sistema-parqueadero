package app;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

class Parqueadero {


    static final int CAPACIDAD_TOTAL = 10;
    static final double TARIFA_HORA_CARRO = 4000.0; // Nota: en Java los decimales usan punto (.)
    static final double TARIFA_DIA_CARRO = 30000.0;
    static final double TARIFA_HORA_MOTO = 2000.0;
    static final double TARIFA_DIA_MOTO = 15000.0;
    static final int UMBRAL_HORAS_DIA = 8;
    static final int HORA_APERTURA = 8;
    static final int HORA_CIERRE = 22;


    static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    static final Scanner SCANNER = new Scanner(System.in);


    static String[] tiposVehiculo = new String[CAPACIDAD_TOTAL];
    static LocalDateTime[] fechasIngreso = new LocalDateTime[CAPACIDAD_TOTAL];

    // Variables de acumulados (recaudos)
    static double recaudoCarros = 0.0;
    static double recaudoMotos = 0.0;

    public static void main(String[] args) {
        inicializarArreglos();



        SCANNER.close();
    }

    static void inicializarArreglos() {
        for (int i = 0; i < CAPACIDAD_TOTAL; i++) {
            tiposVehiculo[i] = null;
            fechasIngreso[i] = null;
        }
    }

    static int buscarCeldaDisponible() {
        for (int i = 0; i < CAPACIDAD_TOTAL; i++) {
            if (tiposVehiculo[i] == null) {
                return i;
            }
        }
        return -1;
    }



}