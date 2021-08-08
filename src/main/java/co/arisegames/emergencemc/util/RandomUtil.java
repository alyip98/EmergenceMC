package co.arisegames.emergencemc.util;

import net.minecraft.util.math.vector.Vector3d;

import java.util.Random;

public class RandomUtil {
    private static final Random rand = new Random();
    public static Vector3d RandomPointInUnitCube() {
        double x = rand.nextDouble() - 0.5;
        double y = rand.nextDouble() - 0.5;
        double z = rand.nextDouble() - 0.5;
        return new Vector3d(x, y, z);
    }
}
