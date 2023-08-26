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
package warranty.pc.app;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author bhagc
 */
public class AppConfiguration {
    private final static String APP_PROPERTIES_FILE_NAME="app.properties";
    private static Properties appProps=null;
    
    public static Properties getConfig()throws FileNotFoundException,IOException{
        if(appProps==null){
            String rootPath=Thread.currentThread().getContextClassLoader().getResource("").getPath();
            System.out.print(rootPath);
            String appConfigPath=rootPath+APP_PROPERTIES_FILE_NAME;
            appProps=new Properties();
            appProps.load(new FileInputStream(appConfigPath));
        }
        return appProps;
    }
}
