<#include "../include/header.ftl">
<h1>Create New Item</h1>
<form action="/items/create" method="post">
    <label for="data">Data:</label>
    <input type="text" id="data" name="data">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <button type="submit">Create</button>
</form>
<a href="/items">Back to Home</a>
</body>
</html>