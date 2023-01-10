package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.DepartamentDao;
import model.entities.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartamentDao {

    private final Connection conn;

    public DepartmentDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Department department) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("INSERT INTO department "
                    + "(NAME) "
                    + "VALUES (?) ",
                    Statement.RETURN_GENERATED_KEYS
            );

            st.setString(1, department.getName());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    department.setId(id);
                }
                DB.closeResultSet(rs);
            } else {
                throw new DbException("Unexpected error! No rows affected");
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Department department) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("UPDATE department "
                    + "SET name = ? WHERE Id = ?"
            );

            st.setString(1, department.getName());
            st.setInt(2, department.getId());

            st.executeUpdate();

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("DELETE FROM department where id = ?");
            st.setInt(1, id);
            st.executeUpdate();

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }

    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("SELECT * from department d where id = ?");

            st.setInt(1, id);

            rs = st.executeQuery();

            if (rs.next()) {
                return instanciateDepartment(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public List<Department> findAll() {

        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM department d order by d.id");

            rs = st.executeQuery();

            List<Department> departmentList = new ArrayList<>();

            while (rs.next()) {
                Department dep = instanciateDepartment(rs);
                departmentList.add(dep);
            }

            return departmentList;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }

    }

    private Department instanciateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt("id"));
        dep.setName(rs.getString("name"));
        return dep;
    }
}
