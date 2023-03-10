package model.dao.implementations;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    private final Connection connection;

    public SellerDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    private static Department instantiateDepartment(ResultSet resultSet) throws SQLException {
        Department department = new Department();
        department.setId(resultSet.getInt("DepartmentId"));
        department.setName(resultSet.getString("DepName"));
        return department;
    }

    private static Seller instantiateSeller(ResultSet resultSet, Department department) throws SQLException {
        Seller seller = new Seller();
        seller.setId(resultSet.getInt("Id"));
        seller.setName(resultSet.getString("Name"));
        seller.setEmail(resultSet.getString("Email"));
        seller.setBaseSalary(resultSet.getDouble("BaseSalary"));
        seller.setBirthDate(resultSet.getDate("BirthDate"));
        seller.setDepartment(department);
        return seller;
    }

    @Override
    public void insert(Seller seller) {

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = this.connection.prepareStatement(
                    "INSERT INTO seller "
                            + "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
                            + "VALUES "
                            + "(?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            preparedStatement.setString(1, seller.getName());
            preparedStatement.setString(2, seller.getEmail());
            preparedStatement.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
            preparedStatement.setDouble(4, seller.getBaseSalary());
            preparedStatement.setInt(5, seller.getDepartment().getId());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();

                if (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    seller.setId(id);
                }
                DB.closeResultSet(resultSet);
            } else {
                throw new DbException("Unexpected error! No rows affected!");
            }

        } catch (SQLException exception) {
            throw new DbException(exception.getMessage());
        } finally {
            DB.closeStatement(preparedStatement);
        }
    }

    @Override
    public void update(Seller seller) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = this.connection.prepareStatement(
                    "UPDATE seller "
                            + "SET Name=?,Email=?,BirthDate=?,BaseSalary=?,DepartmentId=? "
                            + "WHERE Id=?");

            preparedStatement.setString(1, seller.getName());
            preparedStatement.setString(2, seller.getEmail());
            preparedStatement.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
            preparedStatement.setDouble(4, seller.getBaseSalary());
            preparedStatement.setInt(5, seller.getDepartment().getId());
            preparedStatement.setInt(6, seller.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException exception) {
            throw new DbException(exception.getMessage());
        } finally {
            DB.closeStatement(preparedStatement);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = this.connection.prepareStatement(
                    "DELETE FROM seller "
                            + "WHERE Id=?");
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();

        } catch (SQLException exception) {
            throw new DbException(exception.getMessage());
        } finally {
            DB.closeStatement(preparedStatement);
        }
    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = this.connection.prepareStatement(
                    "SELECT seller.*,department.Name as DepName "
                            + "FROM seller INNER JOIN department "
                            + "ON seller.DepartmentId = department.Id "
                            + "WHERE seller.Id = ?");
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Department department = instantiateDepartment(resultSet);

                return instantiateSeller(resultSet, department);
            } else {
                return null;
            }

        } catch (SQLException exception) {
            throw new DbException(exception.getMessage());
        } finally {
            DB.closeStatement(preparedStatement);
            DB.closeResultSet(resultSet);
        }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = this.connection.prepareStatement(
                    "SELECT seller.*,department.Name as DepName "
                            + "FROM seller INNER JOIN department "
                            + "ON seller.DepartmentId = department.Id "
                            + "WHERE DepartmentId = ? "
                            + "ORDER BY Name");

            preparedStatement.setInt(1, department.getId());

            resultSet = preparedStatement.executeQuery();

            List<Seller> sellerItems = new ArrayList<>();

            Map<Integer, Department> departmentMap = new HashMap<>();

            while (resultSet.next()) {
                Integer departmentId = resultSet.getInt("DepartmentId");

                Department _department = departmentMap.get(departmentId);

                if (_department == null) {
                    _department = instantiateDepartment(resultSet);
                    departmentMap.put(departmentId, _department);
                }

                Seller seller = instantiateSeller(resultSet, _department);

                sellerItems.add(seller);

            }

            return sellerItems;

        } catch (SQLException exception) {
            throw new DbException(exception.getMessage());
        } finally {
            DB.closeStatement(preparedStatement);
            DB.closeResultSet(resultSet);
        }
    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = this.connection.prepareStatement(
                    "SELECT seller.*,department.Name as DepName "
                            + "FROM seller INNER JOIN department "
                            + "ON seller.DepartmentId = department.Id "
                            + "ORDER BY Id");

            resultSet = preparedStatement.executeQuery();

            List<Seller> sellerItems = new ArrayList<>();

            Map<Integer, Department> departmentMap = new HashMap<>();

            while (resultSet.next()) {
                Integer departmentId = resultSet.getInt("DepartmentId");

                Department _department = departmentMap.get(departmentId);

                if (_department == null) {
                    _department = instantiateDepartment(resultSet);
                    departmentMap.put(departmentId, _department);
                }

                Seller seller = instantiateSeller(resultSet, _department);

                sellerItems.add(seller);

            }

            return sellerItems;

        } catch (SQLException exception) {
            throw new DbException(exception.getMessage());
        } finally {
            DB.closeStatement(preparedStatement);
            DB.closeResultSet(resultSet);
        }
    }
}
