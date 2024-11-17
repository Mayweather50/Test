package com.example.demo.specification;

import com.example.demo.dto.task.*;
import com.example.demo.entity.*;
import org.springframework.data.jpa.domain.*;

public class TaskSpecification {

    public static Specification<Task> withFilter(TaskFilterDTO filter) {
        return (root, query, cb) -> {
            var predicate = cb.conjunction();

            if (filter.getStatus() != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("status"), filter.getStatus()));
            }

            if (filter.getPriority() != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("priority"), filter.getPriority()));
            }

            if (filter.getAuthorId() != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("author").get("id"), filter.getAuthorId()));
            }

            if (filter.getAssigneeId() != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("assignee").get("id"), filter.getAssigneeId()));
            }

            return predicate;
        };
    }
}
