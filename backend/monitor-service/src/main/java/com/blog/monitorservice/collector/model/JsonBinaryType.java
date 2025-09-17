package com.blog.monitorservice.collector.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;
import org.postgresql.util.PGobject;

import java.io.IOException;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;
import java.util.Objects;

public class JsonBinaryType implements UserType {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final int[] SQL_TYPES = {Types.JAVA_OBJECT};

    @Override
    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    @Override
    public Class<?> returnedClass() {
        return Map.class;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return Objects.equals(x, y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return Objects.hashCode(x);
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
        Object value = rs.getObject(names[0]);
        if (value == null) {
            return null;
        }
        try {
            if (value instanceof PGobject && ((PGobject) value).getType().equals("jsonb")) {
                String json = ((PGobject) value).getValue();
                return OBJECT_MAPPER.readValue(json, Map.class);
            }
            return OBJECT_MAPPER.readValue(value.toString(), Map.class);
        } catch (IOException e) {
            throw new HibernateException("Failed to convert JSON to Map", e);
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, Types.OTHER);
        } else {
            try {
                PGobject pgObject = new PGobject();
                pgObject.setType("jsonb");
                pgObject.setValue(OBJECT_MAPPER.writeValueAsString(value));
                st.setObject(index, pgObject);
            } catch (Exception e) {
                throw new HibernateException("Failed to convert Map to JSON", e);
            }
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        if (value == null) {
            return null;
        }
        try {
            String json = OBJECT_MAPPER.writeValueAsString(value);
            return OBJECT_MAPPER.readValue(json, Map.class);
        } catch (IOException e) {
            throw new HibernateException("Failed to deep copy Map", e);
        }
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) deepCopy(value);
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return deepCopy(cached);
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return deepCopy(original);
    }
}