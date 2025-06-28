package com.alura.LiterAlura.principal;

import com.alura.LiterAlura.model.*;
import com.alura.LiterAlura.repository.AutorRepository;
import com.alura.LiterAlura.repository.LibroRepository;
import com.alura.LiterAlura.service.ConsumoAPI;
import com.alura.LiterAlura.service.ConvierteDatos;
import com.alura.LiterAlura.utils.EntradaUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Principal {

    private static final String URL_BASE = "https://gutendex.com/books/";
    private final ConvierteDatos conversor = new ConvierteDatos();
    private final ConsumoAPI consumoApi = new ConsumoAPI();
    private final LibroRepository repository;
    private final AutorRepository autorRepository;
    private String nombreLibro;
    private List<Libro> libros;

    public Principal(LibroRepository repository, AutorRepository autorRepository) {
        this.repository = repository;
        this.autorRepository = autorRepository;
    }

    /**
     * Modo principal que muestra el menú y gestiona las acciones del usuario
     **/
    public void muestraelMenu() {
        int opcion;
        do {
            mostrarMenuPrincipal();
            opcion = EntradaUtils.leerEntero("Selecciona una opción: ");

            switch (opcion) {
                case 1  -> guardarLibrosBuscados();
                case 2  -> mostrarLibrosGuardados();
                case 3  -> mostrarAutoresGuardados();
                case 4  -> mostrarAutoresPorFecha();
                case 5  -> buscarLibrosPorIdioma();
                case 6  -> mostrarStatsDeLibrosEnBD();
                case 7  -> top10LibrosMasDescargados();
                case 8  -> buscarPorAutor();
                case 9  -> buscarLibrosPorDerechosDeAutor();
                case 10 -> mostrarAutoresEntreFechas();
                case 11 ->  mostrarAutoresVivos();
                case 0  -> System.out.println("¡Hasta la próxima!");
                default -> System.out.println("Opción no válida.");
            }

        } while (opcion != 0);
    }

    /**
     * Muestra el menú principal en consola.
     */
    private void mostrarMenuPrincipal() {
        System.out.println("""
                \n|||||****[[[[ Bienvenidos al buscador de libros ]]]]****|||||
                Menú:
                1  - Buscar y guardar libro.
                2  - Mostrar libros registrados.
                3  - Mostrar autores registrados.
                4  - Mostrar autores nacidos después de un año.
                5  - Buscar libros por idioma.
                6  - Ver estadísticas de descargas.
                7  - Top 10 libros más descargados.
                8  - Buscar autores por nombre.
                9  - Filtrar libros por derechos de autor.
                10 - Mostrar autores vivos durante un periodo.
                11 - Mostrar autores vivos actualmente.
                0 - Salir.
                """);
    }

    /**
     * Obtiene resultados de búsqueda desde la API por nombre de libro.
     */
    private DatosResultadoLibros getDatosBusqueda() {
        nombreLibro = EntradaUtils.leerTexto("Ingresa el libro que quieras buscar: ");
        String url = URL_BASE + "?search=" + URLEncoder.encode(nombreLibro, StandardCharsets.UTF_8);
        String json = consumoApi.obtenerDatosAPI(url);
        return conversor.convertirDatos(json, DatosResultadoLibros.class);
    }

    /**
     * Busca un libro, procesa los datos y guarda autor si corresponde.
     */
    private Libro guardarLibrosBuscados() {
        DatosResultadoLibros resultado = getDatosBusqueda();

        return resultado.datosTodosLosLibros().stream()
                .filter(l -> l.titulo().toLowerCase().contains(nombreLibro.toLowerCase()))
                .findFirst()
                .map(this::procesarLibro)
                .orElseGet(() -> {
                    System.out.println("Libro no encontrado.");
                    return null;
                });
    }

    /**
     * Procesa y guarda un libro junto con su autor si no existe.
     */
    private Libro procesarLibro(DatosLibro datos) {
        Libro libro = new Libro(datos);

        if(datos.autores() != null && !datos.autores().isEmpty()){
            DatosAutor datosAutor = datos.autores().get(0);

            System.out.println("Autor encontrado: " + datosAutor); // NUEVA LÍNEA

            Optional<Autor> autorBuscado = autorRepository
                    .findByNombreIgnoreCaseAndFechaNac(datosAutor.nombre(), datosAutor.fechaNac());

            Autor autor = autorBuscado.orElse(
                    new Autor(datosAutor.nombre(), datosAutor.fechaNac(), datosAutor.fechaDeceso())
            );

            autor.setLibro(libro);
            autorRepository.save(autor);
        }

        return libro;
    }

    /**
     * Muestra los libros almacenados en la base de datos.
     */
    private void mostrarLibrosGuardados() {
        libros = repository.findAll();
        if (libros.isEmpty()) {
            System.out.println("❌ No hay libros registrados.");
        } else {
            libros.forEach(System.out::println);
            System.out.println("✅ Total de libros: " + libros.size());
        }
    }

    /**
     * Muestra todos los autores registrados, ordenados por nombre.
     */
    private void mostrarAutoresGuardados() {
        List<Autor> autores = autorRepository.findAll();
        autores.stream().sorted(Comparator.comparing(Autor::getNombre))
                .forEach(System.out::println);
        System.out.println("✅ Total de autores: " + autores.size());
    }

    /**
     * Muestra autores nacidos después de un año específico.
     */
    private void mostrarAutoresPorFecha() {
        int anio = EntradaUtils.leerEntero("Ingresa el año de nacimiento: ");
        List<Autor> autores = autorRepository.findByFechaNacGreaterThanEqual(anio);
        if (autores.isEmpty()) {
            System.out.println("❌ No se encontraron autores nacidos desde " + anio);
        } else {
            autores.forEach(System.out::println);
            System.out.println("✅ Total: " + autores.size());
        }
    }

    /**
     * Solicita idioma y muestra libros correspondientes.
     */
    private void buscarLibrosPorIdioma() {
        String idioma = EntradaUtils.leerTexto("""
                ----- Ingresa el idioma (código) -----
                es -> Español
                en -> Inglés
                fr -> Francés
                pt -> Portugués
                -> """);
        try {
            CategoriaIdioma categoria = CategoriaIdioma.fromEspanol(idioma);
            List<Libro> librosPorIdioma = repository.findByIdioma(categoria);
            librosPorIdioma.forEach(System.out::println);
            System.out.printf("✅ Se encontraron %d libro(s) en %s\n", librosPorIdioma.size(), idioma);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Idioma no válido. Intenta con es, en, fr o pt.");
        }
    }

    /**
     * Muestra estadísticas sobre las descargas de libros registrados.
     */
    private void mostrarStatsDeLibrosEnBD() {
        libros = repository.findAll();
        var stats = libros.stream()
                .filter(l -> l.getDescargas() > 0)
                .collect(Collectors.summarizingDouble(Libro::getDescargas));

        Optional<Libro> max = libros.stream().max(Comparator.comparing(Libro::getDescargas));
        Optional<Libro> min = libros.stream().min(Comparator.comparing(Libro::getDescargas));

        System.out.printf("\nPromedio descargas: %.2f | Evaluados: %d\n", stats.getAverage(), stats.getCount());
        max.ifPresent(l -> System.out.printf("Más descargado: %s (%.0f descargas)\n", l.getTitulo(), l.getDescargas()));
        min.ifPresent(l -> System.out.printf("Menos descargado: %s (%.0f descargas)\n", l.getTitulo(), l.getDescargas()));
    }

    /**
     * Muestra los 10 libros con más descargas.
     */
    private void top10LibrosMasDescargados() {
        libros = repository.findAll();
        System.out.println("Top 10 libros más descargados:");
        AtomicInteger i = new AtomicInteger(1);
        libros.stream()
                .sorted(Comparator.comparing(Libro::getDescargas).reversed())
                .limit(10)
                .forEach(l -> System.out.printf("%d.- %s (%.0f descargas)\n",
                        i.getAndIncrement(), l.getTitulo(), l.getDescargas()));
    }

    /**
     * Permite buscar autores registrados por nombre.
     */
    private void buscarPorAutor() {
        String nombre = EntradaUtils.leerTexto("Ingresa el nombre del autor: ");
        List<Autor> autores = autorRepository.findByNombreContainsIgnoreCase(nombre);
        autores.forEach(a -> {
            System.out.println("Autor: " + a.getNombre());
            a.getLibros().forEach(l -> System.out.println(" - " + l.getTitulo()));
            System.out.println("-------------------------------------------");
        });
    }

    /**
     * Filtra libros según si tienen o no derechos de autor.
     */
    private void buscarLibrosPorDerechosDeAutor() {
        int opcion = EntradaUtils.leerEntero("""
                ¿Qué libros deseas ver?
                1 - Con derechos de autor
                2 - Sin derechos de autor
                -> """);

        List<Libro> librosFiltrados = switch (opcion) {
            case 1 -> repository.findByDerechosAutor(true);
            case 2 -> repository.findByDerechosAutor(false);
            default -> {
                System.out.println("Opción no válida.");
                yield Collections.emptyList();
            }
        };

        librosFiltrados.stream()
                .map(Libro::getTitulo)
                .forEach(titulo -> System.out.println("- " + titulo));
        System.out.println("Total: " + librosFiltrados.size() + " libro(s).\n");
    }

    /**
     * Muestra autores vivos durante un periodo definido.
     */
    private void mostrarAutoresEntreFechas() {
        int inicio = EntradaUtils.leerEntero("Año de inicio: ");
        int fin = EntradaUtils.leerEntero("Año final: ");

        List<Autor> autores = autorRepository
                .findByFechaNacLessThanEqualAndFechaDecesoGreaterThanEqual(fin, inicio);

        autores.forEach(a -> {
            System.out.println("Autor: " + a.getNombre());
            System.out.println("Nacimiento: " + a.getFechaNac() + " - Fallecimiento: " + a.getFechaDeceso());
        });

        System.out.printf("Total autores vivos entre %d y %d: %d\n", inicio, fin, autores.size());
    }

    private void mostrarAutoresVivos() {
        List<Autor> autoresVivos = autorRepository.findAll().stream()
                .filter(a -> a.getFechaDeceso() == null)
                .collect(Collectors.toList());

        if (autoresVivos.isEmpty()) {
            System.out.println("❌ No se encontraron autores vivos.");
        } else {
            System.out.println("\nAutores vivos registrados:");
            autoresVivos.forEach(a -> System.out.println("- " + a.getNombre()));
            System.out.println("Total: " + autoresVivos.size() + " autor(es) vivos.");

        }
    }

}
