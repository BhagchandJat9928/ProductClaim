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
package warranty.pc.model;

import java.time.LocalDate;

/**
 *
 * @author bhagc
 */
public class Warranty {
    private  Integer productId;
    private  String serialNumber;
    private  Integer warrantyNumber;
    private  LocalDate dateOpened;
    private  LocalDate expiryDate;
    private boolean vaild;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("productId=").append(productId).append(" | ");
        sb.append(", serialNumber=").append(serialNumber).append(" | ");
        sb.append(", warrantyNumber=").append(warrantyNumber).append(" | ");
        sb.append(", dateOpened=").append(dateOpened).append(" | ");
        sb.append(", expiryDate=").append(expiryDate).append(" | ");
        sb.append(", vaild=").append(vaild);
        return sb.toString();
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getWarrantyNumber() {
        return warrantyNumber;
    }

    public void setWarrantyNumber(Integer warrantyNumber) {
        this.warrantyNumber = warrantyNumber;
    }

    public LocalDate getDateOpened() {
        return dateOpened;
    }

    public void setDateOpened(LocalDate dateOpened) {
        this.dateOpened = dateOpened;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isVaild() {
        return vaild;
    }

    public void setVaild(boolean vaild) {
        this.vaild = vaild;
    }
    
    

    
}
