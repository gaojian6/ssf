package com.icourt.common.entity;

import com.icourt.common.dao.Id;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple utility class to simply field access.
 *
 * @author lan
 */
public class BaseEntity implements Cloneable, Serializable {

    /**
     * serial Version UID.
     */
    private static final long serialVersionUID = -1L;

    /**
     * get primary key
     *
     * @param <T> primary key class type
     * @return primary value
     */
    public <T> T getPrimaryKey() {
        final List<Field> FIELDS = new ArrayList<Field>(1);
        ReflectionUtils.doWithFields(getClass(), new FieldCallback() {

            public void doWith(Field field) throws IllegalArgumentException,
                    IllegalAccessException {
                if (field.getAnnotation(Id.class) != null) {
                    field.setAccessible(true);
                    FIELDS.add(field);
                    return;
                }
            }
        });
        if (FIELDS.size() == 1) {
            Field field = FIELDS.get(0);
            return (T) ReflectionUtils.getField(field, this);
        }
        return null;
    }

}
