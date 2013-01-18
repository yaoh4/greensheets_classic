package gov.nih.nci.iscs.velocity.tools.view.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.ResourceNotFoundException;

/**
 * Created by IntelliJ IDEA. User: Jed Date: Sep 7, 2003 Time: 12:34:42 PM To change this template use Options | File Templates.
 */
public class VelocityViewIdServlet extends
        org.apache.velocity.tools.view.servlet.VelocityViewServlet {

    private static String templateIdParamName = null;

    private static final Logger logger = Logger
            .getLogger(VelocityViewIdServlet.class);

    public void init(ServletConfig config) throws ServletException {
        // log.debug("Init starting.");
        super.init(config);
        // log.debug("Super init done.");
        templateIdParamName = "TEMPLATE_ID";
        // log.info("Template ID param name: " + templateIdParamName);
    }

    protected Template handleRequest(HttpServletRequest request,
            HttpServletResponse response, Context ctx) throws Exception {
        String tIdStr = null;

        tIdStr = request.getParameter(templateIdParamName);
        if (tIdStr == null)
            tIdStr = (String) request.getAttribute(templateIdParamName);
        if (tIdStr == null)
            tIdStr = (String) request.getSession().getAttribute(
                    templateIdParamName);
        if (tIdStr == null)
            tIdStr = (String) request.getSession().getServletContext()
                    .getAttribute(templateIdParamName);
        if (tIdStr == null)
            throw new ResourceNotFoundException("Template ID not specified.");

        return getTemplate(tIdStr);
    }
}
