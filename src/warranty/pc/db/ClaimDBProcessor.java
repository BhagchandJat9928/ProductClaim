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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import warranty.pc.model.Claim;

/**
 *
 * @author bhagc
 */
public class ClaimDBProcessor {

    private static final Logger LOGGER = Logger.getLogger(ClaimDBProcessor.class.getName());
    private static final String NEW_STATUS = "NEW";
    private final Connection conn;

    public ClaimDBProcessor(Connection dbConnection) throws Exception {
        if (dbConnection == null) {
            throw new Exception("Database Connection is NULL");
        }
        conn = dbConnection;
    }

    public void saveClaims(List<Claim> claimList) {
        if (claimList == null) {
            LOGGER.log(Level.WARNING, "Claim list was null, cannot save them to database");
            return;
        }
        claimList.forEach(c -> save(c));
        LOGGER.log(Level.FINEST, "All claims saved");
    }

    private void save(Claim claim) {

        //if the rest data augementation failed ,then the country information is not available
        String sql = "insert into Claim "
                + "(CUSTOMER_ID,CUTOMER_FIRSTNAME,CUSTOMER_LASTNAME,"
                + "CUTOMER_EMAIL,PRODUCT_ID,PRODUCT_NAME,SERIAL_NUMBER,"
                + "WARRANTY_NUMBER,COUNTRY_CODE,COUNTRY_REGION,STATUS,CLAIM_DATE,SUBJECT,SUMMARY)"
                + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        // String returnCols[] = {"CLAIM_ID"};
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, claim.getCustomerId());
            st.setString(2, claim.getCustomerName());
            st.setString(3, claim.getCustomerLastName());
            st.setString(4, claim.getCustomerEmail());
            st.setInt(5, claim.getProductId());
            st.setString(6, claim.getProductName());
            st.setString(7, claim.getSerialNumber());
            st.setInt(8, claim.getWarranty().getWarrantyNumber());

            String countryCode = claim.getCountry() != null ? claim.getCountry().getAlpha3Code() : "+91";
            String countryRegion = claim.getCountry() != null ? claim.getCountry().getRegion() : "kolkata";
            st.setString(9, countryCode);
            st.setString(10, countryRegion);
            st.setString(11, NEW_STATUS);
            st.setDate(12, Date.valueOf(claim.getClaimDate()));
            st.setString(13, claim.getSubject());
            st.setString(14, claim.getSummary());
            st.executeUpdate();
            LOGGER.log(Level.INFO, "Claim saved to database:{0}", claim);
        } catch (SQLException ex) {
            LOGGER.log(Level.WARNING, "Error while saving claim:{0}", claim);
            LOGGER.warning(ex.getMessage());

        }
    }

}
