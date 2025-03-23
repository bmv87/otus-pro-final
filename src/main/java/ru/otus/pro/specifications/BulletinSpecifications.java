package ru.otus.pro.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.otus.pro.entities.Bulletin;

import java.time.LocalDateTime;

public class BulletinSpecifications {

    public static Specification<Bulletin> hasBoardId(long value) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Bulletin.Fields.boardId), value);
    }

    public static Specification<Bulletin> hasTitle(String value) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(Bulletin.Fields.title), "%" + value + "%");
    }

    public static Specification<Bulletin> hasContent(String value) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(Bulletin.Fields.content), "%" + value + "%");
    }

    public static Specification<Bulletin> createDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        var start = startDate.with(LocalDateTime.MIN);
        var end = endDate.with(LocalDateTime.MAX);
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get(Bulletin.Fields.createDate), start, end);
    }

    public static Specification<Bulletin> createDateLessThanOrEqualTo(LocalDateTime endDate) {
        var end = endDate.with(LocalDateTime.MAX);
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(Bulletin.Fields.createDate), end);
    }

    public static Specification<Bulletin> createDateGreaterThanOrEqualTo(LocalDateTime startDate) {
        var start = startDate.with(LocalDateTime.MIN);
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(Bulletin.Fields.createDate), start);
    }

    public static Specification<Bulletin> isActive(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get(Bulletin.Fields.startDate), root.get(Bulletin.Fields.endDate), criteriaBuilder.literal(date));
    }
}
