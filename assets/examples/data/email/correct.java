
class Example {
    static void example() throws Exception {
        // Path: emptyEmail -> senderSet -> receiverSet -> receiverSet -> bodySet
        Email e = new Email();
        e.from("Alice");
        e.to("Bob");
        e.to("Carol");
        e.body("Hello!");
    }
}
