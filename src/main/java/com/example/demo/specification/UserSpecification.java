package com.example.demo.specification;

import com.example.demo.dto.user.*;
import com.example.demo.entity.*;
import org.springframework.data.jpa.domain.*;

public class UserSpecification {

    public static Specification<User> withFilter(UserFilterDTO filter) {
        return (root, query, cb) -> {
            var predicate = cb.conjunction();

            if (filter.getRole() != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("role"), filter.getRole()));
            }

            if (filter.getEmail() != null) {
                predicate = cb.and(predicate,
                        cb.like(cb.lower(root.get("email")),
                                "%" + filter.getEmail().toLowerCase() + "%"));
            }

            return predicate;
        };
    }
}
