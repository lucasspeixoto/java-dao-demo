package model.dao.implementations;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SellerDaoJDBC implements SellerDao {

    private final Connection connection;

    public SellerDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    /**
     * @param seller
     */
    @Override
    public void insert(Seller seller) {

    }

    /**
     * @param seller
     */
    @Override
    public void update(Seller seller) {

    }

    /**
     * @param id
     */
    @Override
    public void deleteById(Integer id) {

    }

    /**
     * @param id
     * @return
     */
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
                Department department = new Department();
                department.setId(resultSet.getInt("DepartmentId"));
                department.setName(resultSet.getString("DepName"));

                Seller seller = new Seller();
                seller.setId(resultSet.getInt("Id"));
                seller.setName(resultSet.getString("Name"));
                seller.setEmail(resultSet.getString("Email"));
                seller.setBaseSalary(resultSet.getDouble("BaseSalary"));
                seller.setBirthDate(resultSet.getDate("BirthDate"));
                seller.setDepartment(department);

                return seller;
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

    /**
     * @return
     */
    @Override
    public List<Seller> findAll() {
        return null;
    }
}
