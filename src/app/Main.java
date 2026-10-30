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



    static void registrarEntrada() {
        int celdaLibre = buscarCeldaDisponible();
        if (celdaLibre == -1) {
            System.out.println("Error: El parqueadero está lleno.");
            return;
        }

        System.out.println("\n--- REGISTRO DE ENTRADA ---");
        String tipo = leerTipoVehiculo();
        LocalDateTime fechaIngreso = leerFechaHora("Ingrese fecha y hora de ingreso (yyyy-MM-dd HH:mm): ");

        tiposVehiculo[celdaLibre] = tipo;
        fechasIngreso[celdaLibre] = fechaIngreso;

        System.out.println("-> Vehículo registrado en la Celda #" + (celdaLibre + 1));
    }

    static String leerTipoVehiculo() {
        while (true) {
            System.out.print("Ingrese tipo de vehículo (Carro / Moto): ");
            String tipo = SCANNER.nextLine().trim();
            if (tipo.equalsIgnoreCase("Carro") || tipo.equalsIgnoreCase("Moto")) {
                return tipo.substring(0, 1).toUpperCase() + tipo.substring(1).toLowerCase();
            }
            System.out.println("Debe ser 'Carro' o 'Moto'.");
        }
    }


    static void registrarSalida() {
        System.out.println("\n--- REGISTRO DE SALIDA ---");
        int numCelda = leerNumeroCelda("Ingrese el número de celda (1 - " + CAPACIDAD_TOTAL + "): ");
        int indice = numCelda - 1;

        if (tiposVehiculo[indice] == null) {
            System.out.println("Error: La celda #" + numCelda + " está vacía.");
            return;
        }

        String tipo = tiposVehiculo[indice];
        LocalDateTime ingreso = fechasIngreso[indice];

        LocalDateTime salida;
        while (true) {
            salida = leerFechaHora("Ingrese fecha y hora de salida (yyyy-MM-dd HH:mm): ");
            if (salida.isBefore(ingreso)) {
                System.out.println("Error: La salida no puede ser anterior a la entrada.");
            } else {
                break;
            }
        }

        double totalPagado = calcularYLiquidar(numCelda, tipo, ingreso, salida);

        if (tipo.equalsIgnoreCase("Carro")) {
            recaudoCarros += totalPagado;
        } else {
            recaudoMotos += totalPagado;
        }

        tiposVehiculo[indice] = null;
        fechasIngreso[indice] = null;

        System.out.println("-> Celda #" + numCelda + " liberada.");
    }

    static int leerNumeroCelda(String mensaje) {
        return leerOpcionMenu(mensaje, 1, CAPACIDAD_TOTAL);
    }

    static int leerOpcionMenu(String mensaje, int min, int max) {
        while (true) {
            System.out.print(mensaje);
            try {
                int valor = Integer.parseInt(SCANNER.nextLine().trim());
                if (valor >= min && valor <= max) return valor;
            } catch (NumberFormatException ignored) {}
            System.out.println("Opción inválida.");
        }
    }



    static double calcularYLiquidar(int numCelda, String tipo, LocalDateTime ingreso, LocalDateTime salida) {
        long diasACobrar = contarDiasCompletos(ingreso, salida);
        LocalDateTime cursor = ingreso.plusDays(diasACobrar);
        long horasACobrar = calcularHorasRestantes(cursor, salida);

        if (horasACobrar >= UMBRAL_HORAS_DIA) {
            diasACobrar++;
            horasACobrar = 0;
        }

        double tarifaDia = tipo.equalsIgnoreCase("Carro") ? TARIFA_DIA_CARRO : TARIFA_DIA_MOTO;
        double tarifaHora = tipo.equalsIgnoreCase("Carro") ? TARIFA_HORA_CARRO : TARIFA_HORA_MOTO;

        double subtotalDias = diasACobrar * tarifaDia;
        double subtotalHoras = horasACobrar * tarifaHora;
        double total = subtotalDias + subtotalHoras;

        System.out.println("\n==========================================");
        System.out.println("            FACTURA DE SALIDA             ");
        System.out.println("==========================================");
        System.out.println("Celda Liberada : " + numCelda);
        System.out.println("Tipo Vehículo  : " + tipo);
        System.out.println("Fecha Ingreso  : " + ingreso.format(FORMATO_FECHA_HORA));
        System.out.println("Fecha Salida   : " + salida.format(FORMATO_FECHA_HORA));
        System.out.println("------------------------------------------");
        if (diasACobrar > 0) System.out.println("- Días (" + diasACobrar + "): $" + (int)subtotalDias);
        if (horasACobrar > 0 || diasACobrar == 0) System.out.println("- Horas (" + horasACobrar + "): $" + (int)subtotalHoras);
        System.out.println("------------------------------------------");
        System.out.println("TOTAL A PAGAR  : $" + (int)total);
        System.out.println("==========================================\n");

        return total;
    }

    static long contarDiasCompletos(LocalDateTime ingreso, LocalDateTime salida) {
        return java.time.temporal.ChronoUnit.DAYS.between(ingreso, salida);
    }

    static long calcularHorasRestantes(LocalDateTime cursor, LocalDateTime salida) {
        return (java.time.Duration.between(cursor, salida).toMinutes() + 59) / 60;



}