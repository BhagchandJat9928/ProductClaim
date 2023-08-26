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
 * aint with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package warranty.pc.model;

import java.time.LocalDate;

/**
 *
 * @author bhagc
 */
public class Claim {

    private  String customerId;
    private  String customerName;
    private  String customerLastName;
    private  String customerEmail;
    private  Integer productId;
    private  String productName;
    private  String serialNumber;
    private  LocalDate claimDate;
    private  String subject;
    private  String summary;
    private Warranty warranty;
    private Country country;



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("customerId: ").append(customerId).append(" | ");
        sb.append(", customerName: ").append(customerName).append(" | ");
        sb.append(", customerLastName: ").append(customerLastName).append(" | ");
        sb.append(", customerEmail: ").append(customerEmail).append(" | ");
        sb.append(", productId: ").append(productId).append(" | ");
        sb.append(", productName: ").append(productName).append(" | ");
        sb.append(", serialNumber: ").append(serialNumber).append(" | ");
        sb.append(", claimDate: ").append(claimDate).append(" | ");
        sb.append(", subject: ").append(subject).append(" | ");
        sb.append(", summary: ").append(summary);
        return sb.toString();
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public LocalDate getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(LocalDate claimDate) {
        this.claimDate = claimDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Warranty getWarranty() {
        return warranty;
    }

    public void setWarranty(Warranty warranty) {
        this.warranty = warranty;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

   


}
