<%@ tag trimDirectiveWhitespaces="true" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="imageUrl" required="true" %>
<%@ attribute name="id" required="true" %>
<%@ attribute name="description" required="true" %>
<%@ attribute name="price" required="true" %>
<%@ attribute name="symbol" required="true" %>

    <div class="viewed-tile">

                     <div class="viewed-image">
                       <img src="${imageUrl}">
                     </div>
                     <div class="viewed-description">
                       <a href="${pageContext.servletContext.contextPath}/products/${id}">
                         ${description}</a>

                       <fmt:formatNumber value="${price}" type="currency"
                                             currencySymbol="${symbol}" />
                     </div>
                   </div>