package ru.otus.pro.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.otus.pro.entities.BulletinBoard;

import java.time.LocalDateTime;

public class BulletinBoardSpecifications {

    public static Specification<BulletinBoard> notEqualId(Long id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get(BulletinBoard.Fields.id), id);
    }

    public static Specification<BulletinBoard> equalName(String value) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(criteriaBuilder.lower(root.get(BulletinBoard.Fields.name)), value.toLowerCase());
    }

    public static Specification<BulletinBoard> hasName(String value) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(BulletinBoard.Fields.name), "%" + value + "%");
    }

    public static Specification<BulletinBoard> hasDescription(String value) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(BulletinBoard.Fields.description), "%" + value + "%");
    }


    public static Specification<BulletinBoard> createDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        var start = startDate.with(LocalDateTime.MIN);
        var end = endDate.with(LocalDateTime.MAX);
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get(BulletinBoard.Fields.createDate), start, end);
    }

    public static Specification<BulletinBoard> createDateLessThanOrEqualTo(LocalDateTime endDate) {
        var end = endDate.with(LocalDateTime.MAX);
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(BulletinBoard.Fields.createDate), end);
    }

    public static Specification<BulletinBoard> createDateGreaterThanOrEqualTo(LocalDateTime startDate) {
        var start = startDate.with(LocalDateTime.MIN);
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(BulletinBoard.Fields.createDate), start);
    }
}
