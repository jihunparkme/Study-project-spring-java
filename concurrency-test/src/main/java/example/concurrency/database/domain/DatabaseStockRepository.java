package example.concurrency.database.domain;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DatabaseStockRepository extends JpaRepository<DatabaseStock, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from DatabaseStock s where s.id = :id")
    DatabaseStock findByIdWithPessimisticLock(@Param("id") Long id);
}
