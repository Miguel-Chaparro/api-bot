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
import com.dom.ws.rest.bot.DAO.projectsDAO;
import com.dom.ws.rest.bot.DAO.ContratoDAO;
import com.dom.ws.rest.bot.DAO.InventoryMovementDAO;
import com.dom.ws.rest.bot.DAO.InventarioDAO;
import com.dom.ws.rest.bot.DAO.ContratoServicioDetalleDAO;
import com.dom.ws.rest.bot.DAO.InventarioStockDAO;
import com.dom.ws.rest.bot.DAO.MovimientosStockDAO;
import com.dom.ws.rest.bot.DTO.ProfileDTO;
import com.dom.ws.rest.bot.DTO.UserDTO;
import com.dom.ws.rest.bot.DTO.ContratoDTO;
import com.dom.ws.rest.bot.DTO.InventoryMovementDTO;
import com.dom.ws.rest.bot.DTO.InventoryRequestDTO;
import com.dom.ws.rest.bot.DTO.ContratoServicioDetalleDTO;
import com.dom.ws.rest.bot.DTO.InventarioStockDTO;
import com.dom.ws.rest.bot.DTO.MovimientosStockDTO;
import com.dom.ws.rest.bot.DTO.answerDTO;
import com.dom.ws.rest.bot.DTO.projectDTO;
import com.dom.ws.rest.bot.DTO.questionsDTO;
import com.dom.ws.rest.bot.DTO.raspiDTO;
import com.dom.ws.rest.bot.DTO.EmpresaDTO;
import com.dom.ws.rest.bot.DTO.RaspberryNewDTO;
import com.dom.ws.rest.bot.DTO.RaspberryDTO;
import com.dom.ws.rest.bot.DTO.RaspberryUserRelationDTO;
import com.dom.ws.rest.bot.Helpers.MqttHelper;
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

import java.util.ArrayList;
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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

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
     * 
     * @param asyncResponse
     * @param request
     */
    @POST
    @Path(value = "/domBot")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Operation(summary = "Bot de preguntas", description = "Procesa preguntas y respuestas del bot.", requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = questionsAnswersReq.class))), responses = {
            @ApiResponse(responseCode = "200", description = "Respuesta del bot", content = @Content(schema = @Schema(implementation = answerResp.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public void questions(@Suspended final AsyncResponse asyncResponse, final questionsAnswersReq request,
            @CookieParam("session") String session) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                /*
                 * try {
                 * questionsController controller = new questionsController();
                 * getQuestionsAnswerResp response = controller.questions(request,session);
                 * asyncResponse.resume(response);
                 * } catch (Exception e) {
                 * msgError error = new msgError();
                 * error.setError(e.getMessage());
                 * asyncResponse.resume(error);
                 * }
                 */
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
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Operation(summary = "Bot de encuestas", description = "Procesa encuestas del bot.", requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = questionsAnswersReq.class))), responses = {
            @ApiResponse(responseCode = "200", description = "Respuesta de la encuesta", content = @Content(schema = @Schema(implementation = answerResp.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Operation(summary = "Bot de preguntas para Raspberry Pi", description = "Procesa preguntas y respuestas del bot para dispositivos Raspberry Pi.", requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = questionsAnswersReq.class))), responses = {
            @ApiResponse(responseCode = "200", description = "Respuesta del bot", content = @Content(schema = @Schema(implementation = answerResp.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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
     * @param requestContext
     */
    @GET
    @Path(value = "/getProjects")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Operation(summary = "Obtener proyectos del usuario", description = "Devuelve los proyectos asociados al usuario autenticado.", responses = {
            @ApiResponse(responseCode = "200", description = "Lista de proyectos", content = @Content(schema = @Schema(implementation = projectsResp.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public void getProjects(@Suspended final AsyncResponse asyncResponse,
            @Context ContainerRequestContext requestContext) {
        FirebaseToken user = (FirebaseToken) requestContext.getProperty("user");
        if (user == null) {
            asyncResponse.resume(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("No autorizado")
                    .build());
            return;
        }
        executorService.submit(() -> {
            try {
                ProfileDAO profileDAO = new ProfileDAO();
                List<ProfileDTO> profiles = profileDAO.getUserProfiles(user.getUid());
                boolean isAdmin = false;
                boolean isAdminGlobal = false;
                for (ProfileDTO profile : profiles) {
                    if ("Administrador".equalsIgnoreCase(profile.getName())) {
                        isAdmin = true;
                        if ("Administrador".equalsIgnoreCase(profile.getDescription())) {
                            isAdminGlobal = true;
                            break;
                        }
                    }
                }
                projectsResp resp;
                projectController ctrl = new projectController();
                if (isAdminGlobal) {
                    resp = ctrl.getAllProjects();
                } else if (isAdmin) {
                    // Solo proyectos creados por el usuario
                    createProjectsReq req = new createProjectsReq();
                    req.setIdUser(user.getUid());
                    resp = ctrl.getProjectUser(req);
                } else {
                    createProjectsReq req = new createProjectsReq();
                    req.setIdUser(user.getUid());
                    resp = ctrl.getProjectUser(req);
                }
                asyncResponse.resume(resp);
            } catch (Exception e) {
                asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(new msgError(-1, e.getMessage()))
                        .build());
            }
        });
    }

    /**
     *
     * @param asyncResponse
     * @param request
     */
    @POST
    @Path(value = "/getQuestionsAnswers")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Operation(summary = "Obtener preguntas y respuestas", description = "Devuelve las preguntas y respuestas para un proyecto.", requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = createProjectsReq.class))), responses = {
            @ApiResponse(responseCode = "200", description = "Preguntas y respuestas", content = @Content(schema = @Schema(implementation = getQuestionsAnswerResp.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Operation(summary = "Obtener preguntas", description = "Devuelve las preguntas para un proyecto.", requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = createProjectsReq.class))), responses = {
            @ApiResponse(responseCode = "200", description = "Preguntas", content = @Content(schema = @Schema(implementation = getQuestionsResp.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Operation(summary = "Obtener respuestas", description = "Devuelve las respuestas para una pregunta de un proyecto.", requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = answerReq.class))), responses = {
            @ApiResponse(responseCode = "200", description = "Respuestas", content = @Content(schema = @Schema(implementation = getAnswerResp.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Operation(summary = "Crear proyecto", description = "Crea un nuevo proyecto.", requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = projectDTO.class))), responses = {
            @ApiResponse(responseCode = "200", description = "Proyecto creado", content = @Content(schema = @Schema(implementation = projectDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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
        request.setUser(null); // O asigna el usuario admin si aplica
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
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Operation(summary = "Crear o actualizar muchas preguntas", description = "Crea o actualiza varias preguntas para un proyecto.", requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = createQuestionsReq.class))), responses = {
            @ApiResponse(responseCode = "200", description = "Preguntas procesadas", content = @Content(schema = @Schema(implementation = createQuestionsResp.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Operation(summary = "Crear o actualizar una pregunta", description = "Crea o actualiza una pregunta para un proyecto.", requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = questionsDTO.class))), responses = {
            @ApiResponse(responseCode = "200", description = "Pregunta procesada", content = @Content(schema = @Schema(implementation = msgError.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Operation(summary = "Crear o actualizar una respuesta", description = "Crea o actualiza una respuesta para una pregunta.", requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = answerDTO.class))), responses = {
            @ApiResponse(responseCode = "200", description = "Respuesta procesada", content = @Content(schema = @Schema(implementation = msgError.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Operation(summary = "Actualizar proyecto", description = "Actualiza un proyecto existente.", requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = projectDTO.class))), responses = {
            @ApiResponse(responseCode = "200", description = "Proyecto actualizado", content = @Content(schema = @Schema(implementation = msgError.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public void updateProject(@Suspended final AsyncResponse asyncResponse, final projectDTO request) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                asyncResponse.resume(doUpdateProject(request));
            }
        });
    }

    private msgError doUpdateProject(projectDTO request) {

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
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Operation(summary = "Obtener configuración de dispositivos", description = "Obtiene la configuración de dispositivos para un proyecto.", requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = raspiReq.class))), responses = {
            @ApiResponse(responseCode = "200", description = "Configuración de dispositivos", content = @Content(schema = @Schema(implementation = raspiResp.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public void configDevices(@Suspended final AsyncResponse asyncResponse, final raspiReq request) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                asyncResponse.resume(doConfigDevices(request));
            }
        });
    }

    private raspiResp doConfigDevices(raspiReq request) {
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
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Operation(summary = "Crear configuración de dispositivos", description = "Crea la configuración de dispositivos para un proyecto.", requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = raspiDTO.class))), responses = {
            @ApiResponse(responseCode = "200", description = "Configuración creada", content = @Content(schema = @Schema(implementation = msgError.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public void createConfigDevices(@Suspended final AsyncResponse asyncResponse, final raspiDTO request) {
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
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Operation(summary = "Login de usuario con Firebase", description = "Autentica un usuario usando el token de Firebase y retorna información y perfiles.", requestBody = @RequestBody(required = false, description = "No se requiere body, solo el header Authorization: Bearer <firebase-token>"), responses = {
            @ApiResponse(responseCode = "200", description = "Login exitoso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class), examples = @ExampleObject(value = "{\n  \"isNewUser\": true,\n  \"created\": true,\n  \"profiles\": [{\n    \"id\": 3,\n    \"name\": \"Cliente Internet\",\n    \"description\": \"Cliente de servicios de internet\",\n    \"active\": true\n  }]\n}"))),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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
        UserDAO userDAO = new UserDAO();
        ProfileDAO profileDAO = new ProfileDAO();
        UserDTO userDTO = userDAO.readOneById(decodedToken.getUid());
        boolean exists = userDTO != null && userDTO.getId() != null;
        if (exists && userDTO != null) {
            // Refrescar datos del usuario con la información más reciente del token
            userDTO.setDisplayName(decodedToken.getName());
            userDTO.setPhotoUrl(decodedToken.getPicture());
            userDTO.setEmail(decodedToken.getEmail());
            userDTO.setEmailVerified(decodedToken.isEmailVerified());
            userDTO.setLastSignInTime(new java.sql.Timestamp(System.currentTimeMillis()));
            userDAO.update(userDTO);
            response.setNewUser(false);
        } else {
            // Si no existe, crear usuario nuevo
            userDTO = new UserDTO();
            userDTO.setId(decodedToken.getUid());
            userDTO.setEmail(decodedToken.getEmail());
            userDTO.setDisplayName(decodedToken.getName());
            userDTO.setPhotoUrl(decodedToken.getPicture());
            userDTO.setEmailVerified(decodedToken.isEmailVerified());
            userDTO.setProviderId(decodedToken.getIssuer());
            userDTO.setCreationTime(new java.sql.Timestamp(System.currentTimeMillis()));
            userDTO.setLastSignInTime(new java.sql.Timestamp(System.currentTimeMillis()));
            userDAO.create(userDTO);
            response.setNewUser(true);
            response.setCreated(true);
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
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Operation(summary = "Asignar perfiles a usuario", description = "Permite a un administrador asignar una lista de perfiles a un usuario. Elimina las relaciones previas y asigna los nuevos perfiles.", requestBody = @RequestBody(required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = AssignProfileRequest.class), examples = @ExampleObject(value = "{\n  \"userId\": \"uid123\",\n  \"profileIds\": [1,2,3]\n}"))), responses = {
            @ApiResponse(responseCode = "200", description = "Perfiles asignados correctamente", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n  \"code\": 0,\n  \"message\": \"Perfiles asignados correctamente\"\n}"))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n  \"code\": -1,\n  \"message\": \"No se pudieron asignar los perfiles\"\n}"))),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
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
                asyncResponse.resume(doAssignProfiles(request, decodedToken.getUid()));
            } catch (Exception e) {
                asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(new msgError(-1, e.getMessage()))
                        .build());
            }
        });
    }

    private Response doAssignProfiles(AssignProfileRequest request, String adminUserId) {
        ProfileDAO profileDAO = new ProfileDAO();
        // Verificar si el usuario que hace la petición es administrador
        if (!profileDAO.isUserAdmin(adminUserId)) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(new msgError(-1, "Solo los administradores pueden asignar perfiles"))
                    .build();
        }
        // Validar entrada
        if (request.getUserId() == null || request.getProfileIds() == null || request.getProfileIds().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new msgError(-1, "Debe especificar el usuario y al menos un perfil"))
                    .build();
        }
        // Eliminar relaciones previas
        boolean deleted = profileDAO.deleteAllProfilesForUser(request.getUserId());
        boolean allAssigned = true;
        for (Integer profileId : request.getProfileIds()) {
            boolean assigned = profileDAO.assignProfileToUser(request.getUserId(), profileId);
            if (!assigned)
                allAssigned = false;
        }
        if (allAssigned && deleted) {
            return Response.ok(new msgError(0, "Perfiles asignados correctamente")).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new msgError(-1, "No se pudieron asignar los perfiles"))
                    .build();
        }
    }

    /**
     * Endpoint para consultar usuarios registrados (solo administradores)
     */
    @GET
    @Path(value = "/users")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Operation(summary = "Obtener usuarios registrados", description = "Permite a un administrador consultar todos los usuarios registrados.", parameters = {
        @Parameter(name = "page", description = "Página (1-based)", in = ParameterIn.QUERY),
        @Parameter(name = "pageSize", description = "Tamaño de página (máximo 100)", in = ParameterIn.QUERY)
    }, responses = {
        @ApiResponse(responseCode = "200", description = "Página de usuarios", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.dom.ws.rest.bot.DTO.UsersPageDTO.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public void getUsers(@Suspended final AsyncResponse asyncResponse,
        @Context ContainerRequestContext requestContext,
        @javax.ws.rs.QueryParam("page") Integer page,
        @javax.ws.rs.QueryParam("pageSize") Integer pageSize) {
        FirebaseToken decodedToken = (FirebaseToken) requestContext.getProperty("user");

        if (decodedToken == null) {
            asyncResponse.resume(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("No autorizado")
                    .build());
            return;
        }

        executorService.submit(() -> {
            try {
                asyncResponse.resume(doGetUsers(decodedToken.getUid(), page, pageSize));
            } catch (Exception e) {
                asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(new msgError(-1, e.getMessage()))
                        .build());
            }
        });
    }

    // Helper DTO to return paginated users
    public static class UsersPageDTO {
        public List<UserDTO> users;
        public int page;
        public int pageSize;
        public long total;
        public boolean hasMore;

        public UsersPageDTO() {}
    }

    private Response doGetUsers(String userId, Integer page, Integer pageSize) {
        ProfileDAO profileDAO = new ProfileDAO();
        UserDAO userDAO = new UserDAO();
        if (page == null || page < 1) page = 1;
        if (pageSize == null || pageSize < 1) pageSize = 100;
        // enforce maximum page size 100
        if (pageSize > 100) pageSize = 100;
        List<ProfileDTO> profiles = profileDAO.getUserProfiles(userId);
        boolean isAdmin = false;
        boolean isOperator = false;
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
            } else if ("Operator".equalsIgnoreCase(profile.getName())) {
                // Operator: solo puede ver usuarios con perfil Customer
                isOperator = true;
                if (!"Administrador".equalsIgnoreCase(profile.getDescription())) {
                    empresaDesc = profile.getDescription();
                }
            }
        }
        
        // Restricciones de acceso
        if (!isAdmin && !isOperator) {
            // Técnico, Customer o cualquier otro perfil no puede consultar usuarios
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(new msgError(-1, "No tiene permisos para consultar usuarios"))
                    .build();
        }
        
        // Si es Operator, solo puede ver usuarios con perfil Customer
        if (isOperator && !isAdmin) {
            return getCustomersForOperator(profileDAO, userDAO, empresaDesc, page, pageSize);
        }
        
        // Admin puede ver solo usuarios con perfil Customer
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
            List<UserDTO> allUsers = userDAO.readAllByEmpresaId(empresaId);
            // Filtrar solo usuarios con perfil Customer
            List<UserDTO> customersOnly = new ArrayList<>();
            for (UserDTO user : allUsers) {
                List<ProfileDTO> userProfiles = profileDAO.getUserProfiles(user.getId());
                for (ProfileDTO profile : userProfiles) {
                    if ("Customer".equalsIgnoreCase(profile.getName())) {
                        customersOnly.add(user);
                        break;
                    }
                }
            }
            
            long total = customersOnly.size();
            int fromIndex = (page - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, customersOnly.size());
            if (fromIndex >= customersOnly.size()) {
                UsersPageDTO pageResp = new UsersPageDTO();
                pageResp.users = new ArrayList<>();
                pageResp.page = page;
                pageResp.pageSize = pageSize;
                pageResp.total = total;
                pageResp.hasMore = false;
                return Response.ok(pageResp).build();
            }
            List<UserDTO> users = customersOnly.subList(fromIndex, toIndex);
            // Populate tipoPerfil for each user
            for (UserDTO u : users) {
                try {
                    List<ProfileDTO> ups = profileDAO.getUserProfiles(u.getId());
                    if (ups != null && !ups.isEmpty()) {
                        // join profile names by comma
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < ups.size(); i++) {
                            if (i > 0) sb.append(",");
                            sb.append(ups.get(i).getName());
                        }
                        u.setTipoPerfil(sb.toString());
                    } else {
                        u.setTipoPerfil(null);
                    }
                } catch (Exception ex) {
                    u.setTipoPerfil(null);
                }
            }
            UsersPageDTO pageResp = new UsersPageDTO();
            pageResp.users = users;
            pageResp.page = page;
            pageResp.pageSize = pageSize;
            pageResp.total = total;
            pageResp.hasMore = toIndex < total;
            return Response.ok(pageResp).build();
        } else {
            // Admin global - retorna solo usuarios con perfil Customer
            List<UserDTO> allUsers = userDAO.readAll();
            // Filtrar solo usuarios con perfil Customer
            List<UserDTO> customersOnly = new ArrayList<>();
            for (UserDTO user : allUsers) {
                List<ProfileDTO> userProfiles = profileDAO.getUserProfiles(user.getId());
                for (ProfileDTO profile : userProfiles) {
                    if ("Customer".equalsIgnoreCase(profile.getName())) {
                        customersOnly.add(user);
                        break;
                    }
                }
            }
            
            long total = customersOnly.size();
            int fromIndex = (page - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, customersOnly.size());
            if (fromIndex >= customersOnly.size()) {
                UsersPageDTO pageResp = new UsersPageDTO();
                pageResp.users = new ArrayList<>();
                pageResp.page = page;
                pageResp.pageSize = pageSize;
                pageResp.total = total;
                pageResp.hasMore = false;
                return Response.ok(pageResp).build();
            }
            List<UserDTO> users = customersOnly.subList(fromIndex, toIndex);
            // Populate tipoPerfil for each user
            for (UserDTO u : users) {
                try {
                    List<ProfileDTO> ups = profileDAO.getUserProfiles(u.getId());
                    if (ups != null && !ups.isEmpty()) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < ups.size(); i++) {
                            if (i > 0) sb.append(",");
                            sb.append(ups.get(i).getName());
                        }
                        u.setTipoPerfil(sb.toString());
                    } else {
                        u.setTipoPerfil(null);
                    }
                } catch (Exception ex) {
                    u.setTipoPerfil(null);
                }
            }
            UsersPageDTO pageResp = new UsersPageDTO();
            pageResp.users = users;
            pageResp.page = page;
            pageResp.pageSize = pageSize;
            pageResp.total = total;
            pageResp.hasMore = toIndex < total;
            return Response.ok(pageResp).build();
        }
    }
    
    /**
     * Obtiene usuarios con perfil Customer para un Operator
     */
    private Response getCustomersForOperator(ProfileDAO profileDAO, UserDAO userDAO, String empresaDesc, Integer page, Integer pageSize) {
        EmpresaDAO empresaDAO = new EmpresaDAO();
        List<EmpresaDTO> empresas = empresaDAO.readAll();
        Integer empresaId = null;
        
        if (empresaDesc != null) {
            for (EmpresaDTO empresa : empresas) {
                if (Integer.valueOf(empresaDesc).equals(empresa.getId())) {
                    empresaId = empresa.getId();
                    break;
                }
            }
        }
        
        if (empresaId == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new msgError(-1, "No se encontró la empresa asociada al perfil"))
                    .build();
        }
        
        // Obtener todos los usuarios de la empresa
        List<UserDTO> allUsers = userDAO.readAllByEmpresaId(empresaId);
        
        // Filtrar solo usuarios con perfil Customer
        List<UserDTO> customerUsers = new ArrayList<>();
        for (UserDTO u : allUsers) {
            List<ProfileDTO> ups = profileDAO.getUserProfiles(u.getId());
            if (ups != null) {
                for (ProfileDTO p : ups) {
                    if ("Customer".equalsIgnoreCase(p.getName())) {
                        customerUsers.add(u);
                        break;
                    }
                }
            }
        }
        
        long total = customerUsers.size();
        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, customerUsers.size());
        
        if (fromIndex >= customerUsers.size()) {
            UsersPageDTO pageResp = new UsersPageDTO();
            pageResp.users = new ArrayList<>();
            pageResp.page = page;
            pageResp.pageSize = pageSize;
            pageResp.total = total;
            pageResp.hasMore = false;
            return Response.ok(pageResp).build();
        }
        
        List<UserDTO> users = customerUsers.subList(fromIndex, toIndex);
        // Populate tipoPerfil for each user
        for (UserDTO u : users) {
            try {
                List<ProfileDTO> ups = profileDAO.getUserProfiles(u.getId());
                if (ups != null && !ups.isEmpty()) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < ups.size(); i++) {
                        if (i > 0) sb.append(",");
                        sb.append(ups.get(i).getName());
                    }
                    u.setTipoPerfil(sb.toString());
                } else {
                    u.setTipoPerfil(null);
                }
            } catch (Exception ex) {
                u.setTipoPerfil(null);
            }
        }
        
        UsersPageDTO pageResp = new UsersPageDTO();
        pageResp.users = users;
        pageResp.page = page;
        pageResp.pageSize = pageSize;
        pageResp.total = total;
        pageResp.hasMore = toIndex < total;
        return Response.ok(pageResp).build();
    }


    /**
     * Endpoint para consultar usuarios registrados (solo administradores)
     */
    @GET
    @Path(value = "/employees")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Operation(summary = "Obtener usuarios registrados", description = "Permite a un administrador consultar todos los usuarios registrados.", parameters = {
        @Parameter(name = "page", description = "Página (1-based)", in = ParameterIn.QUERY),
        @Parameter(name = "pageSize", description = "Tamaño de página (máximo 100)", in = ParameterIn.QUERY)
    }, responses = {
        @ApiResponse(responseCode = "200", description = "Página de usuarios", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.dom.ws.rest.bot.DTO.UsersPageDTO.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public void getEmployees(@Suspended final AsyncResponse asyncResponse,
        @Context ContainerRequestContext requestContext,
        @javax.ws.rs.QueryParam("page") Integer page,
        @javax.ws.rs.QueryParam("pageSize") Integer pageSize) {
        FirebaseToken decodedToken = (FirebaseToken) requestContext.getProperty("user");

        if (decodedToken == null) {
            asyncResponse.resume(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("No autorizado")
                    .build());
            return;
        }

        executorService.submit(() -> {
            try {
                asyncResponse.resume(doGetEmployees(decodedToken.getUid(), page, pageSize));
            } catch (Exception e) {
                asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(new msgError(-1, e.getMessage()))
                        .build());
            }
        });
    }



    private Response doGetEmployees(String userId, Integer page, Integer pageSize) {
        ProfileDAO profileDAO = new ProfileDAO();
        UserDAO userDAO = new UserDAO();
        if (page == null || page < 1) page = 1;
        if (pageSize == null || pageSize < 1) pageSize = 100;
        // enforce maximum page size 100
        if (pageSize > 100) pageSize = 100;
        List<ProfileDTO> profiles = profileDAO.getUserProfiles(userId);
        boolean isAdmin = false;
        boolean isSuperAdmin = false;
        boolean isOperator = false;
        String empresaDesc = null;
        
        for (ProfileDTO profile : profiles) {
            if ("Administrador".equalsIgnoreCase(profile.getName())) {
                if ("Administrador".equalsIgnoreCase(profile.getDescription())) {
                    // Admin global: puede ver todos los usuarios
                    isSuperAdmin = true;
                    break;
                } else {
                    // Admin de empresa: solo usuarios de su empresa
                    empresaDesc = profile.getDescription();
                    isAdmin = true;
                    break;
                }
            } else if ("Operator".equalsIgnoreCase(profile.getName())) {
                // Operator: solo puede ver usuarios con perfil Customer
                isOperator = true;
                if (!"Administrador".equalsIgnoreCase(profile.getDescription())) {
                    empresaDesc = profile.getDescription();
                }
            }
        }
        
        // Restricciones de acceso
        if (!isAdmin && !isOperator) {
            // Técnico, Customer o cualquier otro perfil no puede consultar usuarios
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(new msgError(-1, "No tiene permisos para consultar usuarios"))
                    .build();
        }
        
        // Si es Operator, solo puede ver usuarios con perfil diferente aCustomer
        if (isOperator && !isAdmin && !isSuperAdmin) {
            return getEmployeesForOperator(profileDAO, userDAO, empresaDesc, page, pageSize);
        }
        
        // Admin puede ver solo usuarios con perfil Customer
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
            List<UserDTO> allUsers = userDAO.readAllByEmpresaId(empresaId);
            // Filtrar solo usuarios con perfil Customer
            List<UserDTO> customersOnly = new ArrayList<>();
            for (UserDTO user : allUsers) {
                List<ProfileDTO> userProfiles = profileDAO.getUserProfiles(user.getId());
                for (ProfileDTO profile : userProfiles) {
                    if (!"Customer".equalsIgnoreCase(profile.getName())) {
                        customersOnly.add(user);
                        break;
                    }
                }
            }
            
            long total = customersOnly.size();
            int fromIndex = (page - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, customersOnly.size());
            if (fromIndex >= customersOnly.size()) {
                UsersPageDTO pageResp = new UsersPageDTO();
                pageResp.users = new ArrayList<>();
                pageResp.page = page;
                pageResp.pageSize = pageSize;
                pageResp.total = total;
                pageResp.hasMore = false;
                return Response.ok(pageResp).build();
            }
            List<UserDTO> users = customersOnly.subList(fromIndex, toIndex);
            // Populate tipoPerfil for each user
            for (UserDTO u : users) {
                try {
                    List<ProfileDTO> ups = profileDAO.getUserProfiles(u.getId());
                    if (ups != null && !ups.isEmpty()) {
                        // join profile names by comma
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < ups.size(); i++) {
                            if (i > 0) sb.append(",");
                            sb.append(ups.get(i).getName());
                        }
                        u.setTipoPerfil(sb.toString());
                    } else {
                        u.setTipoPerfil(null);
                    }
                } catch (Exception ex) {
                    u.setTipoPerfil(null);
                }
            }
            UsersPageDTO pageResp = new UsersPageDTO();
            pageResp.users = users;
            pageResp.page = page;
            pageResp.pageSize = pageSize;
            pageResp.total = total;
            pageResp.hasMore = toIndex < total;
            return Response.ok(pageResp).build();
        } if (!isAdmin && isSuperAdmin) {
            // Admin global - retorna solo usuarios con perfil Customer
            List<UserDTO> allUsers = userDAO.readAll();
            // Filtrar solo usuarios con perfil Customer
            List<UserDTO> customersOnly = new ArrayList<>();
            for (UserDTO user : allUsers) {
                List<ProfileDTO> userProfiles = profileDAO.getUserProfiles(user.getId());
                for (ProfileDTO profile : userProfiles) {
                    if ("Customer".equalsIgnoreCase(profile.getName())) {
                        customersOnly.add(user);
                        break;
                    }
                }
            }
            
            long total = customersOnly.size();
            int fromIndex = (page - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, customersOnly.size());
            if (fromIndex >= customersOnly.size()) {
                UsersPageDTO pageResp = new UsersPageDTO();
                pageResp.users = new ArrayList<>();
                pageResp.page = page;
                pageResp.pageSize = pageSize;
                pageResp.total = total;
                pageResp.hasMore = false;
                return Response.ok(pageResp).build();
            }
            List<UserDTO> users = customersOnly.subList(fromIndex, toIndex);
            // Populate tipoPerfil for each user
            for (UserDTO u : users) {
                try {
                    List<ProfileDTO> ups = profileDAO.getUserProfiles(u.getId());
                    if (ups != null && !ups.isEmpty()) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < ups.size(); i++) {
                            if (i > 0) sb.append(",");
                            sb.append(ups.get(i).getName());
                        }
                        u.setTipoPerfil(sb.toString());
                    } else {
                        u.setTipoPerfil(null);
                    }
                } catch (Exception ex) {
                    u.setTipoPerfil(null);
                }
            }
            UsersPageDTO pageResp = new UsersPageDTO();
            pageResp.users = users;
            pageResp.page = page;
            pageResp.pageSize = pageSize;
            pageResp.total = total;
            pageResp.hasMore = toIndex < total;
            return Response.ok(pageResp).build();
        }
        else {
            // Admin global - retorna todos los usuarios
            List<UserDTO> allUsers = userDAO.readAll();
            long total = allUsers.size();
            int fromIndex = (page - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, allUsers.size());
            if (fromIndex >= allUsers.size()) {
                UsersPageDTO pageResp = new UsersPageDTO();
                pageResp.users = new ArrayList<>();
                pageResp.page = page;
                pageResp.pageSize = pageSize;
                pageResp.total = total;
                pageResp.hasMore = false;
                return Response.ok(pageResp).build();
            }
            List<UserDTO> users = allUsers.subList(fromIndex, toIndex);
            // Populate tipoPerfil for each user
            for (UserDTO u : users) {
                try {
                    List<ProfileDTO> ups = profileDAO.getUserProfiles(u.getId());
                    if (ups != null && !ups.isEmpty()) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < ups.size(); i++) {
                            if (i > 0) sb.append(",");
                            sb.append(ups.get(i).getName());
                        }
                        u.setTipoPerfil(sb.toString());
                    } else {
                        u.setTipoPerfil(null);
                    }
                } catch (Exception ex) {
                    u.setTipoPerfil(null);
                }
            }
            UsersPageDTO pageResp = new UsersPageDTO();
            pageResp.users = users;
            pageResp.page = page;
            pageResp.pageSize = pageSize;
            pageResp.total = total;
            pageResp.hasMore = toIndex < total;
            return Response.ok(pageResp).build();
        }

    }
    
    /**
     * Obtiene empleados con perfil Customer para un Operator
     */
    private Response getEmployeesForOperator(ProfileDAO profileDAO, UserDAO userDAO, String empresaDesc, Integer page, Integer pageSize) {
        EmpresaDAO empresaDAO = new EmpresaDAO();
        List<EmpresaDTO> empresas = empresaDAO.readAll();
        Integer empresaId = null;
        
        if (empresaDesc != null) {
            for (EmpresaDTO empresa : empresas) {
                if (Integer.valueOf(empresaDesc).equals(empresa.getId())) {
                    empresaId = empresa.getId();
                    break;
                }
            }
        }
        
        if (empresaId == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new msgError(-1, "No se encontró la empresa asociada al perfil"))
                    .build();
        }
        
        // Obtener todos los usuarios de la empresa
        List<UserDTO> allUsers = userDAO.readAllByEmpresaId(empresaId);
        
        // Filtrar solo usuarios con perfil Customer
        List<UserDTO> customerUsers = new ArrayList<>();
        for (UserDTO u : allUsers) {
            List<ProfileDTO> ups = profileDAO.getUserProfiles(u.getId());
            if (ups != null) {
                for (ProfileDTO p : ups) {
                    if (!"Customer".equalsIgnoreCase(p.getName())) {
                        customerUsers.add(u);
                        break;
                    }
                }
            }
        }
        
        long total = customerUsers.size();
        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, customerUsers.size());
        
        if (fromIndex >= customerUsers.size()) {
            UsersPageDTO pageResp = new UsersPageDTO();
            pageResp.users = new ArrayList<>();
            pageResp.page = page;
            pageResp.pageSize = pageSize;
            pageResp.total = total;
            pageResp.hasMore = false;
            return Response.ok(pageResp).build();
        }
        
        List<UserDTO> users = customerUsers.subList(fromIndex, toIndex);
        // Populate tipoPerfil for each user
        for (UserDTO u : users) {
            try {
                List<ProfileDTO> ups = profileDAO.getUserProfiles(u.getId());
                if (ups != null && !ups.isEmpty()) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < ups.size(); i++) {
                        if (i > 0) sb.append(",");
                        sb.append(ups.get(i).getName());
                    }
                    u.setTipoPerfil(sb.toString());
                } else {
                    u.setTipoPerfil(null);
                }
            } catch (Exception ex) {
                u.setTipoPerfil(null);
            }
        }

        UsersPageDTO pageResp = new UsersPageDTO();
        pageResp.users = users;
        pageResp.page = page;
        pageResp.pageSize = pageSize;
        pageResp.total = total;
        pageResp.hasMore = toIndex < total;
        return Response.ok(pageResp).build();
    }

    /**
     * Crear una empresa
     */
    @POST
    @Path(value = "/createEmpresa")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Operation(summary = "Crear empresa", 
        description = "Crea una nueva empresa con información básica y configuración de pasarela de pago.", 
        requestBody = @RequestBody(
            required = true, 
            content = @Content(
                schema = @Schema(implementation = EmpresaDTO.class),
                examples = @ExampleObject(
                    value = "{\"nombre\":\"Empresa ABC\",\"nit\":\"123456789\",\"direccion\":\"Calle 10 #20-30\",\"telefono\":\"+573001234567\",\"email\":\"contacto@empresaabc.com\",\"estado\":1,\"numeroChatbot\":\"591234567890\",\"precio\":49.99,\"usaPasarela\":true,\"tarifaFijaPasarela\":500.00,\"porcentajePasarela\":2.9,\"cobrarPasarela\":true,\"host\":\"empresaabc.com\"}"
                )
            )
        ), 
        responses = {
            @ApiResponse(responseCode = "200", description = "Empresa creada exitosamente", content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "400", description = "Error en la creación de proyectos por defecto"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        }
    )
    public void createEmpresa(@Suspended final AsyncResponse asyncResponse, final EmpresaDTO request) {
        executorService.submit(() -> {
            EmpresaDAO dao = new EmpresaDAO();
            boolean created = dao.create(request);
            boolean allProjectsCreated = true;
            String errorMsg = null;
            if (created) {
                try {
                    // Crear los 3 proyectos por defecto
                    String[] perfiles = { "Administrador", "Tecnico", "Customer" };
                    projectsDAO projectsDao = new projectsDAO();
                    for (String perfil : perfiles) {
                        projectDTO project = new projectDTO();
                        project.setUser(null); // O asigna el usuario admin si aplica
                        project.setProjectDesc(request.getNombre() + "-" + perfil);
                        project.setIdFrom(request.getNumeroChatbot());
                        project.setDateProject(new java.sql.Timestamp(System.currentTimeMillis()));
                        project.setOpenProject(1); // O el valor por defecto
                        project.setEndProject(null);
                        project.setStatusProject(1); // O el valor por defecto
                        project.setFlgEndProject(0); // O el valor por defecto
                        boolean projectCreated = projectsDao.create(project);
                        if (!projectCreated) {
                            allProjectsCreated = false;
                            errorMsg = "No se pudo crear el proyecto: " + project.getProjectDesc();
                            break;
                        }
                    }
                } catch (Exception e) {
                    allProjectsCreated = false;
                    errorMsg = e.getMessage();
                }
            }
            if (created && allProjectsCreated) {
                asyncResponse.resume(true);
            } else if (!created) {
                asyncResponse.resume(false);
            } else {
                asyncResponse
                        .resume(new msgError(-1, errorMsg != null ? errorMsg : "Error creando proyectos por defecto"));
            }
        });
    }

    /**
     * Actualizar una empresa
     */
    @POST
    @Path(value = "/updateEmpresa")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Operation(summary = "Actualizar empresa", 
        description = "Actualiza los datos de una empresa existente, incluyendo configuración de pasarela de pago y precio base.",
        requestBody = @RequestBody(
            required = true,
            content = @Content(
                schema = @Schema(implementation = EmpresaDTO.class),
                examples = @ExampleObject(
                    value = "{\"id\":1,\"nombre\":\"Empresa ABC Actualizada\",\"nit\":\"123456789\",\"direccion\":\"Calle 20 #30-40\",\"telefono\":\"+573001234567\",\"email\":\"nuevocontacto@empresaabc.com\",\"estado\":1,\"numeroChatbot\":\"591234567890\",\"precio\":59.99,\"usaPasarela\":true,\"tarifaFijaPasarela\":500.00,\"porcentajePasarela\":2.9,\"cobrarPasarela\":false,\"host\":\"empresaabc.com\"}"
                )
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Empresa actualizada exitosamente", content = @Content(schema = @Schema(implementation = Boolean.class))),
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
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Operation(summary = "Eliminar empresa", description = "Elimina una empresa existente.", requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = EmpresaDTO.class))), responses = {
            @ApiResponse(responseCode = "200", description = "Empresa eliminada", content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Operation(summary = "Obtener usuarios por empresa", description = "Devuelve los usuarios asociados a una empresa específica.", responses = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "Empresa no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public void getUsersByEmpresa(@Suspended final AsyncResponse asyncResponse,
            @javax.ws.rs.PathParam("empresaId") Integer empresaId) {
        executorService.submit(() -> {
            UserDAO userDAO = new UserDAO();
            List<UserDTO> users = userDAO.readAllByEmpresaId(empresaId);
            asyncResponse.resume(users);
        });
    }

    /**
     * Crear un usuario (solo administradores y técnicos)
     * 
     * NOTA: Solo los usuarios con perfil "Administrador" o "Técnico" pueden crear usuarios.
     * 
     * Comportamiento según perfil asignado:
     * - Si "Customer": Crea contrato y movimientos de inventario automáticamente
     * - Otros perfiles: Solo crea el usuario y asigna el perfil
     * 
     * Tipos de servicio soportados en contrato:
     * - "internet": Requiere ppoe (usuario) y ppoEPassword (contraseña)
     * - "energia_solar": Requiere energiaTipoPanel (tipo de panel)
     * - "evento": Requiere eventoTipo (tipo de evento)
     * - "otro": Sin campos específicos obligatorios
     * 
     * Tipos de inventario en InventoryRequest:
     * - "serializado": Controla inventario individual, crea movimiento en movimientos_inventario con tipo "prestamo"
     * - "stock": Controla cantidad en inventario_stock, crea movimiento en movimientos_stock con tipo "asignacion_cliente"
     * 
     * Validaciones:
     * - El teléfono debe estar en formato E.164 internacional (ej: +573001234567)
     * - Tipo y número de identificación son obligatorios
     * - Si usuario ya existe en Firebase, se actualiza; si no, se crea
     */
    @POST
    @Path(value = "/createUser")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Operation(
        summary = "Crear usuario individual con contrato",
        description = "Crea un nuevo usuario con perfil y empresa. Si el perfil es 'Customer', automáticamente se genera un contrato con campos de servicio (internet, energía solar, eventos) y movimientos de inventario (serializado o stock) según los datos proporcionados. Para importaciones masivas sin contratos, use /createUsersBatch.",
        tags = {"Usuarios"},
        requestBody = @RequestBody(
            required = true,
            description = "Datos del usuario a crear",
            content = @Content(
                schema = @Schema(implementation = UserDTO.class),
                examples = {
                    @ExampleObject(
                        name = "Usuario Cliente con Inventario Serializado",
                        summary = "Crear usuario cliente con servicio de internet y equipos serializados",
                        value = "{\"email\":\"cliente@empresa.com\",\"displayName\":\"Juan Pérez\",\"tipoIdentificacion\":\"CC\",\"numeroIdentificacion\":\"123456789\",\"phoneNumber\":\"+573001234567\",\"empresaId\":1,\"direccion\":\"Calle 10 #20-30, Apt 401\",\"planInternetId\":5,\"precioMensual\":49.99,\"ppoe\":\"usuario_pppoe\",\"ppoEPassword\":\"password_pppoe123\",\"tipoServicio\":\"internet\",\"idRaspi\":\"RASPI-001\",\"inventoryRequests\":[{\"inventarioId\":10,\"precioAsignacion\":150.00,\"notas\":\"Router TP-Link Archer C7\",\"tipoInventario\":\"serializado\"},{\"inventarioId\":11,\"precioAsignacion\":25.00,\"notas\":\"Cable Ethernet Cat5e 20m\",\"tipoInventario\":\"serializado\"}]}"
                    ),
                    @ExampleObject(
                        name = "Usuario Cliente con Inventario Stock",
                        summary = "Crear usuario cliente con servicio de internet y productos de stock",
                        value = "{\"email\":\"cliente2@empresa.com\",\"displayName\":\"María López\",\"tipoIdentificacion\":\"CC\",\"numeroIdentificacion\":\"987654321\",\"phoneNumber\":\"+573002345678\",\"empresaId\":1,\"direccion\":\"Calle 20 #30-40, Apt 501\",\"planInternetId\":5,\"precioMensual\":49.99,\"ppoe\":\"usuario_pppoe2\",\"ppoEPassword\":\"password_pppoe456\",\"tipoServicio\":\"internet\",\"idRaspi\":\"RASPI-002\",\"inventoryRequests\":[{\"productoId\":5,\"cantidad\":2.00,\"notas\":\"2 unidades de cable RJ45\",\"tipoInventario\":\"stock\"},{\"productoId\":8,\"cantidad\":1.00,\"notas\":\"1 unidad de adaptador POE\",\"tipoInventario\":\"stock\"}]}"
                    )
                }
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Usuario creado exitosamente (true si nuevo, false si actualizado)",
                content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "true"))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Errores de validación",
                content = @Content(
                    mediaType = "application/json",
                    examples = {
                        @ExampleObject(name = "Identificación faltante", value = "{\"code\":-1,\"message\":\"El tipo y número de identificación son obligatorios\"}"),
                        @ExampleObject(name = "Teléfono incorrecto", value = "{\"code\":-1,\"message\":\"El número de teléfono debe estar en formato internacional E.164, por ejemplo: +573001234567\"}")
                    }
                )
            ),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token inválido o ausente"),
            @ApiResponse(responseCode = "403", description = "Permiso denegado - Usuario no es administrador ni técnico", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"code\":-1,\"message\":\"No tiene permisos para crear usuarios\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        }
    )
    public void createUser(@Suspended final AsyncResponse asyncResponse, final UserDTO request,
            @Context ContainerRequestContext requestContext) {
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
                        .entity(new msgError(-1,
                                "El número de teléfono debe estar en formato internacional E.164, por ejemplo: +573001234567"))
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
            empresaId = newUser.getEmpresaId();
            if (empresaId == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new msgError(-1, "Debe especificar la empresa para el usuario"))
                        .build();
            }
        } else {
            if (newUser.getEmpresaId() == null) {
                List<EmpresaDTO> empresas = empresaDAO.readAll();
                for (EmpresaDTO empresa : empresas) {
                    if (empresa.getId() == Integer.valueOf(empresaDesc)) {
                        empresaId = empresa.getId();
                        break;
                    }
                }
                if (empresaId == null) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity(new msgError(-1, "No se encontró la empresa asociada al perfil del administrador"))
                            .build();
                }
            } else {
                empresaId = newUser.getEmpresaId();
            }
        }
        newUser.setEmpresaId(empresaId);
        // Crear usuario en Firebase primero
        boolean firebaseUserExists = false;
        try {
            UserRecord userRecord;
            try {
                userRecord = FirebaseAuth.getInstance().getUserByEmail(newUser.getEmail());
                firebaseUserExists = true;
                newUser.setId(userRecord.getUid());
            } catch (com.google.firebase.auth.FirebaseAuthException ex) {
                if (ex.getAuthErrorCode() != null && ex.getAuthErrorCode().name().equals("USER_NOT_FOUND")) {
                    firebaseUserExists = false;
                } else {
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(new msgError(-1, "Error consultando usuario en Firebase: " + ex.getMessage()))
                        .build();
                }
            }
            if (!firebaseUserExists) {
                CreateRequest fbRequest = new CreateRequest()
                        .setEmail(newUser.getEmail())
                        .setPassword(newUser.getNumeroIdentificacion())
                        .setDisplayName(newUser.getDisplayName());
                if (newUser.getPhoneNumber() != null && !newUser.getPhoneNumber().isEmpty()) {
                    fbRequest.setPhoneNumber(newUser.getPhoneNumber());
                }
                userRecord = FirebaseAuth.getInstance().createUser(fbRequest);
                newUser.setId(userRecord.getUid());
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new msgError(-1, "Error creando/consultando usuario en Firebase: " + e.getMessage()))
                    .build();
        }
        
        // Establecer la fecha de creación actual
        newUser.setCreationTime(new java.sql.Timestamp(System.currentTimeMillis()));
        
        boolean created;
        // Crear o actualizar registro en la base de datos
        created = userDAO.create(newUser);
        if (!created) {
            created = userDAO.update(newUser);

        }

        // if (firebaseUserExists) {
        //     //trata de crear un nuevo registro
        //     // Solo actualiza la información en la BD
        //     created = userDAO.update(newUser);
        // } else {
        //     created = userDAO.create(newUser);
        // }
        // Asignar perfil
        int idPerfil = -1;
        String assignedProfileName = null;
        try {
            List<ProfileDTO> perfilesEmpresa = profileDAO.getAllActiveProfiles();
            for (ProfileDTO perfil : perfilesEmpresa) {
                
                if (!perfil.getDescription().equalsIgnoreCase("Administrador") ) {

                    try {
                        // Intentar convertir a número para comparar
                        idPerfil = Integer.parseInt(perfil.getDescription());
                        if (idPerfil == newUser.getEmpresaId() && perfil.getName().equalsIgnoreCase(newUser.getTipoPerfil())) {
                            // Si el perfil es un número y coincide con la empresa, asignar
                            idPerfil = perfil.getId();
                            assignedProfileName = perfil.getName();
                            break;
                        }

                    } catch (NumberFormatException e) {
                        // Si no es un número, comparar por nombre
                        return Response.status(Response.Status.BAD_REQUEST)
                                .entity(new msgError(-1,
                                        "El perfil debe tener una descripción numérica o un nombre válido"))
                                .build();
                    }
                }

            }
            // Si no se encuentra el perfil solicitado, asignar Customer
            if (idPerfil == -1) {
                for (ProfileDTO perfil : perfilesEmpresa) {
                    if ("Customer".equalsIgnoreCase(perfil.getName())) {
                        idPerfil = perfil.getId();
                        assignedProfileName = perfil.getName();
                        break;
                    }
                }
            }
            if (idPerfil != -1) {
                profileDAO.assignProfileToUser(newUser.getId(), idPerfil);
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new msgError(-1, "Usuario creado pero error asignando perfil: " + e.getMessage()))
                    .build();
        }
        // Crear registro en customerWhatsapp solo si es usuario nuevo
        try {
            if (created) {
                CustomerWhatsappHelper.createCustomerWhatsappForUser(newUser, userId);
            }
        } catch (Exception e) {
            // Log error, pero no impedir creación de usuario
        }
    // Si el usuario fue creado ahora y el perfil asignado es Customer, generar contrato y movimiento de inventario
    if (created && "Customer".equalsIgnoreCase(assignedProfileName)) {
            try {
                ContratoDAO contratoDAO = new ContratoDAO();
                ContratoDTO contrato = new ContratoDTO();
                contrato.setUsuarioId(newUser.getId());
                contrato.setPlanInternetId(newUser.getPlanInternetId());
                // Obtener la siguiente secuencia por empresa para componer el numero de contrato
                int nextSeq = contratoDAO.getNextSeqForEmpresa(newUser.getEmpresaId());
                String idRaspi = (newUser.getIdRaspi() != null && !newUser.getIdRaspi().isEmpty()) ? newUser.getIdRaspi() : "0";
                String numeroContrato = newUser.getEmpresaId() + "-" + idRaspi + "-" + nextSeq;
                contrato.setNumeroContrato(numeroContrato);
                contrato.setFechaInicio(new Date(System.currentTimeMillis()));
                contrato.setFechaFin(null);
                contrato.setDireccionInstalacion(newUser.getDireccion());
                contrato.setEstado("inactivo");
                contrato.setPrecioMensual(newUser.getPrecioMensual());
                Calendar cal = Calendar.getInstance();
                int dia = cal.get(Calendar.DAY_OF_MONTH) + 1;
                contrato.setDiaCorte(dia);
                contrato.setObservaciones(null);
                contrato.setEmpresaId(newUser.getEmpresaId());
                
                // Nuevos campos de tipo de servicio
                contrato.setTipoServicio(newUser.getTipoServicio() != null ? newUser.getTipoServicio() : "internet");
                contrato.setDevice(newUser.getIdRaspi()); // Asignar el ID de la raspberry/dispositivo
                if ("internet".equalsIgnoreCase(contrato.getTipoServicio())) {
                    contrato.setInternetPpoEUsuario(newUser.getPpoe());
                    contrato.setInternetPpoEPassword(newUser.getPpoEPassword());
                } else if ("energia_solar".equalsIgnoreCase(contrato.getTipoServicio())) {
                    contrato.setEnergiaTipoPanel(newUser.getEnergiaTipoPanel());
                } else if ("evento".equalsIgnoreCase(contrato.getTipoServicio())) {
                    contrato.setEventoTipo(newUser.getEventoTipo());
                }
                
                // Campos de identificación del contratante
                contrato.setTipoId(newUser.getTipoIdentificacion());
                contrato.setNumId(newUser.getNumeroIdentificacion());
                contrato.setContratoNombre(newUser.getDisplayName());
                contrato.setPhoneNumber(newUser.getPhoneNumber());
                contrato.setCreatedBy(userId);
                
                // Nuevos campos de internet
                contrato.setTipoInternet(newUser.getTipoInternet());
                contrato.setPuerto(newUser.getPuerto());
                contrato.setCaja(newUser.getCaja());
                contrato.setNodo(newUser.getNodo());
                
                boolean contratoOk = contratoDAO.create(contrato);
                if (contratoOk) {
                    // Crear detalles del contrato a partir del arreglo de inventarios
                    try {
                        ContratoServicioDetalleDAO detalleDAO = new ContratoServicioDetalleDAO();
                        java.util.List<InventoryRequestDTO> invReqs = newUser.getInventoryRequests();
                        if (invReqs != null && !invReqs.isEmpty()) {
                            for (InventoryRequestDTO req : invReqs) {
                                try {
                                    ContratoServicioDetalleDTO detalle = new ContratoServicioDetalleDTO();
                                    detalle.setContratoServicioId(contrato.getId());
                                    detalle.setInventarioId(req.getInventarioId());
                                    detalle.setFechaAsignacion(new Timestamp(System.currentTimeMillis()));
                                    detalle.setPrecioAsignacion(req.getPrecioAsignacion());
                                    boolean detalleOk = detalleDAO.create(detalle);
                                    if (!detalleOk) {
                                        // registrar fallo en detalle (no impedimos retorno)
                                    }
                                } catch (Exception ex) {
                                    // registrar fallo en detalle individual
                                }
                            }
                        }
                    } catch (Exception e) {
                        // registrar fallo en detalles (no impedimos retorno)
                    }
                    
                    try {
                        InventoryMovementDAO imDao = new InventoryMovementDAO();
                        InventarioDAO inventarioDAO = new InventarioDAO();
                        InventarioStockDAO stockDAO = new InventarioStockDAO();
                        MovimientosStockDAO movStockDAO = new MovimientosStockDAO();
                        
                        // Crear movimientos a partir del arreglo enviado en la petición
                        java.util.List<InventoryRequestDTO> invReqs = newUser.getInventoryRequests();
                        if (invReqs != null && !invReqs.isEmpty()) {
                            for (InventoryRequestDTO req : invReqs) {
                                try {
                                    // Verificar el tipo de inventario
                                    String tipoInv = req.getTipoInventario() != null ? req.getTipoInventario() : "serializado";
                                    
                                    if ("stock".equalsIgnoreCase(tipoInv)) {
                                        // Manejar inventario tipo "stock"
                                        if (req.getProductoId() != null && req.getCantidad() != null) {
                                            // Restar cantidad del inventario_stock
                                            boolean stockOk = stockDAO.decrementarCantidad(req.getProductoId(), newUser.getEmpresaId(), req.getCantidad());
                                            if (stockOk) {
                                                // Obtener costo unitario del registro de stock
                                                InventarioStockDTO stock = stockDAO.readOne(req.getProductoId(), newUser.getEmpresaId());
                                                java.math.BigDecimal costoUnitario = (stock != null) ? stock.getCostoPromedio() : null;
                                                
                                                // Crear movimiento en movimientos_stock
                                                MovimientosStockDTO mvStock = new MovimientosStockDTO();
                                                mvStock.setProductoId(req.getProductoId());
                                                mvStock.setEmpresaId(newUser.getEmpresaId());
                                                mvStock.setEmpleadoId(userId);
                                                mvStock.setClienteId(newUser.getId());
                                                mvStock.setTipoMovimiento("asignacion_cliente");
                                                mvStock.setCantidad(req.getCantidad());
                                                mvStock.setCostoUnitario(costoUnitario);
                                                mvStock.setFechaMovimiento(new Timestamp(System.currentTimeMillis()));
                                                mvStock.setNotas(req.getNotas());
                                                movStockDAO.create(mvStock);
                                            }
                                        }
                                    } else {
                                        // Manejar inventario tipo "serializado" (comportamiento original)
                                        InventoryMovementDTO mv = new InventoryMovementDTO();
                                        mv.setEmpresaId(newUser.getEmpresaId());
                                        mv.setInventarioId(req.getInventarioId());
                                        mv.setEmpleadoId(userId); // quien realiza la accion
                                        mv.setClienteId(newUser.getId());
                                        mv.setTipoMovimiento("prestamo"); // default en creación
                                        mv.setFechaMovimiento(new Timestamp(System.currentTimeMillis()));
                                        mv.setNotas(req.getNotas());
                                        mv.setMovimientoRelacionadoId(null);
                                        boolean mvOk = imDao.create(mv);
                                        if (mvOk) {
                                            // Actualizar estado del inventario a "prestado"
                                            inventarioDAO.updateEstado(req.getInventarioId(), "prestado");
                                        }
                                    }
                                } catch (Exception ex) {
                                    // registrar fallo en movimiento individual
                                }
                            }
                        } else {
                            // Si no se envían movimientos específicos, crear un movimiento genérico de préstamo por contrato
                            try {
                                InventoryMovementDTO mv = new InventoryMovementDTO();
                                mv.setEmpresaId(newUser.getEmpresaId());
                                mv.setEmpleadoId(userId);
                                mv.setClienteId(newUser.getId());
                                mv.setTipoMovimiento("prestamo");
                                mv.setFechaMovimiento(new Timestamp(System.currentTimeMillis()));
                                mv.setNotas("Creacion de contrato " + numeroContrato);
                                imDao.create(mv);
                            } catch (Exception ex) {
                                // registrar fallo
                            }
                        }
                    } catch (Exception e) {
                        // registrar fallo en movimiento (no impedimos retorno)
                    }
                    
                    // Enviar notificación MQTT con información del contrato
                    try {
                        if (newUser.getPhoneNumber() != null && !newUser.getPhoneNumber().isEmpty()) {
                            // Obtener el nombre de la empresa
                            List<EmpresaDTO> empresas = empresaDAO.readAll();
                            String companyName = "Dommatos SAS";
                            for (EmpresaDTO emp : empresas) {
                                if (emp.getId() == newUser.getEmpresaId()) {
                                    companyName = emp.getNombre();
                                    break;
                                }
                            }
                            
                            // Enviar mensaje MQTT
                            MqttHelper.sendContractNotification(
                                newUser.getPhoneNumber(),
                                newUser.getDisplayName(),
                                companyName,
                                newUser.getEmail(),
                                contrato.getId()
                            );
                        }
                    } catch (Exception e) {
                        // Log error pero no impedir creación (error no crítico)
                    }
                } else {
                    // registrar fallo en creacion de contrato (no impedimos retorno)
                }
            } catch (Exception e) {
                // registrar excepcion general (no impedimos retorno)
            }
        }
        return Response.ok(created).build();
    }

    /**
     *
     * @param asyncResponse
     * @param request
     */
    @POST
    @Path(value = "/updateUser")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Operation(summary = "Actualizar usuario", description = "Permite actualizar un usuario según reglas de perfil y empresa.", requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = UserDTO.class))), responses = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado", content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para actualizar usuarios"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o empresa no encontrada")
    })
    public void updateUser(@Suspended final AsyncResponse asyncResponse, final UserDTO request,
            @Context ContainerRequestContext requestContext) {
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
            int idBussiness = 0;
            try {
                idBussiness = Integer.parseInt(empresaDesc);
                empresaId = idBussiness;

            } catch (NumberFormatException e) {
                // Manejar el caso en que la descripción de la empresa no es un número
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new msgError(-1, "La descripción de la empresa no es válida"))
                        .build();
            }
            for (EmpresaDTO empresa : empresas) {
                if (idBussiness == empresa.getId()) {
                    empresaId = empresa.getId();
                    break;
                }
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
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Operation(summary = "Obtener perfiles activos", description = "Permite a un usuario con perfil Administrador consultar los perfiles activos. Si el perfil es Administrador y la descripción es 'Administrador', retorna todos los perfiles activos. Si la descripción es diferente, retorna solo los perfiles activos con la misma descripción.", responses = {
            @ApiResponse(responseCode = "200", description = "Lista de perfiles activos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProfileDTO.class), examples = @ExampleObject(value = "[{\n  \"id\": 1,\n  \"name\": \"Administrador\",\n  \"description\": \"Administrador\",\n  \"active\": true\n}]"))),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public void getActiveProfiles(@Suspended final AsyncResponse asyncResponse,
            @Context ContainerRequestContext requestContext) {
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
    @Operation(summary = "Consultar dispositivos de usuario", description = "Devuelve la estructura de dispositivos Raspberry asociados al usuario autenticado.", responses = {
            @ApiResponse(responseCode = "200", description = "Estructura de dispositivos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RaspberryNewDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "No se encontraron dispositivos asociados")
    })
    public void getUserDevices(@Suspended final AsyncResponse asyncResponse,
            @Context ContainerRequestContext requestContext) {
        FirebaseToken decodedToken = (FirebaseToken) requestContext.getProperty("user");
        if (decodedToken == null) {
            asyncResponse.resume(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("No autorizado")
                    .build());
            return;
        }
        executorService.submit(() -> {
            RaspberryNewDAO dao = new RaspberryNewDAO();
            try {
                RaspberryNewDTO dto = dao.getRaspberryByUserId(decodedToken.getUid());
                if (dto == null) {
                    asyncResponse.resume(Response.status(Response.Status.NOT_FOUND)
                            .entity("No se encontraron dispositivos asociados")
                            .build());
                } else {
                    asyncResponse.resume(Response.ok(dto).build());
                }
            } catch (java.sql.SQLException e) {
                asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Error al consultar dispositivos: " + e.getMessage())
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
    @Operation(summary = "Consultar dispositivos por número de celular", description = "Devuelve la estructura de dispositivos Raspberry asociados al número de celular.", responses = {
            @ApiResponse(responseCode = "200", description = "Estructura de dispositivos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RaspberryNewDTO.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron dispositivos asociados")
    })
    public void getUserDevicesByNumber(@Suspended final AsyncResponse asyncResponse,
            @javax.ws.rs.PathParam("number") String number) {
        executorService.submit(() -> {
            RaspberryNewDAO dao = new RaspberryNewDAO();
            try {
                RaspberryNewDTO dto = dao.getRaspberryByUserNumber(number);
                if (dto == null) {
                    asyncResponse.resume(Response.status(Response.Status.NOT_FOUND)
                            .entity("No se encontraron dispositivos asociados")
                            .build());
                } else {
                    asyncResponse.resume(Response.ok(dto).build());
                }
            } catch (java.sql.SQLException e) {
                asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Error al consultar dispositivos: " + e.getMessage())
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
    @Operation(summary = "Crear o actualizar dispositivos y asociaciones", description = "Crea o actualiza la configuración de una Raspberry y asocia usuarios por userId y/o número de celular.", requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = RaspberryNewDTO.class))), responses = {
            @ApiResponse(responseCode = "200", description = "Configuración guardada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public void createOrUpdateUserDevices(@Suspended final AsyncResponse asyncResponse, final RaspberryNewDTO request) {
        executorService.submit(() -> {
            RaspberryNewDAO dao = new RaspberryNewDAO();
            try {
                boolean ok = dao.createOrUpdateRaspberryWithUsers(request);
                if (ok) {
                    asyncResponse.resume(Response.ok("Configuración guardada correctamente").build());
                } else {
                    asyncResponse.resume(Response.status(Response.Status.BAD_REQUEST)
                            .entity("No se pudo guardar la configuración")
                            .build());
                }
            } catch (java.sql.SQLException e) {
                asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Error al guardar la configuración: " + e.getMessage())
                        .build());
            }
        });
    }

    /**
     * Endpoint para consultar los perfiles asignados a un usuario (solo
     * administradores)
     */
    @GET
    @Path("/user/{userId}/profiles")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener perfiles de un usuario", description = "Permite a un administrador consultar los perfiles asignados a un usuario.", responses = {
            @ApiResponse(responseCode = "200", description = "Lista de perfiles", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProfileDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public void getUserProfiles(@Suspended final AsyncResponse asyncResponse,
            @javax.ws.rs.PathParam("userId") String userId, @Context ContainerRequestContext requestContext) {
        FirebaseToken decodedToken = (FirebaseToken) requestContext.getProperty("user");
        if (decodedToken == null) {
            asyncResponse.resume(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("No autorizado")
                    .build());
            return;
        }
        executorService.submit(() -> {
            ProfileDAO profileDAO = new ProfileDAO();
            if (!profileDAO.isUserAdmin(decodedToken.getUid())) {
                asyncResponse.resume(Response.status(Response.Status.FORBIDDEN)
                        .entity("Solo los administradores pueden consultar perfiles de otros usuarios")
                        .build());
                return;
            }
            List<ProfileDTO> profiles = profileDAO.getUserProfiles(userId);
            asyncResponse.resume(Response.ok(profiles).build());
        });
    }

    /**
     * Endpoint para consultar todas las empresas (solo administradores globales)
     */
    @GET
    @Path("/empresas")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener todas las empresas", description = "Permite a un administrador global (name=Administrador y description=Administrador) consultar todas las empresas registradas.", responses = {
            @ApiResponse(responseCode = "200", description = "Lista de empresas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmpresaDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public void getAllEmpresas(@Suspended final AsyncResponse asyncResponse,
            @Context ContainerRequestContext requestContext) {
        FirebaseToken decodedToken = (FirebaseToken) requestContext.getProperty("user");
        if (decodedToken == null) {
            asyncResponse.resume(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("No autorizado")
                    .build());
            return;
        }
        executorService.submit(() -> {
            ProfileDAO profileDAO = new ProfileDAO();
            List<ProfileDTO> profiles = profileDAO.getUserProfiles(decodedToken.getUid());
            boolean isAdminGlobal = false;
            for (ProfileDTO profile : profiles) {
                if ("Administrador".equalsIgnoreCase(profile.getName())
                        && "Administrador".equalsIgnoreCase(profile.getDescription())) {
                    isAdminGlobal = true;
                    break;
                }
            }
            if (!isAdminGlobal) {
                asyncResponse.resume(Response.status(Response.Status.FORBIDDEN)
                        .entity("Solo administradores globales pueden consultar todas las empresas")
                        .build());
                return;
            }
            EmpresaDAO empresaDAO = new EmpresaDAO();
            List<EmpresaDTO> empresas = empresaDAO.readAll();
            asyncResponse.resume(Response.ok(empresas).build());
        });
    }

    /**
     * Endpoint para crear un dispositivo (solo campos de dommapi.raspberry)
     */
    @POST
    @Path("/devices")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Crear o actualizar dispositivo simple", description = "Crea o actualiza un dispositivo solo con los campos de la tabla dommapi.raspberry. Si se envía el id, actualiza; si no, crea. Solo permitido para Administrador global.", requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = RaspberryDTO.class))), responses = {
            @ApiResponse(responseCode = "200", description = "Dispositivo procesado correctamente"),
            @ApiResponse(responseCode = "403", description = "No autorizado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public void createOrUpdateDevice(@Suspended final AsyncResponse asyncResponse, final RaspberryDTO request,
            @Context ContainerRequestContext requestContext) {
        FirebaseToken user = (FirebaseToken) requestContext.getProperty("user");
        if (user == null) {
            asyncResponse.resume(
                    Response.status(Response.Status.UNAUTHORIZED).entity(new msgError(-1, "No autorizado")).build());
            return;
        }
        executorService.submit(() -> {
            try {
                ProfileDAO profileDAO = new ProfileDAO();
                List<ProfileDTO> profiles = profileDAO.getUserProfiles(user.getUid());
                boolean isAdminGlobal = profiles.stream().anyMatch(
                        p -> "Administrador".equalsIgnoreCase(p.getName())
                                && "Administrador".equalsIgnoreCase(p.getDescription()));
                if (!isAdminGlobal) {
                    asyncResponse.resume(Response.status(Response.Status.FORBIDDEN)
                            .entity(new msgError(-1, "Solo permitido para Administrador global")).build());
                    return;
                }
                RaspberryNewDAO dao = new RaspberryNewDAO();
                boolean ok;
                if (request.getId() > 0) {
                    ok = dao.updateRaspberry(request);
                } else {
                    ok = dao.createRaspberry(request);
                }
                if (ok) {
                    asyncResponse.resume(Response.ok(new msgError(0, "Dispositivo procesado correctamente")).build());
                } else {
                    asyncResponse.resume(Response.status(Response.Status.BAD_REQUEST)
                            .entity(new msgError(-1, "No se pudo procesar la Raspberry")).build());
                }
            } catch (Exception e) {
                asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(new msgError(-1, e.getMessage())).build());
            }
        });
    }

    /**
     * Endpoint para consultar todas las Raspberrys (solo campos de
     * dommapi.raspberry)
     */
    @GET
    @Path("/devices")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Consultar todas las Raspberrys simple", description = "Devuelve todas las Raspberrys solo con los campos de la tabla dommapi.raspberry. Permitido para Administrador global, Administrador de empresa, Técnico y Operator.", responses = {
            @ApiResponse(responseCode = "200", description = "Lista de Raspberrys", content = @Content(schema = @Schema(implementation = RaspberryDTO.class))),
            @ApiResponse(responseCode = "403", description = "No autorizado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public void getAllRaspberrys(@Suspended final AsyncResponse asyncResponse,
            @Context ContainerRequestContext requestContext) {
        FirebaseToken user = (FirebaseToken) requestContext.getProperty("user");
        if (user == null) {
            asyncResponse.resume(Response.status(Response.Status.UNAUTHORIZED).entity("No autorizado").build());
            return;
        }
        executorService.submit(() -> {
            try {
                ProfileDAO profileDAO = new ProfileDAO();
                List<ProfileDTO> profiles = profileDAO.getUserProfiles(user.getUid());
                boolean isAdminGlobal = profiles.stream().anyMatch(
                        p -> "Administrador".equalsIgnoreCase(p.getName())
                                && "Administrador".equalsIgnoreCase(p.getDescription()));
                boolean isAdmin = profiles.stream().anyMatch(
                        p -> "Administrador".equalsIgnoreCase(p.getName()));
                boolean isTecnico = profiles.stream().anyMatch(
                        p -> "Tecnico".equalsIgnoreCase(p.getName()));
                boolean isOperator = profiles.stream().anyMatch(
                        p -> "Operator".equalsIgnoreCase(p.getName()));
                
                RaspberryNewDAO dao = new RaspberryNewDAO();
                List<RaspberryDTO> raspberrys;
                if (isAdminGlobal) {
                    raspberrys = dao.getAllRaspberrysSimple();
                } else if (isAdmin || isTecnico || isOperator) {
                    raspberrys = dao.getRaspberrysSimpleByUserId(user.getUid());
                } else {
                    asyncResponse.resume(Response.status(Response.Status.FORBIDDEN)
                            .entity("No tiene permisos para consultar dispositivos").build());
                    return;
                }
                asyncResponse.resume(Response.ok(raspberrys).build());
            } catch (Exception e) {
                asyncResponse
                        .resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build());
            }
        });
    }

    /**
     * Endpoint para crear la relación usuario-raspberry (solo campos de
     * dommapi.raspberry_user)
     */
    @POST
    @Path("/user/raspberry/relation")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Crear relación usuario-raspberry simple", description = "Crea la relación usuario-raspberry solo con los campos de la tabla dommapi.raspberry_user. Solo permitido para Administrador global.", requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = RaspberryUserRelationDTO.class))), responses = {
            @ApiResponse(responseCode = "200", description = "Relación creada correctamente", content = @Content(schema = @Schema(implementation = msgError.class))),
            @ApiResponse(responseCode = "403", description = "No autorizado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public void createUserRaspberryRelation(@Suspended final AsyncResponse asyncResponse,
            final RaspberryUserRelationDTO request, @Context ContainerRequestContext requestContext) {
        FirebaseToken user = (FirebaseToken) requestContext.getProperty("user");
        if (user == null) {
            asyncResponse.resume(
                    Response.status(Response.Status.UNAUTHORIZED).entity(new msgError(-1, "No autorizado")).build());
            return;
        }
        executorService.submit(() -> {
            try {
                ProfileDAO profileDAO = new ProfileDAO();
                List<ProfileDTO> profiles = profileDAO.getUserProfiles(user.getUid());
                boolean isAdminGlobal = profiles.stream().anyMatch(
                        p -> "Administrador".equalsIgnoreCase(p.getName()));
                if (!isAdminGlobal) {
                    asyncResponse.resume(Response.status(Response.Status.FORBIDDEN)
                            .entity(new msgError(-1, "Solo permitido para Administrador global")).build());
                    return;
                }
                RaspberryNewDAO dao = new RaspberryNewDAO();
                boolean ok = dao.createRaspberryUserRelation(request);
                if (ok) {
                    asyncResponse.resume(Response.ok(new msgError(0, "Relación creada correctamente")).build());
                } else {
                    asyncResponse.resume(Response.status(Response.Status.BAD_REQUEST)
                            .entity(new msgError(-1, "No se pudo crear la relación")).build());
                }
            } catch (Exception e) {
                asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(new msgError(-1, e.getMessage())).build());
            }
        });
    }

    /**
     * Endpoint para consultar relaciones usuario-raspberry por raspberry_id (solo
     * Administrador global)
     */
    @GET
    @Path("/user/raspberry/relations/{raspberryId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Consultar relaciones usuario-raspberry por raspberry_id", description = "Devuelve las relaciones usuario-raspberry filtradas por raspberry_id. Solo permitido para Administrador global.", responses = {
            @ApiResponse(responseCode = "200", description = "Lista de relaciones", content = @Content(schema = @Schema(implementation = RaspberryUserRelationDTO.class))),
            @ApiResponse(responseCode = "403", description = "No autorizado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public void getRaspberryUserRelationsByRaspberryId(@Suspended final AsyncResponse asyncResponse,
            @javax.ws.rs.PathParam("raspberryId") int raspberryId,
            @Context ContainerRequestContext requestContext) {
        FirebaseToken user = (FirebaseToken) requestContext.getProperty("user");
        if (user == null) {
            asyncResponse.resume(Response.status(Response.Status.UNAUTHORIZED).entity("No autorizado").build());
            return;
        }
        executorService.submit(() -> {
            try {
                ProfileDAO profileDAO = new ProfileDAO();
                List<ProfileDTO> profiles = profileDAO.getUserProfiles(user.getUid());
                boolean isAdmin = profiles.stream().anyMatch(
                        p -> "Administrador".equalsIgnoreCase(p.getName()));
                if (!isAdmin) {
                    asyncResponse.resume(Response.status(Response.Status.FORBIDDEN)
                            .entity("Solo permitido para Administrador global").build());
                    return;
                }
                RaspberryNewDAO dao = new RaspberryNewDAO();
                List<RaspberryUserRelationDTO> relations = dao.getRaspberryUserRelationsByRaspberryId(raspberryId);
                asyncResponse.resume(Response.ok(relations).build());
            } catch (Exception e) {
                asyncResponse
                        .resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build());
            }
        });
    }

    /**
     * Obtener las IPs de una Raspberry (solo Administrador)
     */
    @GET
    @Path("/raspberry/{raspberryId}/ips")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener IPs de una Raspberry", description = "Devuelve las IPs asignadas a una Raspberry. Solo permitido para Administrador.", responses = {
            @ApiResponse(responseCode = "200", description = "Lista de IPs", content = @Content(schema = @Schema(implementation = RaspberryNewDTO.RaspiIpDTO.class))),
            @ApiResponse(responseCode = "403", description = "No autorizado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public void getRaspberryIps(@Suspended final AsyncResponse asyncResponse,
            @javax.ws.rs.PathParam("raspberryId") int raspberryId,
            @Context ContainerRequestContext requestContext) {
        FirebaseToken user = (FirebaseToken) requestContext.getProperty("user");
        if (user == null) {
            asyncResponse.resume(Response.status(Response.Status.UNAUTHORIZED).entity("No autorizado").build());
            return;
        }
        executorService.submit(() -> {
            try {
                ProfileDAO profileDAO = new ProfileDAO();
                List<ProfileDTO> profiles = profileDAO.getUserProfiles(user.getUid());
                boolean isAdmin = profiles.stream().anyMatch(
                        p -> "Administrador".equalsIgnoreCase(p.getName()));
                boolean isOperator = profiles.stream().anyMatch(
                        p -> "Operator".equalsIgnoreCase(p.getName()));
                boolean isTecnico = profiles.stream().anyMatch(
                        p -> "Tecnico".equalsIgnoreCase(p.getName()));
                if (!isAdmin && !isOperator && !isTecnico) {
                    asyncResponse.resume(Response.status(Response.Status.FORBIDDEN)
                            .entity("Solo permitido para Administrador, Operador o Técnico").build());
                    return;
                }
                RaspberryNewDAO dao = new RaspberryNewDAO();
                List<RaspberryNewDTO.RaspiIpDTO> ips = dao.getIpsByRaspberryId(raspberryId);
                asyncResponse.resume(Response.ok(ips).build());
            } catch (Exception e) {
                asyncResponse
                        .resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build());
            }
        });
    }

    /**
     * Crear o actualizar una IP de una Raspberry (solo Administrador)
     */
    @POST
    @Path("/raspberry/{raspberryId}/ip")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Crear o actualizar IP de una Raspberry", description = "Crea o actualiza una IP de la tabla dommapi.raspberry_ip para una Raspberry. Solo permitido para Administrador.", requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = RaspberryNewDTO.RaspiIpDTO.class))), responses = {
            @ApiResponse(responseCode = "200", description = "IP procesada correctamente", content = @Content(schema = @Schema(implementation = msgError.class))),
            @ApiResponse(responseCode = "403", description = "No autorizado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public void createOrUpdateRaspberryIp(@Suspended final AsyncResponse asyncResponse,
            @javax.ws.rs.PathParam("raspberryId") int raspberryId,
            final RaspberryNewDTO.RaspiIpDTO ipDTO,
            @Context ContainerRequestContext requestContext) {
        FirebaseToken user = (FirebaseToken) requestContext.getProperty("user");
        if (user == null) {
            asyncResponse.resume(
                    Response.status(Response.Status.UNAUTHORIZED).entity(new msgError(-1, "No autorizado")).build());
            return;
        }
        executorService.submit(() -> {
            try {
                ProfileDAO profileDAO = new ProfileDAO();
                List<ProfileDTO> profiles = profileDAO.getUserProfiles(user.getUid());
                boolean isAdmin = profiles.stream().anyMatch(
                        p -> "Administrador".equalsIgnoreCase(p.getName()));
                if (!isAdmin) {
                    asyncResponse.resume(Response.status(Response.Status.FORBIDDEN)
                            .entity(new msgError(-1, "Solo permitido para Administrador")).build());
                    return;
                }
                RaspberryNewDAO dao = new RaspberryNewDAO();
                boolean ok = dao.createOrUpdateIp(ipDTO, raspberryId);
                if (ok) {
                    asyncResponse.resume(Response.ok(new msgError(0, "IP procesada correctamente")).build());
                } else {
                    asyncResponse.resume(Response.status(Response.Status.BAD_REQUEST)
                            .entity(new msgError(-1, "No se pudo procesar la IP")).build());
                }
            } catch (Exception e) {
                asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(new msgError(-1, e.getMessage())).build());
            }
        });
    }

    /**
     * Obtener los dispositivos de una Raspberry (solo Administrador)
     */
    @GET
    @Path("/raspberry/{raspberryId}/device")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener dispositivos de una Raspberry", description = "Obtiene la lista de dispositivos de una Raspberry. Solo permitido para Administrador.", responses = {
            @ApiResponse(responseCode = "200", description = "Lista de dispositivos", content = @Content(schema = @Schema(implementation = RaspberryNewDTO.RaspiDeviceDTO.class))),
            @ApiResponse(responseCode = "403", description = "No autorizado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public void getRaspberryDevices(@Suspended final AsyncResponse asyncResponse,
            @javax.ws.rs.PathParam("raspberryId") int raspberryId,
            @Context ContainerRequestContext requestContext) {
        FirebaseToken user = (FirebaseToken) requestContext.getProperty("user");
        if (user == null) {
            asyncResponse.resume(Response.status(Response.Status.UNAUTHORIZED).entity("No autorizado").build());
            return;
        }
        executorService.submit(() -> {
            try {
                ProfileDAO profileDAO = new ProfileDAO();
                List<ProfileDTO> profiles = profileDAO.getUserProfiles(user.getUid());
                boolean isAdmin = profiles.stream().anyMatch(
                        p -> "Administrador".equalsIgnoreCase(p.getName()));
                if (!isAdmin) {
                    asyncResponse.resume(Response.status(Response.Status.FORBIDDEN)
                            .entity("Solo permitido para Administrador").build());
                    return;
                }
                RaspberryNewDAO dao = new RaspberryNewDAO();
                List<RaspberryNewDTO.RaspiDeviceDTO> devices = dao.getDevicesByRaspberryId(raspberryId);
                asyncResponse.resume(Response.ok(devices).build());
            } catch (Exception e) {
                asyncResponse
                        .resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build());
            }
        });
    }

    /**
     * Crear o actualizar un dispositivo de una Raspberry (solo Administrador)
     */
    @POST
    @Path("/raspberry/{raspberryId}/device")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void createOrUpdateRaspberryDevice(@Suspended final AsyncResponse asyncResponse,
            @javax.ws.rs.PathParam("raspberryId") int raspberryId,
            final RaspberryNewDTO.RaspiDeviceDTO deviceDTO,
            @Context ContainerRequestContext requestContext) {
        FirebaseToken user = (FirebaseToken) requestContext.getProperty("user");
        if (user == null) {
            asyncResponse.resume(
                    Response.status(Response.Status.UNAUTHORIZED).entity(new msgError(-1, "No autorizado")).build());
            return;
        }
        executorService.submit(() -> {
            try {
                ProfileDAO profileDAO = new ProfileDAO();
                List<ProfileDTO> profiles = profileDAO.getUserProfiles(user.getUid());
                boolean isAdmin = profiles.stream().anyMatch(
                        p -> "Administrador".equalsIgnoreCase(p.getName()));
                if (!isAdmin) {
                    asyncResponse.resume(Response.status(Response.Status.FORBIDDEN)
                            .entity(new msgError(-1, "Solo permitido para Administrador")).build());
                    return;
                }
                RaspberryNewDAO dao = new RaspberryNewDAO();
                boolean ok = dao.createOrUpdateDevice(deviceDTO, raspberryId);
                if (ok) {
                    asyncResponse.resume(Response.ok(new msgError(0, "Dispositivo procesado correctamente")).build());
                } else {
                    asyncResponse.resume(Response.status(Response.Status.BAD_REQUEST)
                            .entity(new msgError(-1, "No se pudo procesar el dispositivo")).build());
                }
            } catch (Exception e) {
                asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(new msgError(-1, e.getMessage())).build());
            }
        });
    }

    /**
     * Crear usuarios en lote (máximo 100 por solicitud)
     * 
     * CAMBIO: Ahora TAMBIÉN crea contratos (sin detalles de inventario)
     * 
     * Comportamiento:
     * - Crea usuarios y asigna perfiles
     * - Si el perfil es "Customer", crea el contrato automáticamente
     * - NO crea detalles de contratos ni movimientos de inventario (a diferencia de /createUser)
     * - Ideal para importaciones masivas con contratos básicos
     * 
     * Tipos de inventario en InventoryRequest (si se incluyen):
     * - "serializado": Controla inventario individual
     * - "stock": Controla cantidad en inventario_stock
     * 
     * Validaciones:
     * - Máximo 100 usuarios por solicitud
     * - Teléfono debe estar en formato E.164 (ej: +573001234567)
     * - Tipo y número de identificación son obligatorios
     */
    @POST
    @Path(value = "/createUsersBatch")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    @Operation(
        summary = "Crear usuarios en lote (con contratos básicos)",
        description = "Crea múltiples usuarios (hasta 100) con contratos básicos en una sola solicitud. Crea usuarios, asigna perfiles y si el perfil es 'Customer' crea el contrato automáticamente. A diferencia de /createUser, NO crea detalles de contratos ni movimientos de inventario.",
        tags = {"Usuarios"},
        requestBody = @RequestBody(
            required = true,
            description = "Array de usuarios a crear (máximo 100)",
            content = @Content(
                schema = @Schema(type = "array", implementation = UserDTO.class),
                examples = @ExampleObject(
                    name = "Batch de 3 usuarios",
                    summary = "Crear 3 usuarios con contratos en una solicitud",
                    value = "[{\"email\":\"usuario1@empresa.com\",\"displayName\":\"Carlos García\",\"tipoIdentificacion\":\"CC\",\"numeroIdentificacion\":\"111111111\",\"phoneNumber\":\"+573001111111\",\"empresaId\":1,\"direccion\":\"Calle 1 #10-20\",\"precioMensual\":49.99},{\"email\":\"usuario2@empresa.com\",\"displayName\":\"María López\",\"tipoIdentificacion\":\"CC\",\"numeroIdentificacion\":\"222222222\",\"phoneNumber\":\"+573002222222\",\"empresaId\":1,\"direccion\":\"Calle 2 #30-40\",\"precioMensual\":49.99},{\"email\":\"usuario3@empresa.com\",\"displayName\":\"Pedro Rodríguez\",\"tipoIdentificacion\":\"CC\",\"numeroIdentificacion\":\"333333333\",\"phoneNumber\":\"+573003333333\",\"empresaId\":1,\"direccion\":\"Calle 3 #50-60\",\"precioMensual\":49.99}]"
                )
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Usuarios procesados exitosamente - Devuelve array de booleanos",
                content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Respuesta exitosa", value = "[true,true,false]"))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Errores de validación",
                content = @Content(
                    mediaType = "application/json",
                    examples = {
                        @ExampleObject(name = "Array vacío", value = "{\"code\":-1,\"message\":\"Debe enviar al menos un usuario\"}"),
                        @ExampleObject(name = "Excede límite", value = "{\"code\":-1,\"message\":\"Solo se pueden crear hasta 100 usuarios por solicitud\"}")
                    }
                )
            ),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token inválido o ausente"),
            @ApiResponse(responseCode = "403", description = "Permiso denegado - Usuario no es administrador ni técnico"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        }
    )
    public void createUsersBatch(@Suspended final AsyncResponse asyncResponse, final List<UserDTO> request,
            @Context ContainerRequestContext requestContext) {
        FirebaseToken decodedToken = (FirebaseToken) requestContext.getProperty("user");
        if (decodedToken == null) {
            asyncResponse.resume(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("No autorizado")
                    .build());
            return;
        }
        if (request == null || request.isEmpty()) {
            asyncResponse.resume(Response.status(Response.Status.BAD_REQUEST)
                    .entity(new msgError(-1, "Debe enviar al menos un usuario"))
                    .build());
            return;
        }
        if (request.size() > 100) {
            asyncResponse.resume(Response.status(Response.Status.BAD_REQUEST)
                    .entity(new msgError(-1, "Solo se pueden crear hasta 100 usuarios por solicitud"))
                    .build());
            return;
        }
        executorService.submit(() -> {
            List<Object> results = new ArrayList<>();
            for (UserDTO user : request) {
                user.setCreatedBy(decodedToken.getUid());
                // En batch, no crear contratos, solo usuario
                Response resp = doCreateUserBatchSimple(decodedToken.getUid(), user);
                Object entity = resp.getEntity();
                results.add(entity);
            }
            asyncResponse.resume(results);
        });
    }

    /**
     * Crear usuario en lote (con contratos pero sin detalles ni movimiento de inventario)
     */
    private Response doCreateUserBatchSimple(String userId, UserDTO newUser) {
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
            empresaId = newUser.getEmpresaId();
            if (empresaId == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new msgError(-1, "Debe especificar la empresa para el usuario"))
                        .build();
            }
        } else {
            if (newUser.getEmpresaId() == null) {
                List<EmpresaDTO> empresas = empresaDAO.readAll();
                for (EmpresaDTO empresa : empresas) {
                    if (empresa.getId() == Integer.valueOf(empresaDesc)) {
                        empresaId = empresa.getId();
                        break;
                    }
                }
                if (empresaId == null) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity(new msgError(-1, "No se encontró la empresa asociada al perfil del administrador"))
                            .build();
                }
            } else {
                empresaId = newUser.getEmpresaId();
            }
        }
        newUser.setEmpresaId(empresaId);
        // Crear usuario en Firebase primero
        boolean firebaseUserExists = false;
        try {
            UserRecord userRecord;
            try {
                userRecord = FirebaseAuth.getInstance().getUserByEmail(newUser.getEmail());
                firebaseUserExists = true;
                newUser.setId(userRecord.getUid());
            } catch (com.google.firebase.auth.FirebaseAuthException ex) {
                if (ex.getAuthErrorCode() != null && ex.getAuthErrorCode().name().equals("USER_NOT_FOUND")) {
                    firebaseUserExists = false;
                } else {
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(new msgError(-1, "Error consultando usuario en Firebase: " + ex.getMessage()))
                        .build();
                }
            }
            if (!firebaseUserExists) {
                CreateRequest fbRequest = new CreateRequest()
                        .setEmail(newUser.getEmail())
                        .setPassword(newUser.getNumeroIdentificacion())
                        .setDisplayName(newUser.getDisplayName());
                if (newUser.getPhoneNumber() != null && !newUser.getPhoneNumber().isEmpty()) {
                    fbRequest.setPhoneNumber(newUser.getPhoneNumber());
                }
                userRecord = FirebaseAuth.getInstance().createUser(fbRequest);
                newUser.setId(userRecord.getUid());
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new msgError(-1, "Error creando/consultando usuario en Firebase: " + e.getMessage()))
                    .build();
        }
        
        // Establecer la fecha de creación actual (para batch)
        newUser.setCreationTime(new java.sql.Timestamp(System.currentTimeMillis()));
        
        boolean created;
        created = userDAO.create(newUser);
        if (!created) {
            userDAO.update(newUser);
        }
        // Asignar perfil
        int idPerfil = -1;
        String assignedProfileName = null;
        try {
            List<ProfileDTO> perfilesEmpresa = profileDAO.getAllActiveProfiles();
            for (ProfileDTO perfil : perfilesEmpresa) {
                if (!perfil.getDescription().equalsIgnoreCase("Administrador") ) {
                    try {
                        idPerfil = Integer.parseInt(perfil.getDescription());
                        if (idPerfil == newUser.getEmpresaId()) {
                            idPerfil = perfil.getId();
                            assignedProfileName = perfil.getName();
                            break;
                        }
                    } catch (NumberFormatException e) {
                        return Response.status(Response.Status.BAD_REQUEST)
                                .entity(new msgError(-1,
                                        "El perfil debe tener una descripción numérica o un nombre válido"))
                                .build();
                    }
                }
            }
            if (idPerfil == -1) {
                for (ProfileDTO perfil : perfilesEmpresa) {
                    if ("Customer".equalsIgnoreCase(perfil.getName())) {
                        idPerfil = perfil.getId();
                        assignedProfileName = perfil.getName();
                        break;
                    }
                }
            }
            if (idPerfil != -1) {
                profileDAO.assignProfileToUser(newUser.getId(), idPerfil);
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new msgError(-1, "Usuario creado pero error asignando perfil: " + e.getMessage()))
                    .build();
        }
        
        // Crear contrato en batch (sin detalles ni movimientos)
        if (created && "Customer".equalsIgnoreCase(assignedProfileName)) {
            try {
                ContratoDAO contratoDAO = new ContratoDAO();
                ContratoDTO contrato = new ContratoDTO();
                contrato.setUsuarioId(newUser.getId());
                contrato.setPlanInternetId(newUser.getPlanInternetId());
                // Obtener la siguiente secuencia por empresa para componer el numero de contrato
                int nextSeq = contratoDAO.getNextSeqForEmpresa(newUser.getEmpresaId());
                String idRaspi = (newUser.getIdRaspi() != null && !newUser.getIdRaspi().isEmpty()) ? newUser.getIdRaspi() : "0";
                String numeroContrato = newUser.getEmpresaId() + "-" + idRaspi + "-" + nextSeq;
                contrato.setNumeroContrato(numeroContrato);
                contrato.setFechaInicio(new Date(System.currentTimeMillis()));
                contrato.setFechaFin(null);
                contrato.setDireccionInstalacion(newUser.getDireccion());
                contrato.setEstado("inactivo");
                contrato.setPrecioMensual(newUser.getPrecioMensual());
                Calendar cal = Calendar.getInstance();
                int dia = cal.get(Calendar.DAY_OF_MONTH) + 1;
                contrato.setDiaCorte(dia);
                contrato.setObservaciones(null);
                contrato.setEmpresaId(newUser.getEmpresaId());
                
                // Nuevos campos de tipo de servicio
                contrato.setTipoServicio(newUser.getTipoServicio() != null ? newUser.getTipoServicio() : "internet");
                contrato.setDevice(newUser.getIdRaspi()); // Asignar el ID de la raspberry/dispositivo
                if ("internet".equalsIgnoreCase(contrato.getTipoServicio())) {
                    contrato.setInternetPpoEUsuario(newUser.getPpoe());
                    contrato.setInternetPpoEPassword(newUser.getPpoEPassword());
                } else if ("energia_solar".equalsIgnoreCase(contrato.getTipoServicio())) {
                    contrato.setEnergiaTipoPanel(newUser.getEnergiaTipoPanel());
                } else if ("evento".equalsIgnoreCase(contrato.getTipoServicio())) {
                    contrato.setEventoTipo(newUser.getEventoTipo());
                }
                
                // Campos de identificación del contratante
                contrato.setTipoId(newUser.getTipoIdentificacion());
                contrato.setNumId(newUser.getNumeroIdentificacion());
                contrato.setContratoNombre(newUser.getDisplayName());
                contrato.setPhoneNumber(newUser.getPhoneNumber());
                contrato.setCreatedBy(userId);
                
                // Nuevos campos de internet
                contrato.setTipoInternet(newUser.getTipoInternet());
                contrato.setPuerto(newUser.getPuerto());
                contrato.setCaja(newUser.getCaja());
                contrato.setNodo(newUser.getNodo());
                
                boolean contratoOk = contratoDAO.create(contrato);
                if (!contratoOk) {
                    // Contrato no se creó, pero continuamos en batch
                }
            } catch (Exception e) {
                // Log error pero no impedir creación (error no crítico)
            }
        }
        
        return Response.ok(created).build();
    }

}
