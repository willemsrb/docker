package nl.futureedge.maven.docker.mojo;

import java.lang.reflect.Field;

public final class ReflectionTestUtils {

    private ReflectionTestUtils() {
    }

    public static void setField(final Object object, final String fieldName, final Object value) {
        try {
            final Field field = findField(object.getClass(), fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (final ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static Field findField(final Class<?> clazz, final String fieldName) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (final NoSuchFieldException e) {
            final Class<?> parent = clazz.getSuperclass();
            if (parent == null) {
                throw new NoSuchFieldException("Field '" + fieldName + "' not found");
            } else {
                return findField(parent, fieldName);
            }
        }
    }
}
