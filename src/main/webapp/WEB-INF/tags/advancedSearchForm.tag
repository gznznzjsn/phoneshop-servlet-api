<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  <%@ tag trimDirectiveWhitespaces="true" %>
    <%@ attribute name="label" required="true" %>
      <%@ attribute name="name" required="true" %>
<%@ attribute name="errors" required="true" type="java.util.Map" %>



            <tr>
              <td>${label}<span style="color:red">*</span></td>
                <c:set var="error" value="${errors[name]}" />
                <td>
                <input name="${name}" value="${param[name]}" type="text">
                    <c:if test="${not empty error}">
                        <p class="error">
                            ${error}
                        </p>
                    </c:if>
              </td>
            </tr>