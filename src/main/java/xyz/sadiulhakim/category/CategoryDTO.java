package xyz.sadiulhakim.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryDTO(long id,long userId, @NotBlank @Size(max = 55) String name, @Size(max = 200) String description) {

}
