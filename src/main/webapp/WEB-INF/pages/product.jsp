<%@ page contentType="text/html;charset=UTF-8" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
      <%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

        <jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request" />
        <tags:master pageTitle="Product Details">
          <p>
            ${cart}
          </p>
          <c:if test="${not empty param.message}">
            <p class="success">
              ${param.message}
            </p>
          </c:if>
          <c:if test="${not empty param.error}">
            <p class="error">
              ${param.error}
            </p>
          </c:if>
          <p>
            ${product.description}
          </p>
          <table>
            <tr>
              <td>Image</td>
              <td>
                <img src="${product.imageUrl}">
              </td>
            </tr>
            <tr>
              <td>Code</td>
              <td>
                ${product.code}
              </td>
            </tr>
            <tr>
              <td>Stock</td>
              <td>
                ${product.stock}
              </td>
            </tr>
            <tr>
              <td>Price</td>
              <td class="price">
                <fmt:formatNumber value="${product.price}" type="currency"
                  currencySymbol="${product.currency.symbol}" />
                <button class="history-button">price-history</button>

              </td>
            </tr>
            <tr>
              <td>Quantity</td>
              <td>
                <form method="post">
                  <input class="quantity" name="quantity" value="${not empty error ? param.quantity : 1}">
                  <c:if test="${not empty param.error}">
                    <span class="error">
                      ${param.error}
                    </span>
                  </c:if>
                  <button>Add to cart</button>
                </form>
              </td>
            </tr>
          </table>

          <div class="history-background">
            <div class="price-history">
              <h2>Price History</h2>
              <h3>${product.description}</h3>
              <table class="price-table">
                <thead>
                  <tr>
                    <td>
                      <strong>Start date</strong>
                    </td>
                    <td>
                      <strong>Price</strong>
                    </td>
                  </tr>
                </thead>
                <c:forEach var="priceHistoryBin" items="${product.priceHistory}">
                  <tr>
                    <td>
                      ${priceHistoryBin.date.dayOfMonth}.
                      ${priceHistoryBin.date.monthValue}.
                      ${priceHistoryBin.date.year}

                    </td>
                    <td>
                      <fmt:formatNumber value="${priceHistoryBin.price}" type="currency"
                        currencySymbol="${product.currency.symbol}" />
                    </td>
                  </tr>
                </c:forEach>
              </table>
            </div>
          </div>
          <section class="viewed">
            <p class="viewed-title">Recently viewed</p>
            <div class="viewed-panel">
              <c:forEach var="viewedProduct" items="${viewedList.previouslyViewedProducts}">
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
          <a class="button-products" href="${pageContext.servletContext.contextPath}/products">
            <p>Back to products</p>
          </a>
        </tags:master>