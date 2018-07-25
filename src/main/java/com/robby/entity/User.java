package com.robby.entity;

import org.bson.types.ObjectId;

/**
 *
 * @author Robby
 */
public class User {

    private ObjectId id;
    private String m_id;
    private String name;
    private String role;

    public User() {
    }

    public User(ObjectId id, String m_id, String name, String role) {
        this.id = id;
        this.m_id = m_id;
        this.name = name;
        this.role = role;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getM_id() {
        return m_id;
    }

    public void setM_id(String m_id) {
        this.m_id = m_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public static class UserColumn {

        public static final String COL_ID = "_id";
        public static final String COL_M_ID = "m_id";
        public static final String COL_NAME = "name";
        public static final String COL_ROLE = "role";
    }
}
