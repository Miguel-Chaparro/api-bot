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

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MIGUEL
 */
public class conexionBD {

    Properties atributosBD = new Properties();

    //ServletContext context = event.getServletContext();
    private final String usuario = "udsbr";
    //Contraseña si tiene, si no tiene entonces dejar en blanco
    // private final String pass = "GDXmix14318";
    private final String pass = "udsbr#01";
    //Servidor (localhost si lo tenemos local) o puede ser un servidor remoto, vinajo
    // private final String host = "mysql23332-env-1119869.j.facilcloud.com";
    private final String host = "localHost";
    //Nombre de la base de datos a la cual queremos conectarnos
    private final String nombre_BD = "udsbr";
    URL fileLocation = getClass().getClassLoader().getResource("configBD.properties");

    public static conexionBD instance;
    private Connection cnn;
    static final Logger logg = Logger.getLogger(conexionBD.class.getName());

    private conexionBD() {

        //PropertyConfigurator.configure("log4j.properties");
        logg.log(Level.INFO, "*** Inicio conexionBD ***");
        String servidor, usuarioBD, contrasenaBD;
        //log.debug("localización URL configDB "+fileLocation.getPath());
        //atributosBD.load(new FileReader("/home/gidenutas/Dommatos/config/configBD.properties"));
        //servidor = "jdbc:mysql://" + atributosBD.getProperty("host") + "/" + atributosBD.getProperty("nombreBD") ;
        /*servidor = atributosBD.getProperty("UrlBD");
        usuarioBD = atributosBD.getProperty("usuarioBD");
        contrasenaBD = atributosBD.getProperty("contrasena");*/
        //servidor = "jdbc:mysql://localhost:3306/DommApi?autoReconnect=true";
        servidor = "jdbc:sqlserver://apibot-database.database.windows.net:1433;database=apibot-database;user=apibot-database@apibot-database;password=Kelimporta0;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
        usuarioBD = "dommatos";
        contrasenaBD = "D0mm4t0s";
        logg.log(Level.INFO, System.getenv("RDS_HOSTNAME"));
        //log.debug("Datos de Conexión: Servidor = "+servidor + "Usuario: " + usuarioBD + "contrasena = "+ contrasenaBD);
        //String servidor = "jdbc:mysql://" + host + "/" + nombre_BD;
        try {
            //Class.forName("com.mysql.jdbc.Driver");//driver de conexion de la base de datos Mysql
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            cnn = DriverManager.getConnection(servidor);
        } catch (ClassNotFoundException | SQLException ex) {
            //log.error("Erro Driver de Conexion BD" + ex);
            Logger.getLogger(conexionBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        //log.error("Erro al Conexion BD " + ex);
        //log.error("Error Lectura de Properties BD "+ex);
        logg.info("*** Fin conexionBD ***");
    }

    public Connection getCnn() {
        //log.info("*** Inicio getCnn ***");
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
        /* try {
        cnn.close();
        } catch (SQLException ex) {
        log.error("Error al Cerrar la conexion a BD "+ex);
        //Logger.getLogger(conexionBD.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
        instance = null;
        }*/
        logg.info("*** Fin cerrarConexion ***");
    }
}
