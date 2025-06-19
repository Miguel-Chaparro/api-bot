// Helper for customerWhatsapp creation after user creation
package com.dom.ws.rest.bot.Services;

import com.dom.ws.rest.bot.DTO.UserDTO;
import com.dom.ws.rest.bot.DTO.EmpresaDTO;
import com.dom.ws.rest.bot.DTO.projectDTO;
import com.dom.ws.rest.bot.DTO.customerWhatsappDTO;
import com.dom.ws.rest.bot.DAO.EmpresaDAO;
import com.dom.ws.rest.bot.DAO.projectsDAO;
import com.dom.ws.rest.bot.DAO.customerWhatsappDAO;

public class CustomerWhatsappHelper {
    public static void createCustomerWhatsappForUser(UserDTO newUser, String createdBy) {
        try {
            // 1. Get Empresa info for the user who created the new user
            EmpresaDAO empresaDAO = new EmpresaDAO();
            EmpresaDTO empresa = null;
            for (EmpresaDTO e : empresaDAO.readAll()) {
                if (e.getId() == newUser.getEmpresaId()) {
                    empresa = e;
                    break;
                }
            }
            if (empresa == null) return;
            String numeroChatbot = empresa.getNumeroChatbot();
            String nombreEmpresa = empresa.getNombre();

            // 2. Format idWhatsapp
            String phone = newUser.getPhoneNumber();
            if (phone == null) return;
            String idWhatsapp = phone.replace("+", "") + "@c.us";

            // 3. idCustomer
            String idCustomer = newUser.getNumeroIdentificacion();

            // 4. nameWAP
            String nameWAP = newUser.getDisplayName();

            // 5. questionId
            String questionId = "0";

            // 6. idFrom
            String idFrom = numeroChatbot;

            // 7. idProject: nombreEmpresa + '-' + tipoPerfil, buscar en projectsDAO
            String tipoPerfil = newUser.getTipoPerfil();
            String projectDesc = nombreEmpresa + "-" + tipoPerfil;
            projectsDAO projectsDao = new projectsDAO();
            projectDTO projectQuery = new projectDTO();
            projectQuery.setIdFrom(idFrom);
            projectQuery.setProjectDesc(projectDesc);
            projectQuery.setTokenId("1");
            projectDTO foundProject = projectsDao.readOne(projectQuery);
            int idProject = foundProject.getIdProject();

            // 8. Crear customerWhatsappDTO y guardar
            customerWhatsappDTO dto = new customerWhatsappDTO();
            dto.setIdWhatsapp(idWhatsapp);
            dto.setIdCustomer(idCustomer);
            dto.setName(nameWAP);
            dto.setIdQuestions(questionId);
            dto.setIdProject(idProject);
            dto.setIdFrom(idFrom);
            customerWhatsappDAO dao = new customerWhatsappDAO();
            dao.create(dto);
        } catch (Exception e) {
            // Log or handle error
        }
    }
}
