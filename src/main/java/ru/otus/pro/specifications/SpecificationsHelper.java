package ru.otus.pro.specifications;

import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class SpecificationsHelper {

    public static <T> Specification<T> and(List<Specification<T>> specList) {
        Specification<T> specResult = null;
        for (var spec : specList) {
            specResult = specResult == null
                    ? Specification.where(spec)
                    : specResult.and(spec);
        }

        return specResult != null ? specResult : Specification.allOf();
    }
}
