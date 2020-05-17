<#import "/spring.ftl" as spring/>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>Request Form</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css"/>
</head>
<body>
<h1>Request Form</h1>
<div>
    <a href="/">Back to Index</a> |
    <a href="/logout">Logout</a>
</div>
<#if request?? >
    <form class="form-action" action="/requests/add" method="post">
        Name:<br>
        <input type="text" name="name" value="${request.name}">
        <br><br>
        Description:<br>
        <input type="text" name="description" value="${request.description}">
        <input type="number" hidden name="id" value="${request.id}">
        <input type="number" hidden name="userId" value="${request.userId}">
        <br><br>
        <input type="submit" value="Submit">
    </form>
<#else>
    <form class="form-action" action="/requests/add" method="post">
        Name:<br>
        <input type="text" name="name">
        <br><br>
        Description:<br>
        <input type="text" name="description">
        <input type="number" hidden name="id">
        <br><br>
        <input type="submit" value="Submit">
    </form>
</#if>
</body>
</html>