package ideiafy.backend.Inputs;

import java.util.List;
import java.util.UUID;

public record PostInput(UUID id, String title, String description, List<String> comment, List<String> images ) {
}
