package ru.otus.pro.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.otus.pro.entities.Bulletin;

public interface BulletinRepository extends JpaRepository<Bulletin, Long>, JpaSpecificationExecutor<Bulletin> {
}

