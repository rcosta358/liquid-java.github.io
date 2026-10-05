import java.util.UUID;

class Example {
    static void example() throws Exception {
        // Path: timeBased -> timeBased -> timeBased
        // 4096 / 4096 == 1, and 1 % 16 == 1, so the predicate holds:
        // the SMT solver picks the `timeBased` branch of the ternary.
        UUID u = new UUID(4096L, 42L);
        u.clockSequence();
        u.clockSequence();
    }
}
