package biblioteca_virtual.main;

import biblioteca_virtual.model.*;
import biblioteca_virtual.repository.AutorRepository;
import biblioteca_virtual.repository.LibroRepository;
import biblioteca_virtual.service.DataConverter;
import biblioteca_virtual.service.LibroApiClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    LibroApiClient libroApiClient = new LibroApiClient();
    Scanner scanner = new Scanner(System.in);
    DataConverter dataConverter = new DataConverter();
    private final String URL_BASE = "http://gutendex.com/books/?search=";
    private AutorRepository autorRepositorio;
    private LibroRepository libroRepositorio;
    private List<Libro> libros;
    private List<Autor> autores;

    public Principal(AutorRepository autorRepository, LibroRepository libroRepository) {
        this.autorRepositorio = autorRepository;
        this.libroRepositorio = libroRepository;
    }

    public void mostrarMenu(){
        //var opcion = -1;
        var opcion = 1;
        while (opcion != 0){
            var opcionMenu = """
                    
                    =================================================
                    1 - Buscar libro y registrarlo en la base de datos
                    2 - Mostrar libros registrados
                    3 - Mostrar autores registrados
                    4 - Mostrar autores vivos según el año indicado
                    5 - Mostrar libros por idioma
                    0 - Salir
                    =================================================
                    
                    """;
            System.out.print(opcionMenu + "Escoge una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion){
                case 0:
                    System.out.println("Has salido de la aplicación");
                    break;
                case 1:
                    registrarLibro();
                    break;
                case 2:
                    mostrarLibros();
                    break;
                case 3:
                    mostrarAutores();
                    break;
                case 4:
                    mostrarAutoresPorAnio();
                    break;
                case 5:
                    mostrarLibroPorIdioma();
                    break;
                default:
                    System.out.println("Opción ingresada no es valida");
            }
        }
    }

    public Autor convertirDataAutor(DataAutor dataAutor){
        Autor autor = new Autor();
        autor.setNombre(dataAutor.nombre());
        autor.setFechaNacimiento(dataAutor.fechaNacimiento());
        autor.setFechaFallecimiento(dataAutor.fechaFallecimiento());
        return autor;
    }

    public Libro convertirDataLibro(DataLibro dataLibro){
        Libro libro = new Libro();
        libro.setTitulo(dataLibro.titulo());
        if (dataLibro.idioma() != null && !dataLibro.idioma().isEmpty()){
            libro.setIdioma(dataLibro.idioma().get(0));
        } else {
            libro.setIdioma("Desconocido");
        }
        libro.setDescargas(dataLibro.totalDescargas());
        if (dataLibro.autor() != null && !dataLibro.autor().isEmpty()){
            Autor autor = convertirDataAutor(dataLibro.autor().get(0));
            libro.setAutor(autor);
        }
        return libro;
    }

    public void registrarLibro(){
        System.out.print("Ingresa el nombre del libro para registrar: ");
        String libroIngresado = scanner.nextLine();
        var jsonObtenido = libroApiClient.getData(URL_BASE + libroIngresado.toLowerCase().replace(" ", "%20"));
        var dataTotal = dataConverter.getData(jsonObtenido, DataTotal.class);

        Optional<DataLibro> libroEncontrado = dataTotal.resultadosLibros().stream()
                .filter(dataLibro -> dataLibro.titulo().toLowerCase().contains(libroIngresado.toLowerCase())).findFirst();

        if (libroEncontrado.isPresent()){
            DataLibro dataLibro = libroEncontrado.get();

            DataAutor dataAutor = dataLibro.autor().get(0);
            String nombreAutor = dataAutor.nombre();

            Optional<Autor> autorExiste = autorRepositorio.findByNombre(nombreAutor);

            // Buscar al autor antes de registrarlo, para evitar duplicados
            Autor autor;
            if (autorExiste.isPresent()){
                System.out.println("El autor no se ha registrado porque ya existe en la base de datos");
                autor = autorExiste.get();
            } else {
                autor = convertirDataAutor(dataAutor);
                autor = autorRepositorio.save(autor);
            }

            Optional<Libro> libroExistente = libroRepositorio.findByTitulo(dataLibro.titulo());
            if (libroExistente.isPresent()){
                System.out.println("Libro ya existe en la base de datos");
                return;
            }

            Libro libro = convertirDataLibro(dataLibro);
            libro.setAutor(autor);
            libroRepositorio.save(libro);
            System.out.println("Libro registrado con exito");

        } else {
            System.out.println("No se ha encontrado el libro");
        }
    }

    public void mostrarLibros(){
        libros = libroRepositorio.findAllConAutor();
        libros.forEach(libro -> System.out.println(
                "\n=========================" +
                "\nTítulo: " + libro.getTitulo() +
                        "\nAutor: " + libro.getAutor().getNombre() +
                        "\nIdioma: " + libro.getIdioma() +
                        "\nDescargas: " + libro.getDescargas() +
                        "\n========================="
        ));
    }

    public void mostrarAutores(){
        autores = autorRepositorio.findAll();
        autores.forEach(autor -> System.out.println(
                "\n=========================" +
                "\nNombre: " + autor.getNombre() +
                "\nFecha de nacimiento: " + autor.getFechaNacimiento() +
                "\nFecha de fallecimiento: " + autor.getFechaFallecimiento() +
                        "\n========================="));
    }

    public void mostrarAutoresPorAnio(){
        System.out.print("Ingresa el año desde el cual deseas buscar autores que sigan vivos: ");
        Integer year = scanner.nextInt();
        autores = autorRepositorio.buscarAutorVivo(year);
        autores.forEach(autor -> System.out.println(autor.getNombre()));
        if (autores.size() == 0){
            System.out.println("No autores vivos desde esa fecha");
        }
    }

    public void mostrarLibroPorIdioma(){
        System.out.println("""
                Seleciona el idioma que buscas
                1 - Español
                2 - Ingles
                3 - Frances:
                """);
        System.out.print("Opción: ");
        Integer number = scanner.nextInt();
        switch (number){
            case 1:
                libros = libroRepositorio.buscarPorIdioma("es");
                break;
            case 2:
                libros = libroRepositorio.buscarPorIdioma("en");
                break;
            case 3:
                libros = libroRepositorio.buscarPorIdioma("fr");
                break;
            default:
                System.out.println("Opción no valida");
                break;
        }
        if (!libros.isEmpty()){
            libros.forEach(libro -> System.out.println(libro.toString()));
        } else {
            System.out.println("No hay libros con ese idioma");
        }
    }
}
