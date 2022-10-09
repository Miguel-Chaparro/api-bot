/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dom.ws.rest.bot.Controller;

import com.dom.ws.rest.bot.DAO.answerDAO;
import com.dom.ws.rest.bot.DAO.customerWhatsappDAO;
import com.dom.ws.rest.bot.DAO.questionsDAO;
import com.dom.ws.rest.bot.DAO.raspiDAO;
import com.dom.ws.rest.bot.DTO.answerDTO;
import com.dom.ws.rest.bot.DTO.customerWhatsappDTO;
import com.dom.ws.rest.bot.DTO.questionsDTO;
import com.dom.ws.rest.bot.DTO.raspiDTO;
import com.dom.ws.rest.bot.Request.questionsAnswersReq;
import com.dom.ws.rest.bot.Response.answerResp;
import com.dom.ws.rest.bot.vo.msgError;
import com.dom.ws.rest.bot.vo.questionsVO;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MIGUEL
 */
public class questionsController {

    static final Logger logg = Logger.getLogger(questionsController.class.getName());

    public answerResp questionsBot(questionsAnswersReq req) {
        logg.info("*** Start questionsController questionsBot ***");
        msgError error = new msgError();
        customerWhatsappDTO dto = new customerWhatsappDTO();
        answerResp statesResp = new answerResp();
        questionsVO question = new questionsVO();
        dto = whatsappData(req.getWhatsappId());
        switch (dto.getError().getCode()) {
            case 1:
                dto.setIdQuestions("1");
                dto.setIdWhatsapp(req.getWhatsappId());
                dto.setIdCustomer("");
                dto.setName("");
                dto.setIdProject(1);
                dto.setPendingState(0);
                statesResp.setError(updateQuestionCustomer(dto, true));
                question = buildNextQuestion(dto, 1, true, new msgError(0, ""));
                statesResp.setQuestion(question);
                break;
            case 0:
                statesResp = response(dto, req.getAnswer());
                break;
            default:
                error.setCode(-1);
                error.setMessage(
                        "🤖Ups! Lo lamento, en estos momentos no puedo procesar la solicitud. \n Estoy aprendiendo día a día para no volver a repetir estos errores, favor intenta mas tarde");
                break;
        }
        statesResp.setError(error);
        statesResp.setWhatsapp(req.getWhatsappId());
        logg.log(Level.INFO, "{0}*** End questionsController whatsappData ***", statesResp.getError().getMessage());
        logg.info("*** End questionsController whatsappData ***");
        return statesResp;
    }

    private customerWhatsappDTO whatsappData(String Whatsapp) {
        logg.info("*** Start questionsController whatsappData ***");
        customerWhatsappDAO customerWap = new customerWhatsappDAO();
        customerWhatsappDTO reqDTO = new customerWhatsappDTO();
        customerWhatsappDTO respDTO = new customerWhatsappDTO();
        reqDTO.setIdWhatsapp(Whatsapp);
        respDTO = customerWap.readOne(reqDTO);
        logg.info("*** End questionsController whatsappData ***");
        return respDTO;

    }

    private answerResp response(customerWhatsappDTO req, String option) {
        logg.info("*** Start questionsController response ***");
        List<answerDTO> answerList = new ArrayList<>();
        answerDTO val = new answerDTO();
        answerResp resp = new answerResp();
        questionsVO question = new questionsVO();
        Date date = new Date();
        List<raspiDTO> listRaspiDTO = new ArrayList<>();
        raspiDTO dto = new raspiDTO();

        // sumale 3 horas a la fecha actual
        date.setTime(date.getTime() - (1000 * 60 * 60 * 3));
        Timestamp ts = new Timestamp(date.getTime());
        if (req.getDate() == null || req.getDate().getTime() > ts.getTime()) {
            val.setError(new msgError(2, ""));
            if (req.getFlg_devices() > 0 && req.getPendingState() == 3) {
                req.setIdQuestions("0");
                req.setPendingState(2);
            } else {
                req.setIdQuestions("1");
            }
            updateQuestionCustomer(req, false);
        }

        if (req.getFlg_devices() > 0 && req.getPendingState() == 2 && req.getIdQuestions() == "0") {
            listRaspiDTO = readRaspi(req.getIdWhatsapp());
            answerList = convertListRaspiDTOtoListAnswerDTO(listRaspiDTO);
            req.setPendingState(3);
            updateQuestionCustomer(req, false);
            resp.setError(new msgError(0, "OK"));
            question.setQuestionDesc(
                    "🤖 Hola Estos son los dispositivos que tienes registrados en el sistema, Digita el número de uno de ellos para continuar ...");
            question.setOptions(answerList);
        } else if (req.getFlg_devices() > 0 && req.getPendingState() == 3 && req.getIdQuestions() == "0") {
            dto = compareDevices(req.getIdWhatsapp(), option);
            if (dto.getError().getCode() == 0) {
                req.setIdQuestions("1");
                req.setPendingState(2);
                req.setDevices(dto.getRaspi());
                resp.setRaspi(dto.getRaspi());
                resp.setError(new msgError(0, "OK"));
                updateQuestionCustomer(req, false);
                question = buildNextQuestion(req, val.getAnswerId(), false, new msgError(3, ""));
            } else {
                resp.setError(dto.getError());
                listRaspiDTO = readRaspi(req.getIdWhatsapp());
                answerList = convertListRaspiDTOtoListAnswerDTO(listRaspiDTO);
                question.setQuestionDesc(dto.getError().getMessage());
                question.setOptions(answerList);
            }
        } else {
            answerList = validateOptions(req, option);
            question.setOptions(answerList);
            val = answerList.get(0);
            resp.setCommand(val.getError().getMessage());
            if (val.getError().getCode() == 1 && req.getIdQuestions().length() < 2 && req.getPendingState() == 0) {
                req.setIdQuestions("1.");
                question = buildNextQuestion(req, val.getAnswerId(), false, val.getError());
            } else if (val.getError().getCode() == 1 && req.getPendingState() == 2) {
                listRaspiDTO = readRaspi(req.getIdWhatsapp());
                answerList = convertListRaspiDTOtoListAnswerDTO(listRaspiDTO);
                req.setPendingState(3);
                req.setIdQuestions("0");
                updateQuestionCustomer(req, false);
                resp.setError(new msgError(0, "OK"));
                question.setQuestionDesc(
                        "🤖 Hola Estos son los dispositivos que tienes registrados en el sistema, Digita el número de uno de ellos para continuar ...");
                question.setOptions(answerList);
            } else {
                switch (val.getError().getCode()) {
                    case 0:
                        question = buildNextQuestion(req, val.getAnswerId(), false, val.getError());
                        break;
                    case 2:
                        resp.setRaspi(req.getDevices());
                        resp.setCommand(val.getError().getMessage());
                        question.setQuestionDesc(
                                "🤖 Nos estamos Comunicando con el Dispositivo, favor espera un momento ...");
                        break;
                    case 3:
                        resp.setRaspi(req.getDevices());
                        question = buildNextQuestion(req, val.getAnswerId(), false, val.getError());
                        break;
                    case 4:
                        question.setQuestionDesc(val.getError().getMessage());
                        break;
                    case 5:
                        question = buildNextQuestion(req, val.getAnswerId(), false, val.getError());
                        break;
                    case 6:
                        resp.setError(new msgError(6, "OK"));
                        question.setQuestionDesc(
                                "🤖 Gracias por utilizar nuestro servicio, En estos momentos un asesor estará atendiendo a tu solicitud ... Pronto te contactaremos");
                        break;

                    default:
                        resp.setError(new msgError(7, "Error"));
                        question.setQuestionDesc(
                                "🤖 Lamentamos informarte que no hemos podido procesar tu solicitud, favor intenta de nuevo ...");
                        break;
                }

            }

        }

        resp.setQuestion(question);
        resp.setError(val.getError());
        // val = answerList.get(0);
        logg.info("*** End questionsController Response ***");
        return resp;
    }

    private questionsVO buildNextQuestion(customerWhatsappDTO req, int option, boolean newRegister, msgError error) {
        logg.info("*** Start questionsController buildNextQuestion ***");
        logg.log(Level.INFO, "{0}***  buildNextQuestion FIRST***\n---", req.getIdQuestions());
        questionsVO response = new questionsVO();
        questionsDAO dao = new questionsDAO();
        questionsDTO dto = new questionsDTO();
        questionsDTO questionDto = new questionsDTO();
        List<answerDTO> answerList = new ArrayList<>();
        answerDTO optionDto = new answerDTO();
        answerDAO ansdao = new answerDAO();
        String questionId = "";
        optionDto.setAnswerId(0);
        questionDto = dao.readOne(dto);
        logg.log(Level.INFO, "{0}***  buildNextQuestion ***\n", "Opción: " + req.getIdQuestions());
        if (option == 0) {
            questionId = req.getIdQuestions().substring(0, req.getIdQuestions().length() - 2);
        } else {
            if (newRegister) {
                questionId = "" + option;
            } else if(questionDto.getNextQuestion()==0) {
                questionId = req.getIdQuestions() + "." + option;
            }else{
                questionId = questionDto.getNextQuestion() + "";
            }
        }
        if (questionId == null || questionId.equals("")) {
            response.setQuestionDesc("Para continuar debes seleccionar algunas de estas opciones:");
            optionDto.setIdQuestion("1");
            optionDto.setIdProject(req.getIdProject());
            optionDto.setAnswerId(0);
            answerList = ansdao.readMany(optionDto);
            response.setIdQuestion("1");

        } else {
            optionDto.setIdQuestion(questionId);
            optionDto.setIdProject(req.getIdProject());
            optionDto.setAnswerId(0);
            dto.setIdQuestions(questionId);
            dto.setIdProject(req.getIdProject());
            answerList = ansdao.readMany(optionDto);
            questionDto = dao.readOne(dto);
            response.setQuestionDesc(message(req.getName(), questionDto));
            response.setIdQuestion(questionId);
        }

        response.setOptions(answerList);
        updateQuestionCustomer(req, false);
        logg.info("*** End questionsController buildNextQuestion ***");
        return response;
    }

    private String message(String name, questionsDTO preview) {
        logg.info("*** Start questionsController message ***");
        String question;
        String data;
        question = preview.getQuestions();

        logg.log(Level.INFO, "{0}***  Message ***\n", "name: -" + name + "-");
        if (name == null || "".equals(name)) {
            logg.log(Level.INFO, "{0}***  Message If true***\n", "name: " + name);
            data = "";
        } else {

            data = " *" + name + "* ";
        }
        // Aca va el siwtch case para validar las preguntas
        logg.log(Level.INFO, "{0}***  Message 2***\n", "preview: -" + preview.getIdQuestions() + "-");

        if (null != question) {
            if (question.split("##").length > 1) {
                logg.log(Level.INFO, "{0}***  Message 2 If true***\n", "preview: " + preview.getIdQuestions());
                question = question.replaceAll("##", data);
            }

        } else {
            question = "🤖Lo siento, encontraste un fallo, Se esta cargando nuevamente en *La Matrix*... No es facíl";
        }

        logg.info("*** END questionsController message ***");
        return question;

    }

    private msgError updateQuestionCustomer(customerWhatsappDTO dto, boolean create) {
        logg.info("*** Start questionsController updateQuestionCustomer ***");
        Instant instan;
        Timestamp current;
        instan = Instant.now();
        current = Timestamp.from(instan);
        dto.setDate(current);
        msgError response = new msgError();
        boolean state;
        customerWhatsappDAO customer = new customerWhatsappDAO();
        if (create) {
            state = customer.create(dto);
        } else {
            state = customer.update(dto);
        }
        if (state) {
            response.setCode(0);
            response.setMessage("");
        } else {
            response.setCode(-1);
            response.setMessage(
                    "🤖Ups! Lo lamento, en estos momentos no puedo procesar la solicitud. \n Estoy aprendiendo día a día para no volver a repetir estos errores, favor intenta mas tarde");
        }
        logg.info("*** End questionsController updateQuestionCustomer ***");
        return response;

    }

    private List<answerDTO> validateOptions(customerWhatsappDTO dto, String option) {
        logg.info("*** Start questionsController validateOptions ***");
        answerDAO dao = new answerDAO();
        answerDTO req = new answerDTO();
        List<answerDTO> answerList = new ArrayList<>();
        req.setIdQuestion(dto.getIdQuestions());
        req.setAnswerId(0);
        msgError rta = new msgError();
        answerList = dao.readMany(req);
        int indicator = -1;
        int count = 1;
        String command = "", pending = "";
        if (dto.getCommand().equals("") || dto.getCommand() == null) {
            command = "";
        } else {
            command = dto.getCommand() + "-";
        }
        if (dto.getPendingDescription().equals("") || dto.getPendingDescription() == null) {
            pending = "";
        } else {
            pending = dto.getPendingDescription() + " ";
        }
        try {
            indicator = Integer.parseInt(option);
            rta.setCode(-1);
            rta.setMessage(
                    "🤖Ups! Lo siento esta opción ingresada no es valida, Estoy aprendiendo día a día con el fin de garantizar un mejor servicio");
            if (indicator == 0) {
                logg.info("*** INDICADOR 0 ***");

                rta.setCode(1);
                rta.setMessage("Back");
            } else {
                for (answerDTO q : answerList) {
                    logg.log(Level.INFO, "{0}\n", "for: " + q.getAnswerId() + "\n" + indicator);
                    if (q.getAnswerId() == indicator) {
                        logg.log(Level.INFO, "{0}\n", "for - IF: " + q.getIdQuestion());
                        if (q.getFlgEnd() == 1 && q.getFlgCommand() == 1) {
                            rta.setCode(2);
                            rta.setMessage(command + q.getCommand());
                            dto.setCommand("");
                            dto.setIdQuestions("0");
                            dto.setPendingState(2);
                            updateQuestionCustomer(dto, false);
                        } else if (q.getFlgEnd() == 0 && q.getFlgCommand() == 1) {
                            rta.setCode(3);
                            rta.setMessage("");
                            dto.setCommand(command + q.getCommand());
                            updateQuestionCustomer(dto, false);
                        } else if (q.getFlgEnd() == 1 && q.getFlgCommand() == 0) {
                            rta.setCode(4);
                            dto.setIdQuestions("1");
                            updateQuestionCustomer(dto, false);
                            rta.setMessage(
                                    "🤖 ¡Gracias por utilizar nuestros servicios!, Recurda que puedes escribinos en cualquier momento... 🤗🤗🤗");
                        } else if (q.getFlgEnd() == 2) {
                            rta.setCode(5);
                            rta.setMessage("");
                            dto.setPendingDescription(pending + q.getAnswerDesc());
                            updateQuestionCustomer(dto, false);
                        } else if (q.getFlgEnd() == 3) {
                            rta.setCode(6);
                            rta.setMessage(pending + q.getAnswerDesc());
                            dto.setPendingDescription("");
                            dto.setIdQuestions("1");
                            dto.setPendingState(0);
                            updateQuestionCustomer(dto, false);
                        } else {
                            rta.setCode(0);
                            rta.setMessage("");
                        }
                        req = q;
                        break;
                    }
                    count++;

                }
            }
        } catch (NumberFormatException ex) {
            if (dto.getIdQuestions().equals("") || dto.getIdQuestions() == null) {
                dto.setIdQuestions("1");
                updateQuestionCustomer(dto, false);
                rta.setCode(0);
                rta.setMessage("Start");

            } else {
                rta.setCode(-1);
                rta.setMessage("🤖Ups! Estoy aprendiendo a leer... por favor ingresa solo digitos");
            }

        }
        /*
         * if (!flgOK) {
         * rta.setCode(-1);
         * rta.
         * setMessage("🤖Ups! Lo siento esta opción ingresada no es valida, Estoy aprendiendo día a día con el fin de garantizar un mejor servicio"
         * );
         * } else {
         * String updateQuestion = dto.getIdQuestions() + "." + option;
         * req.setIdQuestion(updateQuestion);
         * dto.setIdQuestions(updateQuestion);
         * updateQuestionCustomer(dto, false);
         * 
         * }
         */
        req.setAnswerId(indicator);
        req.setAnswerDesc("" + count);
        req.setError(rta);
        answerList.add(0, req);
        logg.info("*** End questionsController validaOptions ***");
        return answerList;

    }

    private raspiDTO compareDevices(String idChat, String idRaspi) {
        logg.info("*** Start questionsController compareDevices ***");

        raspiDTO req = new raspiDTO();
        int idDevice;
        List<raspiDTO> raspiList = new ArrayList<>();
        try {
            idDevice = Integer.parseInt(idRaspi);
            req.setIdDevices(idDevice);
            raspiList = readRaspi(idChat);
            req.setError(new msgError(1, "🤖Ups! Lo siento esta opción ingresada no es valida"));
            for (raspiDTO r : raspiList) {
                if (r.getIdDevices() == idDevice) {
                    req = r;
                    req.setError(new msgError(0, "Ok"));
                    break;
                }
            }
        } catch (NumberFormatException ex) {
            req.setError(new msgError(-1, "🤖 Ups!  Aún no reconozco textos, por favor ingresa solo digitos"));
        }
        logg.info("*** End questionsController compareDevices ***");
        return req;
    }

    private List<raspiDTO> readRaspi(String idChat) {
        logg.info("*** Start questionsController readRaspi ***");
        raspiDAO dao = new raspiDAO();
        raspiDTO req = new raspiDTO();
        req.setIdChat(idChat);
        List<raspiDTO> raspiList = new ArrayList<>();
        raspiList = dao.readMany(req);
        logg.info("*** End questionsController readRaspi ***");
        return raspiList;
    }

    private List<answerDTO> convertListRaspiDTOtoListAnswerDTO(List<raspiDTO> listRaspiDTO) {
        logg.info("*** Start questionsController convertListRaspiDTOtoListAnswerDTO ***");
        List<answerDTO> listAnswerDTO = new ArrayList<>();
        for (raspiDTO raspiDTO : listRaspiDTO) {
            answerDTO answerDTO = new answerDTO();
            answerDTO.setAnswerId(raspiDTO.getIdDevices());
            answerDTO.setAnswerDesc(raspiDTO.getRaspi());
            listAnswerDTO.add(answerDTO);
        }
        logg.info("*** End questionsController convertListRaspiDTOtoListAnswerDTO ***");
        return listAnswerDTO;
    }
}
