package api.dieti2024.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CategoriaDTO {

    private String key;

    private String label;

    private List<CategoriaDTO> children = new ArrayList<>();

    public CategoriaDTO(String nome) {

        this.label = nome;

        this.key = this.label;
    }
}
