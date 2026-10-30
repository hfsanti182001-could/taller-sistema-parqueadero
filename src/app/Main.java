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

    static boolean formatoFechaHoraValido(String texto) {
        if (texto.length() != 16 || texto.charAt(4) != '-' || texto.charAt(7) != '-' || texto.charAt(10) != ' ' || texto.charAt(13) != ':') {
            return false;
        }
        int anio = leerDigitos(texto, 0, 4);
        int mes = leerDigitos(texto, 5, 2);
        int dia = leerDigitos(texto, 8, 2);
        int hora = leerDigitos(texto, 11, 2);
        int minuto = leerDigitos(texto, 14, 2);

        return anio >= 1 && mes >= 1 && mes <= 12 && dia >= 1 && dia <= 31
                && hora >= HORA_APERTURA && hora <= HORA_CIERRE
                && (hora != HORA_CIERRE || minuto <= 0)
                && minuto >= 0 && minuto <= 59;
    }

    static int leerDigitos(String texto, int inicio, int cantidad) {
        int valor = 0;
        for (int i = inicio; i < inicio + cantidad; i++) {
            char c = texto.charAt(i);
            if (c < '0' || c > '9') return -1;
            valor = valor * 10 + c - '0';
        }
        return valor;
    }

    static LocalDateTime leerFechaHora(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = SCANNER.nextLine().trim();
            if (!formatoFechaHoraValido(texto)) {
                System.out.println("Fecha u hora inválidas. Use yyyy-MM-dd HH:mm (08:00 a 22:00).");
                continue;
            }
            try {
                LocalDateTime ldt = LocalDateTime.parse(texto, FORMATO_FECHA_HORA);
                if (!ldt.format(FORMATO_FECHA_HORA).equals(texto)) continue;
                return ldt;
            } catch (Exception ignored) {}
        }
    }



}