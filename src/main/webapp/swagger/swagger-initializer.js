window.onload = function() {
  //<editor-fold desc="Changeable Configuration Block">

  // the following lines will be replaced by docker/configurator, when it runs in a docker-container
  window.ui = SwaggerUIBundle({
    url: "/apiBot/services/openapi.json",
    dom_id: '#swagger-ui',
    deepLinking: true,
    presets: [
      SwaggerUIBundle.presets.apis,
      SwaggerUIStandalonePreset
    ],
    plugins: [
      SwaggerUIBundle.plugins.DownloadUrl
    ],
    layout: "StandaloneLayout",
    requestInterceptor: (req) => {
      // Si el usuario ya puso el API Key en Authorize, Swagger lo agregará automáticamente.
      // Si quieres forzar que siempre lo pida, puedes dejarlo así y definir el esquema de seguridad en tu OpenAPI.
      return req;
    }
  });

  //</editor-fold>
};
