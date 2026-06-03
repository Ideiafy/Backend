package ideiafy.backend.dto;

import java.util.List;

public record PostDto(Integer id,String title, String description, List<String> comment, List<String> images ) {
}
