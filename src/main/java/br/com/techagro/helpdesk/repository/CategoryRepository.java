package br.com.techagro.helpdesk.repository;

import br.com.techagro.helpdesk.domain.CategoryTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryTicket, Long> {

    @Query("SELECT c FROM CategoryTicket c WHERE LOWER(c.description) = LOWER(:description)")
    Optional<CategoryTicket> findByDescription(String description);

}
