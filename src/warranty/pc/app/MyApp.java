/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package warranty.pc.app;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import warranty.pc.db.ClaimDBProcessor;
import warranty.pc.db.WarrantyDatabase;
import warranty.pc.db.WarrantyValidator;
import warranty.pc.io.ClaimFileReader;
import warranty.pc.io.ClaimFileWriter;
import warranty.pc.model.Claim;
import warranty.pc.rest.ManufacturingClient;

/**
 *
 * @author bhagc
 */
public class MyApp {

    private static final Logger LOGGER = Logger.getLogger(MyApp.class.getName());

    private static final String CLAIM_BATCH_FILE_NAME = "app.claims.batch.file";
    private final static String BAD_FORMAT_OUTPUT = "app.bad.format.output";
    private final static String BAD_FORMAT_FN_PREFIX = "app.bad.format.filename.prefix";
    private final static String INVALID_WARRANTY_OUTPUT = "app.invalid.warranty.output";
    private final static String INVALID_WARRANTY_FN_PREFIX = "app.invalid.warranty.filename.prefix";
    private final static String CSV_DATA_FORMAT = "csv.data.format";

    private final static String DB_URL = "app.db.uri";
    private final static String DB_USERNAME = "app.db.username";
    private final static String DB_PASSWORD = "app.db.password";
    private final static String API_ENDPOINT = "app.api.endpoint";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Properties config = AppConfiguration.getConfig();
            System.out.println("hello");
            ClaimFileReader claimReader = new ClaimFileReader(config.getProperty(CSV_DATA_FORMAT));
            List<Claim> claimList = claimReader.readClaims(config.getProperty(CLAIM_BATCH_FILE_NAME));

            ClaimFileWriter claimWriter = new ClaimFileWriter();
            claimWriter.writeInvalidFormat(claimReader.getFailedRecords(),
                    config.getProperty(BAD_FORMAT_OUTPUT),
                    config.getProperty(BAD_FORMAT_FN_PREFIX));

            LOGGER.log(Level.INFO, "{0} claims found in {1}",
                    new Object[]{claimList.size(), config.getProperty(CLAIM_BATCH_FILE_NAME)});

            System.out.println("---Total of Claims processed from CSV file: " + claimList.size() + " ----");

            WarrantyDatabase warrantyDB = new WarrantyDatabase(config.getProperty(DB_URL),
                    config.getProperty(DB_USERNAME), config.getProperty(DB_PASSWORD));

            WarrantyValidator validator = new WarrantyValidator(warrantyDB.getConnection());

            List<Claim> validClaims = validator.validate(claimList);
            claimWriter.writeInvalidWarranty(validator.getInvalidWarrantyClaims(),
                    config.getProperty(INVALID_WARRANTY_OUTPUT),
                    config.getProperty(INVALID_WARRANTY_FN_PREFIX));
            LOGGER.log(Level.INFO, "{0} claims have a valid warranty ", validClaims.size());
            System.out.println("--- Claims with a valid warranty: " + validClaims.size());

            ManufacturingClient manRestApi = new ManufacturingClient(config.getProperty(API_ENDPOINT));
            List<Claim> augmentedClaims = manRestApi.augmentClaimData(validClaims);
            if (manRestApi.hasRestErrors()) {
                LOGGER.log(Level.INFO, "Some claims were not augmented with REST API because of a REST endpoint issue");
            }
            if (augmentedClaims != null &&  ! augmentedClaims.isEmpty()) {
                LOGGER.log(Level.INFO, "Claims augmented with Country code and Country Region from Rest Api");
            }

            ClaimDBProcessor claimProcessor = new ClaimDBProcessor(warrantyDB.getConnection());
            claimProcessor.saveClaims(augmentedClaims);

            LOGGER.log(Level.INFO, "{0} claims found augmented", augmentedClaims.size());
            System.out.println("---Total of Claims found Augmented: " + augmentedClaims.size() + " ----");

        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
}
