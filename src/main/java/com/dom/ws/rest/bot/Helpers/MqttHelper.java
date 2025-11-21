package com.dom.ws.rest.bot.Helpers;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import java.util.logging.Logger;
import java.util.logging.Level;

public class MqttHelper {
    static final Logger log = Logger.getLogger(MqttHelper.class.getName());
    
    private static final String BROKER_URL = "tcp://broker.dommatos.com:1883";
    private static final String USERNAME = "domaccess";
    private static final String PASSWORD = "dommatos2018";
    private static final int QOS = 2;
    private static final boolean RETAIN = true;
    private static final int KEEPALIVE = 60;
    private static final int RECONNECT_PERIOD = 1000;
    
    /**
     * Envía un mensaje MQTT a un cliente notificándole sobre su nuevo contrato
     * 
     * @param phoneNumber Número de teléfono en formato E.164 (ej: +573001234567)
     * @param userName Nombre del usuario
     * @param companyName Nombre de la empresa
     * @param userEmail Email del usuario
     * @param contractId ID del contrato
     * @return true si el mensaje se envió exitosamente, false en caso contrario
     */
    public static boolean sendContractNotification(
            String phoneNumber,
            String userName,
            String companyName,
            String userEmail,
            Integer contractId) {
        
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            log.warning("No se puede enviar MQTT: phoneNumber es nulo o vacío");
            return false;
        }
        
        if (userName == null || companyName == null || userEmail == null || contractId == null) {
            log.warning("No se puede enviar MQTT: parámetros obligatorios faltantes");
            return false;
        }
        
        MqttClient client = null;
        try {
            // Generar clientId único
            String clientId = "apiBot_" + System.currentTimeMillis();
            
            // Crear cliente MQTT
            client = new MqttClient(BROKER_URL, clientId);
            
            // Configurar opciones de conexión
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setUserName(USERNAME);
            options.setPassword(PASSWORD.toCharArray());
            options.setKeepAliveInterval(KEEPALIVE);
            options.setAutomaticReconnect(true);
            options.setConnectionTimeout(10);
            options.setMqttVersion(3); // MQTT 3.1
            
            // Conectar al broker
            client.connect(options);
            log.info("Conectado a broker MQTT: " + BROKER_URL);
            
            // Construir el mensaje
            String message = buildWelcomeMessage(userName, companyName, userEmail, contractId, phoneNumber);
            
            // Publicar el mensaje al topic del teléfono
            String topic = "notifications";
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            mqttMessage.setQos(QOS);
            mqttMessage.setRetained(RETAIN);
            
            client.publish(topic, mqttMessage);
            log.info("Mensaje MQTT enviado al topic: " + topic);
            
            return true;
            
        } catch (MqttException e) {
            log.log(Level.SEVERE, "Error en comunicación MQTT: " + e.getMessage(), e);
            return false;
        } finally {
            // Desconectar cliente
            if (client != null) {
                try {
                    client.disconnect();
                    client.close();
                } catch (MqttException e) {
                    log.log(Level.WARNING, "Error desconectando cliente MQTT: " + e.getMessage(), e);
                }
            }
        }
    }
    
    /**
     * Construye el mensaje de bienvenida con formato WhatsApp
     */
    private static String buildWelcomeMessage(
            String userName,
            String companyName,
            String userEmail,
            Integer contractId,
            String phoneNumber) {
        
        return phoneNumber + "|Hola *" + userName + "* Bienvienido a *" + companyName + "* " +
               "para continuar con la instalación del servicio por favor ingresa con el correo " +
               "*" + userEmail + "* en la plataforma " +
               "https://dashboard.dommatos.com/contract/" + contractId + " y acepta el contrato";
    }
}
