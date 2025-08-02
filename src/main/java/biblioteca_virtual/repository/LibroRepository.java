package biblioteca_virtual.repository;

import biblioteca_virtual.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    Optional<Libro> findByTitulo(String titulo);

    @Query("SELECT l FROM Libro l JOIN FETCH l.autor")
    List<Libro> findAllConAutor();

    @Query("SELECT l FROM Libro l JOIN FETCH l.autor WHERE l.idioma = :idioma")
    List<Libro> buscarPorIdioma(String idioma);
}
