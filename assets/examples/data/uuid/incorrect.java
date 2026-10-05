import java.util.UUID;

class Example {
    static void example() throws Exception {
        // Violation: 42 / 4096 == 0, so (0 % 16 == 1) is false —
        // the SMT solver picks the `dceSecurityNameRandom` branch,
        // where clockSequence() is forbidden.
        UUID u = new UUID(42L, 42L);
        u.clockSequence();   // INVALID: requires maybeTime(u) or timeBased(u),
                             //          but u is in state dceSecurityNameRandom
    }
}
