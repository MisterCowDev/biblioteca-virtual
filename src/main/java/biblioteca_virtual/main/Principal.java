package biblioteca_virtual.main;

import biblioteca_virtual.model.*;
import biblioteca_virtual.repository.AutorRepository;
import biblioteca_virtual.service.DataConverter;
import biblioteca_virtual.service.LibroApiClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {

    LibroApiClient libroApiClient = new LibroApiClient();
    Scanner scanner = new Scanner(System.in);
    DataConverter dataConverter = new DataConverter();
    private final String URL_BASE = "http://gutendex.com/books/?search=";
    private AutorRepository autorRepositorio;

    public Principal(AutorRepository autorRepository) {
        this.autorRepositorio = autorRepository;
    }

    public void mostrarMenu(){
        //var opcion = -1;
        var opcion = 1;
        while (opcion != 0){
            var opcionMenu = """
                    
                    ========================
                    1 - Buscar libro y registrarlo en la base de datos (Romeo y Julieta por ejemplo)
                    2 - Mostrar libros registrados
                    3 - Mostrar autores registrados
                    4 - Mostrar autores vivos según el año indicado
                    5 - Mostrar libros por idioma
                    0 - Salir
                    ========================
                    
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
        String libroIngresado = "Romeo and Juliet";
        var jsonObtenido = libroApiClient.getData(URL_BASE + libroIngresado.toLowerCase().replace(" ", "%20"));
        var dataTotal = dataConverter.getData(jsonObtenido, DataTotal.class);

        Optional<DataLibro> libroEncontrado = dataTotal.resultadosLibros().stream()
                .filter(dataLibro -> dataLibro.titulo().toLowerCase().contains(libroIngresado.toLowerCase())).findFirst();

        if (libroEncontrado.isPresent()){
            DataLibro dataLibro = libroEncontrado.get();

            Autor autor = convertirDataAutor(dataLibro.autor().get(0));
            Autor autorGuardado = autorRepositorio.save(autor);

            Libro libro = convertirDataLibro(dataLibro);

        }
    }

    public void mostrarLibros(){

    }

    public void mostrarAutores(){

    }

    public void mostrarAutoresPorAnio(){

    }

    public void mostrarLibroPorIdioma(){

    }
}
