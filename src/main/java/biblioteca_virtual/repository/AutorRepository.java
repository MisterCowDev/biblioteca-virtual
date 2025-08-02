package biblioteca_virtual.repository;

import biblioteca_virtual.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNombre(String nombre);

    @Query("SELECT a FROM Autor a WHERE a.fechaFallecimiento > :anio")
    List<Autor> buscarAutorVivo(Integer anio);

}
