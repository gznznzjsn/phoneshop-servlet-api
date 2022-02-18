<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageTitle" required="true" %>

<header>
    <a class="shop-title" href="${pageContext.servletContext.contextPath}">
      <img src="${pageContext.servletContext.contextPath}/images/logo.svg"/>
      <p>PhoneShop</p>
    </a>
<jsp:include page="/cart/minicart"/>
  </header>