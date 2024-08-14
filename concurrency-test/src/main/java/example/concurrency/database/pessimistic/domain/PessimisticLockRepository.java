package example.concurrency.database.pessimistic.domain;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PessimisticLockRepository extends JpaRepository<PessimisticLockStock, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from PessimisticLockStock s where s.id = :id")
    PessimisticLockStock findByIdWithPessimisticLock(@Param("id") Long id);
}
