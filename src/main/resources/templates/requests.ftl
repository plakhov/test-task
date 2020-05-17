<html>
<head>
    <meta charset="UTF-8"/>
    <title>Requests List</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css"/>
</head>
<body>

<h1><#if my>My </#if>Requests List</h1>

<div>
    <a href="/requests/add">Add Request</a> |
    <a href="/">Back to Index</a> |
    <a href="/logout">Logout</a>
</div>
<br/><br/>
<div>
    Page ${currentPage} of ${allPages}, rows per page is ${rowPerPage}
</div>
<div>
    <table border="1">
        <tr>
            <th>Id</th>
            <th>Name</th>
            <th>Description</th>
            <th>User</th>
            <th>Edit</th>
            <th>Delete</th>
        </tr>
        <#list requests as request>
            <tr>
                <td>${request.id}</td>
                <td>${request.name}</td>
                <td>${request.description}</td>
                <td>${request.userLastName} ${request.userFirstName}</td>
                <td><a href="${'../requests/' + request.id + '/edit'}">Edit</a></td>
                <td><a href="${'../requests/' + request.id + '/delete'}">Delete</a></td>
            </tr>
        </#list>
    </table>
</div>
<br/><br/>
<div>
    <nobr>
        <#if hasPrev><a href="${'requests?page=' + prev}">Prev</a>&nbsp;&nbsp;&nbsp;</#if>
        <#if hasNext><a href="${'requests?page=' + next}">Next</a></#if>
    </nobr>
</div>
</body>
</html>