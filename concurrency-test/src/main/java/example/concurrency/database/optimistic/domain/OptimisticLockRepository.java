package example.concurrency.database.optimistic.domain;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OptimisticLockRepository extends JpaRepository<OptimisticLockStock, Long> {

    @Lock(value = LockModeType.OPTIMISTIC)
    @Query("select s from OptimisticLockStock s where s.id = :id")
    OptimisticLockStock findByIdWithOptimisticLock(@Param("id") Long id);
}
