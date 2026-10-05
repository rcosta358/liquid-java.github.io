
class Example {
    static void example() throws Exception {
        // Violation: to() called before from() — sender was never set.
        Email e = new Email();
        e.to("Bob");          // INVALID: requires senderSet or receiverSet,
                              //          but e is still in emptyEmail
        e.from("Alice");
        e.body("Hello!");
    }
}
