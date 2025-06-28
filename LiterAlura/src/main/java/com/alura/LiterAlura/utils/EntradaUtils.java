package com.alura.LiterAlura.utils;

import java.util.Scanner;

/**
 * Clase utilitaria para manejar la entrada por consola.
 */
public class EntradaUtils {
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Lee una línea de texto desde consola.
     *
     * @param mensaje Mensaje a mostrar antes de leer.
     * @return Texto ingresado.
     */
    public static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }

    /**
     * Lee un número entero desde consola, validando que sea correcto.
     *
     * @param mensaje Mensaje a mostrar antes de leer.
     * @return Entero ingresado.
     */
    public static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Entrada inválida. Intenta nuevamente.");
            }
        }
    }
}

