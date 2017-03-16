package br.ufpr.inf.gres.sentinel.strategy.operation.impl.select.selection;

import br.ufpr.inf.gres.sentinel.strategy.operation.Operation;
import br.ufpr.inf.gres.sentinel.strategy.operation.impl.select.type.SelectionType;
import br.ufpr.inf.gres.sentinel.strategy.operation.impl.sort.AbstractSorterOperation;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.math.DoubleMath;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @param <T>
 *
 * @author Giovani Guizzo
 */
public class SelectionOperation<T> extends Operation<List<T>, List<T>> {

    protected SelectionType selectionType;
    protected Integer quantity = 0;
    protected Double percentage = 0D;

    protected AbstractSorterOperation sorter;

    public SelectionOperation() {
        super("Selection");
    }

    public SelectionOperation(String name) {
        super(name);
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public SelectionType getSelectionType() {
        return selectionType;
    }

    public void setSelectionType(SelectionType selectionType) {
        this.selectionType = selectionType;
    }

    public AbstractSorterOperation getSorter() {
        return sorter;
    }

    public void setSorter(AbstractSorterOperation sorter) {
        this.sorter = sorter;
    }

    @Override
    public List<T> doOperation(List<T> input) {
        if (input.size() > 0) {
            checkNotNull(selectionType, "No selection type defined for selection!");
            checkArgument(percentage != 0D || quantity != 0,
                    "No quantity or percentage defined for selection! "
                    + "Percentage: "
                    + percentage
                    + ". Quantity: "
                    + quantity
                    + ".");

            int numberToSelect;
            if (percentage != 0D) {
                numberToSelect = DoubleMath.roundToInt(input.size() * percentage, RoundingMode.DOWN);
            } else {
                numberToSelect = quantity;
            }

            if (sorter != null) {
                input.sort(sorter);
            }

            return selectionType.selectItems(input, numberToSelect);
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public boolean isSpecific() {
        boolean isSpecific = false;
        if (sorter != null) {
            isSpecific = isSpecific || sorter.isSpecific();
        }
        if (selectionType != null) {
            isSpecific = isSpecific || selectionType.isSpecific();
        }
        return isSpecific;
    }

}
