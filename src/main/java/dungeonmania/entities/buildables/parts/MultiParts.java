package dungeonmania.entities.buildables.parts;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.inventory.Inventory;

public class MultiParts implements Parts {
    List<Parts> partsList = new ArrayList<>();

    public MultiParts(List<Parts> partsList) {
        this.partsList = partsList;
    }

    @Override
    public boolean enough(Inventory inventory) {
        return partsList.stream().allMatch(p -> p.enough(inventory));
    }

    @Override
    public void use(Inventory inventory) {
        partsList.stream().forEach(p -> p.use(inventory));
    }
}
