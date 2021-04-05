package communication;

/**
 * PacketType would've been an enum, but
 * values can't be derived in switch-cases of enums
 * with associated values since they can change during runtime
 */
public class PacketType {

  public static final char READY = 'R';
  public static final char CONNECT = 'C';
  public static final char LOBBY = 'L';
  public static final char UPDATE = 'U';
  public static final char ACTION = 'A';

}
