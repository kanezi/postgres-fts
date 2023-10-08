package hr.kanezi.postgres.fts.movies;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "movies_info_v")
@Data
@NoArgsConstructor
public class MoviesView {

    @Id
    Long id;
    String title;
    String actors;
    String genres;
}
