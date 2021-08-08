package co.arisegames.emergencemc.util;

import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

public class Util {
    public static Vector3f v3d2f(Vector3d vector3d) {
        return new Vector3f((float) vector3d.x, (float) vector3d.y, (float) vector3d.z);
    }

    public static Vector3d v3f2d(Vector3f vector3f) {
        return new Vector3d(vector3f.getX(), vector3f.getY(), vector3f.getZ());
    }
}
