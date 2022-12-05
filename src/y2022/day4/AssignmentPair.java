package y2022.day4;

import com.google.common.collect.Range;

public class AssignmentPair {
    Range<Integer> sections1;
    Range<Integer> sections2;

    public AssignmentPair(Range<Integer> sections1, Range<Integer> sections2) {
        this.sections1 = sections1;
        this.sections2 = sections2;
    }

    public boolean hasCompleteOverlap() {
        if (sections1.encloses(sections2) || sections2.encloses(sections1)) {
            return true;
        }

        return false;
    }

    public boolean hasAnyOverlap() {
        // Range intersection method throws an exception if there's no intersection, which, why do that?  Lame API.
        try {
            sections1.intersection(sections2);
            return true;
        } catch (IllegalArgumentException e) {
            // Just means the ranges didn't intersect/overlap
            // What a crazy library, who throws an exception instead of returning an empty range for the intersection?!
        }

        return false;
    }

    public static AssignmentPair create(String assignmentDefinition) {
        String[] assignments = assignmentDefinition.split(",");

        Range<Integer> r1 = Range.closed(
                Integer.parseInt(assignments[0].split("-")[0]),
                Integer.parseInt(assignments[0].split("-")[1]));

        Range<Integer> r2 = Range.closed(
                Integer.parseInt(assignments[1].split("-")[0]),
                Integer.parseInt(assignments[1].split("-")[1]));

        return new AssignmentPair(r1, r2);
    }
}
