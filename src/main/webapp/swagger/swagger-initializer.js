document.addEventListener('DOMContentLoaded', function() {
  // Configuración para requerir API Key antes de cargar el spec
  let apiKey = null;

  function promptForApiKey() {
    apiKey = window.prompt("Por favor ingresa tu API Key para acceder a la documentación:");
    if (!apiKey) {
      alert("Debes ingresar una API Key válida para continuar.");
      promptForApiKey();
    }
  }

  promptForApiKey();

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
      if (apiKey) {
        req.headers['x-api-key'] = apiKey;
      }
      return req;
    },
    // Bloquear el input de URL para evitar explorar otras specs
    onComplete: function() {
      const input = document.querySelector('input[placeholder="URL"]');
      if (input) {
        input.disabled = true;
      }
    }
  });
});
