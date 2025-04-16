package com.dom.ws.rest.bot.Conexion;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.IOException;
import java.io.InputStream;

public class FirebaseInitializer {
    private static FirebaseInitializer instance;
    private boolean initialized = false;

    private FirebaseInitializer() {
        // Constructor privado para el patrón Singleton
    }

    public static synchronized FirebaseInitializer getInstance() {
        if (instance == null) {
            instance = new FirebaseInitializer();
        }
        return instance;
    }

    public synchronized void initialize() {
        if (initialized) {
            return;
        }

        try {
            // Ruta al archivo de configuración de Firebase
            InputStream serviceAccount =
                    this.getClass().getClassLoader().getResourceAsStream("firebase-credentials.json");

            if (serviceAccount == null) {
                throw new IOException("No se encontró el archivo de credenciales de Firebase");
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase Admin SDK inicializado correctamente");
                initialized = true;
            }
        } catch (IOException e) {
            System.err.println("Error al inicializar Firebase Admin SDK: " + e.getMessage());
            e.printStackTrace();
        }
    }
}