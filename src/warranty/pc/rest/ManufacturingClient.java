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
package warranty.pc.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import warranty.pc.model.Claim;
import warranty.pc.model.Country;

/**
 *
 * @author bhagc
 */
public class ManufacturingClient {

    private String endpoint;
    private boolean restErrors = false;

    private static final Logger LOGGER = Logger.getLogger(ManufacturingClient.class.getName());

    public ManufacturingClient(String endpoint) throws Exception {
        if (endpoint == null) {
            throw new Exception("Endpoint can't be NULL");
        }
        this.endpoint = endpoint;
    }

    public List<Claim> augmentClaimData(List<Claim> claims) {
        if (claims == null) {
            LOGGER.log(Level.WARNING, "Claim List was null , can't proceed with data augmentation");
            return new ArrayList<>();
        }

        claims.forEach(claim -> {
            Country manufacturingCountry = getManufacturerCountry();
            claim.setCountry(manufacturingCountry);
        });
        return claims;
    }

    private Country getManufacturerCountry() {
        Country country = null;
        try {

            Client client = ClientBuilder.newClient();
            country = client.target(endpoint)
                    .request(MediaType.APPLICATION_JSON)
                    .get(Country.class);

            LOGGER.log(Level.FINEST, "Country code and region:{0}-{1}", new Object[]{
                country.getAlpha3Code(), country.getRegion()});
        } catch (Exception ex) {
            restErrors = true;
            LOGGER.log(Level.WARNING, "Problem invoking the REST endpoint: {0}", endpoint);
            //  ex.printStackTrace();
        }
        return country;
    }

    public boolean hasRestErrors() {
        return restErrors;
    }

}
