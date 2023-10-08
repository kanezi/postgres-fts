package hr.kanezi.postgres.fts.quotes;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "v_quotes")
@Data
@NoArgsConstructor
public class QuotesView {

    @Id
    Long id;

    String quote;
    String author;
    String tags;

}
