package hexlet.code.app.component;


import hexlet.code.app.dto.task.TaskParamsDTO;
import hexlet.code.app.model.Label;
import hexlet.code.app.model.Task;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;


@Component
public class TaskSpecification {
    public Specification<Task> build(TaskParamsDTO params) {
        return withTitleCont(params.getTitleCont())
                .and(withAssigneeId(params.getAssigneeId()))
                        .and(withStatus(params.getStatus()))
                                .and(withLabelId(params.getLabelId()));
    }

    private Specification<Task>  withTitleCont(String title) {
        return (root, query, cb) ->
                title == null ? cb.conjunction() : cb.like(cb.lower(root.get("name")), title);
    }

    private Specification<Task> withAssigneeId(Long assigneeId) {
        return (root, query, cb) ->
                assigneeId == null ? cb.conjunction() : cb.equal(root.get("author").get("id"), assigneeId);
    }

    private Specification<Task> withStatus(String slug) {
        return (root, query, cb) ->
                slug == null ? cb.conjunction() : cb.equal(root.get("taskStatus").get("slug"), slug);
    }

    private Specification<Task> withLabelId(Long labelId) {
        return (root, query, cb) -> {
            if (labelId == null) {
                return cb.conjunction();
            }

            Join<Task, Label> labelsJoin = root.join("labels");
            return cb.equal(labelsJoin.get("id"), labelId);
        };
    }
}
