package ru.otus.pro.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.otus.pro.entities.BulletinBoard;

public interface BulletinBoardRepository extends JpaRepository<BulletinBoard, Long>, JpaSpecificationExecutor<BulletinBoard> {
}
