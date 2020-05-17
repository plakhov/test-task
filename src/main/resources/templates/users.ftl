<#import "/spring.ftl" as spring/>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>Users List</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css"/>
</head>
<body>
<h1>Users List</h1>

<div>
    <a href="/users/add">Add User</a> |
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
            <th>First name</th>
            <th>Last Name</th>
            <th>Login</th>
            <th>Role</th>
            <th>Edit</th>
            <th>Delete</th>
        </tr>
        <#list users as user>
            <tr>
                <td>${user.id}</td>
                <td>${user.firstName}</a></td>
                <td>${user.lastName}</a></td>
                <td>${user.login}</td>
                <td>${user.role}</td>
                <td><a href="${'../users/' + user.id + '/edit'}">Edit</a></td>
                <td><a href="${'../users/' + user.id + '/delete'}">Delete</a></td>
            </tr>
        </#list>
    </table>
</div>
<br/><br/>
<div>
    <#if hasPrev><a href="${'users?page=' + prev}">Prev</a>&nbsp;&nbsp;&nbsp;</#if>
    <#if hasNext><a href="${'users?page=' + next}">Next</a></#if>
</div>
</body>
</html>