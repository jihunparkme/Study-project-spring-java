package vendingMachine;

public class VendingMachine {
    private int changes;

    public VendingMachine() {
        this(0);
    }

    public VendingMachine(final int changes) {
        this.changes = changes;
    }

    public void put(int changes) {
        this.changes += changes;
    }

    public void withdraw(final int changes) {
        final int result = this.changes - changes;
        if (result < 0) {
            throw new IllegalStateException();
        }
        this.changes = result;
    }

    public int getChanges() {
        return changes;
    }
}
