<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<form action="https://www.paypal.com/cgi-bin/webscr" method="post">

  <input type="hidden" name="cmd" value="_xclick"/>

  <input type="hidden" name="business" value="<c:out value="${paypal_business}"/>"/>
  <input type="hidden" name="item_name" value="<c:out value="${parameters.itemName.value}"/>"/>
  <input type="hidden" name="amount" value="<c:out value="${parameters.amount.value}"/>"/>
  <input type="hidden" name="no_shipping" value="0"/>
  <input type="hidden" name="no_note" value="1"/>
  <input type="hidden" name="currency_code" value="<c:out value="${paypal_currencyCode}"/>"/>
  <input type="hidden" name="lc" value="<c:out value="${paypal_lc}"/>"/>
  <input type="hidden" name="bn" value="PP-BuyNowBF"/>

  <c:choose>
    <c:when test="${empty wikiEngine.attributes.paypal_payNow_image}">
      <input type="image" src="https://www.paypal.com/en_US/i/btn/x-click-but6.gif" border="0" name="submit" alt="Make payment with PayPal"/>
    </c:when>
    <c:otherwise>
      <input type="image" src="${wikiEngine.attributes.paypal_payNow_image}" border="0" name="submit" alt="Make payment with PayPal"/>
    </c:otherwise>
  </c:choose>

  <c:if test="${! empty wikiEngine.attributes.paypal_headerback_color}">
    <input type="hidden" name="cpp_headerback_color" value="${wikiEngine.attributes.paypal_headerback_color}"/>
  </c:if>

  <c:if test="${! empty wikiEngine.attributes.paypal_headerborder_color}">
    <input type="hidden" name="cpp_headerback_color" value="${wikiEngine.attributes.paypal_headerborder_color}"/>
  </c:if>

  <c:if test="${! empty wikiEngine.attributes.paypal_return_pageName}">
    <input type="hidden" name="return" value="${wikiEngine.baseUrl}${wikiEngine.attributes.paypal_return_pageName.viewUrl}"/>
  </c:if>

  <input type="hidden" name="cancel_return" value="${wikiEngine.baseUrl}${wikiDocument.wikiPage.viewUrl}"/>

</form>

