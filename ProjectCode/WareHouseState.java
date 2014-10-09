public abstract class WareHouseState {
  protected static WareHouseContext context;
  protected WareHouseState() {
    //context = LibContext.instance();
  }
  public abstract void run();
}
