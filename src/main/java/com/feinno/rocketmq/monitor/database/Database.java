/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-1-24
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.rocketmq.monitor.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.rowset.CachedRowSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.rowset.CachedRowSetImpl;


/**
 * <b>描述: </b>一个数据库接口封装, 保证每个操作都是连接池安全的<br>
 * <p>
 * <b>功能：</b>执行访问数据库的实现类<br>
 * <p>
 * <b>用法: </b>构造一个Database对象，就可以访问某个数据库的存储过程、执行Sql语句。<br>
 * 例如：<br>
 * 
 * <pre>
 * <code>
 * Properties p = ...;  
 * 参考：{@link  DatabaseManager} {@link DBConnectionPoolType} <br/>
 * Database db = DatabaseManager.getDatabase("updb.1",p,DBConnectionPoolType.TomcatPool);<br>
 * String[] params = {"UserId","Name"};<br>
 * db.spExecuteNonQuery("test",params,30008909,"abc");<br>
 * </code>
 * </pre>
 * <p>
 * C3p0的Properties及TomcatPool的Properties请参考相关文档。<br>
 * 
 * @author honghao@feinno.com
 */
public class Database implements Operation {
    private static final Logger LOGGER = LoggerFactory.getLogger(Database.class);

    String dbName = "";
    ConnectionPoolAdapter poolAdapter;


    Database() {

    }


    /**
     * 
     * @param dbName
     * @param configs
     * @param type
     * @throws Exception
     */
    Database(String dbName, Properties configs, DBConnectionPoolType type) throws Exception {
        this.dbName = dbName;
        poolAdapter = ConnectionPoolAdapterFactory.getInstance().getConnectionPoolAdapter(configs, type);
    }


    /**
     * 默认C3p0连接池
     * 
     * @param dbName
     * @param configs
     * @throws Exception
     */
    Database(String dbName, Properties configs) throws Exception {
        this(dbName, configs, DBConnectionPoolType.C3p0);
    }


    /*
     * @see com.feinno.database.Operation#spExecuteNonQuery(java.lang.String,
     * java.lang.String[], java.lang.Object[])
     */
    @Override
    public int spExecuteNonQuery(String spName, String[] params, Object... values) throws SQLException {
        params = params == null ? new String[] {} : params;
        values = values == null ? new String[] {} : values;

        Connection conn = null;
        CallableStatement stmt = null;
        String sql = "";
        String[] paramsTemp = new String[params.length]; // 临时存放参数名称，防止先mysql后sqlserver访问时修改了参数名称出错
        String obKey = "";
        try {
            obKey = dbName + ":" + spName;

            if (params != null)
                if (params.length != values.length) {
                    throw new IllegalArgumentException("params.length != values.length");
                }
            conn = getConnection();

            String dbType = conn.getMetaData().getDatabaseProductName();
            if (dbType.equalsIgnoreCase("MySQL")) {
                convertAtToT(paramsTemp, params);
            }
            // TODO 生成的Sql可以缓冲, 但是性能影响很小
            sql = DatabaseHelper.getCallSql(spName, values == null ? 0 : values.length);
            stmt = conn.prepareCall(sql);
            if (dbType.equalsIgnoreCase("MySQL")) {
                DatabaseHelper.fillStatememt(stmt, paramsTemp, values);
            }
            else {
                DatabaseHelper.fillStatememt(stmt, params, values);
            }

            return stmt.executeUpdate();
        }
        catch (SQLException e) {
            LOGGER.error(obKey + " error: {}", e);
            throw e;
        }
        finally {
            DatabaseHelper.attemptClose(stmt);
            closeConnection(conn);
        }
    }


    /*
     * @see com.feinno.database.Operation#spExecuteTable(java.lang.String,
     * java.lang.String[], java.lang.Object[])
     */
    @Override
    public DataTable spExecuteTable(String spName, String[] params, Object... values) throws SQLException {
        params = params == null ? new String[] {} : params;
        values = values == null ? new String[] {} : values;

        Connection conn = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        String[] paramsTemp = new String[params.length];// 临时存放参数名称，防止先mysql后sqlserver访问时修改了参数名称出错
        String obKey = "";
        try {
            obKey = dbName + ":" + spName;
            if (params != null) {
                if (params.length != values.length) {
                    throw new IllegalArgumentException("params.length != values.length");
                }
            }
            conn = getConnection();
            String dbType = conn.getMetaData().getDatabaseProductName();
            if (dbType.equalsIgnoreCase("MySQL")) {
                convertAtToT(paramsTemp, params);
            }
            sql = DatabaseHelper.getCallSql(spName, values == null ? 0 : values.length);
            stmt = conn.prepareCall(sql);
            if (dbType.equalsIgnoreCase("MySQL")) {
                DatabaseHelper.fillStatememt(stmt, paramsTemp, values);
            }
            else {
                DatabaseHelper.fillStatememt(stmt, params, values);
            }
            rs = stmt.executeQuery();
            return new DataTable(rs);
        }
        catch (SQLException e) {
            LOGGER.error(obKey + " error: {}", e);
            throw e;
        }
        finally {
            DatabaseHelper.attemptClose(rs);
            DatabaseHelper.attemptClose(stmt);
            closeConnection(conn);
        }
    }


    /*
     * @see com.feinno.database.Operation#executeNonQuery(java.lang.String,
     * java.lang.Object[])
     */
    @Override
    public int executeNonQuery(String sql, Object... values) throws SQLException {
        values = values == null ? new String[] {} : values;

        Connection conn = null;
        PreparedStatement stmt = null;
        String obKey = "";
        try {
            String formatSql = sql;
            // 非参数化的sql
            if (values == null || values.length == 0)
                formatSql = SqlNormalizer.format(sql);

            obKey = dbName + ":" + formatSql;

            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            if (values != null)
                for (int i = 0; i < values.length; i++) {
                    stmt.setObject(i + 1, values[i]);
                }

            return stmt.executeUpdate();
        }
        catch (SQLException e) {
            LOGGER.error(obKey + " error: {}", e);
            throw e;
        }
        finally {
            DatabaseHelper.attemptClose(stmt);
            closeConnection(conn);
        }
    }


    /*
     * @see com.feinno.database.Operation#executeTable(java.lang.String,
     * java.lang.Object[])
     */
    @Override
    public DataTable executeTable(String sql, Object... values) throws SQLException {
        values = values == null ? new String[] {} : values;

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String obKey = "";
        try {
            String formatSql = sql;
            // 非参数化的sql
            if (values == null || values.length == 0)
                formatSql = SqlNormalizer.format(sql);

            obKey = dbName + ":" + formatSql;

            conn = getConnection();
            stmt = conn.prepareStatement(sql);

            if (values != null)
                for (int i = 0; i < values.length; i++) {
                    stmt.setObject(i + 1, values[i]);
                }

            rs = stmt.executeQuery();

            return new DataTable(rs);
        }
        catch (SQLException e) {
            LOGGER.error(obKey + " error: {}", e);
            throw e;
        }
        finally {
            DatabaseHelper.attemptClose(rs);
            DatabaseHelper.attemptClose(stmt);
            closeConnection(conn);
        }
    }


    /**
     * 如果数据库类型是MySQL，且参数中有@符号，自动转化@为T_。以前C-sharp中做了自动处理，此处为了兼容
     * 
     * @param params
     */
    private void convertAtToT(String[] paramsTemp, String[] params) {
        paramsTemp = paramsTemp == null ? new String[] {} : paramsTemp;
        params = params == null ? new String[] {} : params;

        for (int i = 0; i < params.length; i++) {
            paramsTemp[i] = params[i];
            if (paramsTemp[i].contains("@"))
                paramsTemp[i] = paramsTemp[i].replaceFirst("@", "v_");
        }
    }


    /*
     * (non-Javadoc)
     * 
     * @see
     * com.feinno.database.Operation#executeAutoIncrementInsert(java.lang.String
     * , java.lang.Object[])
     */
    @Override
    public long executeInsertWithAutoColumn(String insertSql, Object... values) throws SQLException {
        values = values == null ? new String[] {} : values;

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String obKey = "";
        try {
            String formatSql = insertSql;
            // 非参数化的sql
            if (values == null || values.length == 0)
                formatSql = SqlNormalizer.format(insertSql);

            obKey = dbName + ":" + formatSql;

            conn = getConnection();
            stmt = conn.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS);

            if (values != null)
                for (int i = 0; i < values.length; i++) {
                    stmt.setObject(i + 1, values[i]);
                }

            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                return rs.getLong(1);
            }
            else {
                return -1;
            }

        }
        catch (SQLException e) {
            LOGGER.error(obKey + " error: {}", e);
            throw e;
        }
        finally {
            DatabaseHelper.attemptClose(rs);
            DatabaseHelper.attemptClose(stmt);
            closeConnection(conn);
        }
    }


    /**
     * 
     * @param sql
     *            完整的sql
     * @return
     * @throws SQLException
     */
    public CachedRowSet excuteRowSet(String sql, Object... values) throws SQLException {
        values = values == null ? new String[] {} : values;

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String obKey = "";
        try {
            String formatSql = sql;
            // 非参数化的sql
            if (values == null || values.length == 0)
                formatSql = SqlNormalizer.format(sql);

            obKey = dbName + ":" + formatSql;
            conn = getConnection();
            stmt = conn.prepareStatement(sql);

            if (values != null)
                for (int i = 0; i < values.length; i++) {
                    stmt.setObject(i + 1, values[i]);
                }

            rs = stmt.executeQuery();
            if (rs == null || rs.getMetaData().getColumnCount() == 0)
                return null;

            CachedRowSet crs = new CachedRowSetImpl();
            crs.populate(rs);

            return crs;
        }
        catch (SQLException e) {
            LOGGER.error(obKey + " error: {}", e);
            throw e;
        }
        finally {
            DatabaseHelper.attemptClose(rs);
            DatabaseHelper.attemptClose(stmt);
            closeConnection(conn);
        }

    }


    public CachedRowSet spExecuteRowSet(String spName, String[] params, Object... values) throws SQLException {
        params = params == null ? new String[] {} : params;
        values = values == null ? new String[] {} : values;

        Connection conn = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        String[] paramsTemp = new String[params.length];// 临时存放参数名称，防止先mysql后sqlserver访问时修改了参数名称出错
        String obKey = "";
        try {
            obKey = dbName + ":" + spName;

            if (params != null) {
                if (params.length != values.length) {
                    throw new IllegalArgumentException("params.length != values.length");
                }
            }
            conn = getConnection();
            String dbType = conn.getMetaData().getDatabaseProductName();
            if (dbType.equalsIgnoreCase("MySQL")) {
                convertAtToT(paramsTemp, params);
            }
            sql = DatabaseHelper.getCallSql(spName, values == null ? 0 : values.length);
            stmt = conn.prepareCall(sql);
            if (dbType.equalsIgnoreCase("MySQL")) {
                DatabaseHelper.fillStatememt(stmt, paramsTemp, values);
            }
            else {
                DatabaseHelper.fillStatememt(stmt, params, values);
            }
            rs = stmt.executeQuery();

            if (rs == null || rs.getMetaData().getColumnCount() == 0)
                return null;

            CachedRowSet crs = new CachedRowSetImpl();
            crs.populate(rs);

            return crs;

        }
        catch (SQLException e) {
            LOGGER.error(obKey + " error: {}", e);
            throw e;
        }
        finally {
            DatabaseHelper.attemptClose(rs);
            DatabaseHelper.attemptClose(stmt);
            closeConnection(conn);
        }
    }


    /**
     * 
     * @return Connection
     * @throws SQLException
     *             Database每次获取最新的 Connection,用完close Transaction
     *             用自身的Connection,用完不close
     */
    public Connection getConnection() throws SQLException {
        return poolAdapter.getConnection();
    }


    void closeConnection(Connection conn) {
        DatabaseHelper.attemptClose(conn);
    }


    @Override
    public List<DataTable> spExecuteTables(String spName, String[] params, Object... values)
            throws SQLException {

        params = params == null ? new String[] {} : params;
        values = values == null ? new String[] {} : values;

        Connection conn = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        String[] paramsTemp = new String[params.length];// 临时存放参数名称，防止先mysql后sqlserver访问时修改了参数名称出错
        String obKey = "";
        try {
            obKey = dbName + ":" + spName;

            if (params != null) {
                if (params.length != values.length) {
                    throw new IllegalArgumentException("params.length != values.length");
                }
            }
            conn = getConnection();
            String dbType = conn.getMetaData().getDatabaseProductName();
            if (dbType.equalsIgnoreCase("MySQL")) {
                convertAtToT(paramsTemp, params);
            }
            sql = DatabaseHelper.getCallSql(spName, values.length);
            stmt = conn.prepareCall(sql);
            if (dbType.equalsIgnoreCase("MySQL")) {
                DatabaseHelper.fillStatememt(stmt, paramsTemp, values);
            }
            else {
                DatabaseHelper.fillStatememt(stmt, params, values);
            }
            rs = stmt.executeQuery();

            List<DataTable> ds = new ArrayList<DataTable>();
            ds.add(new DataTable(rs));

            while (stmt.getMoreResults()) {
                rs = stmt.getResultSet();
                ds.add(new DataTable(rs));
            }
            return ds;
        }
        catch (SQLException e) {
            LOGGER.error(obKey + " error: {}", e);
            throw e;
        }
        finally {
            DatabaseHelper.attemptClose(rs);
            DatabaseHelper.attemptClose(stmt);
            DatabaseHelper.attemptClose(conn);
        }
    }


    @Override
    public List<DataTable> executeTables(String sql, Object... values) throws SQLException {
        values = values == null ? new String[] {} : values;

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String obKey = "";
        try {
            String formatSql = sql;
            // 非参数化的sql
            if (values == null || values.length == 0)
                formatSql = SqlNormalizer.format(sql);
            obKey = dbName + ":" + formatSql;

            conn = getConnection();
            stmt = conn.prepareStatement(sql);

            for (int i = 0; i < values.length; i++) {
                stmt.setObject(i + 1, values[i]);
            }

            rs = stmt.executeQuery();
            List<DataTable> ds = new ArrayList<DataTable>();
            ds.add(new DataTable(rs));

            while (stmt.getMoreResults()) {
                rs = stmt.getResultSet();
                ds.add(new DataTable(rs));
            }

            return ds;
        }
        catch (SQLException e) {
            LOGGER.error(obKey + " error: {}", e);
            throw e;
        }
        finally {
            DatabaseHelper.attemptClose(rs);
            DatabaseHelper.attemptClose(stmt);
            DatabaseHelper.attemptClose(conn);
        }
    }

}
