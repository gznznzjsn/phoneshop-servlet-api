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
                <td class="quantity">Quantity</td>
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
                <td>
                  <form method="post" action="${pageContext.servletContext.contextPath}/products/${product.id}">
                    <input class="quantity" name="quantity"
                      value="1">
                </td>
                <td class="price">
                  <fmt:formatNumber value="${product.price}" type="currency"
                    currencySymbol="${product.currency.symbol}" />
                </td>
                <td>
                  <button>Add to cart</button>
                </td>
                </form>
              </tr>
            </c:forEach>
          </table>
          <section class="viewed ${viewedList.anyViewed eq true ? 'active' : ''}">
            <p class="viewed-title ${viewedList.anyViewed eq true ? 'active' : ''}">Recently viewed</p>
            <div class="viewed-panel">
              <tags:viewedTile viewedProducts="${viewedList.viewedProducts}" />
            </div>
          </section>
        </tags:master>