<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Product List">
    <p>
        Welcome to Advanced Search!
    </p>
    <c:if test="${not empty errors}">
        <p class="error">
            One or more errors occurred during searching
        </p>
    </c:if>
    <c:if test="${not empty success}">
        <p class="success">
            ${success}
        </p>
    </c:if>
    <form action="${pageContext.servletContext.contextPath}/advanced-products?firstVisit=false" >
        <table>
            <tags:advancedSearchForm label="Product Code" name="productCode" errors="${errors}"/>
            <tags:advancedSearchForm label="Min Price" name="minPrice" errors="${errors}"/>
            <tags:advancedSearchForm label="Max Price" name="maxPrice" errors="${errors}"/>
            <tags:advancedSearchForm label="Min Stock" name="minStock" errors="${errors}"/>
        </table>
        <button style="margin: 20px auto; display: block;">Search</button>
    </form>
        <table>
        <c:forEach var="product" items="${products}">
            <tr>
                <td>
                    <img class="product-tile" src="${product.imageUrl}">
                </td>
                <td>
                    <a href="${pageContext.servletContext.contextPath}/products/${product.id}"> ${product.description}</a>
                </td>
                <form method="post" action="${pageContext.servletContext.contextPath}/products/${product.id}">
                    <td>
                        <input class="quantity" name="quantity" value="1">
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


</tags:master>