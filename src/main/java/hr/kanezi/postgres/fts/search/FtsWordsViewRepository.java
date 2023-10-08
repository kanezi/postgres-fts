package hr.kanezi.postgres.fts.search;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FtsWordsViewRepository extends JpaRepository<FtsWordView, String> {

    @Query(value = """
        select   v.word
        ,        similarity(v.word, :q) as similarity
        from     fts_words v
        where    v.word % :q
        order by v.word <-> :q
     """, nativeQuery = true)
    List<FtsWordView> findSimilar(@Param(value = "q") String q);
}
