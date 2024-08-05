<#include "../include/header.ftl">
<h1>My items</h1>
<a href="/items/new">Create New Item</a>
<div>
    <#list items.content as item>
        <div class="item">
            ${item.data()} - ${item.itemState()}
            <form action="/items/approve/${item.id()}" method="post" style="display:inline;">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button type="submit">Approve</button>
            </form>
            <form action="/items/reject/${item.id()}" method="post" style="display:inline;">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button type="submit">Reject</button>
            </form>
            <form action="/items/delete/${item.id()}" method="post" style="display:inline;">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button type="submit">Delete</button>
            </form>
            <br/>
        </div>
    </#list>
</div>
</body>
</html>