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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import warranty.pc.model.Claim;

/**
 *
 * @author bhagc
 */
public class ClaimFileWriter {

    private static final Logger LOGGER = Logger.getLogger(ClaimFileWriter.class.getName());

    public void writeInvalidFormat(List<String> claimRecords, String filePath, String fileName) throws Exception {

        if (claimRecords == null || filePath == null || filePath.isBlank() || fileName == null) {
            throw new Exception("Not enough information to write claim records to a file: " + fileName + " within the directory: " + filePath);
        }
        //avoid creating an empty file
        if (claimRecords.isEmpty()) {
            return;
        }

        String path = getOutputFileName(filePath, fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            claimRecords.forEach(line -> {
                try {
                    writer.write(line);
                    writer.write("\n");
                } catch (IOException ex) {
                    LOGGER.log(Level.WARNING, "Problem writing record: {0}", line);
                    LOGGER.log(Level.WARNING, ex.getMessage());
                }
            });
        } catch (IOException ex) {
            LOGGER.log(Level.WARNING, ex.getMessage());
        }
    }

    public void writeInvalidWarranty(List<Claim> claimRecords, String filePath, String fileName) throws Exception {
        if (claimRecords == null || filePath == null || filePath.isBlank() || fileName == null) {
            throw new Exception("Not enough information to write invalid claim records to a file: " + fileName + " within the directory: " + filePath);
        }
        //avoid creating an empty file
        if (claimRecords.isEmpty()) {
            return;
        }

        String path = getOutputFileName(filePath, fileName);

        try (BufferedWriter writer
                            = Files.newBufferedWriter(Path.of(path),
                        Charset.forName("UTF-8"),
                        StandardOpenOption.CREATE_NEW)) {

            claimRecords.forEach(claim -> {

                try {
                    LOGGER.log(Level.FINEST, "Saving claim with invalid Warranty: {0}", claim);
                    writer.write(claim.toString());
                    writer.write("\n");
                    LOGGER.info("invalid Warranty Claim saved ");
                } catch (IOException ex) {
                    LOGGER.log(Level.WARNING, "Problem writing claim: {0}", claim);
                    LOGGER.log(Level.WARNING, ex.getMessage());
                }
            });
        } catch (IOException ex) {
            LOGGER.log(Level.WARNING, ex.getMessage());
        }

    }

    private String getOutputFileName(String filePath, String fileName) {

        StringBuilder sb = new StringBuilder();
        sb.append(filePath).append("/").append(fileName);
        sb.append(Calendar.getInstance().getTimeInMillis());
        sb.append(".txt");

        return sb.toString();
    }

}
