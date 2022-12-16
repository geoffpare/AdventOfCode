package y2022.day11;

import com.google.common.collect.Lists;

import java.math.BigInteger;
import java.util.List;

/**
 * Class representing a monkey and what it is holding.
 */
public class Monkey {
    private List<BigInteger> items = Lists.newLinkedList();  // Each entry is the worry level of that item
    private String operation;
    private Integer testDivisibleBy;
    private Integer truePassTo;
    private Integer falsePassTo;
    private Integer numInspections = 0;

    public BigInteger takeItemForThrowing() {
        numInspections++;
        return items.remove(0);
    }

    // Getters / Setters below
    public Integer getNumInspections() {
        return numInspections;
    }

    public List<BigInteger> getItems() {
        return items;
    }

    public void setItems(List<BigInteger> items) {
        this.items = items;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Integer getTestDivisibleBy() {
        return testDivisibleBy;
    }

    public void setTestDivisibleBy(Integer testDivisibleBy) {
        this.testDivisibleBy = testDivisibleBy;
    }

    public Integer getTruePassTo() {
        return truePassTo;
    }

    public void setTruePassTo(Integer truePassTo) {
        this.truePassTo = truePassTo;
    }

    public Integer getFalsePassTo() {
        return falsePassTo;
    }

    public void setFalsePassTo(Integer falsePassTo) {
        this.falsePassTo = falsePassTo;
    }

    @Override
    public String toString() {
        return "Monkey{" +
                "items=" + items +
                "\n  operation='" + operation + '\'' +
                "\n  testDivisibleBy=" + testDivisibleBy +
                "\n  truePassTo=" + truePassTo +
                "\n  falsePassTo=" + falsePassTo +
                "\n  numInspections=" + numInspections +
                "}\n\n";
    }
}
