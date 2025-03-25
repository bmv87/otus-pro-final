package ru.otus.pro.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.otus.pro.entities.Bulletin;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class BulletinSpecifications {

    public static Specification<Bulletin> hasBoardId(long value) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Bulletin.Fields.boardId), value);
    }

    public static Specification<Bulletin> hasTitle(String value) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get(Bulletin.Fields.title)), "%" + value.toLowerCase() + "%");
    }

    public static Specification<Bulletin> hasContent(String value) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get(Bulletin.Fields.content)), "%" + value.toLowerCase() + "%");
    }

    public static Specification<Bulletin> createDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        var start = startDate.toLocalDate().atStartOfDay();
        var end = endDate.toLocalDate().atTime(LocalTime.MAX);
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get(Bulletin.Fields.createDate), start, end);
    }

    public static Specification<Bulletin> createDateLessThanOrEqualTo(LocalDateTime endDate) {
        var end = endDate.toLocalDate().atTime(LocalTime.MAX);
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(Bulletin.Fields.createDate), end);
    }

    public static Specification<Bulletin> createDateGreaterThanOrEqualTo(LocalDateTime startDate) {
        var start = startDate.toLocalDate().atStartOfDay();
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(Bulletin.Fields.createDate), start);
    }

    public static Specification<Bulletin> isActive(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(criteriaBuilder.literal(date), root.get(Bulletin.Fields.startDate), root.get(Bulletin.Fields.endDate));
    }
}
