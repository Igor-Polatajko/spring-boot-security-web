<#include "../include/header.ftl">
<h1>Edit Item</h1>
<form action="/items/update/${itemRequest.id()}" method="post">
    <label for="data">Data:</label>
    <input type="text" id="data" name="data" value="${itemRequest.data()}">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <button type="submit">Update</button>
</form>
</body>
</html>