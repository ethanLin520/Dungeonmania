package dungeonmania.entities.buildables.parts;

import dungeonmania.entities.inventory.Inventory;

public class AndParts implements Parts {
    Parts part1;
    Parts part2;

    public AndParts(Parts part1, Parts part2) {
        this.part1 = part1;
        this.part2 = part2;
    }

    @Override
    public boolean enough(Inventory inventory) {
        return part1.enough(inventory) && part2.enough(inventory);
    }

    @Override
    public void use(Inventory inventory) {
        part1.use(inventory);
        part2.use(inventory);
    }
}
