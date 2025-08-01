package biblioteca_virtual.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DataTotal(
        @JsonAlias("results") List<DataLibro> resultadosLibros
) {
}
