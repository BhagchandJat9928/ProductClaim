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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import warranty.pc.model.Claim;
import warranty.pc.model.Warranty;

/**
 *
 * @author bhagc
 */
public class WarrantyValidator {

    private static final Logger LOGGER = Logger.getLogger(WarrantyDatabase.class.getName());
    private Connection conn;
    private List<Claim> invalidWarrantyClaims;

    public WarrantyValidator(Connection dbConnection) throws Exception {
        if (dbConnection == null) {
            throw new Exception("Database Connection is NULL");
        }
        this.conn = dbConnection;
    }

    public List<Claim> validate(List<Claim> claimList) {
        if (claimList == null) {
            LOGGER.log(Level.WARNING, "Claim list was null validation can't proceed");
            return null;
        }

        List<Claim> validWarranty = new ArrayList<>();
        invalidWarrantyClaims = new ArrayList<>();
        claimList.forEach(claim -> {
            Warranty w = this.queryWarranty(claim.getProductId(), claim.getSerialNumber(), claim.getClaimDate());

            if (w != null && w.isVaild()) {
                validWarranty.add(claim);
                claim.setWarranty(w);
            } else {
                invalidWarrantyClaims.add(claim);
            }
        });

        return validWarranty;
    }

    private Warranty queryWarranty(Integer productId, String serialNumber, LocalDate date) {
        String sqlQuery = "select * from Warranty where productId= ? And serialNumber= ?";
        Warranty warranty = findWarranty(sqlQuery, productId, serialNumber);
        if (warranty != null && warranty.getExpiryDate().isAfter(date)
                && warranty.getDateOpened().isBefore(date)) {
            warranty.setVaild(true);
        }
        return warranty;

    }

    private Warranty findWarranty(String sqlQueryStr, Integer productId, String serialNumber) {
        Warranty warranty = new Warranty();
        try {
            PreparedStatement st = conn.prepareStatement(sqlQueryStr);
            st.setInt(1, productId);
            st.setString(2, serialNumber);
            ResultSet result = st.executeQuery();

            while (result.next()) {

                warranty.setProductId(result.getInt(1));
                warranty.setSerialNumber(result.getString(2));
                warranty.setWarrantyNumber(result.getInt(3));
                warranty.setDateOpened(result.getDate(4).toLocalDate());
                warranty.setExpiryDate(result.getDate(5).toLocalDate());
                return warranty;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.WARNING, "Problem finding a warranty for ProductId:{0} and serial Number: {1}", new Object[]{productId, serialNumber});
            LOGGER.log(Level.WARNING, ex.getMessage(), ex);
        }
        return null;
    }

    public List<Claim> getInvalidWarrantyClaims() {
        return invalidWarrantyClaims;
    }

}
