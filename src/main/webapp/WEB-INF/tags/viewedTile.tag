<%@ tag trimDirectiveWhitespaces="true" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <%@ attribute name="viewedProducts" required="true" type="java.util.ArrayList" %>

<c:forEach var="viewedProduct" items="${viewedProducts}">
    <div class="viewed-tile">
                     <div class="viewed-image">
                       <img src="${viewedProduct.imageUrl}">
                     </div>
                     <div class="viewed-description">
                       <a href="${pageContext.servletContext.contextPath}/products/${viewedProduct.id}">
                         ${viewedProduct.description}</a>

                       <fmt:formatNumber value="${viewedProduct.price}" type="currency"
                                             currencySymbol="${viewedProduct.currency.symbol}" />
                     </div>
                   </div>
                   </c:forEach>