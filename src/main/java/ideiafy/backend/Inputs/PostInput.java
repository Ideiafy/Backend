package ideiafy.backend.Inputs;

import java.util.List;

public record PostInput(Integer id, String title, String description, List<String> comment, List<String> images ) {
}
