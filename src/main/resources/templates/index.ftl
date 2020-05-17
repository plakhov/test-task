<#import "/spring.ftl" as spring/>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>Users List</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css"/>
</head>
<body>
<h1>Main Page</h1>


<div>
    <#if admin>
        <a href="/users">Users List</a> |
        <a href="/requests">Requests List</a> |
    </#if>
    <a href="/requests/my">My requests</a> |
    <a href="/">Back to Index</a> |
    <a href="/logout">Logout</a>
</div>


<br/><br/>

</body>
</html>