package app;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;



public class Parqueadero {

public class Main {

    // constantes, variables y arreglos

 static final int CAPACIDAD_TOTAL = 10;
 static final  double TARIFA_HORA_CARRO = 4.000;
 static final  double TARIFA_DIA_CARRO = 30.000;
 static final  double TARIFA_HORA_MOTO = 2.000;
 static final  double TARIFA_DIA_MOTO = 15.000;
 static final int UMBRAL_HORAS_DIA = 8;
 static final int HORA_APERTURA = 8;
 static final int HORA_CIERRE = 22;

    static final Scanner SCANNER = new Scanner(System.in);
    static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            return LocalDateTime(FORMATO_FECHA_HORA);
        }

 static double recaudoCarros = 0.0;
 static double recaudoMotos = 0.0;




    static final int CAPACIDAD_TOTAL = 10;
    static final double TARIFA_HORA_CARRO = 4.000;
    static final double TARIFA_DIA_CARRO = 30.000;
    static final double TARIFA_HORA_MOTO = 2.000;
    static final double TARIFA_DIA_MOTO = 15.000;
    static final int UMBRAL_HORAS_DIA = 8;
    static final int HORA_APERTURA = 8;
    static final int HORA_CIERRE = 22;

    static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    static final Scanner SCANNER = new Scanner(System.in);

    static String[] tiposVehiculo = new String[CAPACIDAD_TOTAL];
    static LocalDateTime[] fechasIngreso = new LocalDateTime[CAPACIDAD_TOTAL];

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



}
