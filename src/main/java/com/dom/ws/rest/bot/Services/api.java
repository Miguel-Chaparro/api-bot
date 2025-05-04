/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.Services;

import com.dom.ws.rest.bot.Controller.answerController;
import com.dom.ws.rest.bot.Controller.createQuestionController;
import com.dom.ws.rest.bot.Controller.devicesConfigController;
import com.dom.ws.rest.bot.Controller.getQuestionsController;
import com.dom.ws.rest.bot.Controller.projectController;
import com.dom.ws.rest.bot.Controller.questionsController;
import com.dom.ws.rest.bot.DAO.ProfileDAO;
import com.dom.ws.rest.bot.DAO.UserDAO;
import com.dom.ws.rest.bot.DAO.EmpresaDAO;
import com.dom.ws.rest.bot.DAO.RaspberryNewDAO;
import com.dom.ws.rest.bot.DTO.ProfileDTO;
import com.dom.ws.rest.bot.DTO.UserDTO;
import com.dom.ws.rest.bot.DTO.answerDTO;
import com.dom.ws.rest.bot.DTO.projectDTO;
import com.dom.ws.rest.bot.DTO.questionsDTO;
import com.dom.ws.rest.bot.DTO.raspiDTO;
import com.dom.ws.rest.bot.DTO.EmpresaDTO;
import com.dom.ws.rest.bot.DTO.RaspberryNewDTO;
import com.dom.ws.rest.bot.Request.AssignProfileRequest;
import com.dom.ws.rest.bot.Request.answerReq;
import com.dom.ws.rest.bot.Request.questionsAnswersReq;
import com.dom.ws.rest.bot.Request.createProjectsReq;
import com.dom.ws.rest.bot.Request.createQuestionsReq;
import com.dom.ws.rest.bot.Request.raspiReq;
import com.dom.ws.rest.bot.Response.LoginResponse;
import com.dom.ws.rest.bot.Response.answerResp;
import com.dom.ws.rest.bot.Response.createQuestionsResp;
import com.dom.ws.rest.bot.Response.getAnswerResp;
import com.dom.ws.rest.bot.Response.getQuestionsAnswerResp;
import com.dom.ws.rest.bot.Response.getQuestionsResp;
import com.dom.ws.rest.bot.Response.projectsResp;
import com.dom.ws.rest.bot.Response.raspiResp;
import com.dom.ws.rest.bot.vo.msgError;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.google.firebase.auth.FirebaseAuth;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.concurrent.ExecutorService;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.media.ExampleObject;

/**
 *
 * @author MIGUEL
 */
@Path("/AccessApi")
@Provider
public class api {

    private final ExecutorService executorService = java.util.concurrent.Executors.newCachedThreadPool();

    /**
     * agregar cookie para autenticacion
     * @param asyncResponse
     * @param request
     */
    @POST
    @Path(value = "/domBot")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Operation(
        summary = "Bot de preguntas",
        description = "Procesa preguntas y respuestas del bot.",
        requestBody = @RequestBody(
            required = true,
            content = @Content(schema = @Schema(implementation = questionsAnswersReq.class))
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Respuesta del bot", content = @Content(schema = @Schema(implementation = answerResp.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        }
    )
    public void questions(@Suspended final AsyncResponse asyncResponse, final questionsAnswersReq request,@CookieParam("session") String session) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
           /*      try {
                    questionsController controller = new questionsController();
                    getQuestionsAnswerResp response = controller.questions(request,session);
                    asyncResponse.resume(response);
                } catch (Exception e) {
                    msgError error = new msgError();
                    error.setError(e.getMessage());
                    asyncResponse.resume(error);
                } */
                asyncResponse.resume(doQuestions(request));
            }
        });
    }

    private answerResp doQuestions(questionsAnswersReq request) {

        answerResp response = new answerResp();
        questionsController ctrl = new questionsController();
        response = ctrl.questionsBot(request);
        return response;
    }

    /**
     *
     * @param asyncResponse
     * @param request
     */
    @POST
    @Path(value = "/surveyBot")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Operation(
        summary = "Bot de encuestas",
        description = "Procesa encuestas del bot.",
        requestBody = @RequestBody(
            required = true,
            content = @Content(schema = @Schema(implementation = questionsAnswersReq.class))
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Respuesta de la encuesta", content = @Content(schema = @Schema(implementation = answerResp.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        }
    )
    public void survey(@Suspended final AsyncResponse asyncResponse, final questionsAnswersReq request) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                asyncResponse.resume(doSurvey(request));
            }
        });
    }

    private answerResp doSurvey(questionsAnswersReq request) {

        answerResp response = new answerResp();
        questionsController ctrl = new questionsController();
        response = ctrl.questionsBot(request);
        return response;
    }

    /**
     *
     * @param asyncResponse
     * @param request
     */
    @POST
    @Path(value = "/raspi")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Operation(
        summary = "Bot de preguntas para Raspberry Pi",
        description = "Procesa preguntas y respuestas del bot para dispositivos Raspberry Pi.",
        requestBody = @RequestBody(
            required = true,
            content = @Content(schema = @Schema(implementation = questionsAnswersReq.class))
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Respuesta del bot", content = @Content(schema = @Schema(implementation = answerResp.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        }
    )
    public void raspi(@Suspended final AsyncResponse asyncResponse, final questionsAnswersReq request) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                asyncResponse.resume(doRaspi(request));
            }
        });
    }

    private answerResp doRaspi(questionsAnswersReq request) {

        answerResp response = new answerResp();
        questionsController ctrl = new questionsController();
        response = ctrl.questionsBot(request);
        return response;
    }

   /**
     *
     * @param asyncResponse
     * @param request
     */
    @POST
    @Path(value = "/getProjects")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Operation(
        summary = "Obtener proyectos del usuario",
        description = "Devuelve los proyectos asociados al usuario autenticado.",
        requestBody = @RequestBody(
            required = true,
            content = @Content(schema = @Schema(implementation = createProjectsReq.class))
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de proyectos",
                content = @Content(schema = @Schema(implementation = projectsResp.class))
            ),
            @ApiResponse(
                responseCode = "401",
                description = "No autorizado"
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Error interno del servidor"
            )
        }
    )
    public void getProjects(@Suspended final AsyncResponse asyncResponse, final createProjectsReq request, @Context ContainerRequestContext requestContext) {
         FirebaseToken user = (FirebaseToken) requestContext.getProperty("user");
         
        if (user == null) {
            asyncResponse.resume(Response.status(Response.Status.UNAUTHORIZED)
                .entity("No autorizado")
                .build());
            return;
        }

        executorService.submit(() -> {
            try {
                // Agregar el ID del usuario a la petición
                request.setIdUser(user.getUid());
                asyncResponse.resume(doGetProjects(request));
            } catch (Exception e) {
                asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new msgError(-1, e.getMessage()))
                    .build());
            }
        });

    }
    private projectsResp doGetProjects(createProjectsReq request) {

        projectsResp response = new projectsResp();
        projectController ctrl = new projectController();
        response = ctrl.getProjectUser(request);
        return response;
    }

    /**
     *
     * @param asyncResponse
     * @param request
     */
    @POST
    @Path(value = "/getQuestionsAnswers")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Operation(
        summary = "Obtener preguntas y respuestas",
        description = "Devuelve las preguntas y respuestas para un proyecto.",
        requestBody = @RequestBody(
            required = true,
            content = @Content(schema = @Schema(implementation = createProjectsReq.class))
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Preguntas y respuestas", content = @Content(schema = @Schema(implementation = getQuestionsAnswerResp.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        }
    )
    public void getQuestionsAnswer(@Suspended final AsyncResponse asyncResponse, final createProjectsReq request) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                asyncResponse.resume(doGetQuestionsAnswer(request));
            }
        });
    }

    private getQuestionsAnswerResp doGetQuestionsAnswer(createProjectsReq request) {

        getQuestionsAnswerResp response = new getQuestionsAnswerResp();
        getQuestionsController ctrl = new getQuestionsController();
        response = ctrl.getQuestionAnswer(request);
        return response;
    }

    /**
     *
     * @param asyncResponse
     * @param request
     */
    @POST
    @Path(value = "/getQuestions")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Operation(
        summary = "Obtener preguntas",
        description = "Devuelve las preguntas para un proyecto.",
        requestBody = @RequestBody(
            required = true,
            content = @Content(schema = @Schema(implementation = createProjectsReq.class))
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Preguntas", content = @Content(schema = @Schema(implementation = getQuestionsResp.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        }
    )
    public void getQuestions(@Suspended final AsyncResponse asyncResponse, final createProjectsReq request) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                asyncResponse.resume(doGetQuestions(request));
            }
        });
    }

    private getQuestionsResp doGetQuestions(createProjectsReq request) {

        getQuestionsResp response = new getQuestionsResp();
        getQuestionsController ctrl = new getQuestionsController();
        response = ctrl.getQuestions(request);
        return response;
    }

    /**
     *
     * @param asyncResponse
     * @param request
     */
    @POST
    @Path(value = "/getAnswers")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Operation(
        summary = "Obtener respuestas",
        description = "Devuelve las respuestas para una pregunta de un proyecto.",
        requestBody = @RequestBody(
            required = true,
            content = @Content(schema = @Schema(implementation = answerReq.class))
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Respuestas", content = @Content(schema = @Schema(implementation = getAnswerResp.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        }
    )
    public void getAnswers(@Suspended final AsyncResponse asyncResponse, final answerReq request) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                asyncResponse.resume(doGetAnswer(request));
            }
        });
    }

    private getAnswerResp doGetAnswer(answerReq request) {

        getAnswerResp response = new getAnswerResp();
        getQuestionsController ctrl = new getQuestionsController();
        response = ctrl.getAnswers(request);
        return response;
    }

    /**
     *
     * @param asyncResponse
     * @param request
     */
    @POST
    @Path(value = "/createProject")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Operation(
        summary = "Crear proyecto",
        description = "Crea un nuevo proyecto.",
        requestBody = @RequestBody(
            required = true,
            content = @Content(schema = @Schema(implementation = projectDTO.class))
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Proyecto creado", content = @Content(schema = @Schema(implementation = projectDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        }
    )
    public void createProyect(@Suspended final AsyncResponse asyncResponse, final projectDTO request) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                asyncResponse.resume(doCreateProject(request));
            }
        });
    }

    private projectDTO doCreateProject(projectDTO request) {

        projectDTO response = new projectDTO();
        projectController ctrl = new projectController();
        response = ctrl.createProjects(request);
        return response;
    }

    /**
     *
     * @param asyncResponse
     * @param request
     */
    @POST
    @Path(value = "/createUpdateManyQuestions")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Operation(
        summary = "Crear o actualizar muchas preguntas",
        description = "Crea o actualiza varias preguntas para un proyecto.",
        requestBody = @RequestBody(
            required = true,
            content = @Content(schema = @Schema(implementation = createQuestionsReq.class))
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Preguntas procesadas", content = @Content(schema = @Schema(implementation = createQuestionsResp.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        }
    )
    public void createManyQuestions(@Suspended final AsyncResponse asyncResponse, final createQuestionsReq request) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                asyncResponse.resume(doCreateManyQuestions(request));
            }
        });
    }

    private createQuestionsResp doCreateManyQuestions(createQuestionsReq request) {

        createQuestionsResp response = new createQuestionsResp();
        createQuestionController ctrl = new createQuestionController();
        response = ctrl.createQuestions(request);
        return response;
    }
    
    /**
     *
     * @param asyncResponse
     * @param request
     */
    @POST
    @Path(value = "/createUpdateOneQuestion")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Operation(
        summary = "Crear o actualizar una pregunta",
        description = "Crea o actualiza una pregunta para un proyecto.",
        requestBody = @RequestBody(
            required = true,
            content = @Content(schema = @Schema(implementation = questionsDTO.class))
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Pregunta procesada", content = @Content(schema = @Schema(implementation = msgError.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        }
    )
    public void createOneQuestion(@Suspended final AsyncResponse asyncResponse, final questionsDTO request) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                asyncResponse.resume(doCreateOneQuestions(request));
            }
        });
    }

    private msgError doCreateOneQuestions(questionsDTO request) {

        msgError response = new msgError();
        createQuestionController ctrl = new createQuestionController();
        response = ctrl.createUpdateQuestions(request);
        return response;
    }
    
    /**
     *
     * @param asyncResponse
     * @param request
     */
    @POST
    @Path(value = "/createUpdateOneAnswer")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Operation(
        summary = "Crear o actualizar una respuesta",
        description = "Crea o actualiza una respuesta para una pregunta.",
        requestBody = @RequestBody(
            required = true,
            content = @Content(schema = @Schema(implementation = answerDTO.class))
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Respuesta procesada", content = @Content(schema = @Schema(implementation = msgError.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        }
    )
    public void createOneAnswer(@Suspended final AsyncResponse asyncResponse, final answerDTO request) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                asyncResponse.resume(doCreateOneAnswer(request));
            }
        });
    }

    private msgError doCreateOneAnswer(answerDTO request) {

        msgError response = new msgError();
        answerController ctrl = new answerController();
        response = ctrl.updateCreateAnswers(request);
        return response;
    }
    
        /**
     *
     * @param asyncResponse
     * @param request
     */
    @POST
    @Path(value = "/updateProject")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Operation(
        summary = "Actualizar proyecto",
        description = "Actualiza un proyecto existente.",
        requestBody = @RequestBody(
            required = true,
            content = @Content(schema = @Schema(implementation = projectDTO.class))
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Proyecto actualizado", content = @Content(schema = @Schema(implementation = msgError.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        }
    )
    public void updateProject (@Suspended final AsyncResponse asyncResponse, final projectDTO request) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                asyncResponse.resume(doUpdateProject(request));
            }
        });
    }

    private msgError doUpdateProject (projectDTO request) {

        msgError response = new msgError();
        projectController ctrl = new projectController();
        response = ctrl.updateProject(request);
        return response;
    }
    
    
        /**
     *
     * @param asyncResponse
     * @param request
     */
    @POST
    @Path(value = "/configDevices")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Operation(
        summary = "Obtener configuración de dispositivos",
        description = "Obtiene la configuración de dispositivos para un proyecto.",
        requestBody = @RequestBody(
            required = true,
            content = @Content(schema = @Schema(implementation = raspiReq.class))
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Configuración de dispositivos", content = @Content(schema = @Schema(implementation = raspiResp.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        }
    )
    public void configDevices (@Suspended final AsyncResponse asyncResponse, final raspiReq request) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                asyncResponse.resume(doConfigDevices(request));
            }
        });
    }

    private raspiResp doConfigDevices (raspiReq request) {
        raspiResp response = new raspiResp();
        devicesConfigController ctrl = new devicesConfigController();
        response = ctrl.getConfigDevice(request);
        return response;
    }
    
        /**
     *
     * @param asyncResponse
     * @param request
     */
    @POST
    @Path(value = "/createConfigDevices")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Operation(
        summary = "Crear configuración de dispositivos",
        description = "Crea la configuración de dispositivos para un proyecto.",
        requestBody = @RequestBody(
            required = true,
            content = @Content(schema = @Schema(implementation = raspiDTO.class))
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Configuración creada", content = @Content(schema = @Schema(implementation = msgError.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        }
    )
    public void createConfigDevices (@Suspended final AsyncResponse asyncResponse, final raspiDTO request) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                asyncResponse.resume(doCreateConfigDevices(request));
            }
        });
    }

    private msgError doCreateConfigDevices(raspiDTO request) {

        msgError response = new msgError();
        devicesConfigController ctrl = new devicesConfigController();
        response = ctrl.createConfigDevices(request);
        return response;
    }

    /**
     * Endpoint de login que utiliza la información del token de Firebase
     */
    @POST
    @Path(value = "/login")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Operation(
        summary = "Login de usuario con Firebase",
        description = "Autentica un usuario usando el token de Firebase y retorna información y perfiles.",
        requestBody = @RequestBody(
            required = false,
            description = "No se requiere body, solo el header Authorization: Bearer <firebase-token>"
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Login exitoso",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = LoginResponse.class),
                    examples = @ExampleObject(
                        value = "{\n  \"isNewUser\": true,\n  \"created\": true,\n  \"profiles\": [{\n    \"id\": 3,\n    \"name\": \"Cliente Internet\",\n    \"description\": \"Cliente de servicios de internet\",\n    \"active\": true\n  }]\n}"
                    )
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "No autorizado"
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Error interno del servidor"
            )
        }
    )
    public void login(@Suspended final AsyncResponse asyncResponse, @Context ContainerRequestContext requestContext) {
        FirebaseToken decodedToken = (FirebaseToken) requestContext.getProperty("user");
        
        if (decodedToken == null) {
            asyncResponse.resume(Response.status(Response.Status.UNAUTHORIZED)
                .entity("No autorizado")
                .build());
            return;
        }

        executorService.submit(() -> {
            try {
                asyncResponse.resume(doLogin(decodedToken));
            } catch (Exception e) {
                asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new msgError(-1, e.getMessage()))
                    .build());
            }
        });
    }

    private LoginResponse doLogin(FirebaseToken decodedToken) {
        LoginResponse response = new LoginResponse();
        UserDTO userDTO = new UserDTO();
        
        // Llenar el DTO con la información del token
        userDTO.setId(decodedToken.getUid());
        userDTO.setEmail(decodedToken.getEmail());
        userDTO.setDisplayName(decodedToken.getName());
        userDTO.setPhotoUrl(decodedToken.getPicture());
        userDTO.setEmailVerified(decodedToken.isEmailVerified());
        userDTO.setProviderId(decodedToken.getIssuer());
        userDTO.setCreationTime(new Timestamp(System.currentTimeMillis()));
        userDTO.setLastSignInTime(new Timestamp(System.currentTimeMillis()));
        
        UserDAO userDAO = new UserDAO();
        ProfileDAO profileDAO = new ProfileDAO();
        boolean exists = userDAO.exists(userDTO.getId());
        
        if (!exists) {
            boolean created = userDAO.create(userDTO);
            response.setNewUser(true);
            response.setCreated(created);
            
            // Si es un nuevo usuario, asignar perfil por defecto (por ejemplo, cliente)
            if (created) {
                profileDAO.assignProfileToUser(userDTO.getId(), 3); // ID 3 = Cliente por defecto
            }
        } else {
            response.setNewUser(false);
        }
        
        // Obtener los perfiles del usuario
        List<ProfileDTO> userProfiles = profileDAO.getUserProfiles(userDTO.getId());
        response.setProfiles(userProfiles);
        
        return response;
    }

    /**
     * Endpoint para asignar perfiles a usuarios (solo administradores)
     */
    @POST
    @Path(value = "/assignProfile")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Operation(
        summary = "Asignar perfil a usuario",
        description = "Permite a un administrador asignar un perfil a un usuario.",
        requestBody = @RequestBody(
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AssignProfileRequest.class),
                examples = @ExampleObject(
                    value = "{\n  \"userId\": \"uid123\",\n  \"profileId\": 1\n}"
                )
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Perfil asignado correctamente",
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n  \"code\": 0,\n  \"message\": \"Perfil asignado correctamente\"\n}")
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Solicitud inválida",
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n  \"code\": -1,\n  \"message\": \"No se pudo asignar el perfil\"\n}")
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "No autorizado"
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Acceso denegado"
            )
        }
    )
    public void assignProfile(@Suspended final AsyncResponse asyncResponse, 
                            final AssignProfileRequest request,
                            @Context ContainerRequestContext requestContext) {
        FirebaseToken decodedToken = (FirebaseToken) requestContext.getProperty("user");
        
        if (decodedToken == null) {
            asyncResponse.resume(Response.status(Response.Status.UNAUTHORIZED)
                .entity("No autorizado")
                .build());
            return;
        }

        executorService.submit(() -> {
            try {
                asyncResponse.resume(doAssignProfile(request, decodedToken.getUid()));
            } catch (Exception e) {
                asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new msgError(-1, e.getMessage()))
                    .build());
            }
        });
    }

    private Response doAssignProfile(AssignProfileRequest request, String adminUserId) {
        ProfileDAO profileDAO = new ProfileDAO();
        
        // Verificar si el usuario que hace la petición es administrador
        if (!profileDAO.isUserAdmin(adminUserId)) {
            return Response.status(Response.Status.FORBIDDEN)
                .entity(new msgError(-1, "Solo los administradores pueden asignar perfiles"))
                .build();
        }
        
        // Intentar asignar el perfil
        boolean success = profileDAO.assignProfileToUser(request.getUserId(), request.getProfileId());
        
        if (success) {
            return Response.ok(new msgError(0, "Perfil asignado correctamente")).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new msgError(-1, "No se pudo asignar el perfil"))
                .build();
        }
    }

    /**
     * Endpoint para consultar usuarios registrados (solo administradores)
     */
    @GET
    @Path(value = "/users")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Operation(
        summary = "Obtener usuarios registrados",
        description = "Permite a un administrador consultar todos los usuarios registrados.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de usuarios",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserDTO.class),
                    examples = @ExampleObject(
                        value = "[{\n  \"id\": \"uid123\",\n  \"email\": \"user@mail.com\",\n  \"displayName\": \"Juan Perez\",\n  \"photoUrl\": null,\n  \"emailVerified\": true,\n  \"disabled\": false\n}]"
                    )
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "No autorizado"
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Acceso denegado"
            )
        }
    )
    public void getUsers(@Suspended final AsyncResponse asyncResponse,
                        @Context ContainerRequestContext requestContext) {
        FirebaseToken decodedToken = (FirebaseToken) requestContext.getProperty("user");
        
        if (decodedToken == null) {
            asyncResponse.resume(Response.status(Response.Status.UNAUTHORIZED)
                .entity("No autorizado")
                .build());
            return;
        }

        executorService.submit(() -> {
            try {
                asyncResponse.resume(doGetUsers(decodedToken.getUid()));
            } catch (Exception e) {
                asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new msgError(-1, e.getMessage()))
                    .build());
            }
        });
    }

    private Response doGetUsers(String userId) {
        ProfileDAO profileDAO = new ProfileDAO();
        UserDAO userDAO = new UserDAO();
        List<ProfileDTO> profiles = profileDAO.getUserProfiles(userId);
        boolean isAdmin = false;
        String empresaDesc = null;
        for (ProfileDTO profile : profiles) {
            if ("Administrador".equalsIgnoreCase(profile.getName())) {
                if ("Administrador".equalsIgnoreCase(profile.getDescription())) {
                    // Admin global: puede ver todos los usuarios
                    isAdmin = true;
                    break;
                } else {
                    // Admin de empresa: solo usuarios de su empresa
                    empresaDesc = profile.getDescription();
                    isAdmin = true;
                    break;
                }
            }
        }
        if (!isAdmin) {
            return Response.status(Response.Status.FORBIDDEN)
                .entity(new msgError(-1, "Solo los administradores pueden consultar usuarios"))
                .build();
        }
        if (empresaDesc != null && !"Administrador".equalsIgnoreCase(empresaDesc)) {
            // Buscar la empresa por nombre (descripción) y obtener su id
            // Suponiendo que el nombre de la empresa es único y está en la tabla empresa
            EmpresaDAO empresaDAO = new EmpresaDAO();
            List<EmpresaDTO> empresas = empresaDAO.readAll();
            Integer empresaId = null;
            for (EmpresaDTO empresa : empresas) {
                if (Integer.valueOf(empresaDesc).equals(empresa.getId())) {
                    empresaId = empresa.getId();
                    break;
                }
            }
            if (empresaId == null) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity(new msgError(-1, "No se encontró la empresa asociada al perfil"))
                    .build();
            }
            List<UserDTO> users = userDAO.readAllByEmpresaId(empresaId);
            return Response.ok(users).build();
        } else {
            // Admin global
            List<UserDTO> users = userDAO.readAll();
            return Response.ok(users).build();
        }
    }

    /**
     * Crear una empresa
     */
    @POST
    @Path(value = "/createEmpresa")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Operation(
        summary = "Crear empresa",
        description = "Crea una nueva empresa.",
        requestBody = @RequestBody(
            required = true,
            content = @Content(schema = @Schema(implementation = EmpresaDTO.class))
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Empresa creada", content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        }
    )
    public void createEmpresa(@Suspended final AsyncResponse asyncResponse, final EmpresaDTO request) {
        executorService.submit(() -> {
            EmpresaDAO dao = new EmpresaDAO();
            boolean created = dao.create(request);
            asyncResponse.resume(created);
        });
    }

    /**
     * Actualizar una empresa
     */
    @POST
    @Path(value = "/updateEmpresa")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Operation(
        summary = "Actualizar empresa",
        description = "Actualiza una empresa existente.",
        requestBody = @RequestBody(
            required = true,
            content = @Content(schema = @Schema(implementation = EmpresaDTO.class))
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Empresa actualizada", content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        }
    )
    public void updateEmpresa(@Suspended final AsyncResponse asyncResponse, final EmpresaDTO request) {
        executorService.submit(() -> {
            EmpresaDAO dao = new EmpresaDAO();
            boolean updated = dao.update(request);
            asyncResponse.resume(updated);
        });
    }

    /**
     * Eliminar una empresa
     */
    @POST
    @Path(value = "/deleteEmpresa")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Operation(
        summary = "Eliminar empresa",
        description = "Elimina una empresa existente.",
        requestBody = @RequestBody(
            required = true,
            content = @Content(schema = @Schema(implementation = EmpresaDTO.class))
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Empresa eliminada", content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        }
    )
    public void deleteEmpresa(@Suspended final AsyncResponse asyncResponse, final EmpresaDTO request) {
        executorService.submit(() -> {
            EmpresaDAO dao = new EmpresaDAO();
            boolean deleted = dao.delete(request);
            asyncResponse.resume(deleted);
        });
    }

    /**
     * Obtener usuarios por empresa
     */
    @GET
    @Path("/usersByEmpresa/{empresaId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Operation(
        summary = "Obtener usuarios por empresa",
        description = "Devuelve los usuarios asociados a una empresa específica.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de usuarios",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserDTO.class)
                )
            ),
            @ApiResponse(responseCode = "404", description = "Empresa no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        }
    )
    public void getUsersByEmpresa(@Suspended final AsyncResponse asyncResponse, @javax.ws.rs.PathParam("empresaId") Integer empresaId) {
        executorService.submit(() -> {
            UserDAO userDAO = new UserDAO();
            List<UserDTO> users = userDAO.readAllByEmpresaId(empresaId);
            asyncResponse.resume(users);
        });
    }

    /**
     * Crear un usuario (solo administradores y técnicos)
     */
    @POST
    @Path(value = "/createUser")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Operation(
        summary = "Crear usuario",
        description = "Permite crear un usuario según reglas de perfil y empresa.",
        requestBody = @RequestBody(
            required = true,
            content = @Content(schema = @Schema(implementation = UserDTO.class))
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Usuario creado", content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para crear usuarios"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o empresa no encontrada")
        }
    )
    public void createUser(@Suspended final AsyncResponse asyncResponse, final UserDTO request, @Context ContainerRequestContext requestContext) {
        FirebaseToken decodedToken = (FirebaseToken) requestContext.getProperty("user");
        if (decodedToken == null) {
            asyncResponse.resume(Response.status(Response.Status.UNAUTHORIZED)
                .entity("No autorizado")
                .build());
            return;
        }
        // Validar tipo y número de identificación
        if (request.getTipoIdentificacion() == null || request.getTipoIdentificacion().trim().isEmpty() ||
            request.getNumeroIdentificacion() == null || request.getNumeroIdentificacion().trim().isEmpty()) {
            asyncResponse.resume(Response.status(Response.Status.BAD_REQUEST)
                .entity(new msgError(-1, "El tipo y número de identificación son obligatorios"))
                .build());
            return;
        }
        // Validar formato E.164 para phoneNumber si viene presente
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
            String phone = request.getPhoneNumber();
            if (!phone.matches("^\\+[1-9]\\d{1,14}$")) {
                asyncResponse.resume(Response.status(Response.Status.BAD_REQUEST)
                    .entity(new msgError(-1, "El número de teléfono debe estar en formato internacional E.164, por ejemplo: +573001234567"))
                    .build());
                return;
            }
        }
        executorService.submit(() -> {
            // Asignar el usuario que crea
            request.setCreatedBy(decodedToken.getUid());
            asyncResponse.resume(doCreateUser(decodedToken.getUid(), request));
        });
    }

    private Response doCreateUser(String userId, UserDTO newUser) {
        ProfileDAO profileDAO = new ProfileDAO();
        EmpresaDAO empresaDAO = new EmpresaDAO();
        UserDAO userDAO = new UserDAO();
        List<ProfileDTO> profiles = profileDAO.getUserProfiles(userId);
        boolean isAdmin = false;
        boolean isTecnico = false;
        String empresaDesc = null;
        boolean adminGlobal = false;
        for (ProfileDTO profile : profiles) {
            if ("Administrador".equalsIgnoreCase(profile.getName())) {
                isAdmin = true;
                if ("Administrador".equalsIgnoreCase(profile.getDescription())) {
                    adminGlobal = true;
                } else {
                    empresaDesc = profile.getDescription();
                }
                break;
            } else if ("Tecnico".equalsIgnoreCase(profile.getName())) {
                isTecnico = true;
                empresaDesc = profile.getDescription();
                break;
            }
        }
        if (!isAdmin && !isTecnico) {
            return Response.status(Response.Status.FORBIDDEN)
                .entity(new msgError(-1, "No tiene permisos para crear usuarios"))
                .build();
        }
        Integer empresaId = null;
        if (adminGlobal) {
            // Debe venir la empresa en el request
            empresaId = newUser.getEmpresaId();
            if (empresaId == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new msgError(-1, "Debe especificar la empresa para el usuario"))
                    .build();
            }
        } else {
            // Buscar la empresa por nombre (de la descripción del perfil)
            List<EmpresaDTO> empresas = empresaDAO.readAll();
            for (EmpresaDTO empresa : empresas) {
                if (empresaDesc != null && empresaDesc.equalsIgnoreCase(empresa.getNombre())) {
                    empresaId = empresa.getId();
                    break;
                }
            }
            if (empresaId == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new msgError(-1, "No se encontró la empresa asociada al perfil"))
                    .build();
            }
        }
        newUser.setEmpresaId(empresaId);

        // Crear usuario en Firebase primero
        try {
            CreateRequest fbRequest = new CreateRequest()
                .setEmail(newUser.getEmail())
                .setPassword(newUser.getNumeroIdentificacion())
                .setDisplayName(newUser.getDisplayName());
            if (newUser.getPhoneNumber() != null && !newUser.getPhoneNumber().isEmpty()) {
                fbRequest.setPhoneNumber(newUser.getPhoneNumber());
            }
            UserRecord userRecord = FirebaseAuth.getInstance().createUser(fbRequest);
            // Guardar el UID generado por Firebase en el DTO
            newUser.setId(userRecord.getUid());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new msgError(-1, "Error creando usuario en Firebase: " + e.getMessage()))
                .build();
        }

        // Ahora guardar en la base de datos
        boolean created = userDAO.create(newUser);
        return Response.ok(created).build();
    }

    /**
     * Actualizar un usuario (solo administradores y técnicos)
     */
    @POST
    @Path(value = "/updateUser")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Operation(
        summary = "Actualizar usuario",
        description = "Permite actualizar un usuario según reglas de perfil y empresa.",
        requestBody = @RequestBody(
            required = true,
            content = @Content(schema = @Schema(implementation = UserDTO.class))
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado", content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para actualizar usuarios"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o empresa no encontrada")
        }
    )
    public void updateUser(@Suspended final AsyncResponse asyncResponse, final UserDTO request, @Context ContainerRequestContext requestContext) {
        FirebaseToken decodedToken = (FirebaseToken) requestContext.getProperty("user");
        if (decodedToken == null) {
            asyncResponse.resume(Response.status(Response.Status.UNAUTHORIZED)
                .entity("No autorizado")
                .build());
            return;
        }
        executorService.submit(() -> {
            // Asignar el usuario que actualiza (opcional, si quieres registrar auditoría)
            request.setCreatedBy(decodedToken.getUid());
            asyncResponse.resume(doUpdateUser(decodedToken.getUid(), request));
        });
    }

    private Response doUpdateUser(String userId, UserDTO userToUpdate) {
        ProfileDAO profileDAO = new ProfileDAO();
        EmpresaDAO empresaDAO = new EmpresaDAO();
        UserDAO userDAO = new UserDAO();
        List<ProfileDTO> profiles = profileDAO.getUserProfiles(userId);
        boolean isAdmin = false;
        boolean isTecnico = false;
        String empresaDesc = null;
        boolean adminGlobal = false;
        for (ProfileDTO profile : profiles) {
            if ("Administrador".equalsIgnoreCase(profile.getName())) {
                isAdmin = true;
                if ("Administrador".equalsIgnoreCase(profile.getDescription())) {
                    adminGlobal = true;
                } else {
                    empresaDesc = profile.getDescription();
                }
                break;
            } else if ("Tecnico".equalsIgnoreCase(profile.getName())) {
                isTecnico = true;
                empresaDesc = profile.getDescription();
                break;
            }
        }
        if (!isAdmin && !isTecnico) {
            return Response.status(Response.Status.FORBIDDEN)
                .entity(new msgError(-1, "No tiene permisos para actualizar usuarios"))
                .build();
        }
        Integer empresaId = null;
        if (adminGlobal) {
            // Puede actualizar cualquier usuario, la empresa puede venir en el request
            empresaId = userToUpdate.getEmpresaId();
            if (empresaId == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new msgError(-1, "Debe especificar la empresa para el usuario"))
                    .build();
            }
        } else {
            // Buscar la empresa por nombre (de la descripción del perfil)
            List<EmpresaDTO> empresas = empresaDAO.readAll();
            for (EmpresaDTO empresa : empresas) {
                if (empresaDesc != null && empresaDesc.equalsIgnoreCase(empresa.getNombre())) {
                    empresaId = empresa.getId();
                    break;
                }
            }
            if (empresaId == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new msgError(-1, "No se encontró la empresa asociada al perfil"))
                    .build();
            }
            // Solo puede actualizar usuarios de su empresa
            if (!empresaId.equals(userToUpdate.getEmpresaId())) {
                return Response.status(Response.Status.FORBIDDEN)
                    .entity(new msgError(-1, "No puede actualizar usuarios de otra empresa"))
                    .build();
            }
        }
        userToUpdate.setEmpresaId(empresaId);
        boolean updated = userDAO.update(userToUpdate);
        return Response.ok(updated).build();
    }

    /**
     * Endpoint para consultar perfiles activos (solo administradores)
     */
    @GET
    @Path("/activeProfiles")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Operation(
        summary = "Obtener perfiles activos",
        description = "Permite a un usuario con perfil Administrador consultar los perfiles activos. Si el perfil es Administrador y la descripción es 'Administrador', retorna todos los perfiles activos. Si la descripción es diferente, retorna solo los perfiles activos con la misma descripción.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de perfiles activos",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProfileDTO.class),
                    examples = @ExampleObject(
                        value = "[{\n  \"id\": 1,\n  \"name\": \"Administrador\",\n  \"description\": \"Administrador\",\n  \"active\": true\n}]"
                    )
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "No autorizado"
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Acceso denegado"
            )
        }
    )
    public void getActiveProfiles(@Suspended final AsyncResponse asyncResponse, @Context ContainerRequestContext requestContext) {
        FirebaseToken decodedToken = (FirebaseToken) requestContext.getProperty("user");
        if (decodedToken == null) {
            asyncResponse.resume(Response.status(Response.Status.UNAUTHORIZED)
                .entity("No autorizado")
                .build());
            return;
        }
        executorService.submit(() -> {
            asyncResponse.resume(doGetActiveProfiles(decodedToken.getUid()));
        });
    }

    private Response doGetActiveProfiles(String userId) {
        ProfileDAO profileDAO = new ProfileDAO();
        List<ProfileDTO> userProfiles = profileDAO.getUserProfiles(userId);
        boolean isAdmin = false;
        String adminDesc = null;
        for (ProfileDTO profile : userProfiles) {
            if ("Administrador".equalsIgnoreCase(profile.getName())) {
                isAdmin = true;
                adminDesc = profile.getDescription();
                break;
            }
        }
        if (!isAdmin) {
            return Response.status(Response.Status.FORBIDDEN)
                .entity(new msgError(-1, "Solo los administradores pueden consultar perfiles"))
                .build();
        }
        List<ProfileDTO> result;
        if ("Administrador".equalsIgnoreCase(adminDesc)) {
            result = profileDAO.getAllActiveProfiles();
        } else {
            result = profileDAO.getActiveProfilesByDescription(adminDesc);
        }
        return Response.ok(result).build();
    }

    /**
     * Endpoint para consultar los dispositivos asociados a un usuario registrado
     */
    @GET
    @Path("/user/devices")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Consultar dispositivos de usuario",
        description = "Devuelve la estructura de dispositivos Raspberry asociados al usuario autenticado.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Estructura de dispositivos",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RaspberryNewDTO.class)
                )
            ),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "No se encontraron dispositivos asociados")
        }
    )
    public void getUserDevices(@Suspended final AsyncResponse asyncResponse, @Context ContainerRequestContext requestContext) {
        FirebaseToken decodedToken = (FirebaseToken) requestContext.getProperty("user");
        if (decodedToken == null) {
            asyncResponse.resume(Response.status(Response.Status.UNAUTHORIZED)
                .entity("No autorizado")
                .build());
            return;
        }
        executorService.submit(() -> {
            try (Connection conn = DriverManager.getConnection("jdbc:tu_url_bd", "usuario", "password")) {
                RaspberryNewDAO dao = new RaspberryNewDAO(conn);
                RaspberryNewDTO dto = dao.getRaspberryByUserId(decodedToken.getUid());
                if (dto == null) {
                    asyncResponse.resume(Response.status(Response.Status.NOT_FOUND)
                        .entity("No se encontraron dispositivos asociados")
                        .build());
                } else {
                    asyncResponse.resume(Response.ok(dto).build());
                }
            } catch (Exception e) {
                asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build());
            }
        });
    }

    /**
     * Endpoint para consultar dispositivos por número de celular
     */
    @GET
    @Path("/user/devices/by-number/{number}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Consultar dispositivos por número de celular",
        description = "Devuelve la estructura de dispositivos Raspberry asociados al número de celular.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Estructura de dispositivos",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RaspberryNewDTO.class)
                )
            ),
            @ApiResponse(responseCode = "404", description = "No se encontraron dispositivos asociados")
        }
    )
    public void getUserDevicesByNumber(@Suspended final AsyncResponse asyncResponse, @javax.ws.rs.PathParam("number") String number) {
        executorService.submit(() -> {
            try (Connection conn = DriverManager.getConnection("jdbc:tu_url_bd", "usuario", "password")) {
                RaspberryNewDAO dao = new RaspberryNewDAO(conn);
                RaspberryNewDTO dto = dao.getRaspberryByUserNumber(number);
                if (dto == null) {
                    asyncResponse.resume(Response.status(Response.Status.NOT_FOUND)
                        .entity("No se encontraron dispositivos asociados")
                        .build());
                } else {
                    asyncResponse.resume(Response.ok(dto).build());
                }
            } catch (Exception e) {
                asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build());
            }
        });
    }

    /**
     * Endpoint para crear o actualizar dispositivos y asociaciones
     */
    @POST
    @Path("/user/devices")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Crear o actualizar dispositivos y asociaciones",
        description = "Crea o actualiza la configuración de una Raspberry y asocia usuarios por userId y/o número de celular.",
        requestBody = @RequestBody(
            required = true,
            content = @Content(schema = @Schema(implementation = RaspberryNewDTO.class))
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Configuración guardada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        }
    )
    public void createOrUpdateUserDevices(@Suspended final AsyncResponse asyncResponse, final RaspberryNewDTO request) {
        executorService.submit(() -> {
            try (Connection conn = DriverManager.getConnection("jdbc:tu_url_bd", "usuario", "password")) {
                RaspberryNewDAO dao = new RaspberryNewDAO(conn);
                boolean ok = dao.createOrUpdateRaspberryWithUsers(request);
                if (ok) {
                    asyncResponse.resume(Response.ok("Configuración guardada correctamente").build());
                } else {
                    asyncResponse.resume(Response.status(Response.Status.BAD_REQUEST)
                        .entity("No se pudo guardar la configuración")
                        .build());
                }
            } catch (Exception e) {
                asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build());
            }
        });
    }
}
