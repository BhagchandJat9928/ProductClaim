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
package warranty.pc.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import warranty.pc.model.Claim;

/**
 *
 * @author bhagc
 */
public class ClaimFileReader {
    
    private final DateTimeFormatter formatter=DateTimeFormatter.ofPattern("EEE MM dd HH:mm:ss zzz yyyy",Locale.ENGLISH);
    private final MessageFormat claimFormat;
    private List<String> failedRecords;
    private static final Logger LOGGER = Logger.getLogger(ClaimFileReader.class.getName());
    
    public ClaimFileReader(String dataFormat) {
        this.claimFormat = new MessageFormat(dataFormat);
    }
    
    public List<Claim> readClaims(String fileName) {
        List<Claim> claims = new ArrayList<>();
        failedRecords=new ArrayList<>();
        //initialize failed records
        try {    
            claims = Files.lines(new File(fileName).toPath(),Charset.forName("UTF-8"))
                    .filter(line -> line != null)
                    .map(line -> {
                        Object[] values = parseData(line);
                        if (values != null) {
                            Claim claim = new Claim();
                            claim.setCustomerId((String) values[0]);
                            claim.setCustomerName((String)values[1]);
                            claim.setCustomerLastName((String)values[2]);
                            claim.setCustomerEmail((String)values[3]);
                            claim.setProductId(Integer.valueOf((String)values[4]));
                            claim.setProductName((String)values[5]);
                            claim.setSerialNumber((String)values[6]);
                            claim.setClaimDate(LocalDate.parse((String)values[7],formatter));
                            claim.setSubject((String)values[8]);
                            claim.setSummary((String)values[9]);
                            return claim;
                        }
                        return null;
                    })
                    .filter(c -> c != null)
                    .collect(Collectors.toList());
            
        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.WARNING, "File Not Found: ",ex);
        } catch (IOException ex) {
            LOGGER.log(Level.WARNING, "Error while reading claims from CSV file",ex);
        }
        return claims;
    }
    
    private Object[] parseData(String text) {
        try {
            return claimFormat.parse(text);
        } catch (ParseException ex) {
            LOGGER.log(Level.WARNING, "Error parsing data String: {0}",text);
            LOGGER.log(Level.WARNING, ex.getMessage());
            failedRecords.add(text);
        }
        return null;
    }
    
    public List<String> getFailedRecords() {
        return failedRecords;
    }
    
}
