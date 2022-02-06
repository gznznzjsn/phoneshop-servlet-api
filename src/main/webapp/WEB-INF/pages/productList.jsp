<%@ page contentType="text/html;charset=UTF-8" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
      <%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

        <jsp:useBean id="products" type="java.util.ArrayList" scope="request" />
        <tags:master pageTitle="Product List">
          <p>
            Welcome to Expert-Soft training!
          </p>
          <form>
            <input name="query" value="${param.query}">
            <button>Search</button>
          </form>
          <table>
            <thead>
              <tr>
                <td>Image</td>
                <td>
                  Description
                  <tags:sortLink sort="DESCRIPTION" order="ASC" />
                  <tags:sortLink sort="DESCRIPTION" order="DESC" />
                </td>
                <td class="price">
                  Price
                  <tags:sortLink sort="PRICE" order="ASC" />
                  <tags:sortLink sort="PRICE" order="DESC" />
                </td>
              </tr>
            </thead>
            <c:forEach var="product" items="${products}">
              <tr>
                <td>
                  <img class="product-tile" src="${product.imageUrl}">
                </td>
                <td>
                  <a href="${pageContext.servletContext.contextPath}/products/${product.id}"> ${product.description}</a>
                </td>
                <td class="price">
                  <fmt:formatNumber value="${product.price}" type="currency"
                    currencySymbol="${product.currency.symbol}" />
                </td>
              </tr>
            </c:forEach>
          </table>
          <section class="viewed">
            <p class="viewed-title">Recently viewed</p>
            <div class="viewed-panel">
              <c:forEach var="viewedProduct" items="${viewedList.viewedProducts}">
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
            </div>
          </section>
        </tags:master>