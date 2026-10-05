import liquidjava.specification.*;

@StateSet({"emptyEmail", "receiverSet", "senderSet", "bodySet"})
public class Email {

  @StateRefinement(to = "emptyEmail(this)")
  public Email() {}

  @StateRefinement(from = "emptyEmail(this)", to = "senderSet(this)")
  public void from(String s) {}

  @StateRefinement(from = "(senderSet(this)) || (receiverSet(this))",
                   to   = "receiverSet(this)")
  public void to(String s) {}

  @StateRefinement(from = "receiverSet(this)", to = "receiverSet(this)")
  public void subject(String s) {}

  @StateRefinement(from = "receiverSet(this)", to = "bodySet(this)")
  public void body(String s) {}

}
