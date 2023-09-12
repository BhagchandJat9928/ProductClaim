/*
 * Copyright (C) 2023 bhagc
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package warranty.pc.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bhagc
 */
public class WarrantyDatabase {

    private static final Logger LOGGER = Logger.getLogger(WarrantyDatabase.class.getName());
    private static final String CONN_FACTORY_CLASS_NAME = "com.mysql.jdbc.Driver"; //"oracle.jdbc.pool.OracleDataSource";
    private Connection conn;

    public WarrantyDatabase(String dbURL, String username, String password) throws Exception {
        if (dbURL == null || username == null || password == null) {
            throw new Exception("Missing parameters (Url | username | password) in the setup Database");
        }
        setupDatabase(dbURL, username, password);
    }

    public Connection getConnection() {
        return conn;
    }

    private void setupDatabase(String dbURL, String dbUsername, String dbPassword) {
        //Get the PoolDataSource for UCP
        // PoolDataSource pds = PoolDataSourceFactory.getPoolDataSource();

        //set the connection factory first befor all other properties
        try {
            Class.forName(CONN_FACTORY_CLASS_NAME);
            conn = DriverManager.getConnection(dbURL, dbUsername, dbPassword);

            String createTable = "create table Claim if not exists(CLAIM_ID varchar(50),CUSTOMER_ID  varchar(50),"
                    + "CUTOMER_FIRSTNAME varchar(50),CUSTOMER_LASTNAME varchar(50),"
                    + "CUTOMER_EMAIL,PRODUCT_ID varchar(50),PRODUCT_NAME varchar(50),SERIAL_NUMBER varchar(50),"
                    + "WARRANTY_NUMBER integer,COUNTRY_CODE varchar(50),COUNTRY_REGION varchar(50),STATUS varchar(50),"
                    + "CLAIM_DATE Date,SUBJECT varchar(50),SUMMARY varchar(50))";
            PreparedStatement st = conn.prepareStatement(createTable);
            st.execute();

//            pds.setConnectionFactoryClassName(CONN_FACTORY_CLASS_NAME);
//
//            //set the jdbc connection properties after pool has been created
//            pds.setURL(dbURL);
//            pds.setPassword(dbPassword);
//            pds.setUser(dbUsername);
//            pds.setConnectionPoolName("JDBC_UDP_POOL");
//            pds.setInitialPoolSize(5);
//            pds.setMinPoolSize(5);
//            pds.setMaxPoolSize(20);
//            pds.setTimeoutCheckInterval(5);
//            pds.setInactiveConnectionTimeout(10);
//
//
//           Properties connProps=new Properties();
//           connProps.setProperty("fixedString", "false");
//           connProps.setProperty("remarksReporting", "false");
//           connProps.setProperty("includeSynonyms", "false");
//           connProps.setProperty("restrictGetTables", "false");
//           connProps.setProperty("defaultNChar", "false");
//           connProps.setProperty("AccumulateBatchResult", "false");
//
//            //jdbc connection properties will be set on the provided
//            //connection factory
//            pds.setConnectionProperties(connProps);
//            conn=pds.getConnection();
            LOGGER.log(Level.FINEST, "Configured database");
        } catch (SQLException ex) {
            LOGGER.log(Level.INFO, "Error while setting up Database", ex);
        } catch (ClassNotFoundException ex) {
            LOGGER.log(Level.INFO, "Mysql class Not found", ex);
        }
    }

}
