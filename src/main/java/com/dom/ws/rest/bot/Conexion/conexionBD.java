/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.Conexion;

/**
 *
 * @author MIGUEL
 */

import java.net.URL; // Add this import statement

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MIGUEL
 */
public class conexionBD {

    Properties atributosBD = new Properties();

    // ServletContext context = event.getServletContext();
    /*
     * private final String usuario = "udsbr";
     * //Contrase�a si tiene, si no tiene entonces dejar en blanco
     * // private final String pass = "GDXmix14318";
     * private final String pass = "udsbr#01";
     * //Servidor (localhost si lo tenemos local) o puede ser un servidor remoto,
     * vinajo
     * // private final String host = "mysql23332-env-1119869.j.facilcloud.com";
     * private final String host = "localHost";
     * //Nombre de la base de datos a la cual queremos conectarnos
     * private final String nombre_BD = "udsbr";
     */
    URL fileLocation = getClass().getClassLoader().getResource("configBD.properties");

    public static conexionBD instance;
    private Connection cnn;
    static final Logger logg = Logger.getLogger(conexionBD.class.getName());

    private conexionBD() {

        // PropertyConfigurator.configure("log4j.properties");
        logg.log(Level.INFO, "*** Inicio conexionBD ***");
        String servidor, user, password; /* , usuarioBD, contrasenaBD */;
        // log.debug("localizaci�n URL configDB "+fileLocation.getPath());
        // atributosBD.load(new
        // FileReader("/home/gidenutas/Dommatos/config/configBD.properties"));
        // servidor = "jdbc:mysql://" + atributosBD.getProperty("host") + "/" +
        // atributosBD.getProperty("nombreBD") ;
        /*
         * servidor = atributosBD.getProperty("UrlBD");
         * usuarioBD = atributosBD.getProperty("usuarioBD");
         * contrasenaBD = atributosBD.getProperty("contrasena");
         */
        // servidor = "jdbc:mysql://localhost:3306/DommApi?autoReconnect=true";
        // AZURE SQL = jdbc:sqlserver://dommatos.database.windows.net:1433;database=botdb;user=botdb@dommatos;password=Kelimporta0;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;
        // "jdbc:sqlserver://apibot-database.database.windows.net:1433;database=apibot-database;user=apibot-database@apibot-database;password=Kelimporta0;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
        // obtener servidor, usuario y contrase�a de variables de entorno de azure
        servidor = System.getenv("APPSETTING_DB_SERVER");
        user = System.getenv("APPSETTING_DB_USER");
        password = System.getenv("APPSETTING_DB_PASSWORD");

        //String servertest = "jdbc:mysql://localhost:3306/dommapi?autoReconnect=true";
        // mostrar en el log la info recibida
        logg.log(Level.INFO, "APPSETTING_DB_SERVER: {0}", servidor);

        if (servidor == null) {
            logg.warning("Connection string environment variables not found. Using default connection string.");
            // cerrar la conexion
            cnn = null;
        }else {
            try {
                
                //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                Class.forName("com.mysql.cj.jdbc.Driver");
                cnn = DriverManager.getConnection(servidor, user, password);
                // validar tiempo de espera de conexion, si pasa de los 15 segundos cerrar la comunicacion y ejecutar la excepcion
                Executor executor = Executors.newFixedThreadPool(1);
                cnn.setNetworkTimeout(executor, 15000);
                // si la conexion fue exitosa mostramos un mensaje de conexion exitosa
                if (cnn != null) {
                    logg.info("Conexion a base de datos " + servidor + " ... Ok");
                } else {
                    logg.info("Conexion a base de datos " + servidor + " ... Error");

                }

            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(conexionBD.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        logg.info("*** Fin conexionBD ***");

    }

    public Connection getCnn() {
        // log.info("*** Inicio getCnn ***");
        return cnn;

    }

    public synchronized static conexionBD saberEstado() {
        logg.info("*** Inicio saberEstado ***");
        if (instance == null) {
            instance = new conexionBD();
        }
        logg.info("*** Fin saberEstado ***");
        return instance;

    }

    public void cerrarConexion() {
        logg.info("*** Inicio cerrarConexion ***");
        instance = null;
        /*
         * try {
         * cnn.close();
         * } catch (SQLException ex) {
         * log.error("Error al Cerrar la conexion a BD "+ex);
         * //Logger.getLogger(conexionBD.class.getName()).log(Level.SEVERE, null, ex);
         * }finally{
         * instance = null;
         * }
         */
        logg.info("*** Fin cerrarConexion ***");
    }
}
