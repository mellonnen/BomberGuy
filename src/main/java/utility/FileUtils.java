package utility;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class FileUtils {

  public static BufferedImage readImageFromResource(String path) throws IOException {
    return ImageIO.read(Objects.requireNonNull(FileUtils.class.getClassLoader().getResourceAsStream(path)));
  }

}
